package example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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



    @ManyToOne
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;


}
