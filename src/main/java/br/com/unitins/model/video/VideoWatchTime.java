package br.com.unitins.model.video;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoWatchTime {

    @Id
    @SequenceGenerator(name = "VIDEOWATCHTIME_SEQ", sequenceName = "VIDEOWATCHTIME_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "VIDEOWATCHTIME_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long videoId;
    private double watchTime;
}
