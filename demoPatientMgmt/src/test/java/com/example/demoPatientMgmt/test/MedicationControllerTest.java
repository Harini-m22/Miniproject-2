package com.example.demoPatientMgmt.test;

import com.example.demoPatientMgmt.controller.MedicationController;
import com.example.demoPatientMgmt.model.Medication;
import com.example.demoPatientMgmt.model.Patient;
import com.example.demoPatientMgmt.repository.MedicationRepository;
import com.example.demoPatientMgmt.service.MedicationService;
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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
@Import(MedicationControllerTest.TestConfig.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationService medicationService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MedicationRepository medicationRepository() {
            // Provide a dummy (mock) bean for MedicationRepository to satisfy dependencies.
            return Mockito.mock(MedicationRepository.class);
        }

        @Bean
        public MedicationService medicationService() {
            // Provide a mock of MedicationService.
            return Mockito.mock(MedicationService.class);
        }
    }

    @Test
    public void testViewMedications() throws Exception {
        // Create a dummy patient
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");

        // Create sample medications
        Medication med1 = new Medication();
        med1.setId(1L);
        med1.setName("Ibuprofen");
        med1.setDosage("200 mg");
        med1.setInstructions("Take one tablet every 6 hours");
        med1.setPatient(patient);

        Medication med2 = new Medication();
        med2.setId(2L);
        med2.setName("Amoxicillin");
        med2.setDosage("500 mg");
        med2.setInstructions("Take one capsule every 8 hours");
        med2.setPatient(patient);

        // Stub the service call
        when(medicationService.getMedicationsByPatient(patient))
                .thenReturn(Arrays.asList(med1, med2));

        // Since the controller retrieves the patient from the session and none is set,
        // we expect a redirect to login.
        mockMvc.perform(get("/medications"))
                .andExpect(status().is3xxRedirection());
    }
}