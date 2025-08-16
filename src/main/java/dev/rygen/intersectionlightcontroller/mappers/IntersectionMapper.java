package dev.rygen.intersectionlightcontroller.mappers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionSummaryDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IntersectionMapper {
    IntersectionDTO from(Intersection intersection);
    IntersectionSummaryDTO summaryFrom(Intersection intersection);

    Intersection to(IntersectionDTO intersectionDTO);

    List<IntersectionDTO> from(List<Intersection> intersections);

    List<IntersectionSummaryDTO> summaryFrom(List<Intersection> intersections);

    List<Intersection> to(List<IntersectionDTO> intersectionDTOs);
}
