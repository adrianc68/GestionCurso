import gui.login.LoginController;
import gui.usuarios.directivo.principal.PrincipalDirectivoController;
import gui.usuarios.encargado.principal.PrincipalEncargadoController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        LoginController loginController = new LoginController();
//        loginController.showStage();

        PrincipalDirectivoController principalDirectivoController = new PrincipalDirectivoController();
        principalDirectivoController.showStage();

//        List<Actividad> actividadList = new ActividadDAO().obtenerTodosPorTema(1);
//        for (Actividad actividad : actividadList) {
//            System.out.println(actividad);
//        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
