package br.com.unitins.domain.model.video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Video video;
    private long views = 0;
    private LocalDate date;

    public VideoMetrics(Video video, LocalDate date) {
        this.video = video;
        this.date = date;
    }
}