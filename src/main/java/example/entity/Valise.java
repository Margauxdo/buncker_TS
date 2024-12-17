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
    @NotNull
    private String description;
    @NotNull
    private Integer numeroValise;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_valise_id")
    @JsonIgnore
    @ToString.Exclude
    private TypeValise typeValise;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;



    @OneToMany(mappedBy = "valise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  // , fetch = FetchType.LAZY , orphanRemoval = true
    @JsonManagedReference
    @ToString.Exclude
    private List<Mouvement> mouvements = new ArrayList<>();

    @ManyToOne  // , fetch = FetchType.EAGER    , cascade = CascadeType.ALL,
    @JsonBackReference
    @JoinColumn(name = "cle_regle")
    @ToString.Exclude
    private Regle reglesSortie ;

}
