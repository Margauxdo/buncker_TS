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
    private int id;

    @Column(name = "typeRegle_name", unique = true, nullable = false)
    private String nomTypeRegle;

    @ManyToOne(cascade = CascadeType.PERSIST) // Cascade persist
    @JoinColumn(name = "regle_id", nullable = false)
    private Regle regle;
}


