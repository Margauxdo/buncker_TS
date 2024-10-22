package example.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "regle")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Regle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regle")
    private int id;


    private String reglePourSortie;
    @Column(name = "coderegle", unique = true, nullable = false)
    private String coderegle;

    private Date dateRegle;
    private int nombreJours;
    private Long calculCalendaire;
    private Boolean FermeJS1;
    private Boolean FermeJS2;
    private Boolean FermeJS3;
    private Boolean FermeJS4;
    private Boolean FermeJS5;
    private Boolean FermeJS6;
    private Boolean FermeJS7;
    private String typeEntree;
    private Long NBJSMEntree;

    @OneToOne(mappedBy = "regleSortie")
    private Valise valise;

    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SortieSemaine> sortieSemaine = new ArrayList<>();




    @ManyToOne
    @JoinColumn(name = "type_regle_id")
    private TypeRegle typeRegle;

    @ManyToOne
    @JoinColumn(name = "formule_id")
    private Formule formule;

}
