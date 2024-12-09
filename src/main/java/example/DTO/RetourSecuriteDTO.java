package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecuriteDTO {
    private int id;
    private Long numero;
    private Date datesecurite;
    private Boolean cloture;
    private Date dateCloture;
    private List<Integer> clientIds;
    private Integer mouvementId;
    private String mouvementStatut;
}
