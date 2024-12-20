package example.DTO;

import lombok.*;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecuriteDTO {
    private Integer id;
    private Long numero; // Correspond au numéro unique du retour sécurité
    private Date datesecurite; // Date de sécurité
    private Boolean cloture; // Indique si le retour est clôturé
    private Date dateCloture; // Date de clôture du retour

    // Relation avec Client
    private Integer clientId; // ID du client associé

    private String clientNom; // Optionnel : nom du client associé

    // Relation avec Mouvement
    private List<Integer> mouvementIds; // Liste des IDs des mouvements associés
    private List<String> mouvementStatuts;

    @Getter
    @Setter
    private String mouvementStatut;
    private Integer nombreClients;

@Getter @Setter
    private List<Integer> clientIds;


    @Getter
    @Setter
    private String mouvementStatutsString;

}
