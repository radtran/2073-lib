package com.team2073.lib.subsystem;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import com.team2073.lib.inputs.MotorInputsAutoLogged;
import com.team2073.lib.io.MotorIO;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Basic outline of a servo motor subsystem with
 * commands to set the motor and methods to create
 * additional commands.
 */

public class ServoMotorSubsystem<IO extends MotorIO> extends MotorSubsystem<IO> {
    protected final IO io;
    protected final MotorInputsAutoLogged inputs = new MotorInputsAutoLogged();

    private double positionSetpoint;
    
    /**
     * Basic outline of a servo motor subystem with
     * commands to set the motor and methods to create
     * additional commands.
     * 
     * @param io motor methods
     */
    public ServoMotorSubsystem(IO io) {
        super(io);
        this.io = io;

        setDefaultCommand(dutyCycleCommand(() -> 0.0).withName(getName() + " Default Command"));
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs); 
        Logger.processInputs("API/" + getName() + "/inputs", inputs);
    }

    /**
     * Set position setpoint.
     * 
     * @param positionRotations the position setpoint.
     */
    protected void setPositionSetpoint(double positionRotations) {
        positionSetpoint = positionRotations;
        Logger.recordOutput("API/" + getName() + "/setPositionSetpoint/positionSetpoint" , positionSetpoint);
        io.setPositionSetpoint(positionRotations);
    }

    /**
     * Set a MotionMagic setpoint.
     * 
     * @param positionRotations the position setpoint.
     */
    protected void setMotionMagicSetpoint(double positionRotations) {
        positionSetpoint = positionRotations;
        Logger.recordOutput("API/" + getName() + "/setMotionMagicSetpoint/positionSetpoint", positionSetpoint);
        io.setMotionMagicSetpoint(positionRotations);
    }

    /**
     * Zero the motor.
     */
    protected void setPositionAsHome() {
        io.setPositionAsHome();
    }

    /**
     * Gets the current position setpoint in rotations.
     * 
     * @return position setpoint
     */
    public double getPositionSetpoint() {
        return positionSetpoint;
    }

    /**
      * Runs motor to a position setpoint in rotations.
      * 
      * @param positionRotations position setpoint supplier.
      */
    public Command positionSetpointCommand(DoubleSupplier positionRotations) {
        return runEnd(() -> {
            setPositionSetpoint(positionRotations.getAsDouble());
        }, () -> {   
        }).withName(getName() + " Position Setpoint Command");
    }

    /**
      * Runs motor a motion magic setpoint in rotations.
      * 
      * @param positoinRotations supplier position setpoint
      */
    public Command motionMagicSetPointCommand(DoubleSupplier positionRotations) {
        return runEnd(() -> { 
            setMotionMagicSetpoint(positionRotations.getAsDouble());
        }, () -> {
        }).withName(getName() + " Motion Magic Setpoint Command");
    }

}
