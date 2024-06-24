package com.sosnovich.skyward.data.config;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Failure analyzer for {@link DatabaseNotStartedException}.
 * This analyzer provides a detailed failure analysis when a {@link DatabaseNotStartedException} occurs,
 * helping to diagnose issues related to database startup and connectivity.
 */
public class DatabaseNotStartedExceptionExceptionAnalyzer extends AbstractFailureAnalyzer<DatabaseNotStartedException> {

    /**
     * Analyzes the given {@link DatabaseNotStartedException} and provides a {@link FailureAnalysis}
     * with a description and action to resolve the issue.
     *
     * @param rootFailure the root failure causing the exception
     * @param cause the {@link DatabaseNotStartedException} that was thrown
     * @return a {@link FailureAnalysis} object containing a description of the failure and a recommended action
     */
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, DatabaseNotStartedException cause) {
        return new FailureAnalysis(
                "Connection to the database failed.",
                "Consider checking if the database is started and running. Ensure that database connection properties are configured correctly.",
                cause
        );
    }
}
