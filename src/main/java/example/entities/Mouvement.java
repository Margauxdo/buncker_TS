package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "mouvement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mouvement")
    private int id;

    //private Valise numeroValise;
    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;

    //Relation manytoone avec valise
    @ManyToOne
    @JoinColumn(name = "valise_id")
    private Valise valise;

    //relation manytoone avec livreur
    @ManyToOne
    @JoinColumn(name = "livreur-id")
    private Livreur livreur;

}
