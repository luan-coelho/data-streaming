package br.com.unitins.model.video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoMetrics {

    @Id
    @SequenceGenerator(name = "VIDEOMETRICS_SEQ", sequenceName = "VIDEOMETRICS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "VIDEOMETRICS_SEQ", strategy = GenerationType.SEQUENCE)
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
