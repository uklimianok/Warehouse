package local.warehouse.app.entity.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import local.warehouse.app.entity.Identifiable;
import local.warehouse.app.entity.order.OrderPallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paper_cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaperCard implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "paper_card_seq"
    )
    @SequenceGenerator(
        name = "paper_card_seq", 
        sequenceName = "paper_card_seq", 
        allocationSize = 50
    )
    private long id;
    @Column(unique = true, nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_pallet_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private OrderPallet orderPallet;
}
