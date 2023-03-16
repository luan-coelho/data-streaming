package br.com.unitins.queue;

import br.com.unitins.domain.enums.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String description;
    private Long videoId;

    public static Task create(Long videoId) {
        return new Task(null, TaskStatus.PROCESSING, null, videoId);
    }
}
