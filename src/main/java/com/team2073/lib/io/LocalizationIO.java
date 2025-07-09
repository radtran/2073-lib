package com.team2073.lib.io;

import com.team2073.lib.inputs.LocalizationInputs;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.geometry.Rotation2d;

public interface LocalizationIO {
    default void update(LocalizationInputs inputs, Rotation2d rotation, SwerveModulePosition[] positions){}
}
