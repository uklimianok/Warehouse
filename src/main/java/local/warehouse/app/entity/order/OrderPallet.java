package local.warehouse.app.entity.order;

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
import local.warehouse.app.entity.item.Pallet;
import local.warehouse.app.entity.service.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_pallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderPallet implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "order_pallet_seq"
    )
    @SequenceGenerator(
        name = "order_pallet_seq", 
        sequenceName = "order_pallet_seq", 
        allocationSize = 50
    )
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "pallet_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Pallet pallet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "status_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Status status;
}
