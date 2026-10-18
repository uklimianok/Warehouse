package local.warehouse.app.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.item.Pallet;

public interface PalletRepository extends JpaRepository<Pallet, Long> {}
