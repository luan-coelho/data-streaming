package br.com.unitins.model.video;

import br.com.unitins.model.enums.video.Resolution;
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
public class ResourcePath {

    @Id
    @SequenceGenerator(name = "RESOURCEPATH_SEQ", sequenceName = "RESOURCEPATH_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "RESOURCEPATH_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Resolution resolution;
    private String path;

    public ResourcePath(Resolution resolution, String path) {
        this.resolution = resolution;
        this.path = path;
    }
}
