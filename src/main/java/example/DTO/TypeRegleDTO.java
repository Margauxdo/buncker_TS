package example.DTO;

import example.entity.Regle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRegleDTO {
    private Integer id;
    private String nomTypeRegle;
    private List<Integer> regleIds;

    private List<String> regleNoms;
}
