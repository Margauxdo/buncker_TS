package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valise_id")
    private int id;

    @Column(nullable = false)
    @NotNull
    private String description;
    @NotNull
    private Long numeroValise;
    private String refClient;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sortie;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDernierMouvement;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateSortiePrevue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateRetourPrevue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateCreation;
    private String numeroDujeu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_valise_id")
    private TypeValise typeValise;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valise")
    private List<Mouvement> mouvementList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valise", fetch = FetchType.EAGER)
    private List<Regle> regleSortie = new ArrayList<>();


    @Override
    public String toString() {
        return "Valise{id=" + id + ", description='" + description + "'}";
    }

}
