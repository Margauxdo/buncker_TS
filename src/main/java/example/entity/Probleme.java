package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "probleme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Probleme {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "probleme_id")
    private Integer id;

    @NotBlank(message = "description is required")
   @Column(name = "probleme_description",  nullable = false)
    private String descriptionProbleme;


    @NotBlank(message = "details is required")
    @Column(name = "probleme_detail", nullable = false)
    private String detailsProbleme;

    @ManyToOne
    @JoinColumn(name = "numero_de_valise", nullable = false)
    private Valise valise;


    //@ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = true)
    @JsonBackReference
    private Client client;


}
