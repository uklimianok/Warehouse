package com.warehouse.demo.service.product.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.product.ProductRequestMapper;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.product.ProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends AbstractService<Product, Long> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ReturnProductRepository returnProductRepository;

    private final ProductRequestMapper productRequestMapper;

    @Override
    public Product create(ProductRequest productRequest) {
        if (productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(EntityName.BARCODE_NUMBER, OutputMessage.EXISTS));
        
        Product product = new Product();
        return modifyAndSave(product, productRequest);
    }

    @Override
    public Product update(long id, ProductRequest productRequest) {
        Product product = read(id);
        boolean barcodeNumberChanged = !product.getBarcodeNumber().equals(productRequest.getBarcodeNumber());
        boolean barcodeNumberExists = productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber());
        if (barcodeNumberChanged && barcodeNumberExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(EntityName.BARCODE_NUMBER, OutputMessage.EXISTS));

        return modifyAndSave(product, productRequest);
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PRODUCT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPackage = productPackageRepository.existsByProductId(id);
        boolean activeInReturnProduct = returnProductRepository.existsByProductId(id);
        return activeInProductPackage || activeInReturnProduct;
    }

    private Product modifyAndSave(Product target, ProductRequest from) {
        productRequestMapper.updateProductFromRequest(from, target);
        return productRepository.save(target);
    }
}
