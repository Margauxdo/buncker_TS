package example.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Integer id;
    @Setter
    @Getter
    private String name;
    private String adresse;
    private String email;
    private String telephoneExploitation;
    private String ville;
    private String personnelEtFonction;
    private String ramassage1;
    private String ramassage2;
    private String ramassage3;
    private String ramassage4;
    private String ramassage5;
    private String ramassage6;
    private String ramassage7;
    private String envoiparDefaut;
    private String memoRetourSecurite1;
    private String memoRetourSecurite2;
    private String typeSuivie;
    private String codeClient;

   private Integer problemeId;

    private List<Integer> retourSecuriteIds = new ArrayList<>();
    private Integer regleId;

    public List<Integer> valiseIds = new ArrayList<>();
    private List<Integer> problemeIds = new ArrayList<>();
    private Integer retourSecuriteId;

}
