package com.team2073.lib.inputs;

import org.littletonrobotics.junction.AutoLog;

/**
 * Inputs of a motor for reading.
 */

@AutoLog
public class MotorInputs {
    public boolean  connected;
    public boolean  temperatureFault;
    public double   temperatureCelsius;
    public double   velocityRotationsPerSecond;
    public double   positionRotations;
    public double   appliedVoltage;
    public double   supplyCurrentAmps;
    public double   torqueCurrentAmps;
}
