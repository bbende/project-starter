package com.bbende.project.starter.repository;

import com.bbende.project.starter.IntegrationTestApplication;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class for database integration tests.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = IntegrationTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public abstract class DatabaseIT {

    @PersistenceContext
    protected EntityManager entityManager;

    @Before
    @FlywayTest
    public void cleanDatabase() {
        // ----- NOTE ------
        // The @FlywayTest annotation will cause the DB to be reset and re-migrated before each test
        // which ensures a consistent state even if we commit a test transaction to add/delete data
    }

}
