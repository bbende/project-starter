package org.junit.runners.model;

/**
 * Fake class to provide TestRule to Testcontainers until they remove dependency on JUnit 4.
 *
 * This class won't be used since the rest of the project uses JUnit 5.
 */
@SuppressWarnings("unused")
public class Statement {
}
