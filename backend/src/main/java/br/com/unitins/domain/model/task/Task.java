package br.com.unitins.domain.model.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private TaskStatus status;
    private String description;
    private LocalDateTime localDateTime;
    private Long videoId;

    @PrePersist
    @PreUpdate
    public void changeDateTime() {
        this.localDateTime = LocalDateTime.now();
    }

    public Task(Long videoId) {
        this.videoId = videoId;
        this.status = TaskStatus.PROCESSING;
    }
}
