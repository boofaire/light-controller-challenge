package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Transition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitionRepository extends JpaRepository<Transition, Long> {
}
