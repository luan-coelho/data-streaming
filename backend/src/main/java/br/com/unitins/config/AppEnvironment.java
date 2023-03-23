package br.com.unitins.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppEnvironment {
    DEVELOPMENT("dev"),
    TESTING("test"),
    PRODUCTION("prod");

    String description;

    public static AppEnvironment getEnvironmentByDescription(String description) {
        for (AppEnvironment env : AppEnvironment.values()) {
            if (env.description.equalsIgnoreCase(description)) {
                return env;
            }
        }
        return AppEnvironment.DEVELOPMENT;
    }
}

