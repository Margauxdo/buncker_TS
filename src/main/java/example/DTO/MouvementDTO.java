package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.kerberos.KerberosTicket;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementDTO {
    private Integer id;
    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Integer valiseId;
    private List<Integer> livreurIds;
    private List<Integer> retourSecuriteIds;


}
