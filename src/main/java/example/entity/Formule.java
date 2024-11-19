package example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(nullable = false)
    private String libelle;



    private String formule;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regle_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Regle regle;


}
