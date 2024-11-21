package example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "livreur_id")
    private int id;

    private String codeLivreur;
    private String motDePasse;
    @Column(nullable = false)
    private String nomLivreur;
    private String prenomLivreur;
    private String numeroCartePro;
    private String telephonePortable;
    private String telephoneKobby;


    private String telephoneAlphapage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mouvement_id", nullable = false)
    @JsonManagedReference
    private Mouvement mouvement;


}

