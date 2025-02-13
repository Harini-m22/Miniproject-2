package com.example.demoPatientMgmt.service;

import com.example.demoPatientMgmt.model.Medication;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    public List<Medication> getMedicationsByPatient(Patient patient) {
        return medicationRepository.findByPatient(patient);
    }

    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication updatedMedication) {
        Optional<Medication> opt = medicationRepository.findById(id);
        if (opt.isPresent()) {
            Medication med = opt.get();
            med.setName(updatedMedication.getName());
            med.setDosage(updatedMedication.getDosage());
            med.setInstructions(updatedMedication.getInstructions());
            return medicationRepository.save(med);
        }
        return null;
    }

    public boolean deleteMedication(Long id) {
        Optional<Medication> opt = medicationRepository.findById(id);
        if (opt.isPresent()) {
            medicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id).orElse(null);
    }
}
