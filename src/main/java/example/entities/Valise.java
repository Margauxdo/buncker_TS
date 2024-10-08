package example.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    private String description;

    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;

    public TypeValise getTypeValise() {
        return typevalise;
    }

    public void setTypeValise(TypeValise typeValise) {
        this.typevalise = typeValise;
    }



    private String numeroDujeu;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "regle_id")
    private Regle regleSortie;

    @Getter
    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL)
    private List<Mouvement> mouvementList;

    @ManyToOne
    @JoinColumn(name = "typevalise_id")
    private TypeValise typevalise;




}
