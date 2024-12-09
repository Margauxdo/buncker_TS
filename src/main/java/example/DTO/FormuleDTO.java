package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormuleDTO {
    private int id;
    private String libelle;
    private String formule;
    private int regleId;
    private String regleCode;
}
