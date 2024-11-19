package example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Table(name = "sortieSemaine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortieSemaine {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sortieSemaine_id")
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "sortie_semaine")
    private Date dateSortieSemaine;




    @ManyToOne(optional = false)
    @JoinColumn(name = "regle_id", nullable = false)
    @NotNull
    private Regle regle;

}
