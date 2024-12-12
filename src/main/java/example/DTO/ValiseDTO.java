package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String typeValiseDescription;

    private String clientName;


    private ClientDTO client;
    private List<MouvementDTO> mouvementList;
    private List<RegleDTO> regleSortie;

    private List<RegleDTO> regles;
    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTypeValiseDescription() {
        return typeValiseDescription;
    }

    public void setTypeValiseDescription(String typeValiseDescription) {
        this.typeValiseDescription = typeValiseDescription;
    }
    private String typeValise;

    public String getTypeValise() {
        return typeValise;
    }

    public void setTypeValise(String typeValise) {
        this.typeValise = typeValise;
    }

}
