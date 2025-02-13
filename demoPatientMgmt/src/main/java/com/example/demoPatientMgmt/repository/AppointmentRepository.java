package com.example.demoPatientMgmt.repository;

import com.example.demoPatientMgmt.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments that are not booked (available)
    List<Appointment> findByBookedFalse();

    // Find appointments that are booked
    List<Appointment> findByBookedTrue();

    // Find all appointments for a specific doctor
    List<Appointment> findByDoctorName(String doctorName);
}
