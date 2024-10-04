package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Table(name= "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_client")
    private int id;

    private String name;
    private String adresse;
    private String email;
    private String telephoneExploitation;
    private String ville;
    private String PersonnelEtFonction;

    private String ramassage1;
    private String ramassage2;
    private String ramassage3;
    private String ramassage4;
    private String ramassage5;
    private String ramassage6;
    private String ramassage7;
    private String EnvoiparDefaut;
    private String MemoRetourSecurité1;
    private String MemoRetourSecurité2;
    private String typeSuivie; //TODO:? verifier//
    private String codeClient;//TODO:? verifier//

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Valise> valises;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Probleme> problemes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<RetourSécurité> retourSécuritéList;




}
