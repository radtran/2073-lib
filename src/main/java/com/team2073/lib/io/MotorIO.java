package com.team2073.lib.io;

import com.ctre.phoenix6.signals.NeutralModeValue;

import com.team2073.lib.util.MotorInputs;

/**
 * This interface provides methods
 * for the ServoMotorSubsystem
 */

public interface MotorIO {

    /**Update MotorInputs periodically*/
    public void updateInputs(MotorInputs inputs);

    /*Set the duty cycle output as a 0 - 1 fraction of the battery voltage.*/
    public void setDutyCycleOutput(double dutyCycleOutput);

    /**Set the setpoint for the motor in rotations. */
    public void setPositionSetpoint(double positionRotations); 

    /**Set the MotionMagic setpoint in rotations. */
    public void setMotionMagicSetpoint(double positionRotations);

    /**Set the velocity setpoint in rotations per second. */
    public void setVelocitySetpoint(double velocityRotationsPerSecond); 

    /**Zero the motor.*/
    public void setPositionAsHome(); 

    /**Set softstops based on a position*/
    public void enablePositionSoftStops(boolean forwardSoftLimit, boolean reverseSoftLimit); 

    /**Set the neutral mode of a motor */
    public void setNeutralMode(NeutralModeValue mode); 
}
