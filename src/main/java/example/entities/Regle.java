package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "regle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regle")
    private int id;

    private String reglePourSortie;
    private String coderegle;
    //private TypeRegle typeRegle;
    private Date dateRegle;
    //private Client client;
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

    //Relation onetoone avec valise
    @OneToOne(mappedBy = "regleSortie")
    private Valise valise;

    //relation onetomany avec sortieSemaine
    @OneToMany(mappedBy = "regle", cascade = CascadeType.ALL)
    private List<SortieSemaine> sortieSemaine;

    //relation avec typeRegle
    @ManyToOne
    @JoinColumn(name="type_regle_id")
    private TypeRegle typeRegle;

    //Relation avec formule
    @ManyToOne
    @JoinColumn(name = "formule_id")
    private Formule formule;


}
