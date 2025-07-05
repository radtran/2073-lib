package com.team2073.lib.io;

import edu.wpi.first.math.geometry.Pose2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.team2073.lib.config.LimelightConfig;

/**
 * Simulation implementation of a Limelight for detection
 */

public class DetectionIOLimelightSim extends DetectionIOLimelight  {
    private Pose2d targetPose;

    public DetectionIOLimelightSim(LimelightConfig config, Pose2d targetPose) {
        super(config);
        this.targetPose = targetPose;    
    }

    @Override
    public Pose2d getClosestGamePiece() {
        return targetPose;
    }

    @Override
    public List<Pose2d> getTargets() {
        return new ArrayList<Pose2d>(Arrays.asList(targetPose)); 
    }
}
