package br.com.unitins.model.video;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static br.com.unitins.commons.AppConstants.USER_HOME;

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
    private List<VideoResource> resources = new ArrayList<>();

    public Video(String title) {
        this.title = title;
    }

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addResource(VideoResource videoResource) {
        this.resources.add(videoResource);
    }

    public boolean hasValidResolution() {
        if (this.resources == null || this.resources.isEmpty()) {
            return false;
        }

        for (VideoResource resolutionPath : this.resources) {
            if (resolutionPath.getPath() == null || resolutionPath.getPath().isBlank()) {
                return false;
            }

            try {
                String filePath = USER_HOME + resolutionPath.getPath();
                File file = new File(filePath);
                if (file.exists()) {
                    continue;
                }
                return false;
            } catch (NullPointerException e) {
                return false;
            }
        }
        return true;
    }
}
