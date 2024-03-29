package br.com.unitins.service.log;

import br.com.unitins.model.log.Log;
import br.com.unitins.model.log.LogType;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@ApplicationScoped
public class LogService {

    private final Queue<Log> logQueue = new ConcurrentLinkedQueue<>();

    @Inject
    EntityManager em;

    @Scheduled(every = "25s")
    @Transactional
    public void saveLogs() {
        log.info("Checking for outstanding logs for persistence...");
        if (!logQueue.isEmpty()) {
            try {
                log.info("Pending logs for persistence. Starting persistence...");
                while (!logQueue.isEmpty()) {
                    Log log = logQueue.poll();
                    em.persist(log);
                }
                log.info("Logs successfully persisted.");
            } catch (Exception e) {
                log.error("An unexpected error occurred while trying to persist the logs. Message: {}", e.getMessage());
            }
        }
    }

    public List<Log> getAll() {
        TypedQuery<Log> query = em.createQuery("FROM Log ORDER BY timestamp DESC", Log.class);
        return query.getResultList();
    }

    private void add(String message, String details, LogType type) {
        this.logQueue.add(new Log(null, message, details, type, null));
    }

    public void addInfo(String message, String details) {
        add(message, details, LogType.INFO);
    }

    public void addWarn(String message, String details) {
        add(message, details, LogType.WARN);
    }

    public void addError(String message, String details) {
        add(message, details, LogType.ERROR);
    }
}

