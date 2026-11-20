package com.marcel.medical_01.exception;

public class PatientNotFoundException
        extends RuntimeException {

    public PatientNotFoundException() {
        super("Patient not found.");
    }
}
