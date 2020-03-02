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
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TenantConnectionProvider /*implements MultiTenantConnectionProvider*/ extends
    AbstractMultiTenantConnectionProvider {

    @Value("spring.datasource.url")
    private String dbUrl;
    @Value("spring.datasource.username")
    private String userName;
    @Value("spring.datasource.password")
    private String password;
    @Value("spring.datasource.driverClassName")
    private String driverClassName;


    public static String DEFAULT_TENANT = "PUBLIC";
    private DataSource datasource;

    private Map<String,DataSourceConfig> tenantDataSourceConfigMap = new HashMap<String,DataSourceConfig>();
    private Map<String,ConnectionProvider> tenantConnectionProviderMap = new HashMap<String,ConnectionProvider>();

    private ConnectionProvider publicConnectionProvider;

    public static boolean initDone = Boolean.FALSE;

    public TenantConnectionProvider(DataSource dataSource) {
        this.datasource = dataSource;
    }



    @Override
    public Connection getAnyConnection() throws SQLException {
        Connection connection = datasource.getConnection();
        getAnyConnectionProvider();
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.trace("Get connection for tenant {}", tenantIdentifier);
        Connection connection;
        if(!initDone) {
            connection = getAnyConnection();
        }
        else {
            connection = tenantConnectionProviderMap.get(tenantIdentifier).getConnection();
        }
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

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        if(!initDone) {
            try {
                publicConnectionProvider = publicConnectionProvider();
                tenantDataSourceConfigMap.clear();
                ResultSet rs = datasource.getConnection().createStatement()
                    .executeQuery("SELECT * FROM public.DATASOURCECONFIG");
                if (!rs.isBeforeFirst()) {
                    System.out.println("no rows found");
                } else {
                    int counter = 1;
                    while (rs.next()) {
                        DataSourceConfig dataSourceConfig = new DataSourceConfigMapper()
                            .mapRow(rs, counter++);
                        tenantDataSourceConfigMap
                            .put(dataSourceConfig.getTenantId(), dataSourceConfig);
                        tenantConnectionProviderMap.put(dataSourceConfig.getTenantId(),
                            buildConnectionProvider(dataSourceConfig));
                    }
                }
                rs.close();
                initDone = Boolean.TRUE;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return publicConnectionProvider;
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return tenantConnectionProviderMap.get(tenantIdentifier);
    }

    private ConnectionProvider buildConnectionProvider(DataSourceConfig dataSourceConfig) {
        Properties props = new Properties( null );
        props.put( "hibernate.connection.driver_class", dataSourceConfig.getDriverClassName() );
        props.put( "hibernate.connection.url", dataSourceConfig.getUrl());
        props.put( "hibernate.connection.username", dataSourceConfig.getUserName() );
        props.put( "hibernate.connection.password", dataSourceConfig.getPassword() );
        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure( props );
        return connectionProvider;
    }

    private ConnectionProvider publicConnectionProvider() {
        Properties props = new Properties( null );
        props.put( "hibernate.connection.driver_class", "org.h2.Driver" );
        props.put( "hibernate.connection.url", "jdbc:h2:mem:DATABASEb");
        props.put( "hibernate.connection.username", "sa");
        props.put( "hibernate.connection.password", "");
        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure( props );
        return connectionProvider;
    }
}