package gui.usuarios.docente.plan;

import domain.Actividad;
import domain.Curso;
import domain.Plan;
import domain.Tema;
import domain.database.dao.CursoDAO;
import domain.database.dao.PlanDAO;
import domain.enumeration.Estado;
import domain.enumeration.EstadoPlan;
import domain.usuarios.Docente;
import gui.recursos.card.OperationAlert;
import gui.recursos.card.ValidatorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import util.Autentication;
import util.Validator;
import util.exceptions.NoThemeException;
import util.exceptions.ThemeLimitReachedException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgregarPlanCursoController extends ValidatorController implements Initializable {
    private final int LIMIT_THEME = 10;
    private Tema temaSeleccionado;
    private Actividad actividadSeleccionada;
    private ObservableList<Tema> temaObservableList;
    private ObservableList<Actividad> actividadObservableList;
    ObservableList<Curso> cursosObservableList;
    private boolean estadoOperacion;
    @FXML private AnchorPane rootStage;
    @FXML private Label systemLabel;
    @FXML private ChoiceBox<Curso> cursosChoiceBox;
    @FXML private TextField horasTextField;
    @FXML private TextArea metodologiaTextArea;
    @FXML private TextArea objetivoGeneralTextArea;
    @FXML private TableView<Tema> temaTableView;
    @FXML private TableColumn<Tema, String> temasTableColumn;
    @FXML private TableColumn<Tema, String> fechaTableColumn;
    @FXML private TableView<Actividad> actividadTableView;
    @FXML private TableColumn<Actividad, String> actividadesTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTablas();
        obtenerCursosDeBaseDeDatos();
        configurarColumnasEditables();
        initValidatorToTextFields();
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/docente/plan/AgregarPlanVista.fxml"), this);
        stage.showAndWait();
    }

    public boolean getEstadoOperacion() {
        return estadoOperacion;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void agregarActividadBotonPresionado(ActionEvent event) {
        if( temaSeleccionado != null ) {
            Actividad actividad = new Actividad();
            actividad.setNombre("Actividad " + actividadObservableList.size() );
            actividadObservableList.add(actividad);
            temaSeleccionado.getActividades().add(actividad);
        }
    }

    @FXML
    void agregarTemaBotonPresionado(ActionEvent event) {
        Tema tema = new Tema();
        tema.setFecha( LocalDate.now().toString() );
        tema.setActividades(null);
        tema.setNombre("Tema " + temaObservableList.size() );
        tema.setEstado(Estado.PENDIENTE);
        temaObservableList.add(tema);
    }

    private void verificarTemasYActividades() throws NoThemeException, ThemeLimitReachedException {
        if( temaObservableList.size() == 0) {
            throw new NoThemeException("¡Al parecer nos has agregado un TEMA para el plan de curso!");
        } else if( temaObservableList.size() > LIMIT_THEME) {
            throw new ThemeLimitReachedException("¡Al parecer has pasado el LÍMITE (" + LIMIT_THEME + ") para los temas!");
        }
    }

    @FXML
    void agregarPlanBotonPresionado(ActionEvent event) {
        if( cursosChoiceBox.getSelectionModel().getSelectedItem() != null && verifyInputData() ) {
            try {
                verificarTemasYActividades();
                registrarPlan();
            } catch (NoThemeException e) {
                OperationAlert.showUnsuccessfullAlert("¡Cuidado!", e.getLocalizedMessage());
                Logger.getLogger( AgregarPlanCursoController.class.getName() ).log(Level.FINE, null, e);
            } catch (ThemeLimitReachedException le) {
                OperationAlert.showUnsuccessfullAlert("¡Cuidado!", le.getLocalizedMessage());
                Logger.getLogger( AgregarPlanCursoController.class.getName() ).log(Level.FINE, null, le);
            }
            if( estadoOperacion ) {
                OperationAlert.showSuccessfullAlert("¡Se ha agregado el PLAN para curso", "¡Has agregado un PLAN para el curso seleccionado!");
                stage.close();
            }
        } else {
            systemLabel.setText("¡Verifica tus datos!");
        }
    }


    private List<Tema> castObservableListToListTema(ObservableList<Tema> observableList) {
        List<Tema> list = new ArrayList<>();
        for ( Tema input : observableList ) {
            list.add(input);
        }
        return list;
    }

    private void registrarPlan() {
        PlanDAO planDAO = new PlanDAO();
        try {
            estadoOperacion = planDAO.agregarPlanCurso(crearPlan(), cursosChoiceBox.getSelectionModel().getSelectedItem().getClaveCurso() );
        } catch (SQLException e) {
            Logger.getLogger( AgregarPlanCursoController.class.getName() ).log(Level.SEVERE, null, e);
        }

    }

    private Plan crearPlan() {
        Plan plan = new Plan();
        plan.setFechaElaboracion( LocalDate.now().toString() );
        plan.setFechaActualizacion( LocalDate.now().toString() );
        plan.setAvance( Float.parseFloat( horasTextField.getText() ) );
        plan.setEstado( EstadoPlan.ACTIVO );
        plan.setDocente( (Docente) Autentication.getInstance().getCurrentUser() );
        plan.setListaTemas( castObservableListToListTema(temaObservableList) );
        return plan;
    }


    @FXML
    void eliminarActividadBotonPresionado(ActionEvent event) {
        if(actividadSeleccionada != null) {
            actividadObservableList.remove(actividadSeleccionada);
        }
    }

    @FXML
    void eliminarTemaBotonPresionado(ActionEvent event) {
        if(temaSeleccionado != null) {
            temaObservableList.remove(temaSeleccionado);
            actividadTableView.getItems().clear();
        }
    }

    private void obtenerCursosDeBaseDeDatos() {
        List cursoList = null;
        try {
            CursoDAO cursoDAO = new CursoDAO();
            cursoList = cursoDAO.obtenerCursosSinPlan( Autentication.getInstance().getCurrentUser().getNumeroPersonal() );
        } catch (SQLException e) {
            Logger.getLogger( AgregarPlanCursoController.class.getName() ).log(Level.SEVERE, null, e);
        }
        if(cursoList != null) {
            ObservableList<Curso> cursosObservableList = FXCollections.observableArrayList();
            cursosObservableList.addAll(cursoList);
            cursosChoiceBox.setConverter(new StringConverter<Curso>() {
                @Override
                public String toString(Curso object) {
                    return object.getNombre();
                }

                @Override
                public Curso fromString(String string) {
                    return null;
                }
            });
            cursosChoiceBox.setItems(cursosObservableList);
        }
    }

    private void configurarTablas() {
        temasTableColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") );
        fechaTableColumn.setCellValueFactory( new PropertyValueFactory<>("fecha") );
        actividadesTableColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") );
        actividadObservableList = FXCollections.observableArrayList();
        temaObservableList = FXCollections.observableArrayList();
        temaTableView.setItems(temaObservableList);
        actividadTableView.setItems(actividadObservableList);
        temaTableView.setFocusTraversable(false);
        actividadTableView.setFocusTraversable(false);
        configurarListenersATableView();
    }

    private void configurarListenersATableView() {
        temaTableView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if( newValue != null ) {
                temaSeleccionado = newValue;
                mostrarEnTablaLasActividadesDeTema();
            }
        });

        actividadTableView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if( newValue != null ) {
                actividadSeleccionada = newValue;
            }
        });

    }

    private void mostrarEnTablaLasActividadesDeTema() {
        actividadObservableList.clear();
        if( temaSeleccionado.getActividades() != null ) {
            actividadObservableList.addAll( temaSeleccionado.getActividades() );
            actividadTableView.setItems(actividadObservableList);
        } else {
            temaSeleccionado.setActividades( new ArrayList<>() );
        }
    }

    private void configurarColumnasEditables() {
        temaTableView.setEditable(true);
        actividadTableView.setEditable(true);
        temasTableColumn.setEditable(true);

        temasTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        temasTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
        });

        fechaTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        fechaTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
        });
        actividadesTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        actividadesTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
        });

    }

    private void initValidatorToTextFields() {
        interruptorMap = new LinkedHashMap<>();
        interruptorMap.put(horasTextField, false);
        interruptorMap.put(metodologiaTextArea, false);
        interruptorMap.put(objetivoGeneralTextArea, false);
        Map<TextInputControl, Object[]> validator = new HashMap<>();
        Object[] horasConstraints = {Validator.NUMBER_PATTERN, Validator.TINY_INT_LENGTH};
        validator.put(horasTextField, horasConstraints);
        Object[] metodologiaConstraints = {Validator.FREE_PATTERN, Validator.LARGE_TEXT_LENGTH};
        validator.put(metodologiaTextArea, metodologiaConstraints);
        Object[] objetivoGeneralConstraints = {Validator.FREE_PATTERN, Validator.LARGE_TEXT_LENGTH};
        validator.put(objetivoGeneralTextArea, objetivoGeneralConstraints);
        super.initValidatorToTextInputControl(validator);
    }

}
