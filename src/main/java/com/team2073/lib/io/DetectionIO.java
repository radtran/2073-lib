package com.team2073.lib.io;

import com.team2073.lib.inputs.DetectionInputs;

/**
 * Interface that provides the methods for 
 * detecting and tracking game pieces.
 */

public interface DetectionIO {
    default void update(DetectionInputs inputs) {}
}
