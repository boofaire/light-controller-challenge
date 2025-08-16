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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transition")
public class Transition {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transition_id")
    private Long transitionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "intersection-transition-reference")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Intersection intersection;

    @Getter
    @OneToMany(mappedBy = "transition", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "transition-action-reference")
    @Builder.Default
    private List<TransitionAction> transitionActions = new ArrayList<>();

    @Getter
    @Column(name = "sequence_number")
    private int sequenceNumber;

    @Getter
    @Column(name = "duration")
    private long duration;

    public void setIntersection(Intersection intersection) throws Exception {
        if(this.intersection != null) {
            throw new Exception("Transition is already assigned to an intersection.");
        }
        this.intersection = intersection;
    }

    public void addTransitionAction(TransitionAction transitionAction) throws Exception {
        this.ensureUniqueLightAction(transitionAction.getLight().getLightId());
        this.transitionActions.add(transitionAction);
        transitionAction.setTransition(this);
    }

    private void ensureUniqueLightAction(long lightId) throws Exception {
        if(!this.transitionActions
                .stream()
                .filter(action -> action.getLight().getLightId() == lightId)
                .collect(Collectors.toSet()).isEmpty()) {
            throw new Exception("Action for light already present in transition");
        }
    }
}
