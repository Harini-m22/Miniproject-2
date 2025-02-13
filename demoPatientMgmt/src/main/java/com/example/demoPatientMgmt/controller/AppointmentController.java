package com.example.demoPatientMgmt.controller;


import com.example.demoPatientMgmt.model.Appointment;
import com.example.demoPatientMgmt.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Display available appointment slots
    @GetMapping("/appointments")
    public String viewAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAvailableAppointments());
        return "appointments";  // appointments.html
    }

    // Process booking of an appointment slot
    @PostMapping("/appointments/book/{id}")
    public String bookAppointment(@PathVariable("id") Long id, Model model) {
        Appointment appointment = appointmentService.bookAppointment(id);
        if (appointment != null) {
            model.addAttribute("successMessage", "Appointment booked successfully for Dr. "
                    + appointment.getDoctorName() + " at " + appointment.getAppointmentTime());
        } else {
            model.addAttribute("errorMessage", "Appointment could not be booked. It might already be booked or does not exist.");
        }
        model.addAttribute("appointments", appointmentService.getAvailableAppointments());
        return "appointments";
    }

    // Display doctor details and schedule
    @GetMapping("/doctor/{doctorName}")
    public String viewDoctorDetails(@PathVariable("doctorName") String doctorName, Model model) {
        model.addAttribute("doctorName", doctorName);
        model.addAttribute("appointments", appointmentService.getAppointmentsByDoctor(doctorName));
        return "doctorDetails";  // doctorDetails.html
    }

    // New: List booked appointments with options to view, edit, and delete
    @GetMapping("/appointments/booked")
    public String viewBookedAppointments(Model model) {
        model.addAttribute("bookedAppointments", appointmentService.getBookedAppointments());
        return "bookedAppointments";  // bookedAppointments.html
    }

    // New: View details of a specific booked appointment
    @GetMapping("/appointments/view/{id}")
    public String viewAppointment(@PathVariable("id") Long id, Model model) {
        Appointment appointment = appointmentService.getBookedAppointments().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
        if (appointment != null) {
            model.addAttribute("appointment", appointment);
            return "viewAppointment";  // viewAppointment.html
        } else {
            model.addAttribute("errorMessage", "Appointment not found.");
            return "redirect:/appointments/booked";
        }
    }

    // New: Display the edit form for a booked appointment
    @GetMapping("/appointments/edit/{id}")
    public String showEditAppointmentForm(@PathVariable("id") Long id, Model model) {
        Appointment appointment = appointmentService.getBookedAppointments().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
        if (appointment != null) {
            model.addAttribute("appointment", appointment);
            return "editAppointment";  // editAppointment.html
        } else {
            model.addAttribute("errorMessage", "Appointment not found.");
            return "redirect:/appointments/booked";
        }
    }

    // New: Process the edit form submission
    @PostMapping("/appointments/edit/{id}")
    public String editAppointment(@PathVariable("id") Long id, @ModelAttribute("appointment") Appointment appointment,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            return "editAppointment";
        }
        Appointment updated = appointmentService.updateAppointment(id, appointment);
        if (updated != null) {
            model.addAttribute("successMessage", "Appointment updated successfully.");
            return "redirect:/appointments/booked";
        } else {
            model.addAttribute("errorMessage", "Failed to update appointment.");
            return "editAppointment";
        }
    }

    // New: Delete a booked appointment
    @PostMapping("/appointments/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Long id, Model model) {
        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            model.addAttribute("successMessage", "Appointment deleted successfully.");
        } else {
            model.addAttribute("errorMessage", "Failed to delete appointment.");
        }
        return "redirect:/appointments/booked";
    }
}