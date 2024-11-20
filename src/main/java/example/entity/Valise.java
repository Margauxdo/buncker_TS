package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "valise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valise {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valise")
    private int id;

    private String description;
    private Long numeroValise;
    private String refClient;
    private Date sortie;
    private Date dateDernierMouvement;
    private Date dateSortiePrevue;
    private Date dateRetourPrevue;
    private Date dateCreation;
    private String numeroDujeu;

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Regle> regleSortie = new ArrayList<>();

    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mouvement> mouvementList = new ArrayList<>();

    @OneToMany
    @Column(name = "typeValise_id")
    private List<TypeValise> typeValiseList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnore
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void addProbleme(Probleme probleme) {

    }

    public short getRegles() {
        return 0;
    }

    public void add(Regle regle) {

    }

    public List<Object> getProblemes() {
        return List.of();
    }


}
