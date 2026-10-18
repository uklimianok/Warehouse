package local.warehouse.app.repository.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.employee.OrganizationType;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Long> {

    boolean existsByName(String name);
    
    Optional<OrganizationType> findByName(String name);
}
