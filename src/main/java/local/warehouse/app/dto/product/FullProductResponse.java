package local.warehouse.app.dto.product;

import java.math.BigDecimal;

import local.warehouse.app.dto.employee.organization.OrganizationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FullProductResponse extends ProductResponse {
    private String barcodeNumber;
    private BigDecimal cost;
    private OrganizationResponse producer;

    public FullProductResponse(
        long id, 
        String name, 
        String barcodeNumber, 
        BigDecimal cost, 
        OrganizationResponse producer
    ) {
        super(
            id, 
            name
        );
        this.barcodeNumber = barcodeNumber;
        this.cost = cost;
        this.producer = producer;
    }
}
