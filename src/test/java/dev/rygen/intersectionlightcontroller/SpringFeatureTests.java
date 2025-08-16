package dev.rygen.intersectionlightcontroller;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.entities.Transition;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.services.RoadService;
import dev.rygen.intersectionlightcontroller.services.TransitionActionService;
import dev.rygen.intersectionlightcontroller.services.TransitionService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@CucumberContextConfiguration
@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features")
public class SpringFeatureTests {
    private final IntersectionService intersectionService;
    private final RoadService roadService;
    private final LightService lightService;
    private final TransitionService transitionService;
    private final TransitionActionService transitionActionService;

    private Long intersectionId;
    private Light roadOneLightOne;

    public SpringFeatureTests(IntersectionService intersectionService,
                           RoadService roadService,
                           LightService lightService,
                           TransitionService transitionService,
                           TransitionActionService transitionActionService) {
        this.intersectionService = intersectionService;
        this.roadService = roadService;
        this.lightService = lightService;
        this.transitionService = transitionService;
        this.transitionActionService = transitionActionService;
    }

    @Given("^an intersection with two roads$")
    @Test
    public void anIntersectionWithTwoRoads() throws Exception {
        Intersection intersection = this.intersectionService.createIntersection("testTwoRoads");
        this.intersectionId = intersection.getIntersectionId();
        intersection.addRoad(this.roadService.createRoad("primary road"));
        intersection.addRoad(this.roadService.createRoad("secondary road"));
        this.intersectionService.saveIntersection(intersection);
    }

    @And("^each road has two sets of traffic lights$")
    public void eachRoadHasTwoSetsOfTrafficLights() throws Exception {
        Intersection intersection = this.intersectionService.findById(this.intersectionId);
        List<Road> roads = intersection.getRoads();
        roads.get(0).addLight(this.lightService.createLight("North", LightColor.OFF));
        roads.get(0).addLight(this.lightService.createLight("South", LightColor.OFF));
        roads.get(1).addLight(this.lightService.createLight("East", LightColor.OFF));
        roads.get(1).addLight(this.lightService.createLight("West", LightColor.OFF));
        this.intersectionService.saveIntersection(intersection);
    }

    @And("^lights have standard colors: Green, Yellow, and Red$")
    public void lightsHaveGreenYellowRedColors() {
        Assert.assertEquals("GREEN", LightColor.GREEN.toString());
        Assert.assertEquals("YELLOW", LightColor.YELLOW.toString());
        Assert.assertEquals("RED", LightColor.RED.toString());
    }

