package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="client_id")
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    @Getter@Setter
    private String name;



    private String adresse;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
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
    @Column(name = "type_suivi")
    private String typeSuivie;

    public Client(
            int i, String s, String s1, String mail, String number, String s2, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11) {
    }


    @Column(name = "code_client")
    private String codeClient;

    @ToString.Exclude
    //@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Valise> valises;



    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="probleme_id")
    @JsonBackReference
    private Probleme probleme ;


    @ToString.Exclude
    //@OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RetourSecurite> retourSecurites ;


    @ManyToOne()   // fetch = FetchType.LAZY
    @JoinColumn(name = "cle_regle")
    @JsonBackReference
    @ToString.Exclude
    private Regle regle;


    public Client(String name) {
        this.name = name;
    }

}
