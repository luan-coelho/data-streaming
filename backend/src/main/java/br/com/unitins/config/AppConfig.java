package br.com.unitins.config;

import br.com.unitins.domain.model.Course;
import br.com.unitins.domain.model.Module;
import br.com.unitins.domain.model.User;

import java.util.List;

public class AppConfig {

    private static User userLogged;

    public static User getLoggedUser() {
        if (userLogged != null) {
            return userLogged;
        }

        Module module = new Module(1L, "Conhecendo a ferramenta", "...");
        Course course = new Course(null, "Introdução ao Vuejs", "Iremos conhecer sobre Vuejs", List.of(module));

        return new User(null, "luancoelho", "Luan", "Coêlho de Souza", "luan@gmail.com", List.of(course));
    }
}
