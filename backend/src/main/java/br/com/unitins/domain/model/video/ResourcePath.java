package br.com.unitins.domain.model.video;

import br.com.unitins.domain.enums.video.Resolution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResourcePath {

    @Id
    @SequenceGenerator(name = "RESOURCEPATH_SEQ", sequenceName = "RESOURCEPATH_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "RESOURCEPATH_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;
    private Resolution resolution;
    private String path;

    public ResourcePath(Resolution resolution, String path) {
        this.resolution = resolution;
        this.path = path;
    }
}
