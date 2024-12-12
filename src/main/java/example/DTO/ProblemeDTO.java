package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemeDTO {
    private Integer id;
    private String descriptionProbleme;
    private String detailsProbleme;
    private Integer valiseId;
    private Integer clientId;
    private ClientDTO client;
    private ValiseDTO valise;
}
