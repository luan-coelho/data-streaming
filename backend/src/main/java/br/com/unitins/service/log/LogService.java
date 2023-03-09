package br.com.unitins.service.log;

import br.com.unitins.domain.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class LogService {

    private final Queue<String> logQueue = new ConcurrentLinkedQueue<>();

    @Inject
    EntityManager em;

    public void addLog(String message) {
        logQueue.add(message);
    }

    @Transactional
    public void saveLogs() {
        while (!logQueue.isEmpty()) {
            String message = logQueue.poll();
            Log log = new Log(null, message, null);
            em.persist(log);
        }
    }
}

