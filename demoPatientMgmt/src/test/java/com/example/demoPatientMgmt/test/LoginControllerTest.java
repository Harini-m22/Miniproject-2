package com.example.demoPatientMgmt.test;


import com.example.demoPatientMgmt.controller.LoginController;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.repository.PatientRepository;
import com.example.demoPatientMgmt.service.PatientService;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
@Import(LoginControllerTest.TestConfig.class)
public class LoginControllerTest {

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
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginDto"));
    }

    @Test
    public void testProcessLoginSuccess() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPassword("password");

        when(patientService.findByEmail(anyString())).thenReturn(patient);

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("email", "john@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("patientName", "John Doe"))
                .andExpect(model().attribute("successMessage", "Login successful! Welcome, John Doe"));
    }
}
