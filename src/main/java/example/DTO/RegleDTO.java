package example.DTO;

import example.entity.TypeRegle;
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
public class RegleDTO {
    private Integer id;
    private String reglePourSortie;
    private String coderegle;
    private Date dateRegle;
    private Integer nombreJours;
    private Integer calculCalendaire = 1;
    private Boolean fermeJS1;
    private Boolean fermeJS2;
    private Boolean fermeJS3;
    private Boolean fermeJS4;
    private Boolean fermeJS5;
    private Boolean fermeJS6;
    private Boolean fermeJS7;
    private String typeEntree;
    private TypeRegle typeRegle;
    private Long nbjsmEntree;
    private Integer valiseId;
    private ValiseDTO valise;
    private Integer typeRegleId;
    private Integer formuleId;
    private FormuleDTO formule;

    private Integer jourFerieId;
    private JourFerieDTO jourFerie;
    private List<Integer> sortieSemaineIds;
}

