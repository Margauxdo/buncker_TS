package example.entities;

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
public class RegleManuelle extends Regle {


    private String descriptionRegle;

    @NotNull(message = "Le créateur de la règle est obligatoire")
    @NotEmpty(message = "Le créateur ne peut pas être vide")
    private String createurRegle;

    public void setCreateurRegle(String createurRegle) {
        this.createurRegle = createurRegle;
    }

    public void setDescriptionRegle(String descriptionRegle) {
        this.descriptionRegle = descriptionRegle;
    }
}
