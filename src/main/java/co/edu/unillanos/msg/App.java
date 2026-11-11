package co.edu.unillanos.msg;

import co.edu.unillanos.msg.application.port.InvestmentRepository;
import co.edu.unillanos.msg.application.port.MortgageRepository;
import co.edu.unillanos.msg.application.port.OperatingExpensesRepository;
import co.edu.unillanos.msg.application.usecase.EstimateFundsForWeek;
import co.edu.unillanos.msg.application.usecase.RegisterMortgage;
import co.edu.unillanos.msg.application.usecase.RegisterMortgageRequest;
import co.edu.unillanos.msg.domain.Investment;
import co.edu.unillanos.msg.domain.OperatingExpenses;
import co.edu.unillanos.msg.infrastructure.InMemoryInvestmentRepository;
import co.edu.unillanos.msg.infrastructure.InMemoryMortgageRepository;
import co.edu.unillanos.msg.infrastructure.InMemoryOperatingExpensesRepository;
import co.edu.unillanos.msg.infrastructure.ui.FundsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Raíz de composición de la ventana: el único lugar que conoce todas las capas
 * y las conecta, igual que Main hace con la consola.
 * Rol responsable: Integrador.
 *
 * Ninguna regla de negocio vive aquí: los datos de ejemplo y el cableado son los
 * mismos de Main; el cálculo sigue siendo del caso de uso.
 *
 * Se ejecuta con: mvn javafx:run (desde el IDE se ejecuta Launcher).
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Los tres repositorios y los datos de ejemplo: igual que en Main
        MortgageRepository mortgages = new InMemoryMortgageRepository();
        InvestmentRepository investments = new InMemoryInvestmentRepository();
        OperatingExpensesRepository expenses =
                new InMemoryOperatingExpensesRepository(new OperatingExpenses(39000.00));

        RegisterMortgage register = new RegisterMortgage(mortgages);
        register.execute(new RegisterMortgageRequest("M-001", 300.00, 2600.00, 1040.00, 1000.00));
        register.execute(new RegisterMortgageRequest("M-002", 200.00, 1560.00, 520.00, 1000.00));

        investments.save(new Investment("I-01", "Bonos del Tesoro", 52000.00));
        investments.save(new Investment("I-02", "CDT", 26000.00));

        // 2. El caso de uso
        EstimateFundsForWeek estimateFunds =
                new EstimateFundsForWeek(mortgages, investments, expenses);

        // 3. La vista, con el controlador que recibe el caso de uso
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("infrastructure/ui/funds-view.fxml"));
        loader.setControllerFactory(type -> new FundsController(estimateFunds));

        stage.setScene(new Scene(loader.load()));
        stage.setTitle("MSG Foundation");
        stage.show();
    }
}
