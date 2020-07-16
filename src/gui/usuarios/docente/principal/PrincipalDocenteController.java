package gui.usuarios.docente.principal;

import gui.recursos.card.Controller;
import gui.usuarios.docente.CursosController;
import gui.usuarios.docente.plan.AgregarPlanCursoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.Autentication;

import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalDocenteController extends Controller implements Initializable {
    @FXML private AnchorPane rootStage;
    @FXML private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if( Autentication.getInstance().getCurrentUser() != null ) {
            nameLabel.setText(Autentication.getInstance().getCurrentUser().getNombre() );
        }
    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/gui/usuarios/docente/principal/PrincipalDocenteVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void avanceBotonPresionado(ActionEvent event) {

    }

    @FXML
    void cursosBotonPresionado(ActionEvent event) {

    }

    @FXML
    void logOutButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void modificarPlanBotonPresionado(ActionEvent event) {
        CursosController cursosController = new CursosController();
        cursosController.showStage();
    }

    @FXML
    void registrarPlanBotonPresionado(ActionEvent event) {
        AgregarPlanCursoController agregarPlanCursoController = new AgregarPlanCursoController();
        agregarPlanCursoController.showStage();
    }



}
