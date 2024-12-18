package example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Le nom du type de règle est obligatoire.")
    @Column(name = "nom_type_regle", nullable = false)
    private String nomTypeRegle;

    @Column(name = "description")
    private String description;


}




