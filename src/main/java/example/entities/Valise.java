package example.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valise")
    private int id;

    private String description;
    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;
    private String numeroDujeu;

    // Relation Many-to-One avec Client, suppression en cascade non activée pour éviter des suppressions involontaires
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "regle_id")
    private Regle regleSortie;


    // Relation One-to-Many avec Mouvement, suppression en cascade et orphanRemoval pour supprimer les mouvements liés
    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvementList;

    @ManyToOne
    @JoinColumn(name = "typevalise_id")
    private TypeValise typevalise;
}

