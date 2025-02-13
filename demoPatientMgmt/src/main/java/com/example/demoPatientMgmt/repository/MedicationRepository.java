package com.example.demoPatientMgmt.repository;

import com.example.demoPatientMgmt.model.Medication;
import com.example.demoPatientMgmt.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPatient(Patient patient);
}