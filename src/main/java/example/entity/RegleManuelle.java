package example.entity;

import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("MANUELLE")
public class RegleManuelle extends Regle {


    private String descriptionRegle;

    @NotNull(message = "Rule creator is required")
    @NotEmpty(message = "Creator cannot be empty")
    private String createurRegle;

}
