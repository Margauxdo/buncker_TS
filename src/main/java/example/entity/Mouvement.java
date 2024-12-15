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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "valise_id", nullable = false)
    private Valise valise;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_livreur_id", nullable = false)
    //@JsonBackReference
    private Livreur livreur;


    @OneToMany
    @JoinColumn(name = "retourSecurite_id")
    private List<RetourSecurite> retourSecurites = new ArrayList<>();

    public Valise getValise() {
        return valise;
    }

    public void setValise(Valise valise) {
        this.valise = valise;
    }


}
