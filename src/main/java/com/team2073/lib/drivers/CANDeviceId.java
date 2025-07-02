package com.team2073.lib.drivers;

/**
 * Class representing the CAN device ID for
 * a CTRE device running on a CAN bus.
 */

public class CANDeviceId {
    private int deviceId;
    private String bus;

    /**
     * Class representing the CAN device ID for a CTRE device running on a CAN bus.
     * 
     * @param deviceId device ID
     * @param bus CAN bus
     */
    public CANDeviceId(int deviceId, String bus) {
        this.deviceId = deviceId;
        this.bus = bus;
    }

    /**
     * Class representing the CAN device ID for a CTRE device running on a CAN bus.
     * 
     * @param deviceId device ID
     */
    public CANDeviceId(int deviceId) {
        this.deviceId = deviceId;
        this.bus = ""; // since a bus was not passed in, we set it to the default empty string
    }

    /**
     * Gets the ID of a device
     * 
     * @return the ID of the device
     */
    public int getDeviceId() {
        return deviceId;
    }

    /**
     * Gets the bus of a device
     * 
     * @return the bus
     */
    public String getBus() {
        return bus;
    }
}
