package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.tasks.TransitionTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IntersectionService {

    private final IntersectionRepository intersectionRepo;
    private final LightService lightService;

    public Intersection createIntersection(Intersection intersection) {
        return this.intersectionRepo.save(intersection);
    }

    public Intersection createIntersection(String name) {
        Intersection intersection = Intersection.builder()
                .name(name)
                .build();
        return this.intersectionRepo.save(intersection);
    }


    public Intersection saveIntersection(Intersection intersection) {
        return this.intersectionRepo.save(intersection);
    }

    public Intersection findById(Long id) throws Exception {
        return this.intersectionRepo.findById(id).orElseThrow(Exception::new);
    }

    public List<Intersection> findAll() {
        return intersectionRepo.findAll();
    }

    public String stop(Long id) throws Exception {
        Intersection intersection = this.findById(id);
        if(!intersection.isCycling()) {
            return "Intersection is already stopped";
        }
        intersection.setCycling(false);
        intersection.setActiveTransition(0);
        for(Road road : intersection.getRoads()) {
            lightService.turnOff(road.getLights());
        }
        this.saveIntersection(intersection);
        return "Intersection cycling stopped";
    }

    public String start(Long id) throws Exception {
        Intersection intersection = this.findById(id);
        if (intersection.isCycling()) {
            return "Intersection is already cycling";
        }
        intersection.setCycling(true);
        this.saveIntersection(intersection);
        TransitionTask task = new TransitionTask(id, this, lightService);
        task.scheduleNextTransition();
        return "Intersection cycling started";
    }

    public String nextTransition(Long id) throws Exception {
        Intersection intersection = this.findById(id);
        if (!intersection.isCycling()) {
            return "Intersection is not cycling";
        }
        TransitionTask task = new TransitionTask(id, this, lightService);
        task.scheduleNextTransition();
        return "Next intersection cycle scheduled";
    }
}
