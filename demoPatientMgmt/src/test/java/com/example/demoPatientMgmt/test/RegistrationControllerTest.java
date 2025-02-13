package com.example.demoPatientMgmt.test;


import com.example.demoPatientMgmt.controller.RegistrationController;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.repository.PatientRepository;
import com.example.demoPatientMgmt.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for testing
@Import(RegistrationControllerTest.TestConfig.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientService patientService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public PatientService patientService() {
            return Mockito.mock(PatientService.class);
        }

        @Bean
        public PatientRepository patientRepository() {
            return Mockito.mock(PatientRepository.class);
        }
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    public void testRegisterPatientSuccess() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("1234567890");
        patient.setPassword("password");
        patient.setMedicalHistory("None");

        when(patientService.registerPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/register")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("phone", "1234567890")
                        .param("password", "password")
                        .param("medicalHistory", "None"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(view().name("registration"));
    }
}