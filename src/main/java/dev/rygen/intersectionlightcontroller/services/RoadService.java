package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;

    public Road createRoad(Road road) {
        return this.roadRepository.save(road);
    }

    public Road createRoad(String name) {
        Road road = Road.builder()
                .name(name)
                .build();
        return this.roadRepository.save(road);
    }

    public Road saveRoad(Road road) {
        return this.roadRepository.save(road);
    }

    public Road retrieveRoad(Long id) throws Exception {
        return this.roadRepository.findById(id).orElseThrow(Exception::new);
    }
}
