package br.com.unitins.commons;

import br.com.unitins.service.log.LogService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LogScheduler {

    @Inject
    LogService logService;

    @Scheduled(every="10m")
    public void saveLogs() {
        logService.saveLogs();
    }
}
