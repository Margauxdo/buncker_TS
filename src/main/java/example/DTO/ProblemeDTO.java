package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private List<Integer> clientIds;
    private List<String> clientNoms;
}
