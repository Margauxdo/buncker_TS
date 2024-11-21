package example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Probleme {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pb_id")
    private int id;

    @NotBlank(message = "required problem description")
   @Column(name = "pb_id", unique = true, nullable = false)
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
