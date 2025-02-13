package com.example.demoPatientMgmt.controller;

import com.example.demoPatientMgmt.model.Medication;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.service.MedicationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    // List medications for the current patient
    @GetMapping("/medications")
    public String viewMedications(Model model, HttpSession session) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        model.addAttribute("medications", medicationService.getMedicationsByPatient(currentPatient));
        return "medications"; // renders medications.html
    }

    // Show form to add a new medication
    @GetMapping("/medications/add")
    public String showAddMedicationForm(Model model, HttpSession session) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        model.addAttribute("medication", new Medication());
        return "addMedication"; // renders addMedication.html
    }

    // Process form to add a new medication
    @PostMapping("/medications/add")
    public String addMedication(@Valid @ModelAttribute("medication") Medication medication,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()){
            return "addMedication";
        }
        medication.setPatient(currentPatient);
        medicationService.addMedication(medication);
        return "redirect:/medications";
    }

    // Show form to edit an existing medication
    @GetMapping("/medications/edit/{id}")
    public String showEditMedicationForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        Medication medication = medicationService.getMedicationById(id);
        if (medication == null || !medication.getPatient().getId().equals(currentPatient.getId())) {
            return "redirect:/medications";
        }
        model.addAttribute("medication", medication);
        return "editMedication"; // renders editMedication.html
    }

    // Process form to update medication details
    @PostMapping("/medications/edit/{id}")
    public String editMedication(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("medication") Medication medication,
                                 BindingResult bindingResult,
                                 HttpSession session) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()){
            return "editMedication";
        }
        Medication existingMedication = medicationService.getMedicationById(id);
        if (existingMedication == null || !existingMedication.getPatient().getId().equals(currentPatient.getId())){
            return "redirect:/medications";
        }
        // Ensure the medication remains associated with the current patient
        medication.setPatient(currentPatient);
        medicationService.updateMedication(id, medication);
        return "redirect:/medications";
    }

    // Delete a medication
    @PostMapping("/medications/delete/{id}")
    public String deleteMedication(@PathVariable("id") Long id, HttpSession session) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            return "redirect:/login";
        }
        Medication medication = medicationService.getMedicationById(id);
        if (medication != null && medication.getPatient().getId().equals(currentPatient.getId())){
            medicationService.deleteMedication(id);
        }
        return "redirect:/medications";
    }
}