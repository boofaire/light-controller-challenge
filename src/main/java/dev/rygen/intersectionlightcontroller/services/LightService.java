package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.LightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class LightService {

    private final LightRepository lightRepository;

    public Light createLight(Light light) {
        return this.lightRepository.save(light);
    }

    public Light createLight(String name, LightColor lightColor) {
        Light light = Light.builder()
                .name(name)
                .activeColor(lightColor)
                .build();
        return this.lightRepository.save(light);
    }

    public Light saveLight(Light light) {
        return this.lightRepository.save(light);
    }

    public List<Light> change(List<Light> lights) {
        for(Light light : lights) {
            switch(light.getActiveColor()) {
                case GREEN -> light.setActiveColor(LightColor.YELLOW);
                case YELLOW -> light.setActiveColor(LightColor.RED);
                case RED -> light.setActiveColor(LightColor.GREEN);
            }
            this.lightRepository.save(light);
        }
        return lights;
    }

    public List<Light> turnOff(List<Light> lights) {
        for(Light light : lights) {
            light.setActiveColor(LightColor.OFF);
            this.lightRepository.save(light);
        }
        return lights;
    }

    public List<Light> turnOn(List<Light> lights) {
        for(Light light : lights) {
            light.setActiveColor(LightColor.OFF);
            this.lightRepository.save(light);
        }
        return lights;
    }

  /*  public List<Light> flash(List<Light> lights) {
        for(Light light : lights) {
            if(light.setActiveColor()) {

            }
            light.setActiveColor(LightColor.OFF);
            this.lightRepository.save(light);
        }
        return lights;
    }

   */

    public Light setColor(Light light, LightColor lightColor) {
        light.setActiveColor(lightColor);
        return this.lightRepository.save(light);
    }

}
