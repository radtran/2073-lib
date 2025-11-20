package com.team2073.lib.config;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.team2073.lib.drivers.CANDeviceId;

public class CANCoderConfig {
    public CANDeviceId id = new CANDeviceId(-1, "NO_BUS");
    public String name = "UNNAMED";
    public CANcoderConfiguration config = new CANcoderConfiguration();
}
