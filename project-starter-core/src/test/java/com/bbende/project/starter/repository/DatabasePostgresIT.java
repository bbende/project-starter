package com.bbende.project.starter.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.test.context.ActiveProfiles;

/**
 * Database IT that uses an embedded Postgres DB provided by @AutoConfigureEmbeddedDatabase.
 *
 * See: https://github.com/zonkyio/embedded-database-spring-test
 *
 * This test specifies the @ActiveProfile of "postgres" so application-postgres.properties will be used,
 * which then switches the flyway vendor folder from H2 to Postgres.
 *
 */
@ActiveProfiles("postgres")
@AutoConfigureEmbeddedDatabase
public abstract class DatabasePostgresIT extends DatabaseIT {

}
