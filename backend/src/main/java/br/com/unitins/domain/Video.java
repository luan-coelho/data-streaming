package br.com.unitins.domain;

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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ResolutionPath> resolutionPaths = new ArrayList<>();
}
