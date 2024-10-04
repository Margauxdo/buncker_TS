package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "valise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valise")
    private int id;

    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;
    private String numeroDujeu;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "regle_id")
    private Regle regleSortie;

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL)
    private List<Mouvement> mouvementList;

    @ManyToOne
    @JoinColumn(name = "type_valise_id")
    private TypeValise typeValise;



}
