package local.warehouse.app.entity.workplace;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import local.warehouse.app.entity.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Gate implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "gate_seq"
    )
    @SequenceGenerator(
        name = "gate_seq", 
        sequenceName = "gate_seq", 
        allocationSize = 1
    )
    private long id;
    @Column(unique = true, nullable = false)
    private String symbol;
}
