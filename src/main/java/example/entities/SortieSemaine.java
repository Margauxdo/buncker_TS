package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Date sortieSemaine;

    public Regle getRegle() {
        return regle;
    }

    public void setRegle(Regle regle) {
        this.regle = regle;
    }

    public Date getSortieSemaine() {
        return sortieSemaine;
    }

    public void setSortieSemaine(Date sortieSemaine) {
        this.sortieSemaine = sortieSemaine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //relation manytoone avec regle
    @ManyToOne
    @JoinColumn(name = "regle_id")
    private Regle regle;
}
