package com.team2073.lib.io;

import edu.wpi.first.math.geometry.Pose2d;

import com.team2073.lib.config.LimelightConfig;
import com.team2073.lib.inputs.DetectionInputs;

/**
 * Simulation implementation of a Limelight for detection.
 *
 * <p>
 *
 * This is just for testing purpose like driving to a point where a game piece could be cause 
 * there's obviously not going to be game pieces in sim lol.
 */

public class DetectionIOLimelightSim extends DetectionIOLimelight  {
    private Pose2d targetPose;

    public DetectionIOLimelightSim(LimelightConfig config, Pose2d targetPose) {
        super(config);
        this.targetPose = targetPose;    
    }

    @Override
    public void update(DetectionInputs inputs) {
        super.update(inputs);

        inputs.closestGamePiece = targetPose;
        inputs.targets.add(targetPose);
        inputs.hasTargets = true;
    }
}
