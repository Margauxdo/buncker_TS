package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;

    public TypeValise getTypeValise() {
        return typeValise;
    }

    public void setTypeValise(TypeValise typeValise) {
        this.typeValise = typeValise;
    }

    public List<Mouvement> getMouvementList() {
        return mouvementList;
    }

    public void setMouvementList(List<Mouvement> mouvementList) {
        this.mouvementList = mouvementList;
    }

    public Regle getRegleSortie() {
        return regleSortie;
    }

    public void setRegleSortie(Regle regleSortie) {
        this.regleSortie = regleSortie;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNumeroDujeu() {
        return numeroDujeu;
    }

    public void setNumeroDujeu(String numeroDujeu) {
        this.numeroDujeu = numeroDujeu;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
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

    public Date getDateDernierMouvement() {
        return dateDernierMouvement;
    }

    public void setDateDernierMouvement(Date dateDernierMouvement) {
        this.dateDernierMouvement = dateDernierMouvement;
    }

    public Date getSortie() {
        return sortie;
    }

    public void setSortie(Date sortie) {
        this.sortie = sortie;
    }

    public String getRefClient() {
        return refClient;
    }

    public void setRefClient(String refClient) {
        this.refClient = refClient;
    }

    public Long getNumeroValise() {
        return numeroValise;
    }

    public void setNumeroValise(Long numeroValise) {
        this.numeroValise = numeroValise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String numeroDujeu;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "regle_id")
    private Regle regleSortie;

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL)
    private List<Mouvement> mouvementList;

    @ManyToOne
    @JoinColumn(name = "type_valise_id")
    private TypeValise typeValise;



}
