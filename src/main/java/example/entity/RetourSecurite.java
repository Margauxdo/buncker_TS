package example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "retourSecurite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecurite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Long numero;

    private Date datesecurite;

    private Boolean cloture;

    private Date dateCloture;

    @OneToMany(mappedBy = "retourSecurite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients;

    @ManyToOne
    @JoinColumn(name = "mouvement_id", nullable = false)
    private Mouvement mouvement;

}

