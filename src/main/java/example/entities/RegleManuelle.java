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
    private String createurRegle;


}
