package com.tenancy.model.multitenancy.repository;

import com.tenancy.model.multitenancy.domain.Catalog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

  @Query("select c from Catalog c where c.catalogName = ?1 AND c.supplierId = ?2")
  Optional<Catalog> findCatalog(String catalogName, String supplierId);
}