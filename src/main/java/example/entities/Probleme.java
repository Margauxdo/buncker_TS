package example.entities;

import jakarta.persistence.*;
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

    @Column(name = "description_probleme", unique = true, nullable = false)
    private String descriptionProbleme;


    private String detailsProbleme;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "valise_id")
    private Valise valise;



    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


}
