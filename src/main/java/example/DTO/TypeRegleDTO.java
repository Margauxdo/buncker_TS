package example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRegleDTO {
    private int id;
    private String nomTypeRegle;
    private Integer regleId;
}
