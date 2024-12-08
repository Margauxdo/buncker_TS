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
    @Column(name = "formule_id")
    private int id;

    @Column(nullable = false)
    @NotNull(message = "Libelle cannot be null")
    private String libelle;

    @Column(name = "formule", length = 500)
    private String formule;

    @ManyToOne
    @JoinColumn(name = "regle_id", nullable = true)
    private Regle regle;

    public Formule(String libelle, String description, Regle regle) {
        this.libelle = libelle;
        this.formule = description;
        this.regle = regle;
    }

}
