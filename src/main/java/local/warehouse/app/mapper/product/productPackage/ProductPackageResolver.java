package local.warehouse.app.mapper.product.productPackage;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.product.ProductPackage;
import local.warehouse.app.repository.product.ProductPackageRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPackageResolver {
    private final ProductPackageRepository productPackageRepository;
    
    public ProductPackage mapProductPackage(long productPackageId) {
        return productPackageRepository.findById(productPackageId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND)));
    }
}
