package com.bbende.project.starter.config;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    public JpaConfig(
            @Qualifier("dataSource")
            final DataSource dataSource,
            final JpaProperties jpaProperties,
            final ObjectProvider<JtaTransactionManager> jtaTransactionManager,
            final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        // Start the properties with everything from "spring.jpa.properties.*"
        final JpaProperties jpaProperties = getProperties();
        final Map<String, Object> vendorProperties = new HashMap<>(jpaProperties.getProperties());

        // Explicitly set the weaving mode based on whether instrumentation is available
        final String weavingMode = InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static";
        vendorProperties.put(PersistenceUnitProperties.WEAVING, weavingMode);

        return vendorProperties;
    }
}
