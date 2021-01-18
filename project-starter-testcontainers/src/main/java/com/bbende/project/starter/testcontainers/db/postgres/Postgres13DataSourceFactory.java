package com.bbende.project.starter.testcontainers.db.postgres;

import com.bbende.project.starter.testcontainers.db.TestDataSourceFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Profile("postgres-13")
public class Postgres13DataSourceFactory extends TestDataSourceFactory {

    private static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer("postgres:13.1");

    static {
        POSTGRESQL_CONTAINER.start();
    }

    @Override
    protected DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUser(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        return dataSource;
    }

    @PostConstruct
    public void initDatabase() throws SQLException, ScriptException {
        DatabaseDelegate databaseDelegate = new JdbcDatabaseDelegate(POSTGRESQL_CONTAINER, "");
        databaseDelegate.execute("DROP DATABASE test; CREATE DATABASE test;", "", 0, false, true);
    }
}

