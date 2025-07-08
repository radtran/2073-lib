package com.team2073.lib.io;

import com.ctre.phoenix6.signals.NeutralModeValue;

import com.team2073.lib.inputs.MotorInputs;

/**
 * This interface provides methods
 * for the ServoMotorSubsystem
 */

public interface MotorIO {

    /**Update MotorInputs periodically*/
    default void updateInputs(MotorInputs inputs) {}

    /*Set the duty cycle output as a 0 - 1 fraction of the battery voltage.*/
    default void setDutyCycleOutput(double dutyCycleOutput) {}

    /**Set the setpoint for the motor in rotations. */
    default void setPositionSetpoint(double positionRotations) {}

    /**Set the MotionMagic setpoint in rotations. */
    default void setMotionMagicSetpoint(double positionRotations) {}

    /**Set the velocity setpoint in rotations per second. */
    default void setVelocitySetpoint(double velocityRotationsPerSecond) {}

    /**Zero the motor.*/
    default void setPositionAsHome() {}

    /**Set softstops based on a position*/
    default void enablePositionSoftStops(boolean forwardSoftLimit, boolean reverseSoftLimit) {}

    /**Set the neutral mode of a motor */
    default void setNeutralMode(NeutralModeValue mode) {}
}
