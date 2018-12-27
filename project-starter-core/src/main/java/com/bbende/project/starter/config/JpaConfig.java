package com.bbende.project.starter.config;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JpaConfig extends JpaBaseConfiguration {

    @Value("${spring.jpa.show-sql}")
    private boolean showSql;

    @Autowired
    public JpaConfig(final DataSource dataSource,
                     final ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                     final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, new JpaProperties(), jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        final String weavingMode = InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static";

        final HashMap<String, Object> vendorProperties = new HashMap<>();
        vendorProperties.put(PersistenceUnitProperties.WEAVING, weavingMode);

        if (showSql) {
            vendorProperties.put(PersistenceUnitProperties.CATEGORY_LOGGING_LEVEL_ + SessionLog.SQL, SessionLog.FINE_LABEL);
            vendorProperties.put(PersistenceUnitProperties.LOGGING_PARAMETERS, "true");
        }

        return vendorProperties;
    }
}
