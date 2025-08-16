package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.TransitionAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitionActionRepository extends JpaRepository<TransitionAction, Long> {
}
