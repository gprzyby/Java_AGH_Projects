package edu.grzprzy.lab12_microservices.repositories;

import edu.grzprzy.lab12_microservices.tables.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface MeasurementRepo extends JpaRepository<Measurement, Integer> {
    @Transactional
    @Query(value = "SELECT AVG(tmp.temp) AS avg_temp, MIN(tmp.date) AS from_date, MAX(tmp.date) AS to_date " +
            "FROM(" +
            " SELECT * " +
            " FROM measurement m" +
            " WHERE m.location_id = :location_ident" +
            " ORDER BY m.measurement_id DESC" +
            " LIMIT :last_measures" +
            ") tmp", nativeQuery = true)
    Map<String, Object> getAvgTempFromMeasures(@Param("location_ident") Integer location_id, @Param("last_measures") Integer measures);
}
