package com.tenancy.model.multitenancy.config;

/**
 * Tenent based connection provder which sets the tenant's schema during runtime for a given connection.
 */

import com.tenancy.model.multitenancy.domain.DataSourceConfig;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TenantConnectionProvider implements MultiTenantConnectionProvider {


    public static String DEFAULT_TENANT = "PUBLIC";
    private DataSource datasource;

    private Map<String,DataSourceConfig> tenantDataSourceConfigMap = new HashMap<String,DataSourceConfig>();
    public static boolean initDone = Boolean.FALSE;

    public TenantConnectionProvider(DataSource dataSource) {
        this.datasource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        Connection connection = datasource.getConnection();
        if(!initDone) {
            tenantDataSourceConfigMap.clear();
            ResultSet rs = connection.createStatement()
                .executeQuery("SELECT * FROM public.DATASOURCECONFIG");
            if (!rs.isBeforeFirst()) {
                System.out.println("no rows found");
            } else {
                int counter = 1;
                while (rs.next()) {
                    DataSourceConfig dataSourceConfig = new DataSourceConfigMapper()
                        .mapRow(rs, counter++);
                    tenantDataSourceConfigMap.put(dataSourceConfig.getTenantId(), dataSourceConfig);
                }
            }

            initDone = Boolean.TRUE;
            rs.close();
        }
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.trace("Get connection for tenant {}", tenantIdentifier);
        Connection connection = getAnyConnection();
        DataSourceConfig dataSourceConfig = tenantDataSourceConfigMap.get(tenantIdentifier);
        connection.setSchema(null==dataSourceConfig?DEFAULT_TENANT:dataSourceConfig.getSchemaName());
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.trace("Release connection for tenant {}", tenantIdentifier);
        connection.setSchema(DEFAULT_TENANT);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }


    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    private static final class DataSourceConfigMapper implements RowMapper<DataSourceConfig> {

        public DataSourceConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setId(rs.getLong("id"));
            dataSourceConfig.setDriverClassName(rs.getString("driverclassname"));
            dataSourceConfig.setUrl(rs.getString("url"));
            dataSourceConfig.setSchemaName(rs.getString("schemaname"));
            dataSourceConfig.setTenantId(rs.getString("tenantid"));
            dataSourceConfig.setUserName(rs.getString("username"));
            dataSourceConfig.setPassword(rs.getString("password"));
            return dataSourceConfig;
        }
    }
}