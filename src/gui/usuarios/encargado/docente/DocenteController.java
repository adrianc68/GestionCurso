package gui.usuarios.encargado.docente;

import domain.database.dao.DocenteDAO;
import domain.enumeration.PerfilProfesional;
import domain.usuarios.Docente;
import gui.recursos.card.Controller;
import gui.usuarios.encargado.docente.agregar.AgregarDocenteController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocenteController extends Controller implements Initializable {
    private ObservableList<Docente> observableList;
    @FXML private TableView<Docente> docentesTableView;
    @FXML private TableColumn<Docente, String> numeroPersonalColumn;
    @FXML private TableColumn<Docente, String> nombreColumn;
    @FXML private TableColumn<Docente, String> rfcColumn;
    @FXML private TableColumn<Docente, String> correoColumn;
    @FXML private TableColumn<Docente, String> fechaNacimientoColumn;
    @FXML private TableColumn<Docente, Integer> experienciaColumn;
    @FXML private TableColumn<Docente, PerfilProfesional> perfilColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTablas();
        obtenerDocentesDeBaseDeDatos();
    }

    public void showStage() {
        super.loadFXMLFile(getClass().getResource("/gui/usuarios/encargado/docente/DocenteVista.fxml"), this);
        stage.showAndWait();
    }

    @FXML
    void addButtonPressed(ActionEvent event) {
        AgregarDocenteController agregarDocenteController = new AgregarDocenteController();
        agregarDocenteController.showStage();
        if(agregarDocenteController.getEstadoOperacion()) {
            obtenerDocentesDeBaseDeDatos();
        }
    }

    @FXML
    void closeButtonPressed(ActionEvent event) {
        stage.close();
    }

    private void configurarTablas() {
        numeroPersonalColumn.setCellValueFactory( new PropertyValueFactory<>("numeroPersonal") );
        nombreColumn.setCellValueFactory( new PropertyValueFactory<>("nombre") ) ;
        rfcColumn.setCellValueFactory( new PropertyValueFactory<>("rfc") );
        correoColumn.setCellValueFactory( new PropertyValueFactory<>("correo") );
        fechaNacimientoColumn.setCellValueFactory( new PropertyValueFactory<>("fechaNacimiento") );
        experienciaColumn.setCellValueFactory( new PropertyValueFactory<>("anosExperiencia") );
        perfilColumn.setCellValueFactory( new PropertyValueFactory<>("perfilProfesional") );
        observableList = FXCollections.observableArrayList();
    }

    private void obtenerDocentesDeBaseDeDatos() {
        List<Docente> docentes = null;
        try {
            DocenteDAO docenteDAO = new DocenteDAO();
            docentes = docenteDAO.obtenerTodos();
        } catch (SQLException e) {
            Logger.getLogger( DocenteController.class.getName() ).log(Level.SEVERE, null, e);
        }
        if(docentes != null) {
            observableList.clear();
            observableList.addAll(docentes);
            docentesTableView.setItems(observableList);
        }
    }



}
