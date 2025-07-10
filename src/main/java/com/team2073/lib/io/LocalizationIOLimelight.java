package com.team2073.lib.io;

import edu.wpi.first.math.geometry.Rotation2d;

import com.team2073.lib.config.LimelightConfig;
import com.team2073.lib.inputs.LocalizationInputs;
import com.team2073.lib.limelight.LimelightHelpers;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.util.Units;

public class LocalizationIOLimelight implements LocalizationIO {
    protected SwerveDrivePoseEstimator poseEstimator;
    protected LimelightHelpers.PoseEstimate mt2;
    protected LimelightConfig config;

    public LocalizationIOLimelight(
            LimelightConfig config,
            Rotation2d initRotation,
            SwerveDriveKinematics kinematics,
            Pose2d initPose,
            SwerveModulePosition[] positions
    ) {
        this.config = config;

        poseEstimator = new SwerveDrivePoseEstimator(
                kinematics,
                initRotation,
                positions,
                initPose
        );
    } 

    @Override
    public void update(LocalizationInputs inputs, Rotation2d rotation, SwerveModulePosition[] positions) {
        mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(this.config.NAME);
        if (mt2 == null) {
            return;
        }

        if (mt2.tagCount == 0) {
            return;
        }
        
        poseEstimator.update(rotation, positions);
        poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(0.1, 0.1, Units.degreesToRadians(3)));
        poseEstimator.addVisionMeasurement(
                mt2.pose,
                mt2.timestampSeconds);
        
        inputs.rotation = rotation;
        inputs.positions = positions;
        inputs.pose = poseEstimator.getEstimatedPosition();
    }
}
