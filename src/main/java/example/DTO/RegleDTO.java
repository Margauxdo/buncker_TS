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
public class RegleDTO {
    private int id;
    private String reglePourSortie;
    private String coderegle;
    private Date dateRegle;
    private Integer nombreJours;
    private Integer calculCalendaire;
    private Boolean fermeJS1;
    private Boolean fermeJS2;
    private Boolean fermeJS3;
    private Boolean fermeJS4;
    private Boolean fermeJS5;
    private Boolean fermeJS6;
    private Boolean fermeJS7;
    private String typeEntree;
    private String typeRegle;
    private Long nbjsmEntree;
    private Integer valiseId;
    private ValiseDTO valise;
    private Integer typeRegleId;
    private Integer formuleId;
    private Integer jourFerieId;
    private List<Integer> sortieSemaineIds;
}

