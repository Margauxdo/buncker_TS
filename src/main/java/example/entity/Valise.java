package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "La description est obligatoire.")
    private String description;

    @Column(name = "numero_valise", unique = true, nullable = false)
    private String numeroValise;


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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "type_valise_id", nullable = true)
    @JsonIgnore
    @ToString.Exclude
    private TypeValise typeValise;


    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "client_id", nullable = true)
    @JsonBackReference
    private Client client;



    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, orphanRemoval = true)  // , fetch = FetchType.LAZY , orphanRemoval = true
    @JsonManagedReference
    @ToString.Exclude
    private List<Mouvement> mouvements = new ArrayList<>() ;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, optional = true)  // , fetch = FetchType.EAGER    , cascade = CascadeType.ALL,
    @JsonBackReference
    @JoinColumn(name = "regle_id", nullable = true)
    @ToString.Exclude
    private Regle reglesSortie ;

}
