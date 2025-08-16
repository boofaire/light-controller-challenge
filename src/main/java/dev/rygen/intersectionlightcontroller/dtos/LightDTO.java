package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LightDTO {
    private long lightId;
    private String name;
    private LightColor activeColor;
}
