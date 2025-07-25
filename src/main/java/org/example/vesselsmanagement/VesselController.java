package org.example.vesselsmanagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vessels")
@Tag(name = "Vessels", description = "Marine vessel management operations")
public class VesselController {

    @Autowired
    private VesselService vesselService;

    @GetMapping
    @Operation(summary = "Get all vessels", description = "Retrieve all vessels with pagination support")
    public ResponseEntity<Page<Vessel>> getAllVessels(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Vessel> vessels = vesselService.getAllVessels(pageable);
        return ResponseEntity.ok(vessels);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vessel by ID", description = "Retrieve a specific vessel by its ID")
    public ResponseEntity<Vessel> getVesselById(@PathVariable Long id) {
        Vessel vessel = vesselService.getVesselById(id);
        return ResponseEntity.ok(vessel);
    }

    @GetMapping("/imo/{imoNumber}")
    @Operation(summary = "Get vessel by IMO number", description = "Retrieve a vessel by its IMO number")
    public ResponseEntity<Vessel> getVesselByImoNumber(@PathVariable String imoNumber) {
        Vessel vessel = vesselService.getVesselByImoNumber(imoNumber);
        return ResponseEntity.ok(vessel);
    }

    @PostMapping
    @Operation(summary = "Create vessel", description = "Create a new vessel")
    public ResponseEntity<Vessel> createVessel(@Valid @RequestBody Vessel vessel) {
        Vessel createdVessel = vesselService.createVessel(vessel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVessel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vessel", description = "Update an existing vessel")
    public ResponseEntity<Vessel> updateVessel(@PathVariable Long id, @Valid @RequestBody Vessel vessel) {
        Vessel updatedVessel = vesselService.updateVessel(id, vessel);
        return ResponseEntity.ok(updatedVessel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vessel", description = "Delete a vessel by ID")
    public ResponseEntity<Void> deleteVessel(@PathVariable Long id) {
        vesselService.deleteVessel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search vessels", description = "Search vessels by name")
    public ResponseEntity<List<Vessel>> searchVessels(@RequestParam String name) {
        List<Vessel> vessels = vesselService.searchVesselsByName(name);
        return ResponseEntity.ok(vessels);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get vessels by type", description = "Retrieve all vessels of a specific type")
    public ResponseEntity<List<Vessel>> getVesselsByType(@PathVariable VesselType type) {
        List<Vessel> vessels = vesselService.getVesselsByType(type);
        return ResponseEntity.ok(vessels);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get vessels by status", description = "Retrieve all vessels with a specific status")
    public ResponseEntity<List<Vessel>> getVesselsByStatus(@PathVariable VesselStatus status) {
        List<Vessel> vessels = vesselService.getVesselsByStatus(status);
        return ResponseEntity.ok(vessels);
    }

    @GetMapping("/flag/{flagState}")
    @Operation(summary = "Get vessels by flag state", description = "Retrieve all vessels registered under a specific flag state")
    public ResponseEntity<List<Vessel>> getVesselsByFlagState(@PathVariable String flagState) {
        List<Vessel> vessels = vesselService.getVesselsByFlagState(flagState);
        return ResponseEntity.ok(vessels);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update vessel status", description = "Update the status of a specific vessel")
    public ResponseEntity<Vessel> updateVesselStatus(@PathVariable Long id, @RequestBody VesselStatus status) {
        Vessel updatedVessel = vesselService.updateVesselStatus(id, status);
        return ResponseEntity.ok(updatedVessel);
    }

    @GetMapping("/statistics/count-by-type/{type}")
    @Operation(summary = "Get vessel count by type", description = "Get the count of vessels by type")
    public ResponseEntity<Long> getVesselCountByType(@PathVariable VesselType type) {
        Long count = vesselService.getVesselCountByType(type);
        return ResponseEntity.ok(count);
    }
}
