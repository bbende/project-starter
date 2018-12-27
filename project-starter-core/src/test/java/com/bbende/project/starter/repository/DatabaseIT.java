package com.bbende.project.starter.repository;

import com.bbende.project.starter.IntegrationTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class for database integration tests.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegrationTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class DatabaseIT {

    @PersistenceContext
    protected EntityManager entityManager;

}
