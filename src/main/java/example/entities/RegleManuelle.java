package example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "regleManuelle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegleManuelle extends Regle{
    private String descriptionRegle;

    public String getCreateurRegle() {
        return createurRegle;
    }

    public void setCreateurRegle(String createurRegle) {
        this.createurRegle = createurRegle;
    }

    public String getDescriptionRegle() {
        return descriptionRegle;
    }

    public void setDescriptionRegle(String descriptionRegle) {
        this.descriptionRegle = descriptionRegle;
    }

    private String createurRegle;


}
