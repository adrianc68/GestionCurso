package gui.usuarios.directivo;

import domain.Actividad;
import domain.Curso;
import domain.Tema;
import domain.database.dao.CursoDAO;
import domain.database.dao.TemaDAO;
import gui.recursos.card.CursoCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import gui.recursos.card.Controller;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursoController extends Controller implements Initializable {
    private ObservableList<Tema> temaObservableList;
    private ObservableList<Actividad> actividadObservableList;
    private Tema temaSeleccionado;
    private Curso cursoSelecionado;
    @FXML private AnchorPane rootStage;
    @FXML private FlowPane cursosPane;
    @FXML private TableView<Tema> temaTableView;
    @FXML private TableColumn<Tema, String> temasTableColumn;
    @FXML private TableColumn<Tema, String> fechaTableColumn;
    @FXML private TableView<Actividad> actividadTableView;
    @FXML private TableColumn<Actividad, String> actividadesTableColumn;
    @FXML private Label systemLabel;
    @FXML private Label cursoLabel;
    @FXML private Label nombreDocenteLabel;
    @FXML private Label estadoCursoLabel;
    @FXML private Label claveCursoLabel;
    @FXML private Label numeroPersonalLabel;
    @FXML private Label seccionLabel;
    @FXML private Label fechaElaboracionLabel;
    @FXML private Label fechaActualizacionLabel;
    @FXML private Label avanceLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obtenerCursosDeBaseDeDatos();
        configurarTablas();
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/directivo/CursoVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    private void obtenerCursosDeBaseDeDatos() {
        List<Curso> cursoList = null;
        try {
            CursoDAO cursoDAO = new CursoDAO();
            cursoList = cursoDAO.obtenerTodos();
        } catch (SQLException e) {
            Logger.getLogger( CursoController.class.getName() ).log(Level.SEVERE, null, e);
        }
        if(cursoList != null) {
            for ( Curso curso: cursoList) {
                agregarCursoACard(curso);
            }
        }
    }

    private List<Tema> obtenerTemasDeBaseDeDatosPorCurso(String claveCurso) {
        List<Tema> temasArrayList = new ArrayList<>();
        try {
            TemaDAO temaDAO = new TemaDAO();
            temasArrayList = temaDAO.obtenerTodosPorCurso(claveCurso);
        } catch (SQLException e) {
            Logger.getLogger( CursoController.class.getName() ).log(Level.SEVERE, null, e);
        }
        return temasArrayList;
    }

    private void agregarCursoACard(Curso curso) {
        CursoCard cursoCard = new CursoCard(curso);
        cursoCard.setOnMouseReleased( MouseEvent -> {
            temaTableView.getItems().clear();
            actividadTableView.getItems().clear();
            temaSeleccionado = null;
            cursoSelecionado = curso;
            mostrarTemasDeCursoEnTableView(curso);
            mostrarInformacionEnLabel();
        });
        cursosPane.getChildren().add(cursoCard);
    }

    private void mostrarInformacionEnLabel() {
        if( cursoSelecionado.getPlanCurso() != null) {
            avanceLabel.setText(String.valueOf( cursoSelecionado.getPlanCurso().getAvance() ) );
            estadoCursoLabel.setText( cursoSelecionado.getPlanCurso().getEstado().toString() );
            fechaActualizacionLabel.setText( cursoSelecionado.getPlanCurso().getFechaActualizacion() );
            fechaElaboracionLabel.setText( cursoSelecionado.getPlanCurso().getFechaElaboracion());
        } else {
            estadoCursoLabel.setText("Sin plan de curso");
            avanceLabel.setText("Sin plan de curso" );
            fechaActualizacionLabel.setText("Sin plan de curso");
            fechaElaboracionLabel.setText("Sin plan de curso");
        }
        claveCursoLabel.setText( cursoSelecionado.getClaveCurso() );
        cursoLabel.setText( cursoSelecionado.getNombre() );
        numeroPersonalLabel.setText( (cursoSelecionado.getDocente() != null) ? cursoSelecionado.getDocente().getNumeroPersonal() : "" );
        seccionLabel.setText( cursoSelecionado.getSeccion() );
        nombreDocenteLabel.setText( (cursoSelecionado.getDocente() != null) ? cursoSelecionado.getDocente().getNombre() : "" );
    }

    private void configurarTablas() {
        temasTableColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") );
        fechaTableColumn.setCellValueFactory( new PropertyValueFactory<>("fecha") );
        actividadesTableColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") );
        actividadObservableList = FXCollections.observableArrayList();
        temaObservableList = FXCollections.observableArrayList();
        temaTableView.setFocusTraversable(false);
        actividadTableView.setFocusTraversable(false);
        configurarListenersATableView();
    }

    private void mostrarTemasDeCursoEnTableView(Curso curso) {
        temaObservableList.clear();
        temaObservableList.addAll( obtenerTemasDeBaseDeDatosPorCurso( curso.getClaveCurso() ) );
        temaTableView.setItems(temaObservableList);
    }

    private void mostrarActividadesDeTemaEnTableView(Tema tema) {
        actividadObservableList.clear();
        actividadObservableList.addAll( tema.getActividades() );
        actividadTableView.setItems(actividadObservableList);
    }

    private void configurarListenersATableView() {
        temaTableView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if( newValue != null ) {
                mostrarActividadesDeTemaEnTableView( temaTableView.getSelectionModel().getSelectedItem() );
                temaSeleccionado = newValue;
            }
        });
    }

}
