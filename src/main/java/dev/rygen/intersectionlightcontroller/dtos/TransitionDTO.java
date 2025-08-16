package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TransitionDTO {
    private Long transitionId;
    private List<TransitionActionDTO> transitionActions;
    private int sequenceNumber;
    private long duration;
}
