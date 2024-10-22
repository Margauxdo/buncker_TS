package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public String getNomTypeRegle() {
        return nomTypeRegle;
    }

    public void setNomTypeRegle(String nomTypeRegle) {
        this.nomTypeRegle = nomTypeRegle;
    }

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

    @Column(name = "nom_type_regle", unique = true, nullable = false)
    private String nomTypeRegle;


    @OneToMany(mappedBy = "typeRegle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Regle> listTypesRegles = new ArrayList<>();

}
