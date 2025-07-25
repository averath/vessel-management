package org.example.vesselsmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class VesselService {

    @Autowired
    private VesselRepository vesselRepository;

    public List<Vessel> getAllVessels() {
        return vesselRepository.findAll();
    }

    public Page<Vessel> getAllVessels(Pageable pageable) {
        return vesselRepository.findAll(pageable);
    }

    public Vessel getVesselById(Long id) {
        return vesselRepository.findById(id)
                .orElseThrow(() -> new VesselNotFoundException("Vessel not found with id: " + id));
    }

    public Vessel getVesselByImoNumber(String imoNumber) {
        return vesselRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> new VesselNotFoundException("Vessel not found with IMO number: " + imoNumber));
    }

    @Transactional
    public Vessel createVessel(Vessel vessel) {
        if (vesselRepository.findByImoNumber(vessel.getImoNumber()).isPresent()) {
            throw new IllegalArgumentException("Vessel with IMO number " + vessel.getImoNumber() + " already exists");
        }
        return vesselRepository.save(vessel);
    }

    public Vessel updateVessel(Long id, Vessel vesselDetails) {
        Vessel vessel = getVesselById(id);

        if (!vessel.getImoNumber().equals(vesselDetails.getImoNumber())) {
            Optional<Vessel> existingVessel = vesselRepository.findByImoNumber(vesselDetails.getImoNumber());
            if (existingVessel.isPresent() && !existingVessel.get().getId().equals(id)) {
                throw new IllegalArgumentException("Vessel with IMO number " + vesselDetails.getImoNumber() + " already exists");
            }
        }

        vessel.setName(vesselDetails.getName());
        vessel.setImoNumber(vesselDetails.getImoNumber());
        vessel.setType(vesselDetails.getType());
        vessel.setFlagState(vesselDetails.getFlagState());
        vessel.setYearBuilt(vesselDetails.getYearBuilt());
        vessel.setLengthMeters(vesselDetails.getLengthMeters());
        vessel.setGrossTonnage(vesselDetails.getGrossTonnage());
        vessel.setStatus(vesselDetails.getStatus());
        vessel.setLastPortOfCall(vesselDetails.getLastPortOfCall());
        vessel.setNextPortOfCall(vesselDetails.getNextPortOfCall());
        vessel.setEstimatedArrival(vesselDetails.getEstimatedArrival());

        return vesselRepository.save(vessel);
    }

    public void deleteVessel(Long id) {
        Vessel vessel = getVesselById(id);
        vesselRepository.delete(vessel);
    }

    public List<Vessel> getVesselsByType(VesselType type) {
        return vesselRepository.findByType(type);
    }

    public List<Vessel> getVesselsByStatus(VesselStatus status) {
        return vesselRepository.findByStatus(status);
    }

    public List<Vessel> getVesselsByFlagState(String flagState) {
        return vesselRepository.findByFlagState(flagState);
    }

    public List<Vessel> searchVesselsByName(String name) {
        return vesselRepository.findByNameContaining(name);
    }

    public Long getVesselCountByType(VesselType type) {
        return vesselRepository.countByType(type);
    }

    public Vessel updateVesselStatus(Long id, VesselStatus status) {
        Vessel vessel = getVesselById(id);
        vessel.setStatus(status);
        return vesselRepository.save(vessel);
    }
}