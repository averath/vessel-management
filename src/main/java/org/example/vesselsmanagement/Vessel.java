package org.example.vesselsmanagement;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vessels")
public class Vessel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vessel name is required")
    @Size(max = 100, message = "Vessel name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "IMO number is required")
    @Pattern(regexp = "^IMO\\d{7}$", message = "IMO number must be in format IMO followed by 7 digits")
    @Column(nullable = false, unique = true, length = 10)
    private String imoNumber;

    @NotNull(message = "Vessel type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VesselType type;

    @NotNull(message = "Flag state is required")
    @Size(max = 50, message = "Flag state must not exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String flagState;

    @Min(value = 1900, message = "Year built must be after 1900")
    @Max(value = 2030, message = "Year built cannot be in the future")
    @Column(name = "year_built")
    private Integer yearBuilt;

    @DecimalMin(value = "0.0", message = "Length must be positive")
    @Column(name = "length_meters")
    private Double lengthMeters;

    @DecimalMin(value = "0.0", message = "Gross tonnage must be positive")
    @Column(name = "gross_tonnage")
    private Double grossTonnage;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VesselStatus status;

    @Column(name = "last_port_of_call", length = 100)
    private String lastPortOfCall;

    @Column(name = "next_port_of_call", length = 100)
    private String nextPortOfCall;

    @Column(name = "estimated_arrival")
    private LocalDateTime estimatedArrival;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Vessel() {}

    public Vessel(String name, String imoNumber, VesselType type, String flagState) {
        this.name = name;
        this.imoNumber = imoNumber;
        this.type = type;
        this.flagState = flagState;
        this.status = VesselStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImoNumber() { return imoNumber; }
    public void setImoNumber(String imoNumber) { this.imoNumber = imoNumber; }

    public VesselType getType() { return type; }
    public void setType(VesselType type) { this.type = type; }

    public String getFlagState() { return flagState; }
    public void setFlagState(String flagState) { this.flagState = flagState; }

    public Integer getYearBuilt() { return yearBuilt; }
    public void setYearBuilt(Integer yearBuilt) { this.yearBuilt = yearBuilt; }

    public Double getLengthMeters() { return lengthMeters; }
    public void setLengthMeters(Double lengthMeters) { this.lengthMeters = lengthMeters; }

    public Double getGrossTonnage() { return grossTonnage; }
    public void setGrossTonnage(Double grossTonnage) { this.grossTonnage = grossTonnage; }

    public VesselStatus getStatus() { return status; }
    public void setStatus(VesselStatus status) { this.status = status; }

    public String getLastPortOfCall() { return lastPortOfCall; }
    public void setLastPortOfCall(String lastPortOfCall) { this.lastPortOfCall = lastPortOfCall; }

    public String getNextPortOfCall() { return nextPortOfCall; }
    public void setNextPortOfCall(String nextPortOfCall) { this.nextPortOfCall = nextPortOfCall; }

    public LocalDateTime getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(LocalDateTime estimatedArrival) { this.estimatedArrival = estimatedArrival; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
