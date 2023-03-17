package br.com.unitins.domain.model.video;

import br.com.unitins.domain.enums.video.Resolution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResourcePath {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private Resolution resolution;
    private String path;
    private File file;

    public ResourcePath(Resolution resolution, String path) {
        this.resolution = resolution;
        this.path = path;
    }
}
