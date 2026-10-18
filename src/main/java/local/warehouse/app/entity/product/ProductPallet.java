package local.warehouse.app.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import local.warehouse.app.entity.Identifiable;
import local.warehouse.app.entity.item.Pallet;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.entity.workplace.WorkStation;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_pallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPallet implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "product_pallet_seq"
    )
    @SequenceGenerator(
        name = "product_pallet_seq", 
        sequenceName = "product_pallet_seq", 
        allocationSize = 50
    )
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "package_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private ProductPackage productPackage;
    @Column(nullable = false)
    private int packageAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "pallet_id", 
        referencedColumnName = "id", 
        nullable = true
    )
    private Pallet pallet;
    @Column(unique = true, nullable = false)
    private String palletNumber;
    @Column(nullable = false)
    private String groupNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "status_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Status status;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(
        name="work_station_id", 
        referencedColumnName = "id", 
        nullable = true
    )
    private WorkStation workStation;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(
        name="next_work_station_id", 
        referencedColumnName = "id", 
        nullable = true
    )
    private WorkStation nextWorkStation;
}
