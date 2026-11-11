package co.edu.unillanos.msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Raíz de composición de la ventana: el único lugar que conoce todas las capas
 * y las conecta, igual que Main hace con la consola.
 * Rol responsable: Integrador.
 *
 * Por ahora muestra una ventana vacía; en feature/app arma el caso de uso y
 * carga la vista.
 *
 * Se ejecuta con: mvn javafx:run (desde el IDE se ejecuta Launcher).
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new StackPane(), 420, 240));
        stage.setTitle("MSG Foundation");
        stage.show();
    }
}
