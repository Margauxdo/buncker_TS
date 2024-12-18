package example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "probleme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Probleme {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "probleme_id")
    private Integer id;

    @NotBlank(message = "description is required")
   @Column(name = "probleme_description",  nullable = false)
    private String descriptionProbleme;


    @NotBlank(message = "details is required")
    @Column(name = "probleme_detail", nullable = false)
    private String detailsProbleme;

    @ManyToOne
    @JoinColumn(name = "numero_valise")//not null
    private Valise valise;




    @ToString.Exclude
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Client> clients= new ArrayList<Client>();




}
