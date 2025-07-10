package com.team2073.lib.inputs;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.geometry.Pose2d;

@AutoLog
public class LocalizationInputs {
    public Pose2d pose;
    public Rotation2d rotation;
    public SwerveModulePosition[] positions;
}
