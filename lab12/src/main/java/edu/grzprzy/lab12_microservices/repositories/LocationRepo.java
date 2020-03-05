package edu.grzprzy.lab12_microservices.repositories;

import edu.grzprzy.lab12_microservices.tables.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location,Integer> {
}
