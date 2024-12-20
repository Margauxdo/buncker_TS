package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @Column(name = "mouvement_id")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateHeureMouvement;
    private String statutSortie;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateSortiePrevue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateRetourPrevue;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "valise_id", nullable = true)
    @JsonBackReference
    private Valise valise;

    @ToString.Exclude
    @ManyToOne//(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    @JsonBackReference
    private Livreur livreur;

    @ToString.Exclude
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "retour_securite_id", nullable = true)
    @JsonBackReference
    private RetourSecurite retourSecurite ;


}
