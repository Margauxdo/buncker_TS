package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "type_valise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeValise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeValise_id")
    private int id;

    public List<Valise> getValises() {
        return valises;
    }

    public void setValises(List<Valise> valises) {
        this.valises = valises;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    private String proprietaire;

    //Relation onetomany avec valise
    @OneToMany(mappedBy = "typevalise", cascade = CascadeType.ALL)
    private List<Valise> valises;

}
