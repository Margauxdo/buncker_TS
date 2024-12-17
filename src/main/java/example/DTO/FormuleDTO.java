package example.DTO;

import example.entity.Regle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormuleDTO {
    private Integer cleFormule;
    private String libelle;
    private String formule;


    private List<Integer> regleIds;
    private List<String> codeRegles;
}
