package example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "retourSecurite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetourSécurité {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long numero;
    private Date datesecurite;
    private Boolean cloture;
    private Date dateCloture;

    //relation manytoone avec Client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
