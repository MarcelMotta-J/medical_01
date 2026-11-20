package com.marcel.medical_01.exception;

public class DoctorNotFoundException
        extends RuntimeException {

    public DoctorNotFoundException() {
        super("Doctor not found.");
    }
}
