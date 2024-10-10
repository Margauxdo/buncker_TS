package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "retourSecurite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecurite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Identifiant unique pour chaque retour sécurité

    @Column(nullable = false)
    private Long numero; // Numéro unique pour identifier le retour sécurité

    private Date datesecurite; // Date de sécurité (ex : date de retour prévue)

    private Boolean cloture; // Statut de clôture du retour (ouvert ou fermé)

    private Date dateCloture; // Date de clôture réelle du retour

    // Relation avec l'entité Client (ManyToOne car plusieurs retours peuvent être associés à un même client)
    @ManyToOne
    @JoinColumn(name = "client_id") // Clé étrangère vers Client
    private Client client;

    // Getters et Setters personnalisés (si nécessaire pour des manipulations spécifiques)
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public Boolean getCloture() {
        return cloture;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public Date getDatesecurite() {
        return datesecurite;
    }

    public void setDatesecurite(Date datesecurite) {
        this.datesecurite = datesecurite;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

