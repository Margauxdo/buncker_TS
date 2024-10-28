package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_valise", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"proprietaire", "description"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeValise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeValise_id")
    private int id;

    @Column(nullable = false)
    private String proprietaire;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "typevalise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Valise> valises = new ArrayList<>();
}
