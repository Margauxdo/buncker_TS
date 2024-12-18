package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
//@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "regle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "regle", discriminatorType = DiscriminatorType.STRING)

public class Regle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regle_id")
    private Integer id;

    @Column(name = "regle_pour_sortie")
    private String reglePourSortie;

    @Column( nullable = false)
    private String coderegle;

    @Column(name = "date_regle")
    @Temporal(TemporalType.DATE)
    private Date dateRegle;

    @Column(name = "nombre_jours")
    private Integer nombreJours = 0;

    @Column(name = "calcul_calendaire")
    private Integer calculCalendaire =1;

    public void setCalculCalendaire(Integer calculCalendaire) {
        this.calculCalendaire = calculCalendaire != null ? calculCalendaire : 1; // Valeur par défaut
    }

    private Boolean fermeJS1= false;
    private Boolean fermeJS2 = false;
    private Boolean fermeJS3 = false;
    private Boolean fermeJS4 = false;
    private Boolean fermeJS5 = false;
    private Boolean fermeJS6 = false;
    private Boolean fermeJS7 = false;

    @Column(name = "type_entree")
    private String typeEntree;

    @Column(name = "nb_jsm_entree")
    private Long nbjsmEntree;

    @OneToMany(mappedBy = "regle")
    @JsonManagedReference
    @ToString.Exclude
    private List<Client> clients = new ArrayList<>();

    // Relation ManyToOne avec Valise
    @OneToMany(mappedBy = "reglesSortie", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Valise> valises = new ArrayList<>();

    // Relation OneToMany avec SortieSemaine
    @ManyToOne(cascade = CascadeType.ALL)   // , fetch = FetchType.EAGER, orphanRemoval = true()  //  fetch = FetchType.LAZY
    @JoinColumn(name = "cle_sortie_semaine")
    @JsonBackReference
    @ToString.Exclude
    private SortieSemaine sortieSemaine;

    // Relation ManyToOne avec TypeRegle
    @ManyToOne()  //  fetch = FetchType.LAZY
    @JoinColumn(name = "type_regle_id", referencedColumnName = "type_regle_id")
    @JsonBackReference
    @ToString.Exclude
    private TypeRegle typeRegle;

    // Relation ManyToOne avec Formule
    @ManyToOne()    //  fetch = FetchType.LAZY
    @JoinColumn(name = "formule_id", nullable = true)
    @JsonBackReference
    @ToString.Exclude
    private Formule formule;

    // Relation ManyToOne avec JourFerie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jour_ferie_id")
    private JourFerie jourFerie;

}
