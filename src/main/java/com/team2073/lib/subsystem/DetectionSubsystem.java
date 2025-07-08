package com.team2073.lib.subsystem;

import com.team2073.lib.inputs.DetectionInputsAutoLogged;
import com.team2073.lib.io.DetectionIO;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;

/**
 * Base subsystem for detecting game pieces.
 */

public class DetectionSubsystem<IO extends DetectionIO> extends SubsystemBase {
    protected IO io;
    private DetectionInputsAutoLogged inputs = new DetectionInputsAutoLogged();

    public DetectionSubsystem(IO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.update(inputs); 
    }

    /**
     * Gets the closest game piece. 
     *
     * <p>
     *
     * This should be used mainly for drive to pose
     * commands so that the robot is able to autonomously pick up game pieces.
     *
     * @returns the game piece, null if there are no game pieces
     */

    public Pose2d getClosestGamePiece() {
        return inputs.closestGamePiece;
    }
}

