package example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "regle_manuelle")
public class RegleManuelle extends Regle {

    @Column(name = "description_regle")
    private String descriptionRegle;

    @Column(name = "createur_regle", nullable = false)
    private String createurRegle;
}

