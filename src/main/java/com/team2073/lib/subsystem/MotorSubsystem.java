package com.team2073.lib.subsystem;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2073.lib.io.MotorIO;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

public class MotorSubsystem<IO extends MotorIO> extends SubsystemBase {
    IO io;

    private double dutyCycleOutput;
    private double velocitySetpoint;
    private NeutralModeValue mode;

    public MotorSubsystem(IO io){
        this.io = io;
    }

    protected void setDutyCycleOutput(double dutyCycleOutput) {
        this.dutyCycleOutput = dutyCycleOutput;
        Logger.recordOutput("API/" + getName() + "/setDutyCycleOutput/dutyCycleOutput", dutyCycleOutput);
        io.setDutyCycleOutput(dutyCycleOutput);
    }
    
    protected void setVelocitySetpoint(double velocityRotationsPerSecond) {
        velocitySetpoint = velocityRotationsPerSecond;
        Logger.recordOutput("API/" + getName() + "/setVelocitySetpoint/velocitySetpoint", velocitySetpoint);
        io.setVelocitySetpoint(velocityRotationsPerSecond);
    }

    protected void setNeutralMode(NeutralModeValue mode) {
        this.mode = mode;
        Logger.recordOutput("API/" + getName() + "/setNeutralMode/mode", this.mode);
        io.setNeutralMode(mode);
    }

    public double getVelocitySetpoint() {
        return velocitySetpoint;
    }

    public NeutralModeValue getMode() {
        return mode;
    }

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
