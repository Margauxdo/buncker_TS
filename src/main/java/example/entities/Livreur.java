package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "id_livreur")
    private int id;

    private String codeLivreur;
    private String motDePasse;
    private String nomLivreur;
    private String prenomLivreur;
    private String numeroCartePro;
    private String telephonePortable;
    private String telephoneKobby;
    private String telephoneAlphapage;

    //relation onetomany avec mouvement
    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL)
    private List<Mouvement> mouvements;
}

