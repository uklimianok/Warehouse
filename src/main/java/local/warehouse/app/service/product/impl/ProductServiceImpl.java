package local.warehouse.app.service.product.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.product.ProductRequest;
import local.warehouse.app.entity.product.Product;
import local.warehouse.app.mapper.product.ProductRequestMapper;
import local.warehouse.app.repository.order.ReturnProductRepository;
import local.warehouse.app.repository.product.ProductPackageRepository;
import local.warehouse.app.repository.product.ProductRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.product.ProductService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends AbstractService<Product, Long> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ReturnProductRepository returnProductRepository;

    private final ProductRequestMapper productRequestMapper;

    @Override
    public Product read(Long id) {
        return super.read(id);
    }

    @Override
    public Product create(ProductRequest productRequest) {
        if (productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(Entity.BARCODE_NUMBER, OutputMessage.EXISTS));
        
        return modifyAndSave(new Product(), productRequest);
    }

    @Override
    public Product update(long id, ProductRequest productRequest) {
        Product product = read(id);
        boolean barcodeNumberChanged = !product.getBarcodeNumber().equals(productRequest.getBarcodeNumber());
        boolean barcodeNumberExists = productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber());
        if (barcodeNumberChanged && barcodeNumberExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(Entity.BARCODE_NUMBER, OutputMessage.EXISTS));

        return modifyAndSave(product, productRequest);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    private Product modifyAndSave(Product target, ProductRequest from) {
        productRequestMapper.convertFromRequest(from, target);
        return productRepository.save(target);
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PRODUCT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPackage = productPackageRepository.existsByProductId(id);
        boolean activeInReturnProduct = returnProductRepository.existsByProductId(id);
        return activeInProductPackage || activeInReturnProduct;
    }
}
