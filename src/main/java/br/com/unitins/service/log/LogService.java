package br.com.unitins.service.log;

import br.com.unitins.model.log.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApplicationScoped
public class LogService {

    private final Queue<String> logQueue = new ConcurrentLinkedQueue<>();

    @Inject
    EntityManager em;

    @Transactional
    public void saveLogs() {
        while (!logQueue.isEmpty()) {
            String message = logQueue.poll();
            Log log = new Log(null, message, null);
            em.persist(log);
        }
    }
}

