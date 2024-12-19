package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}

