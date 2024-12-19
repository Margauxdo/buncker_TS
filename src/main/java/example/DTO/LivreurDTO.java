package example.DTO;

import example.entity.Livreur;
import example.entity.Mouvement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivreurDTO {
    private Integer id;
    private String codeLivreur;
    //private String motDePasse;
    private String nomLivreur;
    private String prenomLivreur;
    private String numeroCartePro;
    private String telephonePortable;
    private String telephoneKobby;
    private String telephoneAlphapage;
    private String description;

    private List<Integer> mouvementIds;
    private List<String> mouvementStatuts;
    private Integer livreurId;

    private List<String> mouvementDescriptions;


    // Constructeur pour mapper l'entité Livreur vers le DTO
    public LivreurDTO(Livreur livreur) {
        this.id = livreur.getId();
        this.codeLivreur = livreur.getCodeLivreur();
        this.nomLivreur = livreur.getNomLivreur();
        this.prenomLivreur = livreur.getPrenomLivreur();
        this.numeroCartePro = livreur.getNumeroCartePro();
        this.telephonePortable = livreur.getTelephonePortable();
        this.telephoneKobby = livreur.getTelephoneKobby();
        this.telephoneAlphapage = livreur.getTelephoneAlphapage();
        this.description = livreur.getDescription();

        // Map des mouvements associés
        if (livreur.getMouvements() != null) {
            this.mouvementIds = livreur.getMouvements().stream()
                    .map(Mouvement::getId)
                    .collect(Collectors.toList());

            this.mouvementDescriptions = livreur.getMouvements().stream()
                    .map(Mouvement::getStatutSortie) // Assurez-vous que `getStatutSortie` existe dans Mouvement
                    .collect(Collectors.toList());
        }
    }

}

