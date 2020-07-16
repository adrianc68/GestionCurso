package gui.usuarios.docente;

import domain.Actividad;
import domain.Curso;
import domain.Tema;
import domain.database.dao.ActividadDAO;
import domain.database.dao.CursoDAO;
import domain.database.dao.TemaDAO;
import gui.recursos.card.CursoCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import gui.recursos.card.Controller;
import util.Autentication;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursosController extends Controller implements Initializable {
    private ObservableList<Tema> temaObservableList;
    private ObservableList<Actividad> actividadObservableList;
    private Tema temaSeleccionado;
    @FXML private AnchorPane rootStage;
    @FXML private FlowPane cursosPane;
    @FXML private TableView<Tema> temaTableView;
    @FXML private TableColumn<Tema, String> temasTableColumn;
    @FXML private TableColumn<Tema, String> fechaTableColumn;
    @FXML private TableView<Actividad> actividadTableView;
    @FXML private TableColumn<Actividad, String> actividadesTableColumn;
    @FXML private Label systemLabel;
    @FXML private Button agregarBoton;
    @FXML private Label seleccionadoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obtenerCursosDeBaseDeDatos();
        configurarTablas();
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/docente/CursosVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        stage.close();
    }


    @FXML
    void agregarPlanBotonPresionado(ActionEvent event) {

    }

    @FXML
    void guardarBotonPresionado(ActionEvent event) {
        temaTableView.setEditable(false);
        actividadTableView.setEditable(false);
        temasTableColumn.setEditable(false);
        systemLabel.setText("");
    }

    @FXML
    void modificarBotonPresionado(ActionEvent event) {
        temaTableView.setEditable(true);
        actividadTableView.setEditable(true);
        temasTableColumn.setEditable(true);
        systemLabel.setText("**Modo edición**");
    }

    private void obtenerCursosDeBaseDeDatos() {
        List<Curso> cursoList = new ArrayList<>();
        try {
            CursoDAO cursoDAO = new CursoDAO();
            cursoList = cursoDAO.obtenerPorDocente( Autentication.getInstance().getCurrentUser().getNumeroPersonal() );
        } catch (SQLException e) {
            Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, e);
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
            Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, e);
        }
        return temasArrayList;
    }

    private void agregarCursoACard(Curso curso) {
        CursoCard cursoCard = new CursoCard(curso);
        cursoCard.setOnMouseReleased( MouseEvent -> {
            temaTableView.getItems().clear();
            actividadTableView.getItems().clear();
            temaSeleccionado = null;
            seleccionadoLabel.setText( cursoCard.getCurso().getNombre() );
            mostrarTemasDeCursoEnTableView(curso);

        });
        cursosPane.getChildren().add(cursoCard);
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
        configurarColumnasEditables();
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

    private void configurarColumnasEditables() {

        temasTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        temasTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
            try {
                new TemaDAO().actualizarNombreTema( e.getNewValue(), temaSeleccionado.getId() );
            } catch (SQLException ex) {
                Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, ex);
            }
        });

        fechaTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        fechaTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
            try {
                new TemaDAO().actualizarFecha( e.getNewValue(), temaSeleccionado.getId() );
            } catch (SQLException ex) {
                Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, ex);
            }
        });
        actividadesTableColumn.setCellFactory( TextFieldTableCell.forTableColumn() );
        actividadesTableColumn.setOnEditCommit( e -> {
            e.getTableView().getItems().get( e.getTablePosition().getRow() ).setNombre( e.getNewValue() );
            try {
                new ActividadDAO().actualizarDatosActividad(temaSeleccionado.getId(), e.getTableView().getItems() );
            } catch (SQLException ex) {
                Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, e);
            }
        });
    }

}
