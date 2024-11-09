package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "livreur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livreur")
    private int id;

    private String codeLivreur;
    private String motDePasse;
    @Column(nullable = false)
    private String nomLivreur;
    private String prenomLivreur;
    private String numeroCartePro;
    private String telephonePortable;
    private String telephoneKobby;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeLivreur() {
        return codeLivreur;
    }

    public void setCodeLivreur(String codeLivreur) {
        this.codeLivreur = codeLivreur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNomLivreur() {
        return nomLivreur;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPrenomLivreur() {
        return prenomLivreur;
    }

    public void setPrenomLivreur(String prenomLivreur) {
        this.prenomLivreur = prenomLivreur;
    }

    public String getNumeroCartePro() {
        return numeroCartePro;
    }

    public void setNumeroCartePro(String numeroCartePro) {
        this.numeroCartePro = numeroCartePro;
    }

    public String getTelephonePortable() {
        return telephonePortable;
    }

    public void setTelephonePortable(String telephonePortable) {
        this.telephonePortable = telephonePortable;
    }

    public String getTelephoneKobby() {
        return telephoneKobby;
    }

    public void setTelephoneKobby(String telephoneKobby) {
        this.telephoneKobby = telephoneKobby;
    }

    public String getTelephoneAlphapage() {
        return telephoneAlphapage;
    }

    public void setTelephoneAlphapage(String telephoneAlphapage) {
        this.telephoneAlphapage = telephoneAlphapage;
    }

    public List<Mouvement> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<Mouvement> mouvements) {
        this.mouvements = mouvements;
    }

    private String telephoneAlphapage;

    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL)
    private List<Mouvement> mouvements;
}

