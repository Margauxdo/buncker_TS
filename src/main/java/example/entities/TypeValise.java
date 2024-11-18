package example.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @OneToMany(mappedBy = "typevalise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valise> valises = new ArrayList<>();

}
