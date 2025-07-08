package com.team2073.lib.io;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.util.Units;
import static edu.wpi.first.units.Units.*;

import java.util.List;
import java.util.ArrayList;

import edu.wpi.first.units.measure.Distance;
import com.team2073.lib.limelight.LimelightHelpers;
import com.team2073.lib.config.LimelightConfig;
import com.team2073.lib.limelight.LimelightHelpers.RawDetection;
import com.team2073.lib.inputs.DetectionInputs;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

import org.littletonrobotics.junction.Logger;

public class DetectionIOLimelight implements DetectionIO {
    private NetworkTable table;
    private LimelightConfig config;
    private Distance centerOfGamepiece = Inches.of(2);
    private List<Pose2d> targets = new ArrayList<>();
    
    public DetectionIOLimelight(LimelightConfig config) {
        this.config = config;

        table = NetworkTableInstance.getDefault().getTable(config.NAME);
    }

    @Override
    public void update(DetectionInputs inputs) {
        targets.clear();

        inputs.hasTargets = table.getEntry("tv").getDouble(0) == 1;
        if (!inputs.hasTargets) {
            inputs.closestGamePiece = null;
            targets = null;
            return;
        }

        RawDetection[] detections = LimelightHelpers.getRawDetections(config.NAME);
        for (RawDetection detection : detections) {
            double tx = detection.txnc;
            double ty = detection.tync;

            Logger.recordOutput("API/" + config.NAME + "/update/tx", tx);
            Logger.recordOutput("API/" + config.NAME + "/update/ty", ty);

           targets.add(new Pose2d(calcGamePieceLocation(tx, ty), new Rotation2d()));
        }

        // looks very complicated bc you get the norm (distance away from (0,0)) and it is better
        // at comparing targets whose distances are almost the same.
        inputs.closestGamePiece = targets.stream()
            .min((a, b) -> Double.compare(
                        a.getTranslation().getNorm(), 
                        b.getTranslation().getNorm()))
            .orElse(null);

    }

    private Translation2d calcGamePieceLocation(double tx, double ty) {
        double correctedTy = Units.degreesToRadians(-ty) - config.ROBOT_CAMERA_OFFSET.getRotation().getY();
        Distance yDistance =  config.ROBOT_CAMERA_OFFSET.getMeasureZ().minus(centerOfGamepiece).div(Math.tan(correctedTy));

        double correctedTx = Units.degreesToRadians(-tx) + config.ROBOT_CAMERA_OFFSET.getRotation().getZ();
        Distance xDistance = yDistance.times((Math.tan(correctedTx)));

        Logger.recordOutput("API/" + config.NAME + "/calcGamePieceLocation/correctedTy", correctedTy);
        Logger.recordOutput("API/" + config.NAME + "/calcGamePieceLocation/correctedTx", correctedTx);
        Logger.recordOutput("API/" + config.NAME + "/calcGamePieceLocation/xDistance", xDistance);
        Logger.recordOutput("API/" + config.NAME + "/calcGamePieceLocation/yDistance", yDistance);

        return new Translation2d(xDistance, yDistance);
    }
}
