package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "probleme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Probleme {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptionProbleme() {
        return descriptionProbleme;
    }

    public void setDescriptionProbleme(String descriptionProbleme) {
        this.descriptionProbleme = descriptionProbleme;
    }

    public String getDetailsProbleme() {
        return detailsProbleme;
    }

    public void setDetailsProbleme(String detailsProbleme) {
        this.detailsProbleme = detailsProbleme;
    }

    public Valise getValise() {
        return valise;
    }

    public void setValise(Valise valise) {
        this.valise = valise;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_probleme")
    private int id;

    private String descriptionProbleme;
    private String detailsProbleme;

    //relation manytoone avec valise
    @ManyToOne
    @JoinColumn(name = "valise_id")
    private Valise valise;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


}
