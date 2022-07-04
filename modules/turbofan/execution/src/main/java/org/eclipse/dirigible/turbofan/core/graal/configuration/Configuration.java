package org.eclipse.dirigible.turbofan.core.graal.configuration;

public class Configuration {

    public static String get(String environmentVariableName, String defaultValue) {
        String maybeEnvironmentVariableValue = System.getenv(environmentVariableName);
        return maybeEnvironmentVariableValue != null ? maybeEnvironmentVariableValue : defaultValue;
    }
}
