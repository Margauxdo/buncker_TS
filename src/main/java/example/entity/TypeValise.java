package example.entity;

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
    @Column(name = "type_valise_id")
    private Integer id;

    @Column(nullable = false)
    @NotNull
    private String proprietaire;


    @Column(nullable = false)
    @NotNull
    private String description;

}
