package com.team2073.lib.io;

import com.team2073.lib.config.LimelightConfig;
import com.team2073.lib.limelight.LimelightHelpers;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.VecBuilder;

public class VisionIOLimelight implements VisionIO {
    protected LimelightConfig config;
    protected SwerveDrivePoseEstimator poseEstimator;
    protected boolean rejectUpdate;
    protected Pose2d estimate = new Pose2d(0, 0, new Rotation2d());

    public VisionIOLimelight(LimelightConfig config) {
        this.config = config;
    }

    @Override
    public void update(double velocity, double yaw) {
        LimelightHelpers.SetRobotOrientation(config.name, yaw, 0, 0, 0, 0, 0);
        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
        poseEstimator.update(

        // if our angular velocity is greater than 360 degrees per second, ignore vision updates
        if(Math.abs(velocity) > 360) {
            rejectUpdate = true;
        }

        if(mt2.tagCount == 0){
            rejectUpdate = true;
        }

        if(!rejectUpdate){
            poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
            poseEstimator.addVisionMeasurement(
                    mt2.pose,
                    mt2.timestampSeconds);
            estimate = poseEstimator.getEstimatedPosition();
        }

    }

    @Override
    public Pose2d getEstimate() {
        return estimate;
    }
}
