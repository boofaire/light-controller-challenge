package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionSummaryDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.entities.Transition;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.mappers.IntersectionMapper;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.services.RoadService;
import dev.rygen.intersectionlightcontroller.services.TransitionActionService;
import dev.rygen.intersectionlightcontroller.services.TransitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

    private final IntersectionService intersectionService;
    private final IntersectionMapper intersectionMapper;
    private final RoadService roadService;
    private final LightService lightService;
    private final TransitionService transitionService;
    private final TransitionActionService transitionActionService;

    @PostMapping
    public IntersectionDTO createIntersection(@RequestBody IntersectionDTO intersectionDto) {
        return this.intersectionMapper.from(this.intersectionService.createIntersection(this.intersectionMapper.to(intersectionDto)));
    }

    @PostMapping(path = "/createDefault", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public IntersectionDTO createDefaultIntersection(@RequestBody IntersectionDTO intersectionDto) throws Exception {
        return this.intersectionMapper.from(this.createDefaultIntersection(intersectionDto.getName()));
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IntersectionDTO> getAll() {
        return this.intersectionMapper.from(this.intersectionService.findAll());
    }

    @GetMapping(path = "/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IntersectionSummaryDTO> getAllSummary() {
        return this.intersectionMapper.summaryFrom(this.intersectionService.findAll());
    }

    @GetMapping(path = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public IntersectionDTO getById(@PathVariable("id") Long id) throws Exception {
        return this.intersectionMapper.from(this.intersectionService.findById(id));
    }

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String start(@RequestBody IntersectionDTO intersectionDto) throws Exception {
        return this.intersectionService.start(intersectionDto.getIntersectionId());
    }

    @PostMapping(path = "/stop", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String stop(@RequestBody IntersectionDTO intersectionDto) throws Exception {
        return this.intersectionService.stop(intersectionDto.getIntersectionId());
    }

    private Intersection createDefaultIntersection(String name) throws Exception {
        Intersection intersection = this.intersectionService.createIntersection(name);

        Road primaryRoad = this.roadService.createRoad("primary road");
        Road secondaryRoad = this.roadService.createRoad("secondary road");

        Light lightOne = this.lightService.createLight("North", LightColor.OFF);
        Light lightTwo = this.lightService.createLight("South", LightColor.OFF);
        Light lightThree = this.lightService.createLight("East", LightColor.OFF);
        Light lightFour = this.lightService.createLight("West", LightColor.OFF);

        primaryRoad.addLight(lightOne);
        primaryRoad.addLight(lightTwo);

        secondaryRoad.addLight(lightThree);
        secondaryRoad.addLight(lightFour);

        Transition transition1 = this.transitionService.createTransition(0, 1000L);
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(lightOne, LightColor.RED));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(lightTwo, LightColor.RED));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(lightThree, LightColor.GREEN));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(lightFour, LightColor.GREEN));

        Transition transition2 = this.transitionService.createTransition(1, 3000L);
        transition2.addTransitionAction(this.transitionActionService.createTransitionAction(lightThree, LightColor.YELLOW));
        transition2.addTransitionAction(this.transitionActionService.createTransitionAction(lightFour, LightColor.YELLOW));

        Transition transition3 = this.transitionService.createTransition(2, 2000L);
        transition3.addTransitionAction(this.transitionActionService.createTransitionAction(lightThree, LightColor.RED));
        transition3.addTransitionAction(this.transitionActionService.createTransitionAction(lightFour, LightColor.RED));

        Transition transition4 = this.transitionService.createTransition(3, 1000L);
        transition4.addTransitionAction(this.transitionActionService.createTransitionAction(lightOne, LightColor.GREEN));
        transition4.addTransitionAction(this.transitionActionService.createTransitionAction(lightTwo, LightColor.GREEN));

        Transition transition5 = this.transitionService.createTransition(4, 3000L);
        transition5.addTransitionAction(this.transitionActionService.createTransitionAction(lightOne, LightColor.YELLOW));
        transition5.addTransitionAction(this.transitionActionService.createTransitionAction(lightTwo, LightColor.YELLOW));

        Transition transition6 = this.transitionService.createTransition(5, 2000L);
        transition6.addTransitionAction(this.transitionActionService.createTransitionAction(lightOne, LightColor.RED));
        transition6.addTransitionAction(this.transitionActionService.createTransitionAction(lightTwo, LightColor.RED));

        intersection.addRoad(primaryRoad);
        intersection.addRoad(secondaryRoad);

        intersection.setTransitions(Arrays.asList(transition1, transition2, transition3, transition4, transition5, transition6));
        return this.intersectionService.saveIntersection(intersection);
    }
}
