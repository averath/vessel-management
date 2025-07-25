package org.example.vesselsmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {

    Optional<Vessel> findByImoNumber(String imoNumber);

    List<Vessel> findByType(VesselType type);

    List<Vessel> findByStatus(VesselStatus status);

    List<Vessel> findByFlagState(String flagState);

    @Query("SELECT v FROM Vessel v WHERE v.name LIKE %:name%")
    List<Vessel> findByNameContaining(@Param("name") String name);

    @Query("SELECT v FROM Vessel v WHERE v.yearBuilt BETWEEN :startYear AND :endYear")
    List<Vessel> findByYearBuiltBetween(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);

    @Query("SELECT COUNT(v) FROM Vessel v WHERE v.type = :type")
    Long countByType(@Param("type") VesselType type);

    @Query("SELECT v FROM Vessel v WHERE v.grossTonnage > :tonnage")
    List<Vessel> findByGrossTonnageGreaterThan(@Param("tonnage") Double tonnage);

    @Query("SELECT v FROM Vessel v WHERE v.flagState = :flagState AND v.status = :status")
    List<Vessel> findByFlagStateAndStatus(@Param("flagState") String flagState, @Param("status") VesselStatus status);
}
