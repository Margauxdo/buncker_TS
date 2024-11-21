package example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "jourFerie")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JourFerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "jourFerie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Regle> regles;

    @ElementCollection
    @CollectionTable(name = "jourFerie_id", joinColumns = @JoinColumn(name = "jour_ferie_id"))
    @Column(name = "jourFerie_list")
    private List<Date> joursFerieList;


}
