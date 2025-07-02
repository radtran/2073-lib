package com.team2073.lib.io;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.util.Units;
import static edu.wpi.first.units.Units.*;
import edu.wpi.first.units.measure.Distance;
import com.team2073.lib.limelight.LimelightHelpers;
import com.team2073.lib.config.LimelightConfig;
import com.team2073.lib.limelight.LimelightHelpers.RawDetection;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

import java.util.ArrayList;
import java.util.List;

import org.littletonrobotics.junction.Logger;

public class DetectionIOLimelight implements DetectionIO {
    private NetworkTable table;
    private LimelightConfig config;
    private Distance centerOfGamepiece = Inches.of(2);
    private List<Pose2d> targets = new ArrayList<Pose2d>();
    private Pose2d closestGamePiece;
    
    public DetectionIOLimelight(LimelightConfig config) {
        this.config = config;

        table = NetworkTableInstance.getDefault().getTable(config.name);
    }

    @Override
    public void update() {
        targets.clear();

        // if no targets
        if (table.getEntry("tv").getDouble(0) == 0) {
            closestGamePiece = null;
            return;
        }

        RawDetection[] detections = LimelightHelpers.getRawDetections(config.name);
        for (RawDetection detection : detections) {
            double tx = detection.txnc;
            double ty = detection.tync;

            Logger.recordOutput("API/" + config.name + "/update/tx", tx);
            Logger.recordOutput("API/" + config.name + "/update/ty", ty);

            targets.add(new Pose2d(calcGamePieceLocation(tx, ty), new Rotation2d()));
        }

        targets.stream()
            .min((a, b) -> Double.compare(
                        a.getTranslation().getNorm(), 
                        b.getTranslation().getNorm()))
            .orElse(null);
    }

    @Override
    public Pose2d getClosestGamePiece() {
        return closestGamePiece;
    }

    @Override
    public List<Pose2d> getTargets() {
        return targets;
    }

    private Translation2d calcGamePieceLocation(double tx, double ty) {
        double correctedTy = Units.degreesToRadians(-ty) - config.robotCameraOffset.getRotation().getY();
        Distance yDistance =  config.robotCameraOffset.getMeasureZ().minus(centerOfGamepiece).div(Math.tan(correctedTy));

        double correctedTx = Units.degreesToRadians(-tx) + config.robotCameraOffset.getRotation().getZ();
        Distance xDistance = yDistance.times((Math.tan(correctedTx)));

        Logger.recordOutput("API/" + config.name + "/calcGamePieceLocation/correctedTy", correctedTy);
        Logger.recordOutput("API/" + config.name + "/calcGamePieceLocation/correctedTx", correctedTx);
        Logger.recordOutput("API/" + config.name + "/calcGamePieceLocation/xDistance", xDistance);
        Logger.recordOutput("API/" + config.name + "/calcGamePieceLocation/yDistance", yDistance);

        return new Translation2d(xDistance, yDistance);
    }
}
