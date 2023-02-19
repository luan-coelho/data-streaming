package br.com.unitins.domain;

import br.com.unitins.domain.enums.Resolution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResolutionPath {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private Resolution resolution;
    private String path;
}
