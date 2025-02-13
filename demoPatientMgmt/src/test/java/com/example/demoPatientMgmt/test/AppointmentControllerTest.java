package com.example.demoPatientMgmt.test;


import com.example.demoPatientMgmt.controller.AppointmentController;
import com.example.demoPatientMgmt.model.Appointment;
import com.example.demoPatientMgmt.repository.AppointmentRepository;
import com.example.demoPatientMgmt.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
@Import(AppointmentControllerTest.TestConfig.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentService appointmentService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public AppointmentService appointmentService() {
            // Provide a mock AppointmentService
            return Mockito.mock(AppointmentService.class);
        }

        @Bean
        public AppointmentRepository appointmentRepository() {
            // Provide a dummy bean to satisfy the dependency of AppointmentService
            return Mockito.mock(AppointmentRepository.class);
        }
    }

    @Test
    public void testViewAppointments() throws Exception {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setDoctorName("Dr. Smith");
        appointment1.setAppointmentTime(LocalDateTime.now().plusDays(1));
        appointment1.setBooked(false);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        appointment2.setDoctorName("Dr. Jones");
        appointment2.setAppointmentTime(LocalDateTime.now().plusDays(2));
        appointment2.setBooked(false);

        when(appointmentService.getAvailableAppointments())
                .thenReturn(Arrays.asList(appointment1, appointment2));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments"))
                .andExpect(model().attributeExists("appointments"));
    }
}