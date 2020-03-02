package com.tenancy.model.multitenancy.api;

/**
 * Controller Class For Catalog
 *
 * @author Deepak Singhvi
 *
 */

import com.tenancy.model.multitenancy.domain.Catalog;
import com.tenancy.model.multitenancy.service.CatalogService;
import com.tenancy.model.multitenancy.util.Constants;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CatalogController {

  @Autowired
  private CatalogService catalogService;

  @PostMapping("/catalog")
  public ResponseEntity<Catalog> createCatalog(@RequestBody Catalog catalog) {
    log.debug("Catalog Creation Request: {} for tenant: ", catalog);
    return new ResponseEntity<>(catalogService.createCatalog(catalog), HttpStatus.OK);
  }


  @GetMapping("/catalog")
  public ResponseEntity getCatalog(@RequestHeader Map<String, String> headers, @RequestParam  Long catalogId) {
    log.debug("Catalog Id:{} retrival Requestfor tenant: {}", catalogId, headers.get(
        Constants.TENANTID));
    Optional<Catalog> catalog = catalogService.getCatalog(catalogId);
    ResponseEntity response;
    if (catalog.isPresent()) {
      response = new ResponseEntity<Catalog>(catalog.get(), HttpStatus.OK);
    } else {
      response = new ResponseEntity<String>("Catalog Not Found", HttpStatus.OK);
    }
    return response;
  }

}
