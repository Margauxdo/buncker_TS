package example.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "regle")
public class Regle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cle_regle")
    private int id;

    @Column(name = "regle_pour_sortie")
    private String reglePourSortie;

    @Column(name = "code_regle", unique = true, nullable = false)
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

    // Relation ManyToOne avec Valise
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cle_valise")
    private Valise valise;

    // Relation OneToMany avec SortieSemaine
    @OneToMany(mappedBy = "regle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SortieSemaine> sortieSemaine = new ArrayList<>();

    // Relation ManyToOne avec TypeRegle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cle_type_regle")
    private TypeRegle typeRegle;

    // Relation ManyToOne avec Formule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cle_formule", nullable = true)
    private Formule formule;

    // Relation ManyToOne avec JourFerie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cle_jour_ferie")
    private JourFerie jourFerie;

    public void setSomeProperty(String someValue) {
    }

    public void setSomeField(String someValue) {
    }

    @Override
    public String toString() {
        return "Regle{" +
                "id=" + id +
                ", reglePourSortie='" + reglePourSortie + '\'' +
                ", coderegle='" + coderegle + '\'' +
                ", dateRegle=" + dateRegle +
                ", nombreJours=" + nombreJours +
                ", calculCalendaire=" + calculCalendaire +
                '}';
    }

}
