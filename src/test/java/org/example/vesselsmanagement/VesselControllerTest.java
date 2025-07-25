package org.example.vesselsmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VesselController.class)
class VesselControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VesselService vesselService;

    @Autowired
    private ObjectMapper objectMapper;

    private Vessel testVessel;

    @BeforeEach
    void setUp() {
        testVessel = new Vessel();
        testVessel.setId(1L);
        testVessel.setName("Test Vessel");
        testVessel.setImoNumber("IMO1234567");
        testVessel.setType(VesselType.CARGO_SHIP);
        testVessel.setFlagState("Panama");
        testVessel.setStatus(VesselStatus.ACTIVE);
    }

    @Test
    void getAllVessels_ShouldReturnPagedVessels() throws Exception {
        List<Vessel> vessels = Arrays.asList(testVessel);
        Page<Vessel> page = new PageImpl<>(vessels, PageRequest.of(0, 10), 1);
        when(vesselService.getAllVessels(any())).thenReturn(page);

        mockMvc.perform(get("/api/vessels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Vessel"))
                .andExpect(jsonPath("$.content[0].imoNumber").value("IMO1234567"));

        verify(vesselService).getAllVessels(any());
    }

    @Test
    void getVesselById_WhenExists_ShouldReturnVessel() throws Exception {
        when(vesselService.getVesselById(1L)).thenReturn(testVessel);

        mockMvc.perform(get("/api/vessels/1"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.name").value("Test Vessel"))
                .andExpect(jsonPath("$.imoNumber").value("IMO1234567"));

        verify(vesselService).getVesselById(1L);
    }

    @Test
    void createVessel_WithValidData_ShouldCreateVessel() throws Exception {
        when(vesselService.createVessel(any(Vessel.class))).thenReturn(testVessel);

        mockMvc.perform(post("/api/vessels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVessel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Vessel"));

        verify(vesselService).createVessel(any(Vessel.class));
    }

    @Test
    void updateVessel_WithValidData_ShouldUpdateVessel() throws Exception {
        when(vesselService.updateVessel(eq(1L), any(Vessel.class))).thenReturn(testVessel);

        mockMvc.perform(put("/api/vessels/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVessel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Vessel"));

        verify(vesselService).updateVessel(eq(1L), any(Vessel.class));
    }

    @Test
    void deleteVessel_ShouldDeleteVessel() throws Exception {
        doNothing().when(vesselService).deleteVessel(1L);

        mockMvc.perform(delete("/api/vessels/1"))
                .andExpect(status().isNoContent());

        verify(vesselService).deleteVessel(1L);
    }

    @Test
    void getVesselsByType_ShouldReturnVesselsOfType() throws Exception {
        List<Vessel> vessels = Arrays.asList(testVessel);
        when(vesselService.getVesselsByType(VesselType.CARGO_SHIP)).thenReturn(vessels);

        mockMvc.perform(get("/api/vessels/type/CARGO_SHIP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CARGO_SHIP"));

        verify(vesselService).getVesselsByType(VesselType.CARGO_SHIP);
    }

    @Test
    void updateVesselStatus_ShouldUpdateStatus() throws Exception {
        testVessel.setStatus(VesselStatus.IN_PORT);
        when(vesselService.updateVesselStatus(1L, VesselStatus.IN_PORT)).thenReturn(testVessel);

        mockMvc.perform(patch("/api/vessels/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"IN_PORT\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PORT"));

        verify(vesselService).updateVesselStatus(1L, VesselStatus.IN_PORT);
    }
}
