package dev.rygen.intersectionlightcontroller.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transition_action")
public class TransitionAction {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transition_action_id")
    private Long transitionActionId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "transition-action-reference")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Transition transition;

    @Getter
    @Setter
    @ManyToOne
    private Light light;

    @Getter
    @Setter
    @Column(name = "light_color")
    @Enumerated(EnumType.STRING)
    private LightColor activeColor;
}
