package com.bbende.project.starter.test;

import com.bbende.project.starter.persistence.EclipseLinkJpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
 * @DataJpaTest defaults to replacing the DataSource with an embedded H2. In order to leverage the Testcontainer
 * DataSources, @AutoConfigureTestDatabase is used to specify that only auto-configured DataSources shoudl be replaced
 * (i.e. don't replace the Testcontainer DataSources).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(EclipseLinkJpaConfig.class)
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public abstract class DatabaseIT {

    @Autowired
    protected TestEntityManager testEntityManager;

}
