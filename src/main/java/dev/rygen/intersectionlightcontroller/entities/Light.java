package dev.rygen.intersectionlightcontroller.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "light")
public class Light {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "light_id")
    private Long lightId;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @ManyToOne
    @JsonBackReference(value = "road-light-reference")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Road road;

    public void setRoad(Road road) throws Exception {
        if(this.road != null) {
            throw new Exception("Light is already assigned to a road.");
        }
        this.road = road;
    }

    @Getter
    @Setter
    @Column(name = "active_color")
    @Enumerated(EnumType.STRING)
    private LightColor activeColor;
}
