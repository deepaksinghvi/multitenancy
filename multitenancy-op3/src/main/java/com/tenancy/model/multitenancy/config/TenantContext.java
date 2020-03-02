package com.tenancy.model.multitenancy.config;

/**
 * Tenant Context stored in the threadlocal variable to retrieve at any given point of the request workflow
 */
public class TenantContext {
    private static ThreadLocal<String> tenant = new InheritableThreadLocal<>();

    public static String getTenant() {
        return tenant.get();
    }

    public static void setTenant(String aTenant) {
        tenant.set(aTenant);
    }

    public static void clear() {
        tenant.set(null);
    }
}
