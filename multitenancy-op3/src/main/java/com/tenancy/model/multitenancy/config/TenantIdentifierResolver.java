package com.tenancy.model.multitenancy.config;

/**
 * Identifier for current tenant based on the reqeust else it would return the default tenant i.e. public
 */

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {


    @Override
    public String resolveCurrentTenantIdentifier() {
        String t =  TenantContext.getTenant();
        if(t!=null){
            return t;
        } else {
            return TenantConnectionProvider.DEFAULT_TENANT;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}