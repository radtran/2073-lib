package com.team2073.lib.io;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import com.team2073.lib.util.CTREUtil;
import com.team2073.lib.config.ServoMotorConfig;
import com.team2073.lib.inputs.MotorInputs;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

/**
 * A hardware implementation of {@link MotorIO} for a TalonFX motor controller.
 */

public class MotorIOTalonFX implements MotorIO {
    protected final int UPDATE_FREQUENCY_HZ = 50; // 20 ms

    protected final TalonFX talon;
    protected final ServoMotorConfig config;

    protected final DutyCycleOut dutyCycleRequest = new DutyCycleOut(0.0);
    protected final PositionVoltage positionRequest = new PositionVoltage(0.0);
    protected final MotionMagicVoltage motionMagicRequest = new MotionMagicVoltage(0.0);
    protected final VelocityVoltage velocityRequest = new VelocityVoltage(0.0);

    private BaseStatusSignal[] signals;
    private final StatusSignal<Temperature> temperatureSignal;
    private final StatusSignal<Angle> positionSignal;                
    private final StatusSignal<AngularVelocity> velocitySignal;       
    private final StatusSignal<Voltage> voltageSignal;
    private final StatusSignal<Current> supplyCurrentSignal;
    private final StatusSignal<Current> torqueCurrentSignal;

    private final Debouncer isConnected = new Debouncer(UPDATE_FREQUENCY_HZ);

    private final Alert connectionFault;
    private final Alert temperatureFault;

    /**
     * A hardware implementation of {@link MotorIO} for a TalonFX motor controller.
     * 
     * @param config is a {@link ServoMotorConfig}. Ensure that it is configured before passing in.
     */
    public MotorIOTalonFX(ServoMotorConfig config) {
        this.config = config;
        this.talon = new TalonFX(this.config.ID.getDeviceId(), this.config.ID.getBus());
        
        CTREUtil.applyConfiguration(this.talon, this.config.TALON_CONFIG);

        this.temperatureSignal = this.talon.getDeviceTemp();
        this.positionSignal = this.talon.getPosition();             
        this.velocitySignal = this.talon.getVelocity();
        this.voltageSignal = this.talon.getSupplyVoltage();
        this.supplyCurrentSignal = this.talon.getSupplyCurrent();
        this.torqueCurrentSignal = this.talon.getTorqueCurrent();

        this.signals = new BaseStatusSignal[] {
            this.temperatureSignal,
            this.positionSignal,
            this.velocitySignal,
            this.voltageSignal,
            this.supplyCurrentSignal,
            this.torqueCurrentSignal
        };

        BaseStatusSignal.setUpdateFrequencyForAll(        
            this.UPDATE_FREQUENCY_HZ,
            this.signals
        );
        
        connectionFault = new Alert("CONNECTION FAULT, ID " + talon.getDeviceID() + ", " + config.NAME, AlertType.kError);
        temperatureFault = new Alert("TEMPERATURE FAULT, ID " + talon.getDeviceID() + ", " + config.NAME, AlertType.kError);
    }

    private double clampPosition(double position) {
        return MathUtil.clamp(position, config.REVERSE_SOFT_STOP, config.FORWARD_SOFT_STOP);
    }

    @Override
    public void updateInputs(MotorInputs inputs) {
        BaseStatusSignal.refreshAll(signals);
        inputs.connected = isConnected.calculate(BaseStatusSignal.isAllGood(signals));
        inputs.temperatureCelsius = temperatureSignal.getValueAsDouble();
        inputs.positionRotations = positionSignal.getValueAsDouble();
        inputs.velocityRotationsPerSecond = velocitySignal.getValueAsDouble();
        inputs.appliedVoltage = voltageSignal.getValueAsDouble();
        inputs.supplyCurrentAmps = supplyCurrentSignal.getValueAsDouble();
        temperatureFault.set(inputs.temperatureCelsius > config.TEMPERATURE_THRESHOLD_CELSIUS);
        connectionFault.set(!inputs.connected);
        
        if (inputs.temperatureCelsius > config.TEMPERATURE_THRESHOLD_CELSIUS) {
            inputs.temperatureFault = true;
        } else {
            inputs.temperatureFault = false;
        }
    }

    @Override
    public void setDutyCycleOutput(double dutyCycleOutput) {
        talon.setControl(dutyCycleRequest.withOutput(dutyCycleOutput));
    }

    @Override 
    public void setPositionSetpoint(double positionRotations) {
        talon.setControl(positionRequest.withPosition(clampPosition(positionRotations)));
    }

    @Override
    public void setMotionMagicSetpoint(double positionRotations) {
        talon.setControl(motionMagicRequest.withPosition(clampPosition(positionRotations)));
    }

    @Override
    public void setVelocitySetpoint(double velocityRotationsPerSecond) {
        talon.setControl(velocityRequest.withVelocity(velocityRotationsPerSecond));
    }

    @Override
    public void setPositionAsHome() {
        talon.setPosition(0);
    }

    @Override
    public void enablePositionSoftStops(boolean forwardSoftLimit, boolean reverseSoftLimit) {
        CTREUtil.applyConfiguration(talon, config.TALON_CONFIG);
    }

    @Override
    public void setNeutralMode(NeutralModeValue mode) {
        config.TALON_CONFIG.MotorOutput.NeutralMode = mode;
        CTREUtil.applyConfiguration(talon, config.TALON_CONFIG);
    }
}
