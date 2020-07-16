package gui.usuarios.encargado.principal;

import gui.recursos.card.Controller;
import gui.usuarios.encargado.curso.CursosController;
import gui.usuarios.encargado.docente.DocenteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import util.Autentication;

import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalEncargadoController extends Controller implements Initializable {
    @FXML private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if( Autentication.getInstance().getCurrentUser() != null ) {
            nameLabel.setText(Autentication.getInstance().getCurrentUser().getNombre() );
        }
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/encargado/principal/PrincipalEncargadoVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cursosBotonPresionado(ActionEvent event) {
        CursosController cursosController = new CursosController();
        cursosController.showStage();
    }

    @FXML
    void docentesBotonPresionado(ActionEvent event) {
        DocenteController docenteController = new DocenteController();
        docenteController.showStage();
    }

    @FXML
    void logOutButtonPressed(ActionEvent event) {
        stage.close();
    }
}
