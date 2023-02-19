package br.com.unitins.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Entity
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nickName;
    private String name;
    private String surname;
    private String email;
    @OneToMany
    private List<Course> courses;
}
