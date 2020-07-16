package gui.usuarios.encargado.docente.agregar;

import domain.database.dao.DocenteDAO;
import domain.enumeration.PerfilProfesional;
import domain.usuarios.Docente;
import domain.usuarios.Encargado;
import gui.recursos.card.OperationAlert;
import gui.recursos.card.ValidatorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import util.Autentication;
import util.Validator;
import util.exceptions.UserNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgregarDocenteController extends ValidatorController implements Initializable {
    private boolean estadoOperacion;
    @FXML private TextField nombreTextField;
    @FXML private DatePicker fechaNacimientoTextField;
    @FXML private TextField numeroPersonalTextField;
    @FXML private TextField rfcTextField;
    @FXML private ComboBox<PerfilProfesional> perfilProfesionalComboBox;
    @FXML private TextField anosExperienciaTextField;
    @FXML private TextField correoTextField;
    @FXML private Label systemLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextFields();
        initChoiceBox();
    }

    public boolean getEstadoOperacion() {
        return estadoOperacion;
    }

    public void showStage() {
        super.loadFXMLFile( getClass().getResource("/gui/usuarios/encargado/docente/agregar/AgregarDocenteVista.fxml"), this);
        stage.showAndWait();
    }

    private void initChoiceBox() {
        ObservableList<PerfilProfesional> observableListPerfiles = FXCollections.observableArrayList();
        observableListPerfiles.add(PerfilProfesional.MEDIO_SUPERIOR);
        observableListPerfiles.add(PerfilProfesional.PROFESIONALES);
        observableListPerfiles.add(PerfilProfesional.SUPERIOR);
        perfilProfesionalComboBox.setItems(observableListPerfiles);
        perfilProfesionalComboBox.getSelectionModel().select(1);
    }

    @FXML
    void cancelarBotonPresionado(ActionEvent event) {
        stage.close();
    }

    @FXML
    void registrarBotonPresionado(ActionEvent event) {
        if( verifyInputData() ) {
            agregarDocente();
        }
    }

    private void verificarExistenciaDocente(String claveDocente) throws SQLException, UserNotFoundException {
        DocenteDAO docenteDAO = new DocenteDAO();
        if( docenteDAO.existeDocenteRegistrado(claveDocente) ) {
            throw new UserNotFoundException("¡Al parecer existe un DOCENTE registrado con esa clave de personal!");
        }
    }

    private void agregarDocente() {
        Docente docente = new Docente();
        docente.setAnosExperiencia( Integer.parseInt( anosExperienciaTextField.getText() ) );
        docente.setPerfilProfesional(PerfilProfesional.valueOf( perfilProfesionalComboBox.getValue().toString() ) );
        docente.setEncargado( ((Encargado) Autentication.getInstance().getCurrentUser()) );
        docente.setNombre( nombreTextField.getText() );
        docente.setCorreo( correoTextField.getText() );
        docente.setFechaNacimiento( fechaNacimientoTextField.getValue().toString() );
        docente.setNumeroPersonal( numeroPersonalTextField.getText() );
        docente.setRfc( rfcTextField.getText() );
        estadoOperacion = false;
        try {
            verificarExistenciaDocente( numeroPersonalTextField.getText() );
            estadoOperacion = new DocenteDAO().agregarDocente(docente);
        } catch (SQLException e) {
            OperationAlert.showUnsuccessfullAlert("¡Se perdió la conexión!", "¡Lo sentimos! ¡Hemos perdido la conexión con la base de datos!");
            Logger.getLogger( AgregarDocenteController.class.getName() ).log(Level.SEVERE, null, e);
        } catch ( UserNotFoundException ea) {
            OperationAlert.showUnsuccessfullAlert("¡Verifica tus datos!", ea.getLocalizedMessage() );
            numeroPersonalTextField.getStyleClass().clear();
            numeroPersonalTextField.getStyleClass().add("wrongTextField");
        }
        if(estadoOperacion) {
            OperationAlert.showSuccessfullAlert("Se agregó correctamente el docente.", "¡Se ha agregado correctamente al docente!");
            stage.close();
        }
    }

    private void initValidatorToTextFields() {
        interruptorMap = new LinkedHashMap<>();
        interruptorMap.put(nombreTextField, false);
        interruptorMap.put(fechaNacimientoTextField.getEditor(), false);
        interruptorMap.put(numeroPersonalTextField, false);
        interruptorMap.put(rfcTextField, false);
        interruptorMap.put(anosExperienciaTextField, false);
        interruptorMap.put(correoTextField, false);
        Map<TextInputControl, Object[]> validator = new HashMap<>();
        Object[] nameConstraints = {Validator.NAME_PATTERN, Validator.NAME_LENGTH};
        validator.put(nombreTextField, nameConstraints);
        Object[] fechaNacimientoConstraints = {Validator.DATE_PATTERN, Validator.SHORT_LENGTH};
        validator.put(fechaNacimientoTextField.getEditor(), fechaNacimientoConstraints);
        Object[] numeroPersonalConstaints = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(numeroPersonalTextField, numeroPersonalConstaints);
        Object[] rfcConstraints = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.RFC_LENGTH};
        validator.put(rfcTextField, rfcConstraints);
        Object[] anosExperiencia = {Validator.NUMBER_PATTERN, Validator.TINY_INT_LENGTH};
        validator.put(anosExperienciaTextField, anosExperiencia);
        Object[] correoConstraints = {Validator.EMAIL_PATTERN, Validator.EMAIL_LENGTH};
        validator.put(correoTextField, correoConstraints);
        super.initValidatorToTextInputControl(validator);
    }

}
