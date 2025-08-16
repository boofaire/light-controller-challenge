package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoadDTO {
    private long roadId;
    private String name;
    private List<LightDTO> lights;
}
