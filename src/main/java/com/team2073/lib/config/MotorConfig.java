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
    public CANDeviceId id;
    public DCMotor type;
    public String name;
    public TalonFXConfiguration talonConfig; 

    // if no stops, set to Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY
    public double forwardSoftStop;
    public double reverseSoftStop;

    public double momentOfInertia;
    public double gearRatio;

    public double temperatureThresholdCelsius;
}
