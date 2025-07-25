package org.example.vesselsmanagement;

public class VesselNotFoundException extends RuntimeException {
    public VesselNotFoundException(String message) {
        super(message);
    }
}
