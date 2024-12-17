package example.DTO;

import example.entity.DateTimeFormat;
import example.entity.Regle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortieSemaineDTO {
    private Integer id; // Correspond à cle_sortie_semaine dans l'entité
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date dateSortieSemaine; // Date de sortie

    // Liste des IDs des règles associées
    private List<Integer> regleIds;

    // Liste optionnelle des codes des règles
    private List<String> regleCodes;
}
