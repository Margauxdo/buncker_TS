package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "formule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formule")
    private int id;

    private String libelle;
    private String formule;

    //relation manytoone avec regle
    @ManyToOne
    @JoinColumn(name = "regle_id")
    private Regle regle;
}
