package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecuriteDTO {
    private Integer id;
    private String numero;
    private Date datesecurite;
    private Boolean cloture;
    private Date dateCloture;
    private String mouvementStatut;
    private Integer nombreClients;  // Ajouter ce champ


    // Getters et Setters
    public Iterable<Integer> getNombreClients() {
        return Collections.singleton(nombreClients);
    }

    public void setNombreClients(Integer nombreClients) {
        this.nombreClients = nombreClients;
    }

    // Autres getters et setters...
}

