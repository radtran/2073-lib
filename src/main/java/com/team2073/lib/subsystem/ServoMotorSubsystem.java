package com.team2073.lib.subsystem;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.signals.NeutralModeValue;

import com.team2073.lib.io.MotorIO;
import com.team2073.lib.util.MotorInputsAutoLogged;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Basic outline of a servo motor subsystem with
 * commands to set the motor and methods to create
 * additional commands.
 */

public class ServoMotorSubsystem<IO extends MotorIO> extends SubsystemBase {
    protected final IO io;
    protected final MotorInputsAutoLogged inputs = new MotorInputsAutoLogged();

    private double positionSetpoint;
    private double velocitySetpoint;
    private double dutyCycleOutput;
    private NeutralModeValue mode;
    
    /**
     * Basic outline of a servo motor subystem with
     * commands to set the motor and methods to create
     * additional commands.
     * 
     * @param io motor methods
     */
    public ServoMotorSubsystem(IO io) {
        this.io = io;

        setDefaultCommand(dutyCycleCommand(() -> 0.0).withName(getName() + " Default Command"));
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs); 
        Logger.processInputs("API/" + getName() + "/inputs", inputs);
    }

    /**
     * Set a duty cycle output as a fraction of battery 0 - 1.
     * 
     * @param dutyCycleOutput the output
     */
    protected void setDutyCycleOutput(double dutyCycleOutput) {
        this.dutyCycleOutput = dutyCycleOutput;
        Logger.recordOutput("API/" + getName() + "/setDutyCycleOutput/dutyCycleOutput", dutyCycleOutput);
        io.setDutyCycleOutput(dutyCycleOutput);
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
     * Set velocity setpoint.
     * 
     * @param velocityRotationsPerSecond the velocity setpoint.
     */
    protected void setVelocitySetpoint(double velocityRotationsPerSecond) {
        velocitySetpoint = velocityRotationsPerSecond;
        Logger.recordOutput("API/" + getName() + "/setVelocitySetpoint/velocitySetpoint", velocitySetpoint);
        io.setVelocitySetpoint(velocityRotationsPerSecond);
    }

    /**
     * Set motor's neutral mode.
     * 
     * @param mode the neutral mode.
     */
    protected void setNeutralMode(NeutralModeValue mode) {
        this.mode = mode;
        Logger.recordOutput("API/" + getName() + "/setNeutralMode/mode", this.mode);
        io.setNeutralMode(mode);
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
     * Gets the current velocity setpoint in rotations per second.
     * 
     * @return velocity setpoint
     */
    public double getVelocitySetpoint() {
        return velocitySetpoint;
    }

    /**
     * Gets the current {@code NeutralModeValue} of the motor.
     * 
     * @return the neutral mode
     */
    public NeutralModeValue getMode() {
        return mode;
    }

    /**
     * Gets the current duty cycle output being applied to the motor.
     * 
     * @return the duty cycle output.
     */
    public double getDutyCycleOutput() {
        return dutyCycleOutput;
    }

    /**
     * Runs voltage to motor.
     * 
     * @param dutyCycleVoltage DoubleSupplier between 0 and 1.
     * Represents the percentage of the battery in volts.
     */
    public Command dutyCycleCommand(DoubleSupplier dutyCycleVoltage) {
        return runEnd(() -> {
            setDutyCycleOutput(dutyCycleVoltage.getAsDouble());
        }, () -> {
            setDutyCycleOutput(0.0);
        }).withName(getName() + " Duty Cycle Command");
    }

    /**
     * Runs motor to a velocity setpoint in rotations per second.
     * 
     * @param velocityRotationsPerSecond velocity setpoint supplier.
     */
    public Command velocitySetpointCommand(DoubleSupplier velocityRotationsPerSecond) {
        return runEnd(() -> {
            setVelocitySetpoint(velocityRotationsPerSecond.getAsDouble());
        }, () -> {
        }).withName(getName() + " Velocity Setpoint Command");
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

    /**
     * Sets motor's default mode to coast, then, after an action, to brake.
     */
    public Command coastCommand() {
        return startEnd(() -> {
            setNeutralMode(NeutralModeValue.Coast);
        }, () -> {
            setNeutralMode(NeutralModeValue.Brake);
        }).withName(getName() + " Coast Command")
        .ignoringDisable(true);
    }
}
