package org.example.vesselsmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VesselServiceTest {

    @Mock
    private VesselRepository vesselRepository;

    @InjectMocks
    private VesselService vesselService;

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
    void getAllVessels_ShouldReturnAllVessels() {
        List<Vessel> vessels = Arrays.asList(testVessel);
        when(vesselRepository.findAll()).thenReturn(vessels);

        List<Vessel> result = vesselService.getAllVessels();

        assertEquals(1, result.size());
        assertEquals(testVessel.getName(), result.get(0).getName());
        verify(vesselRepository).findAll();
    }

    @Test
    void getVesselById_WhenExists_ShouldReturnVessel() {
        when(vesselRepository.findById(1L)).thenReturn(Optional.of(testVessel));

        Vessel result = vesselService.getVesselById(1L);

        assertEquals(testVessel.getName(), result.getName());
        assertEquals(testVessel.getImoNumber(), result.getImoNumber());
        verify(vesselRepository).findById(1L);
    }

    @Test
    void getVesselById_WhenNotExists_ShouldThrowException() {
        when(vesselRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VesselNotFoundException.class, () -> vesselService.getVesselById(1L));
        verify(vesselRepository).findById(1L);
    }

    @Test
    void createVessel_WhenImoNumberUnique_ShouldCreateVessel() {
        when(vesselRepository.findByImoNumber(testVessel.getImoNumber())).thenReturn(Optional.empty());
        when(vesselRepository.save(any(Vessel.class))).thenReturn(testVessel);

        Vessel result = vesselService.createVessel(testVessel);

        assertEquals(testVessel.getName(), result.getName());
        verify(vesselRepository).findByImoNumber(testVessel.getImoNumber());
        verify(vesselRepository).save(testVessel);
    }

    @Test
    void createVessel_WhenImoNumberExists_ShouldThrowException() {
        when(vesselRepository.findByImoNumber(testVessel.getImoNumber())).thenReturn(Optional.of(testVessel));

        assertThrows(IllegalArgumentException.class, () -> vesselService.createVessel(testVessel));
        verify(vesselRepository).findByImoNumber(testVessel.getImoNumber());
        verify(vesselRepository, never()).save(any());
    }

    @Test
    void updateVessel_WhenExists_ShouldUpdateVessel() {
        Vessel updatedDetails = new Vessel();
        updatedDetails.setName("Updated Vessel");
        updatedDetails.setImoNumber("IMO1234567");
        updatedDetails.setType(VesselType.TANKER);
        updatedDetails.setFlagState("Liberia");
        updatedDetails.setStatus(VesselStatus.IN_PORT);

        when(vesselRepository.findById(1L)).thenReturn(Optional.of(testVessel));
        when(vesselRepository.save(any(Vessel.class))).thenReturn(testVessel);

        Vessel result = vesselService.updateVessel(1L, updatedDetails);

        assertEquals("Updated Vessel", testVessel.getName());
        assertEquals(VesselType.TANKER, testVessel.getType());
        verify(vesselRepository).findById(1L);
        verify(vesselRepository).save(testVessel);
    }

    @Test
    void deleteVessel_WhenExists_ShouldDeleteVessel() {
        when(vesselRepository.findById(1L)).thenReturn(Optional.of(testVessel));

        vesselService.deleteVessel(1L);

        verify(vesselRepository).findById(1L);
        verify(vesselRepository).delete(testVessel);
    }

    @Test
    void getVesselsByType_ShouldReturnVesselsOfType() {
        List<Vessel> vessels = Arrays.asList(testVessel);
        when(vesselRepository.findByType(VesselType.CARGO_SHIP)).thenReturn(vessels);

        List<Vessel> result = vesselService.getVesselsByType(VesselType.CARGO_SHIP);

        assertEquals(1, result.size());
        assertEquals(testVessel.getType(), result.get(0).getType());
        verify(vesselRepository).findByType(VesselType.CARGO_SHIP);
    }

    @Test
    void updateVesselStatus_WhenExists_ShouldUpdateStatus() {
        when(vesselRepository.findById(1L)).thenReturn(Optional.of(testVessel));
        when(vesselRepository.save(any(Vessel.class))).thenReturn(testVessel);

        Vessel result = vesselService.updateVesselStatus(1L, VesselStatus.IN_PORT);

        assertEquals(VesselStatus.IN_PORT, testVessel.getStatus());
        verify(vesselRepository).findById(1L);
        verify(vesselRepository).save(testVessel);
    }
}
