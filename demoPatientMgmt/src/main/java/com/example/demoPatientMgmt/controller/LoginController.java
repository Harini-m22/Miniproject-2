package com.example.demoPatientMgmt.controller;

import com.example.demoPatientMgmt.model.LoginDto;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private PatientService patientService;

    // Display the login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    // Process the login form submission
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginDto") LoginDto loginDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Retrieve patient using email
        Patient patient = patientService.findByEmail(loginDto.getEmail());
        if (patient != null && patient.getPassword().equals(loginDto.getPassword())) {
            // Set the patient in session so the profile and dashboard can access it
            session.setAttribute("currentPatient", patient);
            model.addAttribute("patientName", patient.getName());
            model.addAttribute("successMessage", "Login successful! Welcome, " + patient.getName());
            return "dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }
    }


    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Patient currentPatient = (Patient) session.getAttribute("currentPatient");
        if (currentPatient == null) {
            // If not logged in, redirect to login.
            return "redirect:/login";
        }
        // Add the patient's name to the model.
        model.addAttribute("patientName", currentPatient.getName());
        return "dashboard";  // Renders dashboard.html
    }
}