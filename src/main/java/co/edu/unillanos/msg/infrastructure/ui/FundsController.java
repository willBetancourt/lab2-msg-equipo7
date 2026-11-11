package co.edu.unillanos.msg.infrastructure.ui;

import co.edu.unillanos.msg.application.usecase.EstimateFundsForWeek;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de JavaFX: el adaptador entre la ventana y el caso de uso.
 * Rol responsable: Controlador.
 *
 * Decisión D1: este NO es el Controller de GRASP. El Controller de GRASP es
 * EstimateFundsForWeek, que recibe el evento del sistema «estimar los fondos de
 * la semana» y coordina a los expertos. Esta clase solo traduce un clic de la
 * ventana en una llamada a ese caso de uso y escribe el resultado en la etiqueta.
 *
 * Decisión D2: recibe el caso de uso por el constructor; no lo construye. Por eso
 * esta clase no conoce ningún repositorio ni ninguna fórmula: no instancia casos
 * de uso y no contiene ninguna constante del negocio (ni las semanas del año ni
 * el porcentaje del ingreso de la pareja).
 */
public class FundsController {

    @FXML
    private Label totalLabel;

    private final EstimateFundsForWeek estimateFunds;

    public FundsController(EstimateFundsForWeek estimateFunds) {
        this.estimateFunds = estimateFunds;
    }

    @FXML
    private void onEstimate() {
        double total = estimateFunds.execute();
        totalLabel.setText(MoneyFormat.format(total));
    }
}
