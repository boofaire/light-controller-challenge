package dev.rygen.intersectionlightcontroller.mappers;

import dev.rygen.intersectionlightcontroller.dtos.RoadDTO;
import dev.rygen.intersectionlightcontroller.entities.Road;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoadMapper {
    RoadDTO from(Road road);
    Road to(RoadDTO roadDto);
    List<RoadDTO> from(List<Road> roads);
    List<Road> to(List<RoadDTO> roadDTOs);
}
