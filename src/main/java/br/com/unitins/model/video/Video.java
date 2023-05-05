package br.com.unitins.model.video;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ResourcePath> resolutionPaths = new ArrayList<>();

    public Video(String title) {
        this.title = title;
    }

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addResolutionPatch(ResourcePath resourcePath) {
        this.resolutionPaths.add(resourcePath);
    }

    public boolean hasValidResolution() {
        if (this.resolutionPaths == null || this.resolutionPaths.isEmpty()) {
            return false;
        }

        for (ResourcePath resolutionPath : this.resolutionPaths) {
            if (resolutionPath.getPath() == null || resolutionPath.getPath().isBlank()) {
                return false;
            }
        }
        return true;
    }
}
