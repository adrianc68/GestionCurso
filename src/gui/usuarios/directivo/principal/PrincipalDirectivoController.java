package gui.usuarios.directivo.principal;

import gui.recursos.card.Controller;
import gui.usuarios.directivo.CursoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import util.Autentication;

import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalDirectivoController extends Controller implements Initializable {
    @FXML private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if( Autentication.getInstance().getCurrentUser() != null ) {
            nameLabel.setText(Autentication.getInstance().getCurrentUser().getNombre() );
        }
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/directivo/principal/PrincipalDirectivoVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void asignarDocenteACurso(ActionEvent event) {
        CursoController cursoController = new CursoController();
        cursoController.showStage();
    }

    @FXML
    void logOutButtonPressed(ActionEvent event) {
        stage.close();
    }
}
