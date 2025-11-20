package com.team2073.lib.util;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team2073.lib.config.MotorConfig;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendableData implements Sendable, AutoCloseable{
    private String title;
    private String type;
    private String[] names;
    private double[] values;
    private PIDController pid;
    private boolean isUpdated = true;
    private double[] pastValues;
    private MotorConfig config;

    public SendableData(String title, MotorConfig config){
        this.title = title;
        this.type = "pid";
        this.config = config;

        pid = new PIDController(
            config.talonConfig.Slot0.kP, 
            config.talonConfig.Slot0.kI, 
            config.talonConfig.Slot0.kD);

        SmartDashboard.putData(this.title, pid);
    }


    public SendableData(String title, String... names){
        this.title = title;
        this.type = "default";
        this.names = names;
        this.values = new double[names.length];

        SendableRegistry.addLW(this, title + " ");
        SmartDashboard.putData(this);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        for (int i = 0; i < values.length; i++) {
            final int index = i;
            builder.addDoubleProperty(names[index], () -> getValue(index), (newValue) -> setValue(index, newValue));
        }
    }   

    @Override
    public void close() {
        SendableRegistry.remove(this);
    }
    
    public TalonFXConfiguration getPIDConfig() {
        if (this.type.toLowerCase() == "pid") {
            config.talonConfig.Slot0.kP = pid.getP();
            config.talonConfig.Slot0.kI = pid.getI();
            config.talonConfig.Slot0.kD = pid.getD();
        }       
        pastValues = new double[] {pid.getP(), pid.getI(), pid.getD()};
        isUpdated = false;
        return config.talonConfig;
    }

    public boolean isPIDUpdated() {
        if (this.type.toLowerCase() == "pid") {
            if (pastValues == null) {
                pastValues = new double[] {pid.getP(), pid.getI(), pid.getD()};
            } else if (pastValues[0] != pid.getP() || pastValues[1] != pid.getI() || pastValues[2] != pid.getD()) {
                isUpdated = true;
            } else {
                isUpdated = false;
            }
        } else {
            isUpdated = false;
        }
        return isUpdated;
    }

    public boolean isUpdated() {
        if (this.type.toLowerCase().equals("default")) {
            if (pastValues == null) {
                pastValues = values.clone();
            } else {
                for (int i = 0; i < values.length; i++) {
                    if (pastValues[i] != values[i]) {
                        isUpdated = true;
                        pastValues = values.clone();
                        return isUpdated;
                    } else {
                        isUpdated = false;
                    }
                }
            }
        } else {
            isUpdated = false;
        }
        return isUpdated;
    }

    public double getValue(int index) {
        outOfBounds(index);
        return this.values[index];
    }

    public double getValue(String name) {
        for (int i = 0; i < values.length; i++) {
            if (this.names[i] == name) {
                return this.values[i];
            }
        }
        System.out.println("___________________________________________");
        System.out.println("Value not found: " + name + "does not exist in " + this.title);
        System.out.println("___________________________________________");

        return Double.NaN;
    }

    public void setValue(int index, double value) {
        outOfBounds(index);
        this.values[index] = value;
    }

    private void outOfBounds(int index) {
        if (index < 0 || index >= values.length) {
            System.out.println("___________________________________________");
            System.out.println("Index out of bounds");
            System.out.println("___________________________________________");
        }
    }
}
