package local.warehouse.app.entity.order;

import java.math.BigDecimal;

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
import local.warehouse.app.entity.product.ProductPackage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ordered_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedProduct implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "ordered_product_seq"
    )
    @SequenceGenerator(
        name = "ordered_product_seq", 
        sequenceName = "ordered_product_seq", 
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
        name = "package_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private ProductPackage productPackage;
    @Column(nullable = false)
    private BigDecimal orderedVolume;
}
