package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Road;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {
}
