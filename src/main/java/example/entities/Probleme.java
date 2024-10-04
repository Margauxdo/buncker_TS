package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "probleme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Probleme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_probleme")
    private int id;

    private String descriptionProbleme;
    private String detailsProbleme;

    //relation manytoone avec valise
    @ManyToOne
    @JoinColumn(name = "valise_id")
    private Valise valise;

}
