package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "retour_securite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSecurite {

    @Id
    @JoinColumn(name = "retour_securite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long numero;

    private Date datesecurite;

    private Boolean cloture;

    private Date dateCloture;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    private Client client;

    @ToString.Exclude
    @OneToMany(mappedBy = "retourSecurite", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Mouvement> mouvements = new ArrayList<>();



}

