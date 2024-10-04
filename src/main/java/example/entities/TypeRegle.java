package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "typeRegle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRegle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_typeRegle")
    private int id;

    private String typeRegle;

    //relation onetomany avec regle
    @OneToMany(mappedBy = "typeRegle", cascade = CascadeType.ALL)
    private List<Regle> listTypesRegles;
}
