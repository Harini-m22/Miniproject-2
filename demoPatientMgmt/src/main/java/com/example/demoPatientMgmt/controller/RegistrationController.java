package com.example.demoPatientMgmt.controller;

import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class RegistrationController {

    @Autowired
    private PatientService patientService;

    // Display the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "registration";
    }

    // Process the registration form submission
    @PostMapping("/register")
    public String registerPatient(
            @Valid @ModelAttribute("patient") Patient patient,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        patientService.registerPatient(patient);
        model.addAttribute("successMessage", "Registration successful!");
        return "registration";
    }
}
