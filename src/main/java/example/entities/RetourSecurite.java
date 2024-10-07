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
    private int id;

    private Long numero;
    private Date datesecurite;
    private Boolean cloture;

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

    private Date dateCloture;

    //relation manytoone avec Client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
