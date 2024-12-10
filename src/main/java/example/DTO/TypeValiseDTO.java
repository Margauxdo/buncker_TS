package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeValiseDTO {
    private int id;
    private String proprietaire;
    private String description;
    private Integer valiseId;
    private Long numeroValise;
}
