package com.team2073.lib.io;

import edu.wpi.first.math.geometry.Pose2d;

import java.util.List;

/**
 * Interface that provides the methods for 
 * detecting and tracking game pieces.
 */

public interface DetectionIO {
    public void update(); 

    public Pose2d getClosestGamePiece();

    public List<Pose2d> getTargets();
}
