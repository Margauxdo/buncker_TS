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
    private String description;

    @NotNull(message = "L'ID de la valise est obligatoire")
    private Integer valiseId;

    @NotNull(message = "L'ID du livreur est obligatoire")
    private Integer livreurId;

    private String valiseDescription; // Ajout pour afficher la description de la valise
    private String livreurNom;

    private String valiseNumeroValise;
    private List<LivreurDTO> livreurs;
    private List<RetourSecuriteDTO> retourSecurites;

}


