package example.DTO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValiseDTO {
    private Integer id;
    private String description;
    private Long numeroValise;
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
    private Integer clientId;
    private List<Integer> mouvementIds;
    private List<Integer> regleSortieIds;


    @Setter
    @Getter
    private String typeValiseDescription;

    @Setter
    @Getter
    private String clientName;


    @Setter
    @Getter
    private ClientDTO client;
    private List<MouvementDTO> mouvementList;
    private List<RegleDTO> regleSortie;

    private List<RegleDTO> regles;

    @Setter
    @Getter
    private String typeValise;

}
