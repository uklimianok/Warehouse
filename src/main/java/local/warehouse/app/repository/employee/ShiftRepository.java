package local.warehouse.app.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.employee.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    
    boolean existsBySymbol(String symbol);
}
