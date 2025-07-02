package com.team2073.lib.subsystem;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import com.team2073.lib.io.DetectionIO;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.List;

/**
 * Base subsystem for detecting game pieces.
 */

public class DetectionSubsystem<IO extends DetectionIO> extends SubsystemBase {
    protected IO io;
    private Pose2d closestGamePiece;

    public DetectionSubsystem(IO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.update(); 
    }

    /**
     * Gets the closest game piece 
     *
     * @returns the game piece
     */

    public Pose2d getClosestGamePiece() {
        closestGamePiece = io.getClosestGamePiece(); 
        Logger.recordOutput("API/" + getName() + "/getClosestGamePiece/closestGamePiece", closestGamePiece);
        return closestGamePiece;
    }

    /**
     * Gets a List of targets 
     *
     * @returns the targets
     */
    @AutoLogOutput(key = "API/DetectionSubsystem/getTargets/targets")
    public List<Pose2d> getTargets() {
        return io.getTargets();
    }
}

