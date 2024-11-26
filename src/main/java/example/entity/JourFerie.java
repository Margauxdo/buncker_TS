package example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @NotEmpty(message = "A JourFerie must have at least one associated Regle")
    @OneToMany(mappedBy = "jourFerie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Regle> regles = new ArrayList<>();



    @Column(name = "Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;


}
