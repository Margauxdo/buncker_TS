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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReglePourSortie() {
        return reglePourSortie;
    }

    public void setReglePourSortie(String reglePourSortie) {
        this.reglePourSortie = reglePourSortie;
    }

    public String getCoderegle() {
        return coderegle;
    }

    public void setCoderegle(String coderegle) {
        this.coderegle = coderegle;
    }

    public Date getDateRegle() {
        return dateRegle;
    }

    public void setDateRegle(Date dateRegle) {
        this.dateRegle = dateRegle;
    }

    public int getNombreJours() {
        return nombreJours;
    }

    public void setNombreJours(int nombreJours) {
        this.nombreJours = nombreJours;
    }

    public Long getCalculCalendaire() {
        return calculCalendaire;
    }

    public void setCalculCalendaire(Long calculCalendaire) {
        this.calculCalendaire = calculCalendaire;
    }

    public Boolean getFermeJS1() {
        return FermeJS1;
    }

    public void setFermeJS1(Boolean fermeJS1) {
        FermeJS1 = fermeJS1;
    }

    public Boolean getFermeJS2() {
        return FermeJS2;
    }

    public void setFermeJS2(Boolean fermeJS2) {
        FermeJS2 = fermeJS2;
    }

    public Boolean getFermeJS3() {
        return FermeJS3;
    }

    public void setFermeJS3(Boolean fermeJS3) {
        FermeJS3 = fermeJS3;
    }

    public Boolean getFermeJS4() {
        return FermeJS4;
    }

    public void setFermeJS4(Boolean fermeJS4) {
        FermeJS4 = fermeJS4;
    }

    public Boolean getFermeJS5() {
        return FermeJS5;
    }

    public void setFermeJS5(Boolean fermeJS5) {
        FermeJS5 = fermeJS5;
    }

    public Boolean getFermeJS6() {
        return FermeJS6;
    }

    public void setFermeJS6(Boolean fermeJS6) {
        FermeJS6 = fermeJS6;
    }

    public Boolean getFermeJS7() {
        return FermeJS7;
    }

    public void setFermeJS7(Boolean fermeJS7) {
        FermeJS7 = fermeJS7;
    }

    public String getTypeEntree() {
        return typeEntree;
    }

    public void setTypeEntree(String typeEntree) {
        this.typeEntree = typeEntree;
    }

    public Long getNBJSMEntree() {
        return NBJSMEntree;
    }

    public void setNBJSMEntree(Long NBJSMEntree) {
        this.NBJSMEntree = NBJSMEntree;
    }

    public Valise getValise() {
        return valise;
    }

    public void setValise(Valise valise) {
        this.valise = valise;
    }

    public List<SortieSemaine> getSortieSemaine() {
        return sortieSemaine;
    }

    public void setSortieSemaine(List<SortieSemaine> sortieSemaine) {
        this.sortieSemaine = sortieSemaine;
    }

    public TypeRegle getTypeRegle() {
        return typeRegle;
    }

    public void setTypeRegle(TypeRegle typeRegle) {
        this.typeRegle = typeRegle;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

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
