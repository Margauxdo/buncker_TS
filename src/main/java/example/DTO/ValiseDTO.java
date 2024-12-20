package example.DTO;

import example.entity.Valise;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValiseDTO {
    private Integer id;
    private String description;
    private Integer numeroValise;
    private String refClient;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sortie;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDernierMouvement;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateSortiePrevue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateRetourPrevue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateCreation;
    private String numeroDujeu;


    private Integer typeValiseId;
    private String typeValiseDescription;

    @Getter
    @Setter
    private List<MouvementDTO> mouvementList = new ArrayList<>();

    private List<Integer> mouvementIds;
    private List<Integer> regleSortieIds;
    private List<RegleDTO> reglesSortie; // Liste des règles, non plus les ID



    private Integer clientId;

    // Constructeur qui accepte une entité Valise
    public ValiseDTO(Valise valise) {
        this.id = valise.getId();
        this.description = valise.getDescription();
        this.numeroValise = Integer.valueOf(valise.getNumeroValise());
        this.refClient = valise.getClient() != null ? valise.getClient().getName() : "Non défini";
        this.clientId = valise.getClient() != null ? valise.getClient().getId() : null;
        this.sortie = valise.getSortie();
        this.dateDernierMouvement = valise.getDateDernierMouvement();
        this.dateSortiePrevue = valise.getDateSortiePrevue();
        this.dateRetourPrevue = valise.getDateRetourPrevue();
        this.dateCreation = valise.getDateCreation();
        this.numeroDujeu = valise.getNumeroDujeu();
        this.typeValiseId = valise.getTypeValise() != null ? valise.getTypeValise().getId() : null;
        this.typeValiseDescription = valise.getTypeValise() != null ? valise.getTypeValise().getDescription() : "Non défini";
        this.mouvementList = valise.getMouvements().stream()
                .map(mouvement -> MouvementDTO.builder()
                        .id(mouvement.getId())
                        .dateHeureMouvement(mouvement.getDateHeureMouvement())
                        .statutSortie(mouvement.getStatutSortie())
                        .dateSortiePrevue(mouvement.getDateSortiePrevue())
                        .dateRetourPrevue(mouvement.getDateRetourPrevue())
                        .build())
                .toList();
        this.regleSortieIds = valise.getReglesSortie() != null
                ? List.of(valise.getReglesSortie().getId())
                : null;
    }


}
