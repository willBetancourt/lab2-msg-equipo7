package co.edu.unillanos.msg;

import javafx.application.Application;

/**
 * Punto de entrada para el IDE.
 * Rol responsable: Integrador.
 *
 * Existe porque ejecutar directamente una clase que extiende Application falla
 * con «JavaFX runtime components are missing»: esta clase no extiende
 * Application, así que el IDE la arranca sin exigir el módulo de JavaFX.
 */
public class Launcher {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
