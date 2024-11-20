package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mouvement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mouvement")
    private int id;

    private Date dateHeureMouvement;
    private String statutSortie;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;

    @OneToMany
    @JoinColumn(name = "valise_id")
    private List<Valise> valises;

    @OneToMany(mappedBy = "mouvement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Livreur> livreurs;


    @OneToMany
    @JoinColumn(name = "retourSecurite_id")
    private List<RetourSecurite> retourSecurites;
}
