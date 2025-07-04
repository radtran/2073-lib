package com.team2073.lib.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

import com.team2073.lib.config.ServoMotorConfig;
import com.team2073.lib.util.MotorInputs;

/**
 * Simulation implementation of the {@link MotorIO} interface for a TalonFX motor controller.
 */

public class MotorIOTalonFXSim extends MotorIOTalonFX {
    private final double UPDATE_FREQUENCY_SECONDS = 0.02;
    private final int MIN_VOLTAGE = -12;
    private final int MAX_VOLTAGE = 12;

    private final DCMotorSim sim;
    private double appliedVoltage = 0.0;

    /**
     * Simulation implementation of the {@link MotorIO} interface for a TalonFX motor controller.
     * 
     * @param config Servo motor config
     */
    public MotorIOTalonFXSim(ServoMotorConfig config) {
        super(config);

        sim =
            new DCMotorSim(LinearSystemId.createDCMotorSystem(
                this.config.TYPE,
                this.config.SIM_MOMENT_OF_INERTIA, 
                this.config.SIM_GEAR_RATIO), 
                this.config.TYPE
            );
    }

    @Override
    public void updateInputs(MotorInputs inputs) {
        if (DriverStation.isDisabled()) {
            setDutyCycleOutput(0);
        }

        sim.update(UPDATE_FREQUENCY_SECONDS);

        // connection and temperature are irrelavant in simulation
        // so they are left in a happy condition
        inputs.connected = true;
        inputs.temperatureFault = false;
        inputs.temperatureCelsius = 0;
        inputs.velocityRotationsPerSecond = sim.getAngularVelocityRadPerSec();
        inputs.positionRotations = sim.getAngularPositionRad();
        inputs.appliedVoltage = appliedVoltage;
        inputs.supplyCurrentAmps = sim.getCurrentDrawAmps();
        inputs.torqueCurrentAmps = sim.getTorqueNewtonMeters();
    }

    @Override
    public void setDutyCycleOutput(double dutyCycleOutput) {
        appliedVoltage = MathUtil.clamp(dutyCycleOutput, MIN_VOLTAGE, MAX_VOLTAGE);
        sim.setInputVoltage(appliedVoltage);
    }
}
