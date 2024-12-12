package example.DTO;

import example.entity.Livreur;
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
public class MouvementDTO {
    private Integer id;
    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Integer valiseId;
    private List<Livreur> livreurs;
    private List<RetourSecuriteDTO> retourSecurites;
    private ValiseDTO valise;

}
