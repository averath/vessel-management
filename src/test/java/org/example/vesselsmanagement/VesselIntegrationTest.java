package org.example.vesselsmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class VesselIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VesselRepository vesselRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Vessel testVessel;

    @BeforeEach
    void setUp() {
        vesselRepository.deleteAll();

        testVessel = new Vessel();
        testVessel.setName("Integration Test Vessel");
        testVessel.setImoNumber("IMO7654321");
        testVessel.setType(VesselType.CONTAINER_SHIP);
        testVessel.setFlagState("Marshall Islands");
        testVessel.setStatus(VesselStatus.ACTIVE);
        testVessel.setYearBuilt(2018);
        testVessel.setLengthMeters(300.0);
        testVessel.setGrossTonnage(75000.0);
    }

    @Test
    void createVessel_EndToEnd_ShouldWorkCorrectly() throws Exception {
        mockMvc.perform(post("/api/vessels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVessel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test Vessel"))
                .andExpect(jsonPath("$.imoNumber").value("IMO7654321"))
                .andExpect(jsonPath("$.type").value("CONTAINER_SHIP"));
    }

    @Test
    void getVesselByIdAfterCreation_ShouldReturnCreatedVessel() throws Exception {
        Vessel savedVessel = vesselRepository.save(testVessel);

        mockMvc.perform(get("/api/vessels/" + savedVessel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Vessel"))
                .andExpect(jsonPath("$.imoNumber").value("IMO7654321"));
    }

    @Test
    void updateVessel_EndToEnd_ShouldWorkCorrectly() throws Exception {
        Vessel savedVessel = vesselRepository.save(testVessel);

        savedVessel.setName("Updated Integration Test Vessel");
        savedVessel.setStatus(VesselStatus.IN_PORT);

        mockMvc.perform(put("/api/vessels/" + savedVessel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedVessel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Integration Test Vessel"))
                .andExpect(jsonPath("$.status").value("IN_PORT"));
    }

    @Test
    void deleteVessel_EndToEnd_ShouldWorkCorrectly() throws Exception {
        Vessel savedVessel = vesselRepository.save(testVessel);

        mockMvc.perform(delete("/api/vessels/" + savedVessel.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vessels/" + savedVessel.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchVesselsByType_EndToEnd_ShouldWorkCorrectly() throws Exception {
        vesselRepository.save(testVessel);

        mockMvc.perform(get("/api/vessels/type/CONTAINER_SHIP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CONTAINER_SHIP"))
                .andExpect(jsonPath("$[0].name").value("Integration Test Vessel"));
    }
}
