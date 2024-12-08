package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "valise_id", nullable = false)
    private Valise valise;


    @OneToMany(mappedBy = "mouvement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Livreur> livreurs = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "retourSecurite_id")
    private List<RetourSecurite> retourSecurites = new ArrayList<>();



    public void addLivreur(Livreur livreur) {
        this.livreurs.add(livreur);
        livreur.setMouvement(this);
    }


    public void removeLivreur(Livreur livreur) {
        livreurs.remove(livreur);
        livreur.setMouvement(null);
    }


}
