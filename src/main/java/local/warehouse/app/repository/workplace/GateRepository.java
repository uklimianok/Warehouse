package local.warehouse.app.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.workplace.Gate;

public interface GateRepository extends JpaRepository<Gate, Long> {
    
    boolean existsBySymbol(String symbol);
}
