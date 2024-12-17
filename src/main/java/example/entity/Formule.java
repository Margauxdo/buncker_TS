package example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "formule_id")
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Libelle cannot be null")
    private String libelle;

    @Column(name = "formule", length = 500)
    private String formule;

    @ToString.Exclude
    @OneToMany(mappedBy = "formule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Regle> regles = new ArrayList<>();


}
