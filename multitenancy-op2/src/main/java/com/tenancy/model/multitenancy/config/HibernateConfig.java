package com.tenancy.model.multitenancy.config;

/**
 * Hibernate Configuration which configures the tenant based connection provider and tenant identifier resolver.
 *
 * @author Deepak Singhvi
 *
 */

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class HibernateConfig {

  @Autowired
  private JpaProperties jpaProperties;

  @Bean
  JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }


  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
      CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl
  ) {
    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    // SCHEMA based multitenancy
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

    jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
    jpaPropertiesMap.put(Environment.SHOW_SQL, true);

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("com.tenancy.model*");
    em.setJpaVendorAdapter(this.jpaVendorAdapter());
    em.setJpaPropertyMap(jpaPropertiesMap);
    return em;
  }
}
