package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Column(name = "id_probleme")
    private int id;

    @NotBlank(message = "required problem description")
   @Column(name = "description_probleme", unique = true, nullable = false)
    private String descriptionProbleme;


    @NotBlank(message = "required problem details")
    private String detailsProbleme;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "valise_id")
    private Valise valise;



    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


}
