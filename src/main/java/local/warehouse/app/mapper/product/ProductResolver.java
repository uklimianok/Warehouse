package local.warehouse.app.mapper.product;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.product.Product;
import local.warehouse.app.repository.product.ProductRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductResolver {
    private final ProductRepository productRepository;

    public Product mapProduct(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.PRODUCT, OutputMessage.NOT_FOUND)));
    }
}
