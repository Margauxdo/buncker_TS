package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegleManuelleDTO {
    private Integer id; // Identifiant de la règle
    private String reglePourSortie; // Règle pour sortie
    private String coderegle; // Code de la règle
    private Date dateRegle; // Date de la règle
    private Integer nombreJours; // Nombre de jours
    private Integer calculCalendaire; // Calcul calendaire
    private Boolean fermeJS1; // Indicateur fermeture J+1
    private Boolean fermeJS2; // Indicateur fermeture J+2
    private Boolean fermeJS3; // Indicateur fermeture J+3
    private Boolean fermeJS4; // Indicateur fermeture J+4
    private Boolean fermeJS5; // Indicateur fermeture J+5
    private Boolean fermeJS6; // Indicateur fermeture J+6
    private Boolean fermeJS7; // Indicateur fermeture J+7
    private String typeEntree; // Type d'entrée
    private Long nbjsmEntree; // Nombre de jours supplémentaires en entrée
    private Integer valiseId; // ID de la valise associée
    private Integer typeRegleId; // ID du type de règle associé
    private Integer formuleId; // ID de la formule associée
    private Integer jourFerieId; // ID du jour férié associé
    private String descriptionRegle; // Description de la règle manuelle
    private String createurRegle; // Créateur de la règle manuelle
}
