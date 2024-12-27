package example.DTO;

import example.entity.Client;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import example.entity.Valise;
import example.entity.RetourSecurite;


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

    private List<Long> numeroRetourSecurite = new ArrayList<>();
    private List<String> numeroValise = new ArrayList<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.adresse = client.getAdresse();
        this.email = client.getEmail();
        this.telephoneExploitation = client.getTelephoneExploitation();
        this.ville = client.getVille();
        this.personnelEtFonction = client.getPersonnelEtFonction();
        this.ramassage1 = client.getRamassage1();
        this.ramassage2 = client.getRamassage2();
        this.ramassage3 = client.getRamassage3();
        this.ramassage4 = client.getRamassage4();
        this.ramassage5 = client.getRamassage5();
        this.ramassage6 = client.getRamassage6();
        this.ramassage7 = client.getRamassage7();
        this.envoiparDefaut = client.getEnvoiparDefaut();
        this.memoRetourSecurite1 = client.getMemoRetourSecurite1();
        this.memoRetourSecurite2 = client.getMemoRetourSecurite2();
        this.typeSuivie = client.getTypeSuivie();
        this.codeClient = client.getCodeClient();
        this.problemeId = client.getProbleme() != null ? client.getProbleme().getId() : null;
        this.regleId = client.getRegle() != null ? client.getRegle().getId() : null;

        // Conversion des IDs
        this.retourSecuriteIds = client.getRetourSecurites() != null ?
                client.getRetourSecurites().stream().map(RetourSecurite::getId).toList() : new ArrayList<>();
        this.valiseIds = client.getValises() != null ?
                client.getValises().stream().map(Valise::getId).toList() : new ArrayList<>();

        // Conversion des numéros
        this.numeroRetourSecurite = client.getRetourSecurites() != null ?
                client.getRetourSecurites().stream().map(RetourSecurite::getNumero).toList() : new ArrayList<>();
        this.numeroValise = client.getValises() != null ?
                client.getValises().stream().map(Valise::getNumeroValise).toList() : new ArrayList<>();
    }


}
