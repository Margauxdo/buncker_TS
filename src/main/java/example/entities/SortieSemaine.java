package example.entities;

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
    public void setDateSortieSemaine(Date dateSortieSemaine) {
        this.dateSortieSemaine = dateSortieSemaine;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sortieSemaine_id")
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "sortie_semaine")
    private Date dateSortieSemaine;


    public Regle getRegle() {
        return regle;
    }

    public void setRegle(Regle regle) {
        this.regle = regle;
    }

    public Date getDateSortieSemaine() {
        return dateSortieSemaine;
    }

    public void setSortieSemaine(Date sortieSemaine) {
        this.dateSortieSemaine = sortieSemaine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "regle_id", nullable = false)
    @NotNull
    private Regle regle;

}
