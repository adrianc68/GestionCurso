package gui.usuarios.encargado.curso.agregar;

import domain.Curso;
import domain.database.dao.CursoDAO;
import domain.database.dao.DocenteDAO;
import domain.usuarios.Docente;
import domain.usuarios.Encargado;
import gui.recursos.card.OperationAlert;
import gui.recursos.card.ValidatorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import util.Autentication;
import util.Validator;
import util.exceptions.CourseRegisteredException;
import util.exceptions.UserNotFoundException;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgregarCursoController extends ValidatorController implements Initializable {
    private boolean estadoOperacion;
    private boolean toggleButtonUnbugged;
    @FXML private AnchorPane rootStage;
    @FXML private TextArea descripcionTextField;
    @FXML private TextField nombreCursoTextField;
    @FXML private TextField numeroPersonalTextField;
    @FXML private TextField claveTextField;
    @FXML private TextField lunesTextField;
    @FXML private TextField martesTextField;
    @FXML private TextField miercolesTextField;
    @FXML private TextField juevesTextField;
    @FXML private TextField viernesTextField;
    @FXML private TextField sabadoTextField;
    @FXML private CheckBox docenteToggleButton;
    @FXML private Label systemLabel;
    @FXML private ComboBox<String> seccionComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidatorToTextFields();
        initToggleButton();
        initComboBox();
    }

    private void initComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add("Sección 1");
        observableList.add("Sección 2");
        seccionComboBox.setItems(observableList);
        seccionComboBox.getSelectionModel().select(1);
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/encargado/curso/agregar/AgregarCursoVista.fxml"), this);
        stage.showAndWait();
    }

    public boolean getEstadoOperacion() {
        return estadoOperacion;
    }

    private void initToggleButton() {
        toggleButtonUnbugged = true;
        docenteToggleButton.setOnMouseReleased( (MouseEvent e ) -> {
            if(toggleButtonUnbugged) {
                numeroPersonalTextField.setDisable(true);
                numeroPersonalTextField.setText("SIN DOCENTE");
                docenteToggleButton.setSelected(true);
                toggleButtonUnbugged = false;
            } else {
                numeroPersonalTextField.setDisable(false);
                numeroPersonalTextField.setText("");
                docenteToggleButton.setSelected(false);
                toggleButtonUnbugged = true;
            }

        });

    }

    @FXML
    void agregarCursoBotonPresionado(ActionEvent event) {
        if( verifyInputData() ) {
            agregarCurso();
            if(estadoOperacion) {
                stage.close();
            }
        } else {
            systemLabel.setText("¡Verifica tus datos!");
        }
    }

    private void agregarCurso() {
        CursoDAO cursoDAO = new CursoDAO();
        estadoOperacion = false;
        try {
            verificarCursoAgregado( claveTextField.getText() );
            if( !numeroPersonalTextField.getText().equals("SIN DOCENTE") ) {
                verificarExistenciaDocente( numeroPersonalTextField.getText() );
            }
            estadoOperacion = cursoDAO.agregarCurso( obtenerDatosDeCampos() );
        } catch (SQLException e) {
            OperationAlert.showUnsuccessfullAlert("¡Error de conexión!","¡Hubo con problema con la conexión a la base de datos!");
            Logger.getLogger( AgregarCursoController.class.getName() ).log(Level.SEVERE, null, e);
        } catch ( CourseRegisteredException ex) {
            OperationAlert.showUnsuccessfullAlert("¡Verifica tus datos!", ex.getLocalizedMessage() );
            claveTextField.getStyleClass().clear();
            claveTextField.getStyleClass().add("wrongTextField");
        } catch ( UserNotFoundException ec) {
            OperationAlert.showUnsuccessfullAlert("¡Verifica tus datos!", ec.getLocalizedMessage() );
            numeroPersonalTextField.getStyleClass().clear();
            numeroPersonalTextField.getStyleClass().add("wrongTextField");
        }
        if(estadoOperacion) {
            OperationAlert.showSuccessfullAlert("Se agregó correctamente el curso.", "¡Se ha agregado correctamente el curso!");
            stage.close();
        }
    }

    private void verificarExistenciaDocente(String claveDocente) throws SQLException, UserNotFoundException {
        DocenteDAO docenteDAO = new DocenteDAO();
        if( !docenteDAO.existeDocenteRegistrado(claveDocente) ) {
            throw new UserNotFoundException("¡Al parecer no existe un DOCENTE registrado con esa clave de personal!");
        }
    }

    private void verificarCursoAgregado(String claveCurso) throws SQLException, CourseRegisteredException {
        CursoDAO cursoDAO = new CursoDAO();
        if( cursoDAO.existeCursoAgregado(claveCurso) ) {
            throw new CourseRegisteredException("¡Al parecer ya existe un CURSO registrado con la misma clave de curso!");
        }
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    private Curso obtenerDatosDeCampos() {
        Curso curso = new Curso();
        curso.setNombre( nombreCursoTextField.getText() );
        Docente docente = new Docente();
        docente.setNumeroPersonal( numeroPersonalTextField.getText() );
        curso.setDocente(docente);
        curso.setDescripcion( descripcionTextField.getText() );
        String horario = "Lunes: " + lunesTextField.getText() + "Martes: " +  martesTextField.getText() + "Miercoles: " + miercolesTextField.getText() + "Jueves: "+  juevesTextField.getText() + "Viernes: " + viernesTextField.getText() + "Sabado: " + sabadoTextField.getText();
        curso.setHorario(horario);
        curso.setPlanCurso(null);
        curso.setEncargado( ( (Encargado) Autentication.getInstance().getCurrentUser() ) );
        curso.setClaveCurso( claveTextField.getText() );
        curso.setSeccion( seccionComboBox.getSelectionModel().getSelectedItem() );
        return curso;
    }

    private void initValidatorToTextFields() {
        interruptorMap = new LinkedHashMap<>();
        interruptorMap.put(nombreCursoTextField, false);
        interruptorMap.put(numeroPersonalTextField, false);
        interruptorMap.put(claveTextField, false);
        interruptorMap.put(descripcionTextField, false);
        interruptorMap.put(numeroPersonalTextField, true);
        interruptorMap.put(lunesTextField, false);
        interruptorMap.put(martesTextField, false);
        interruptorMap.put(miercolesTextField, false);
        interruptorMap.put(juevesTextField, false);
        interruptorMap.put(viernesTextField, false);
        interruptorMap.put(sabadoTextField, false);
        Map<TextInputControl, Object[]> validator = new HashMap<>();
        Object[] nameConstraints = {Validator.NAME_PATTERN, Validator.NAME_LENGTH};
        validator.put(nombreCursoTextField, nameConstraints);
        Object[] claveConstraints = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(claveTextField, claveConstraints);
        Object[] numeroPersonalConstraints = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(numeroPersonalTextField, numeroPersonalConstraints);
        Object[] descripcionConstraints = {Validator.LARGE_TEXT_PATTERN, Validator.LARGE_TEXT_LENGTH};
        validator.put(descripcionTextField, descripcionConstraints);
        Object[] diasConstraints = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(lunesTextField, diasConstraints);
        Object[] diasConstraints2 = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(martesTextField, diasConstraints2);
        Object[] diasConstraints3 = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(miercolesTextField, diasConstraints3);
        Object[] diasConstraints4 = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(juevesTextField, diasConstraints4);
        Object[] diasConstraints5 = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(viernesTextField, diasConstraints5);
        Object[] diasConstraints6 = {Validator.LETTERS_AND_NUMBERS_PATTERN, Validator.NAME_LENGTH};
        validator.put(sabadoTextField, diasConstraints6);
        super.initValidatorToTextInputControl(validator);
    }


}
