package br.com.unitins.queue;

import br.com.unitins.domain.model.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private TaskStatus status;
    private String result;
    @ManyToOne(fetch = FetchType.LAZY)
    private Video video;

    public enum TaskStatus {
        PROCESSING,
        COMPLETED,
        INTERRUPTED
    }

    public static Task create(Video video) {
        return new Task(null, TaskStatus.PROCESSING, null, video);
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
