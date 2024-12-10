package example.DTO;

import example.entity.Regle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormuleDTO {
    private Integer id;
    private String libelle;
    private String formule;
    private Integer regleId;
    private String regleCode;
    private Regle regle;

}
