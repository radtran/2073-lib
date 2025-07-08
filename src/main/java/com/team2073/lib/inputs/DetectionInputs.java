package com.team2073.lib.inputs;

import java.util.ArrayList;
import java.util.List;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Pose2d;

@AutoLog
public class DetectionInputs {
    public Pose2d closestGamePiece;
    public List<Pose2d> targets = new ArrayList<>();
    public boolean hasTargets;
}
