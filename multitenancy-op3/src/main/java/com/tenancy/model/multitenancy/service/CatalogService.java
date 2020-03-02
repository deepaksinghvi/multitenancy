package com.tenancy.model.multitenancy.service;

import com.tenancy.model.multitenancy.domain.Catalog;
import java.util.List;
import java.util.Optional;


public interface CatalogService {

  Catalog createCatalog(Catalog catalog);

  Optional<Catalog> getCatalog(String catalogName, String supplierId);

  Optional<Catalog> getCatalog(Long catalogId);

  List<Catalog> getCatalogs();

}
