package br.com.unitins.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    private String path;
    private long duration;
    private int views = 0;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ResourcePath> resolutionPaths = new ArrayList<>();

    public Video(String title) {
        this.title = title;
    }

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
