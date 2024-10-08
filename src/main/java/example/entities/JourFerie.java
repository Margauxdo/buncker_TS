package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "jourFerie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JourFerie {
    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }

    public Regle getRegle() {
        return regle;
    }

    public void setRegle(Regle regle) {
        this.regle = regle;
    }

    public List<Date> getJoursFerieList() {
        return joursFerieList;
    }

    public void setJoursFerieList(List<Date> joursFerieList) {
        this.joursFerieList = joursFerieList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jourFerie")
    private int id;



    //relation manytoone avec Regle
    @ManyToOne
    @JoinColumn(name = "regle_id")
    private Regle regle;

    //liste des jours feries
    @ElementCollection
    @CollectionTable(name = "joursFeries", joinColumns = @JoinColumn(referencedColumnName = "id_jourFerie"))
    @Column(name = "jour")
    private List<Date> joursFerieList;


}
