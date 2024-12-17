package example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @Column(name = "type_regle_id")
    private Integer id;

    @Column(nullable = false)
    private String nomTypeRegle;

    @OneToMany(mappedBy = "typeRegle", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Regle> regles = new ArrayList<>();

}



