package dev.rygen.intersectionlightcontroller.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "intersection")
public class Intersection {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private Long intersectionId;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @OneToMany(mappedBy = "intersection", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "intersection-transition-reference")
    @Builder.Default
    private List<Transition> transitions = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "intersection", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "intersection-road-reference")
    @Builder.Default
    private List<Road> roads = new ArrayList<>();

    @Getter
    @Setter
    @Column(name = "cycling")
    @Builder.Default
    private boolean cycling = false;

    @Getter
    @Setter
    @Column(name = "active_transition")
    @Builder.Default
    private int activeTransition = 0;

    public void setTransitions(List<Transition> transitions) throws Exception {
        // TODO: verify transaction integrity
        for(Transition transition : transitions) {
            transition.setIntersection(this);
            this.transitions.add(transition);
        }
    }

    public void addRoad(Road road) throws Exception {
        road.setIntersection(this);
        this.roads.add(road);
    }

    public Transition getCurrentTransition() throws Exception {
        if(this.activeTransition > this.transitions.size() - 1) {
            throw new Exception("No Tx found");
        }
        return this.transitions.get(this.activeTransition);
    }

    public void nextTransition() throws Exception {
        if(this.transitions.isEmpty()) {
            throw new Exception("An intersection must have at least one transition.");
        }
        this.activeTransition += 1;
        if(this.activeTransition >= this.transitions.size()) {
            this.activeTransition = this.activeTransition % this.transitions.size();
        }
    }

}
