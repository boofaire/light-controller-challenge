package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Transition;
import dev.rygen.intersectionlightcontroller.entities.TransitionAction;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.TransitionActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransitionActionService {

    private final TransitionActionRepository transitionActionRepository;

    public TransitionAction createTransitionAction(TransitionAction transitionAction) {
        return this.saveTransitionAction(transitionAction);
    }

    public TransitionAction saveTransitionAction(TransitionAction transitionAction) {
        return this.transitionActionRepository.save(transitionAction);
    }

    public TransitionAction createTransitionAction(Light light, LightColor lightColor) {

        TransitionAction transitionAction = TransitionAction.builder()
                .light(light)
                .activeColor(lightColor)
                .build();
        return this.transitionActionRepository.save(transitionAction);
    }

    public TransitionAction retrieveTransitionAction(Long id) throws Exception {
        return this.transitionActionRepository.findById(id).orElseThrow(Exception::new);
    }
}
