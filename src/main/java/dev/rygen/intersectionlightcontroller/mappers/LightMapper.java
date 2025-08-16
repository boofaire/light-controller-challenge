package dev.rygen.intersectionlightcontroller.mappers;

import dev.rygen.intersectionlightcontroller.dtos.LightDTO;
import dev.rygen.intersectionlightcontroller.entities.Light;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LightMapper {
    LightDTO from(Light light);
    Light to(LightDTO lightDto);
    List<LightDTO> from(List<Light> lights);
    List<Light> to(List<LightDTO> LightDTOs);
}
