package example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "livreur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livreur_id")
    private Integer id;

    private String codeLivreur;
    private String motDePasse;
    @Column(nullable = false)
    private String nomLivreur;
    private String prenomLivreur;
    @Column(name = "numero_carte_pro")
    private String numeroCartePro;
    @Column(name = "telephone_portable")
    private String telephonePortable;
    private String telephoneKobby;


    private String telephoneAlphapage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "livreur")
    //@JoinColumn(name = "mouvement_id", nullable = true)
    @JsonIgnore
    private List<Mouvement> mouvements;

    @Getter
    @Setter
    private String description;
}

