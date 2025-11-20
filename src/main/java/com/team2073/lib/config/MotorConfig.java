package com.team2073.lib.config;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import com.team2073.lib.drivers.CANDeviceId;

import edu.wpi.first.math.system.plant.DCMotor;

/**
 * Configuration for a sevo motor.
 * <p>
 * Includes all settings for the motor, PID,
 * and MotionMagic so it is very important that this
 * be configured properly, mainly the {@code TALON_CONFIG}.
 * <p>
 * However, not all properties of the config need to be set.
 * It is just important to set the main properties of the motor.
 */

public class MotorConfig {
    public CANDeviceId id = new CANDeviceId(-1, "NO_BUS");
    public DCMotor type = DCMotor.getKrakenX60(1);
    public String name = "UNNAMED";
    public TalonFXConfiguration talonConfig = new TalonFXConfiguration(); 

    public double forwardSoftStop = Double.POSITIVE_INFINITY;
    public double reverseSoftStop = Double.NEGATIVE_INFINITY;

    public double momentOfInertia = 0.01;
    public double gearRatio = 1.0;

    public double temperatureThresholdCelsius = 80.0;
}
