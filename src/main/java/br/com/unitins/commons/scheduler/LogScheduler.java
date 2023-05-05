package br.com.unitins.commons.scheduler;

import br.com.unitins.service.log.LogService;
import io.quarkus.scheduler.Scheduled;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LogScheduler {

    @Inject
    LogService logService;

    @Scheduled(every="10m")
    public void saveLogs() {
        logService.saveLogs();
    }
}
