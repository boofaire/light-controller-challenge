package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransitionActionDTO {
    private Long transitionActionId;
    private LightDTO light;
    private LightColor activeColor;
}
