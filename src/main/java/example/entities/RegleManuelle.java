package example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
//@Table(name = "regleManuelle")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RegleManuelle extends Regle {
    private String descriptionRegle;
    private String createurRegle;

    public void setCreateurRegle(String createurRegle) {

        this.createurRegle = createurRegle;
    }

    public void setDescriptionRegle(String descriptionRegle) {

        this.descriptionRegle = descriptionRegle;
    }
}
