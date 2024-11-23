package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "valise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Utilisation de l'auto-incrémentation pour la clé primaire
    @Column(name = "valise_id")
    private int id;

    @Column(nullable = false)
    private String description;
    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;
    private String numeroDujeu;

    @ManyToOne
    @JoinColumn(name = "type_valise_id", nullable = false)  // Relation ManyToOne vers TypeValise
    @JsonBackReference
    private TypeValise typeValise;  // Chaque Valise a un TypeValise

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Regle> regleSortie = new ArrayList<>();

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvementList = new ArrayList<>();

}
