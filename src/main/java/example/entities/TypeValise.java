package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "type_valise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeValise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeValise_id")
    private int id;

    private String proprietaire;

    //Relation onetomany avec valise
    @OneToMany(mappedBy = "typevalise", cascade = CascadeType.ALL)
    private List<Valise> valises;
}
