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

public class ServoMotorConfig {
    public CANDeviceId ID;
    public DCMotor TYPE;
    public String NAME;
    public TalonFXConfiguration TALON_CONFIG; 

    // if no stops, set to Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY
    public double FORWARD_SOFT_STOP;
    public double REVERSE_SOFT_STOP;

    public double SIM_MOMENT_OF_INERTIA;
    public double SIM_GEAR_RATIO;

    public double TEMPERATURE_THRESHOLD_CELSIUS;
}
