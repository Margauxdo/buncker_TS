package example.DTO;

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
public class ValiseDTO {
    private int id;
    private String description;
    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;
    private String numeroDujeu;
    private Integer typeValiseId;
    private Integer clientId;
    private List<Integer> mouvementIds;
    private List<Integer> regleSortieIds;
}
