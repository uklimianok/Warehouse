package local.warehouse.app.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.order.orderedProduct.OrderedProductRequest;
import local.warehouse.app.entity.order.OrderedProduct;
import local.warehouse.app.mapper.order.orderedProduct.OrderedProductRequestMapper;
import local.warehouse.app.repository.order.OrderedProductRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.order.OrderedProductService;
import local.warehouse.app.util.info.Entity;

@Service
public class OrderedProductServiceImpl extends AbstractService<OrderedProduct, Long> implements OrderedProductService {
    //private final OrderedProductService self;

    private final OrderedProductRepository orderedProductRepository;

    private final OrderedProductRequestMapper orderedProductRequestMapper;

    public OrderedProductServiceImpl(
        //@Lazy OrderedProductService self, 
        OrderedProductRepository orderedProductRepository, 
        OrderedProductRequestMapper orderedProductRequestMapper
    ) {
        //this.self = self;
        this.orderedProductRepository = orderedProductRepository;
        this.orderedProductRequestMapper = orderedProductRequestMapper;
    }

    @Override
    public OrderedProduct read(Long id) {
        return super.read(id);
    }

    @Override
    public OrderedProduct create(OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = new OrderedProduct();

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override
    public OrderedProduct update(long id, OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = read(id);

        return modifyAndSave(orderedProduct, orderedProductRequest);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected JpaRepository<OrderedProduct, Long> getRepository() {
        return orderedProductRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORDERED_PRODUCT;
    }

    private OrderedProduct modifyAndSave(OrderedProduct target, OrderedProductRequest from) {
        orderedProductRequestMapper.convertFromRequest(from, target);
        return orderedProductRepository.save(target);
    }
}
