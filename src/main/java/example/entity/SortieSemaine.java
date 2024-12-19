package example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sortieSemaine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortieSemaine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "sortieSemaine_date")
    private Date dateSortieSemaine;

    @ToString.Exclude
    @OneToMany(mappedBy = "sortieSemaine", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Regle> regles = new ArrayList<>();


}
