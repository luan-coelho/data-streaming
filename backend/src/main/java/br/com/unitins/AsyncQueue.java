package br.com.unitins;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.LinkedList;
import java.util.Queue;

public class AsyncQueue<T> {
    private final Queue<Uni<T>> queue = new LinkedList<>();

    public void add(Uni<T> uni) {
        synchronized (queue) {
            queue.add(uni);
            queue.notifyAll();
        }
    }

    public Multi<T> execute() {
        return Multi.createFrom().<T>emitter(emitter -> {
            while (true) {
                Uni<T> uni;
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            emitter.fail(e);
                            return;
                        }
                    }
                    uni = queue.poll();
                }
                uni.subscribe()
                        .with(
                                result -> emitter.emit(result),
                                error -> emitter.fail(error),
                                () -> {
                                    if (queue.isEmpty()) {
                                        emitter.complete();
                                    }
                                }
                        )
                        .withSubscriber(Infrastructure.getDefaultWorkerPool());
            }
        });
    }
}

