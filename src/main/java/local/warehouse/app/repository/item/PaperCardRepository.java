package local.warehouse.app.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.item.PaperCard;

public interface PaperCardRepository extends JpaRepository<PaperCard, Long> {
    
    boolean existsByOrderPalletId(long orderPalletId);
}
