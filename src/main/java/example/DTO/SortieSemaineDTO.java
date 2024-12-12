package example.DTO;

import example.entity.DateTimeFormat;
import example.entity.Regle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortieSemaineDTO {
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateSortieSemaine;

    private Integer regleId;
    private Regle regle;

    public String getRegleCode() {
        return regleCode;
    }

    public void setRegleCode(String regleCode) {
        this.regleCode = regleCode;
    }

    private String regleCode;
}
