package com.team2073.lib.inputs;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Pose2d;

@AutoLog
public class DetectionInputs {
    public Pose2d closestGamePiece;
    public boolean hasTargets;
}
