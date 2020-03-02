package com.tenancy.model.multitenancy.service;

import com.tenancy.model.multitenancy.domain.Catalog;
import com.tenancy.model.multitenancy.repository.CatalogRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

  @Autowired
  private CatalogRepository catalogRepository;

  @Override
  public Catalog createCatalog(Catalog catalog) {
    catalogRepository.save(catalog);
    log.debug(String.format("Created new catalog %s",catalog));
    return catalog;
  }

  @Override
  public Optional<Catalog> getCatalog(String catalogName, String supplierId) {
    return catalogRepository.findCatalog(catalogName, supplierId);
  }

  @Override
  public Optional<Catalog> getCatalog(Long catalogId) {
    return catalogRepository.findById(catalogId);
  }

  @Override
  public List<Catalog> getCatalogs() {
    return catalogRepository.findAll();
  }
}
