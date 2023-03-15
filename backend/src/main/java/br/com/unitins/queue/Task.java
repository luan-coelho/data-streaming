package br.com.unitins.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Task {

    private Long id;
    private TaskStatus status;
    private String result;

    public enum TaskStatus {
        PROCESSING,
        COMPLETED,
        INTERRUPTED
    }

    public static Task create(Long id) {
        return new Task(id, TaskStatus.PROCESSING, null);
    }

    public void changeStatusToProcessing() {
        this.status = TaskStatus.PROCESSING;
    }

    public void changeStatusToCompleted() {
        this.status = TaskStatus.COMPLETED;
    }

    public void changeStatusToInterrupted() {
        this.status = TaskStatus.INTERRUPTED;
    }
}
