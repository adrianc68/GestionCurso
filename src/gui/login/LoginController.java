package gui.login;

import domain.usuarios.Directivo;
import domain.usuarios.Docente;
import domain.usuarios.Encargado;
import domain.usuarios.Personal;
import gui.recursos.card.Controller;
import gui.usuarios.directivo.principal.PrincipalDirectivoController;
import gui.usuarios.docente.principal.PrincipalDocenteController;
import gui.usuarios.encargado.principal.PrincipalEncargadoController;
import util.Autentication;
import util.Cryptography;
import util.exceptions.LimitReachedException;
import util.exceptions.UserNotFoundException;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

public class LoginController extends Controller implements Initializable {
    protected double mousePositionOnX;
    protected double mousePositionOnY;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Label systemLabel;
    @FXML private Button closeButton;
    @FXML private AnchorPane rootStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showStage() {
        loadFXMLFile(getClass().getResource("/gui/login/LoginVista.fxml"), this);
        stage.initStyle(StageStyle.TRANSPARENT );
        stage.show();
    }

    @FXML
    protected void passwordRecoveryButtonPressed(MouseEvent event) {
        throw new UnsupportedOperationException();
    }

    @FXML
    protected void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    protected void loginButtonPressed(ActionEvent event) {
        if ( login() ) {
            showStageBySpecifiedUser();
            clearLogIn();
            stage.show();
        }
    }

    @FXML
    void stageDragged(MouseEvent event) {
        stage.setX( event.getScreenX() - mousePositionOnX );
        stage.setY( event.getScreenY() - mousePositionOnY );
    }

    @FXML
    void stagePressed(MouseEvent event) {
        mousePositionOnX = event.getSceneX();
        mousePositionOnY = event.getSceneY();
    }

    private boolean login() {
        boolean isLogged = false;
        String emailInput = emailTextField.getText();
        String passwordInputEncrypted = Cryptography.cryptSHA2(passwordTextField.getText() );
        try {
            isLogged = Autentication.getInstance().logIn(emailInput, passwordInputEncrypted);
        } catch (UserNotFoundException | LimitReachedException e) {
            systemLabel.setText( e.getMessage() );
            Logger.getLogger( LoginController.class.getName() ).log(Level.FINE, null, e);
        } catch (SQLException e) {
            Logger.getLogger( LoginController.class.getName() ).log(Level.WARNING, null, e);
        }
        return isLogged;
    }

    private void clearLogIn() {
        Autentication.getInstance().logOut();
        passwordTextField.setText("");
        emailTextField.setText("");
        systemLabel.setText("");
    }

    private void showStageBySpecifiedUser() {
        Personal user = Autentication.getInstance().getCurrentUser();
        if (user != null) {
            if (user instanceof Docente) {
                PrincipalDocenteController principalDocenteController = new PrincipalDocenteController();
                principalDocenteController.showStage();
            } else if (user instanceof Directivo) {
                PrincipalDirectivoController principalDirectivoController = new PrincipalDirectivoController();
                principalDirectivoController.showStage();
            } else if (user instanceof Encargado) {
                PrincipalEncargadoController principalEncargadoController = new PrincipalEncargadoController();
                principalEncargadoController.showStage();
            }
        }
    }
}
