package br.com.unitins.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfig {

    @ConfigProperty(name = "quarkus.profile")
    String quarkusProfile;

    public AppEnvironment getEnvironment() {
        return AppEnvironment.getEnvironmentByDescription(quarkusProfile);
    }

    public boolean isInProduction() {
        return quarkusProfile.equals(AppEnvironment.PRODUCTION.getDescription());
    }
}
