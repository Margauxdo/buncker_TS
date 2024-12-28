package example.DTO;

import example.entity.Formule;
import example.entity.TypeRegle;
import example.entity.Valise;
import lombok.*;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegleDTO {
    private Integer id;
    private String reglePourSortie;
    private String coderegle;
    private Date dateRegle;
    private Integer nombreJours;
    private Integer calculCalendaire = 1;
    private Boolean fermeJS1 = false;
    private Boolean fermeJS2 = false;
    private Boolean fermeJS3 = false;
    private Boolean fermeJS4 = false;
    private Boolean fermeJS5 = false;
    private Boolean fermeJS6 = false;
    private Boolean fermeJS7 = false;



    private String typeEntree;
    private Long nbjsmEntree;

    // Relations simplifiées
    private Integer typeRegleId; // ID du TypeRegle associé
    private Integer formuleId; // ID de la Formule associée
    private Integer jourFerieId; // ID du JourFerie associé
    @Getter @Setter
    private Date jourFerieDate;
    private Integer sortieSemaineId; // ID de SortieSemaine associé
    private List<Integer> valiseIds; // Liste des IDs des Valises associées
    private List<Integer> clientIds;

    @Getter
    @Setter
    private TypeRegle typeRegle;


    private String typeRegleNom;

    @Getter
    @Setter
    private Valise valise;

    @Getter
    @Setter
    private String formule;
    @Getter @Setter
    private Integer valiseId;
    private String valiseNumero;

    public RegleDTO(Integer id, String coderegle) {
        this.id = id;
        this.coderegle = coderegle;
    }

}
