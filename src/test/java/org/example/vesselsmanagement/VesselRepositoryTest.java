package org.example.vesselsmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VesselRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VesselRepository vesselRepository;

    private Vessel testVessel;

    @BeforeEach
    void setUp() {
        testVessel = new Vessel();
        testVessel.setName("Test Cargo Ship");
        testVessel.setImoNumber("IMO1234567");
        testVessel.setType(VesselType.CARGO_SHIP);
        testVessel.setFlagState("Panama");
        testVessel.setStatus(VesselStatus.ACTIVE);
        testVessel.setYearBuilt(2020);
        testVessel.setGrossTonnage(50000.0);

        entityManager.persistAndFlush(testVessel);
    }

    @Test
    void findByImoNumber_WhenExists_ShouldReturnVessel() {
        Optional<Vessel> found = vesselRepository.findByImoNumber("IMO1234567");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Cargo Ship");
    }

    @Test
    void findByImoNumber_WhenNotExists_ShouldReturnEmpty() {
        Optional<Vessel> found = vesselRepository.findByImoNumber("IMO9999999");

        assertThat(found).isEmpty();
    }

    @Test
    void findByType_ShouldReturnVesselsOfType() {
        List<Vessel> vessels = vesselRepository.findByType(VesselType.CARGO_SHIP);

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getType()).isEqualTo(VesselType.CARGO_SHIP);
    }

    @Test
    void findByStatus_ShouldReturnVesselsWithStatus() {
        List<Vessel> vessels = vesselRepository.findByStatus(VesselStatus.ACTIVE);

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getStatus()).isEqualTo(VesselStatus.ACTIVE);
    }

    @Test
    void findByFlagState_ShouldReturnVesselsWithFlagState() {
        List<Vessel> vessels = vesselRepository.findByFlagState("Panama");

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getFlagState()).isEqualTo("Panama");
    }

    @Test
    void findByNameContaining_ShouldReturnMatchingVessels() {
        List<Vessel> vessels = vesselRepository.findByNameContaining("Cargo");

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getName()).contains("Cargo");
    }

    @Test
    void findByYearBuiltBetween_ShouldReturnVesselsInRange() {
        List<Vessel> vessels = vesselRepository.findByYearBuiltBetween(2019, 2021);

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getYearBuilt()).isBetween(2019, 2021);
    }

    @Test
    void countByType_ShouldReturnCorrectCount() {
        Long count = vesselRepository.countByType(VesselType.CARGO_SHIP);

        assertThat(count).isEqualTo(1L);
    }

    @Test
    void findByGrossTonnageGreaterThan_ShouldReturnVesselsAboveThreshold() {
        List<Vessel> vessels = vesselRepository.findByGrossTonnageGreaterThan(40000.0);

        assertThat(vessels).hasSize(1);
        assertThat(vessels.get(0).getGrossTonnage()).isGreaterThan(40000.0);
    }
}
