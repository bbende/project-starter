package com.bbende.project.starter.core;

import com.bbende.project.starter.core.config.JpaConfig;
import com.bbende.project.starter.testcontainers.db.TestDataSourceFactory;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Base class for database integration tests.
 *
 * The @DataJpaTest annotation loads an application context with only Spring Data JPA related beans. This allows
 * quick testing of only this slice of the application, without having to wire up services and controllers.
 *
 * By default @DataJpaTest replaces the DataSource with an embedded H2. In order to leverage the Testcontainer
 * DataSources, the includeFilter is added @AutoConfigureTestDatabase is configured to only replace auto-configured
 * DataSources (i.e. don't replace the Testcontainer DataSources).
 */
@DataJpaTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = TestDataSourceFactory.class)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Import(JpaConfig.class)
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public abstract class DatabaseIT {

    @Autowired
    protected TestEntityManager testEntityManager;

    @BeforeEach
    @FlywayTest
    public void cleanDatabase() {
        // ----- NOTE ------
        // The @FlywayTest annotation will cause the DB to be reset and re-migrated before each test
        // which ensures a consistent state even if we commit a test transaction to add/delete data

    }

}