    @And("^lights cycle through colors in order: Green -> Yellow -> Red -> Green$")
    public void lightsCycleThroughColorsInOrderGreenYellowRedGreen() throws Exception {

        Intersection intersection = this.intersectionService.findById(this.intersectionId);
        List<Road> roads = intersection.getRoads();
        List<Light> roadOneLights = roads.get(0).getLights();
        List<Light> roadTwoLights = roads.get(1).getLights();

        Transition transition1 = this.transitionService.createTransition(0, 1000L);
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(0), LightColor.RED));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(1), LightColor.RED));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(0), LightColor.GREEN));
        transition1.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(1), LightColor.GREEN));

        Transition transition2 = this.transitionService.createTransition(1, 3000L);
        transition2.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(0), LightColor.YELLOW));
        transition2.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(1), LightColor.YELLOW));

        Transition transition3 = this.transitionService.createTransition(2, 2000L);
        transition3.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(0), LightColor.RED));
        transition3.addTransitionAction(this.transitionActionService.createTransitionAction(roadTwoLights.get(1), LightColor.RED));

        Transition transition4 = this.transitionService.createTransition(3, 1000L);
        transition4.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(0), LightColor.GREEN));
        transition4.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(1), LightColor.GREEN));

        Transition transition5 = this.transitionService.createTransition(4, 3000L);
        transition5.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(0), LightColor.YELLOW));
        transition5.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(1), LightColor.YELLOW));

        Transition transition6 = this.transitionService.createTransition(5, 2000L);
        transition6.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(0), LightColor.RED));
        transition6.addTransitionAction(this.transitionActionService.createTransitionAction(roadOneLights.get(1), LightColor.RED));

        intersection.setTransitions(Arrays.asList(transition1, transition2, transition3, transition4, transition5, transition6));
        this.intersectionService.saveIntersection(intersection);
    }

    @When("^I activate the lights for a specific intersection$")
    public void iActivateTheLightsForASpecificIntersection() throws Exception {
        this.intersectionService.start(this.intersectionId);
    }

    @Then("^the lights should start their color cycling sequence$")
    public void theLightsShouldStartTheirColorCyclingSequence() throws Exception {
        List<Road> roads = this.intersectionService.findById(this.intersectionId).getRoads();
        List<Light> roadOneLights = roads.get(0).getLights();
        List<Light> roadTwoLights = roads.get(1).getLights();
        Thread.sleep(2000);
        roads = this.intersectionService.findById(this.intersectionId).getRoads();
        List<Light> roadOneLightsChanged = roads.get(0).getLights();
        List<Light> roadTwoLightsChanged = roads.get(1).getLights();
        Assert.assertNotEquals(roadOneLights.get(0).getActiveColor(), roadOneLightsChanged.get(0).getActiveColor());
        Assert.assertNotEquals(roadOneLights.get(1).getActiveColor(), roadOneLightsChanged.get(1).getActiveColor());
        Assert.assertNotEquals(roadTwoLights.get(0).getActiveColor(), roadTwoLightsChanged.get(0).getActiveColor());
        Assert.assertNotEquals(roadTwoLights.get(1).getActiveColor(), roadTwoLightsChanged.get(1).getActiveColor());
    }

    @And("^each road's lights should operate independently$")
    public void eachRoadSLightsShouldOperateIndependently() throws Exception {
        Thread.sleep(2000);
        List<Road> roads = this.intersectionService.findById(this.intersectionId).getRoads();
        List<Light> roadOneLights = roads.get(0).getLights();
        List<Light> roadTwoLights = roads.get(1).getLights();
        Assert.assertNotEquals(roadOneLights.get(0).getActiveColor(), roadTwoLights.get(0).getActiveColor());
        Assert.assertNotEquals(roadOneLights.get(1).getActiveColor(), roadTwoLights.get(1).getActiveColor());
    }

    @Given("^the lights are currently active at an intersection$")
    public void theLightsAreCurrentlyActiveAtAnIntersection() throws Exception {
        this.intersectionService.stop(this.intersectionId);
        this.intersectionService.start(this.intersectionId);
        Assert.assertTrue(this.intersectionService.findById(this.intersectionId).isCycling());
    }

    @When("^I deactivate the lights for that intersection$")
    public void iDeactivateTheLightsForThatIntersection() throws Exception {
        this.intersectionService.stop(this.intersectionId);
        Assert.assertFalse(this.intersectionService.findById(this.intersectionId).isCycling());
    }

    @Then("^all lights at the intersection should be turned off$")
    public void allLightsAtTheIntersectionShouldBeTurnedOff() throws Exception {
        List<Road> roads = this.intersectionService.findById(this.intersectionId).getRoads();
        for(Light light : roads.get(0).getLights()) {
            Assert.assertEquals(LightColor.OFF, light.getActiveColor());
        }
        for(Light light : roads.get(1).getLights()) {
            Assert.assertEquals(LightColor.OFF, light.getActiveColor());
        }
    }

    @Given("^an existing intersection$")
    public void anExistingIntersection() throws Exception {
        Assert.assertNotNull(this.intersectionService.findById(this.intersectionId));
    }

    @When("^I update the configuration for a specific light$")
    public void iUpdateTheConfigurationForASpecificLight() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Update transitionActions instead of lights");
    }

    @Then("^the light's parameters should be modified as requested$")
    public void theLightSParametersShouldBeModifiedAsRequested() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Update transitionActions instead of lights");
    }

    @And("^the change should be immediately reflected in the light's behavior$")
    public void theChangeShouldBeImmediatelyReflectedInTheLightSBehavior() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Update transitionActions instead of lights");
    }

    @When("^I request the configuration for a specific light$")
    public void iRequestTheConfigurationForASpecificLight() throws Exception {
        List<Road> roads = this.intersectionService.findById(this.intersectionId).getRoads();
        this.roadOneLightOne = roads.get(0).getLights().get(0);
    }

    @Then("^I should receive the current settings for that light$")
    public void iShouldReceiveTheCurrentSettingsForThatLight() {
        Assert.assertNotNull(this.roadOneLightOne);
    }

    @And("^the configuration should include all relevant parameters$")
    public void theConfigurationShouldIncludeAllRelevantParameters() {
        Assert.assertEquals(1L, (long) this.roadOneLightOne.getLightId());
        Assert.assertEquals("North", this.roadOneLightOne.getName());
        Assert.assertEquals(1L, (long) this.roadOneLightOne.getRoad().getRoadId());
        Assert.assertEquals(LightColor.OFF, this.roadOneLightOne.getActiveColor());
    }

    @Given("^an intersection with multiple lights$")
    public void anIntersectionWithMultipleLights() throws Exception {
        List<Road> roads = this.intersectionService.findById(this.intersectionId).getRoads();
        for(Road road : roads) {
            Assert.assertEquals(2, road.getLights().size());
        }
    }

    @When("^the lights are activated$")
    public void theLightsAreActivated() throws Exception {
        this.intersectionService.start(this.intersectionId);
    }

    @Then("^the lights on each road should be coordinated$")
    public void theLightsOnEachRoadShouldBeCoordinated() throws Exception {
        Thread.sleep(1100);
        Intersection intersection = this.intersectionService.findById(this.intersectionId);
        List<Light> lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(lights.get(0).getActiveColor(), lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(lights.get(0).getActiveColor(), lights.get(1).getActiveColor());
    }

    @And("^no conflicting signals should be displayed simultaneously$")
    public void noConflictingSignalsShouldBeDisplayedSimultaneously() throws Exception {
        Thread.sleep(3100);
        Intersection intersection = this.intersectionService.findById(this.intersectionId);
        List<Light> lights = intersection.getRoads().get(0).getLights();
        List<Light> lightsTwo = intersection.getRoads().get(1).getLights();
        Assert.assertNotEquals(lights.get(0).getActiveColor(), lightsTwo.get(0).getActiveColor());
        Assert.assertNotEquals(lights.get(1).getActiveColor(), lightsTwo.get(1).getActiveColor());
    }

    @Given("^a traffic light at an intersection$")
    public void aTrafficLightAtAnIntersection() throws Exception {
       Assert.assertNotNull(this.intersectionService.findById(this.intersectionId));
    }

    @When("^the light starts its cycle$")
    public void theLightStartsItsCycle() throws Exception {
        this.intersectionService.stop(this.intersectionId);
        this.intersectionService.start(this.intersectionId);
    }

    @Then("^it should progress through colors in order$")
    public void itShouldProgressThroughColorsInOrder() throws Exception {
        Thread.sleep(1100);
        Intersection intersection = this.intersectionService.findById(this.intersectionId);
        List<Light> lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.GREEN, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.GREEN, lights.get(1).getActiveColor());

        Thread.sleep(3100);
        intersection = this.intersectionService.findById(this.intersectionId);
        lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.YELLOW, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.YELLOW, lights.get(1).getActiveColor());

        Thread.sleep(2100);
        intersection = this.intersectionService.findById(this.intersectionId);
        lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());

        Thread.sleep(1100);
        intersection = this.intersectionService.findById(this.intersectionId);
        lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.GREEN, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.GREEN, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());

        Thread.sleep(3100);
        intersection = this.intersectionService.findById(this.intersectionId);
        lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.YELLOW, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.YELLOW, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());

        Thread.sleep(2100);
        intersection = this.intersectionService.findById(this.intersectionId);
        lights = intersection.getRoads().get(0).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());
        lights = intersection.getRoads().get(1).getLights();
        Assert.assertEquals(LightColor.RED, lights.get(0).getActiveColor());
        Assert.assertEquals(LightColor.RED, lights.get(1).getActiveColor());
    }
}
