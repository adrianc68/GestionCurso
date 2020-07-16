import gui.login.LoginController;
import gui.usuarios.directivo.principal.PrincipalDirectivoController;
import gui.usuarios.encargado.principal.PrincipalEncargadoController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showStage();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
