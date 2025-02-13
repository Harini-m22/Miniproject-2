package com.example.demoPatientMgmt.service;

import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient registerPatient(Patient patient) {
        // Additional checks can be added here before saving.
        return patientRepository.save(patient);
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
}
