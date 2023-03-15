package br.com.unitins.queue;

import br.com.unitins.domain.enums.task.TaskStatus;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private TaskStatus status;
    private String result;
    @ManyToOne(fetch = FetchType.LAZY)
    private Video video;

    public static Task create(Video video) {
        return new Task(null, TaskStatus.PROCESSING, null, video);
    }
}
