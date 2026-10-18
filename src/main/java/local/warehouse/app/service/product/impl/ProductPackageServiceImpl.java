package local.warehouse.app.service.product.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.product.productPackage.ProductPackageRequest;
import local.warehouse.app.entity.product.ProductPackage;
import local.warehouse.app.mapper.product.productPackage.ProductPackageRequestMapper;
import local.warehouse.app.repository.order.OrderedProductRepository;
import local.warehouse.app.repository.order.PickedProductRepository;
import local.warehouse.app.repository.product.ProductPackageRepository;
import local.warehouse.app.repository.product.ProductPalletRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.product.ProductPackageService;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPackageServiceImpl extends AbstractService<ProductPackage, Long> implements ProductPackageService {
    private final ProductPackageRepository productPackageRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final PickedProductRepository pickedProductRepository;

    private final ProductPackageRequestMapper productPackageRequestMapper;

    @Override
    public ProductPackage read(Long id) {
        return super.read(id);
    }

    @Override
    public ProductPackage create(ProductPackageRequest productPackageRequest) {
        return modifyAndSave(new ProductPackage(), productPackageRequest);
    }

    @Override
    public ProductPackage update(long id, ProductPackageRequest productPackageRequest) {
        return modifyAndSave(read(id), productPackageRequest);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    private ProductPackage modifyAndSave(ProductPackage target, ProductPackageRequest from) {
        productPackageRequestMapper.convertFromRequest(from, target);
        return productPackageRepository.save(target);
    }

    @Override
    protected JpaRepository<ProductPackage, Long> getRepository() {
        return productPackageRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PRODUCT_PACKAGE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInPallet = productPalletRepository.existsByProductPackageId(id);
        boolean activeInOrderedProduct = orderedProductRepository.existsByProductPackageId(id);
        boolean activeInPickedProduct = pickedProductRepository.existsByProductPackageId(id);
        return activeInPallet || activeInOrderedProduct || activeInPickedProduct;
    }
}
