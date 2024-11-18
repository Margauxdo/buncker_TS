package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
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
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;
    private String adresse;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
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
    private String MemoRetourSecurite1;
    private String MemoRetourSecurite2;
    private String typeSuivie; //TODO:? verifier//

    public Client(
            int i, String s, String s1, String mail, String number, String s2, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11) {
    }


    private String codeClient;//TODO:? verifier//

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Valise> valises = new ArrayList<>();



    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Probleme> problemes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RetourSecurite> retourSecuriteList = new ArrayList<>();


    public Client(String johnDoe, String mail) {
    }
}
