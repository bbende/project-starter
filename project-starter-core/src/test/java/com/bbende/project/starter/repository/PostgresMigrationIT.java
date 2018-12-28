package com.bbende.project.starter.repository;

import org.junit.Test;

/**
 * Empty test which causes the Flyway migrations to run against an embedded Postgres DB.
 */
public class PostgresMigrationIT extends DatabasePostgresIT {

    @Test
    public void testMigration() {
        // Nothing to do, if the test runs through then the migrations ran successfully against Postgres
    }
}
