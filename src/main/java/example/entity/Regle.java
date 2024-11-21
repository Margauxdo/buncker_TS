package example.entity;

import jakarta.validation.constraints.NotBlank;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "regle")
public class Regle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regle_id")
    private int id;

    private String reglePourSortie;

    @NotBlank(message = "Le code de la règle est obligatoire")
    @Column(name = "regle_code", unique = true, nullable = false)
    private String coderegle;

    private Date dateRegle;

    private int nombreJours;

    private Long calculCalendaire;

    private Boolean fermeJS1;

    private Boolean fermeJS2;

    private Boolean fermeJS3;

    private Boolean fermeJS4;

    private Boolean fermeJS5;

    private Boolean fermeJS6;

    private Boolean fermeJS7;

    private String typeEntree;

    private Long nbjsmEntree;

    // Relation ManyToOne avec Valise
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valise_id")
    private Valise valise;

    // Relation OneToMany avec SortieSemaine
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "regle")
    private List<SortieSemaine> sortieSemaine = new ArrayList<>();

    // Relation OneToMany avec TypeRegle
    @OneToMany
    @JoinColumn(name = "typeRegle_id")
    private List<TypeRegle> typeRegles = new ArrayList<>();

    // Relation ManyToOne avec RegleManuelle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regleManuelle_id")
    private RegleManuelle regleManuelle;

    // Relation ManyToOne avec Formule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formule_id", nullable = true)
    private Formule formule;

    // Relation ManyToOne avec JourFerie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jourFerie_id")
    private JourFerie jourFerie;

    // Constructeur personnalisé
    public Regle(String r001, Date date) {
        this.coderegle = r001;
        this.dateRegle = date;
    }
}
