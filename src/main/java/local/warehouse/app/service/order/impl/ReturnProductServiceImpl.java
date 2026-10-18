package local.warehouse.app.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.order.returnProduct.ReturnProductRequest;
import local.warehouse.app.entity.order.ReturnProduct;
import local.warehouse.app.mapper.order.returnProduct.ReturnProductRequestMapper;
import local.warehouse.app.repository.order.ReturnProductRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.order.ReturnProductService;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnProductServiceImpl extends AbstractService<ReturnProduct, Long> implements ReturnProductService {
    private final ReturnProductRepository returnProductRepository;

    private final ReturnProductRequestMapper returnProductRequestMapper;

    @Override
    public ReturnProduct create(ReturnProductRequest returnProductRequest) {
        return modifyAndSave(new ReturnProduct(), returnProductRequest);
    }

    @Override
    public ReturnProduct update(long id, ReturnProductRequest returnProductRequest) {
        return modifyAndSave(read(id), returnProductRequest);
    }

    private ReturnProduct modifyAndSave(ReturnProduct target, ReturnProductRequest from) {
        returnProductRequestMapper.convertFromRequest(from, target);
        return returnProductRepository.save(target);
    }

    @Override
    protected JpaRepository<ReturnProduct, Long> getRepository() {
        return returnProductRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.RETURN_PRODUCT;
    }
}
