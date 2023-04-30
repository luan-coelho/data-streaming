package br.com.unitins.domain.model.video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video {

    @Id
    @SequenceGenerator(name = "VIDEO_SEQ", sequenceName = "VIDEO_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "VIDEO_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    private String path;
    private long duration = 0;
    private int views = 0;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ResourcePath> resolutionPaths = new ArrayList<>();

    public Video(String title) {
        this.title = title;
    }

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
