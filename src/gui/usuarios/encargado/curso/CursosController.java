package gui.usuarios.encargado.curso;

import domain.Curso;
import domain.database.dao.CursoDAO;
import gui.usuarios.encargado.curso.agregar.AgregarCursoController;
import gui.recursos.card.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursosController extends Controller implements Initializable {
    private ObservableList<Curso> cursosObservableList;
    @FXML private AnchorPane rootStage;
    @FXML private TableView<Curso> cursosTableView;
    @FXML private TableColumn<Curso, String> cursoTableColumn;
    @FXML private TableColumn<Curso, String> seccionTableColumn;
    @FXML private TableColumn<Curso, String> claveTableColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTablas();
        obtenerCursosDeBaseDeDatos();
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/encargado/curso/CursosController.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void agregarCursoBotonPresionado(ActionEvent event) {
        AgregarCursoController agregarCursoController = new AgregarCursoController();
        agregarCursoController.showStage();
        if( agregarCursoController.getEstadoOperacion() ) {
            obtenerCursosDeBaseDeDatos();
        }
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
            Logger.getLogger( CursosController.class.getName() ).log(Level.SEVERE, null, e);
        }
        if(cursoList != null) {
            cursosObservableList.clear();
            cursosObservableList.addAll(cursoList);
            cursosTableView.setItems(cursosObservableList);
        }
    }

    private void configurarTablas() {
        seccionTableColumn.setCellValueFactory( new PropertyValueFactory<>("seccion") );
        claveTableColumn.setCellValueFactory( new PropertyValueFactory<>("claveCurso") );
        cursoTableColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") );
        cursosObservableList = FXCollections.observableArrayList();
        cursosTableView.setFocusTraversable(false);
    }


}
