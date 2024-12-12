package example.entity;

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
    @Column(name = "typeRegle_id")
    private Integer id;

    @Column(name = "typeRegle_name", unique = true, nullable = false)
    private String nomTypeRegle;

    @ManyToOne(optional = true)
    @JoinColumn(name = "regle_id", nullable = true)
    private Regle regle;

    public TypeRegle(Integer id, String type1, Object o) {
        this.id = id;
        this.nomTypeRegle = nomTypeRegle;
        this.regle = regle;
    }


    public TypeRegle(String type1, Object o) {
    }
}



