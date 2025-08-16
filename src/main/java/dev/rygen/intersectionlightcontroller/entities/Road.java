package dev.rygen.intersectionlightcontroller.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "road")
public class Road {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "road_id")
    private Long roadId;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "intersection-reference")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Intersection intersection;

    @Getter
    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "road-light-reference")
    @Builder.Default
    private List<Light> lights = new ArrayList<>();

    public void addLight(Light light) throws Exception {
        if(this.lights.size() > 1) {
            throw new Exception("A road may have at most two lights.");
        }
        light.setRoad(this);
        this.lights.add(light);

    }

    public void addLights(List<Light> lights) throws Exception {
        for(Light light : lights) {
            this.addLight(light);
        }
    }

    public void setIntersection(Intersection intersection) throws Exception {
        if(this.intersection != null) {
            throw new Exception("Road is already assigned to an intersection.");
        }
        this.intersection = intersection;
    }


}
