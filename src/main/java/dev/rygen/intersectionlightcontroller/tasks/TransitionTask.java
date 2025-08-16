package dev.rygen.intersectionlightcontroller.tasks;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.LightService;

import java.util.Timer;
import java.util.TimerTask;

public class TransitionTask extends TimerTask {

    private final Long intersectionId;
    private final IntersectionService intersectionService;
    private final LightService lightService;

    private final Timer timer = new Timer("Timer");

    public TransitionTask(Long intersectionId, IntersectionService intersectionService, LightService lightService) {
        this.intersectionId = intersectionId;
        this.intersectionService = intersectionService;
        this.lightService = lightService;
    }

    public void scheduleNextTransition() throws Exception {
        Intersection intersection = intersectionService.findById(intersectionId);
        if(intersection.isCycling()) {
            this.timer.schedule(this, intersection.getCurrentTransition().getDuration());
        }
    }

    @Override
    public void run() {
        Intersection intersection;
        try {
            intersection = intersectionService.findById(this.intersectionId);
            if (intersection.isCycling()) {
                intersection.getCurrentTransition().getTransitionActions()
                        .forEach(action -> {
                            action.getLight().setActiveColor(action.getActiveColor());
                            lightService.saveLight(action.getLight());
                        });
                intersection.nextTransition();
                this.intersectionService.saveIntersection(intersection);
                this.intersectionService.nextTransition(this.intersectionId);
            } else {
                this.timer.cancel();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
