package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "typeRegle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRegle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_typeRegle")
    private int id;

    public List<Regle> getListTypesRegles() {

        return listTypesRegles;
    }

    public void setListTypesRegles(List<Regle> listTypesRegles) {
        this.listTypesRegles = listTypesRegles;
    }

    public String getTypeRegle() {

        return nomTypeRegle;
    }

    public void setTypeRegle(String typeRegle) {

        this.nomTypeRegle = nomTypeRegle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String nomTypeRegle;

    //relation onetomany avec regle
    @OneToMany(mappedBy = "typeRegle", cascade = CascadeType.ALL)
    private List<Regle> listTypesRegles;
}
