package example.DTO;

import example.entity.Client;
import example.entity.Probleme;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemeDTO {
    private Integer id;
    private String descriptionProbleme;
    private String detailsProbleme;

    private Integer valiseId;
    private String valiseNumero;
    private ValiseDTO valise;

    private List<Integer> clientIds;
    private List<String> clientNoms;
    private Integer clientId;


    @NotBlank(message = "Le numéro de la valise est obligatoire.")
    private String numeroDeValise;

    private ClientDTO client;

    public ProblemeDTO(Probleme probleme) {
        this.id = probleme.getId();
        this.descriptionProbleme = probleme.getDescriptionProbleme();
        this.detailsProbleme = probleme.getDetailsProbleme();
        this.valise = probleme.getValise() != null ? new ValiseDTO(probleme.getValise()) : null;

        //this.valiseId = (probleme.getValise() != null) ? probleme.getValise().getId() : null;
        this.clientIds = probleme.getClients() != null
                ? probleme.getClients().stream().map(Client::getId).collect(Collectors.toList())
                : new ArrayList<>();
        this.clientNoms = probleme.getClients() != null
                ? probleme.getClients().stream().map(Client::getName).collect(Collectors.toList())
                : new ArrayList<>();

        this.numeroDeValise = probleme.getValise() != null ? probleme.getValise().getNumeroValise() : null;
        //this.valise = probleme.getValise() != null ? new ValiseDTO(probleme.getValise()) : null;

    }

}
