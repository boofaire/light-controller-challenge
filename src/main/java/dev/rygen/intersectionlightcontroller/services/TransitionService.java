package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Transition;
import dev.rygen.intersectionlightcontroller.repositories.TransitionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransitionService {

    private final TransitionRepository transitionRepository;

    public Transition createTransition(Transition transition) {
        return this.saveTransition(transition);
    }

    public Transition createTransition(int sequenceNumber, long duration) {
        Transition transition = Transition.builder()
                .sequenceNumber(sequenceNumber)
                .duration(duration)
                .build();
        return this.saveTransition(transition);
    }

    public Transition saveTransition(Transition transition) {
        return this.transitionRepository.save(transition);
    }

    public Transition retrieveTransition(Long id) throws Exception {
        return this.transitionRepository.findById(id).orElseThrow(Exception::new);
    }
}
