package com.example.demoPatientMgmt.service;


import com.example.demoPatientMgmt.model.Appointment;
import com.example.demoPatientMgmt.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAvailableAppointments() {
        return appointmentRepository.findByBookedFalse();
    }

    public Appointment bookAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            if (!appointment.isBooked()) {
                appointment.setBooked(true);
                // Optionally set patient information here if available
                return appointmentRepository.save(appointment);
            }
        }
        return null;
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorName) {
        return appointmentRepository.findByDoctorName(doctorName);
    }

    // New: Retrieve booked appointments (i.e. those with booked==true)
    public List<Appointment> getBookedAppointments() {
        return appointmentRepository.findByBookedTrue();
    }

    // New: Update the details of an appointment
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            Appointment appointment = opt.get();
            // Update fields (doctor name, details, and appointment time)
            appointment.setDoctorName(updatedAppointment.getDoctorName());
            appointment.setDoctorDetails(updatedAppointment.getDoctorDetails());
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    // New: Delete an appointment by its ID
    public boolean deleteAppointment(Long id) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isPresent()) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}