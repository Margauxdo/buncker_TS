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
    @Column(name = "id_jourFerie")
    private int id;


    @OneToMany(mappedBy = "jourFerie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Regle> regles;

    @ElementCollection
    @CollectionTable(name = "joursFeries", joinColumns = @JoinColumn(referencedColumnName = "id_jourFerie"))
    @Column(name = "jour")
    private List<Date> joursFerieList;


}
