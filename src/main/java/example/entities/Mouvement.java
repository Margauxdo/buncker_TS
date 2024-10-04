package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "mouvement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mouvement")
    private int id;

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    public Valise getValise() {
        return valise;
    }

    public void setValise(Valise valise) {
        this.valise = valise;
    }

    public Date getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(Date dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Date getDateSortiePrevue() {
        return dateSortiePrevue;
    }

    public void setDateSortiePrevue(Date dateSortiePrevue) {
        this.dateSortiePrevue = dateSortiePrevue;
    }

    public String getStatutSortie() {
        return statutSortie;
    }

    public void setStatutSortie(String statutSortie) {
        this.statutSortie = statutSortie;
    }

    public Date getDateHeureMouvement() {
        return dateHeureMouvement;
    }

    public void setDateHeureMouvement(Date dateHeureMouvement) {
        this.dateHeureMouvement = dateHeureMouvement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //private Valise numeroValise;
    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;

    //Relation manytoone avec valise
    @ManyToOne
    @JoinColumn(name = "valise_id")
    private Valise valise;

    //relation manytoone avec livreur
    @ManyToOne
    @JoinColumn(name = "livreur-id")
    private Livreur livreur;

}
