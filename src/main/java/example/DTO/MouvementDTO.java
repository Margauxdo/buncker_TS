package example.DTO;

import example.entity.Livreur;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementDTO {
    private Integer id;
    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    //private String description;



    private Integer valiseId;
    private String valiseDescription;

    // Relations avec Livreur
    private Integer livreurId;
    private String livreurNom;

    // Relation avec RetourSecurite
    private Integer retourSecuriteId;

}


