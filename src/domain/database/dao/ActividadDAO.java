package domain.database.dao;

import domain.Actividad;
import domain.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActividadDAO implements IDAO {
    private final Database database;

    public ActividadDAO() {
        this.database = new Database();
    }

    @Override
    public List obtenerTodos() throws SQLException {
        return null;
    }

    public List obtenerTodosPorTema(int idTema) throws SQLException {
        List<Actividad> actividadList = new ArrayList<>();
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT ACT.nombre FROM Actividad AS ACT WHERE id_tema = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idTema);
            ResultSet resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ) {
                Actividad actividad = new Actividad();
                actividad.setNombre( resultSet.getString("ACT.nombre") );
                actividadList.add(actividad);
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return actividadList;
    }

    public boolean actualizarDatosActividad(int idTema, List<Actividad> nuevasActividades) throws SQLException {
        boolean isUpdated;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "DELETE FROM Actividad WHERE id_tema = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idTema);
            isUpdated = preparedStatement.execute();
            String statamentAdd = "INSERT INTO Actividad(nombre, id_tema) VALUES(?,?)";
            preparedStatement = conn.prepareStatement(statamentAdd);
            Iterator iterator = nuevasActividades.iterator();
            while( iterator.hasNext() ) {
                preparedStatement.setString( 1, ( (Actividad) iterator.next() ).getNombre() );
                preparedStatement.setInt( 2, idTema );
                preparedStatement.execute();

            }

            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return isUpdated;
    }

}
