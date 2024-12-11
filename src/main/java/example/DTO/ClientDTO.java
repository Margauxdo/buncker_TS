package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Integer id;
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

    private List<Integer> valiseIds = new ArrayList<>();
    private List<String> valisesDescriptions = new ArrayList<>(); // Initialisé pour éviter les NPE

    private List<Integer> problemeIds = new ArrayList<>();
    private Integer retourSecuriteId;

    private Integer regleId;
    private List<Integer> regleIds = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
