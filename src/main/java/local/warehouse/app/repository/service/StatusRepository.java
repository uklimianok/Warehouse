package local.warehouse.app.repository.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.service.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

    boolean existsByNameAndType(String name, String type);

    Optional<Status> findByNameAndType(String name, String type);
    
    Optional<Status> findByIdAndType(long id, String Type);
}
