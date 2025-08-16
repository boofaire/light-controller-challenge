package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IntersectionDTO {
    private long intersectionId;
    private String name;
    private List<TransitionDTO> transitions;
    private List<RoadDTO> roads;
    private boolean cycling;
    private int activeTransition;
}
