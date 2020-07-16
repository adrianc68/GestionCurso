package domain.database.dao;

import domain.Actividad;
import domain.Tema;
import domain.database.Database;
import domain.enumeration.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemaDAO implements IDAO {
    private final Database database;

    public TemaDAO() {
        this.database = new Database();
    }

    @Override
    public List obtenerTodos() throws SQLException {
        return null;
    }

    public boolean actualizarFecha(String fecha, int idTema) throws SQLException {
        boolean isUpdated;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "UPDATE Tema SET fecha = ? WHERE id_tema = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, fecha);
            preparedStatement.setInt(2, idTema);
            isUpdated = preparedStatement.execute();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return isUpdated;
    }

    public boolean actualizarNombreTema(String nombreTema, int idTema) throws SQLException {
        boolean isUpdated;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "UPDATE Tema SET nombre = ? WHERE id_tema = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, nombreTema);
            preparedStatement.setInt(2, idTema);
            isUpdated = preparedStatement.execute();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return isUpdated;
    }

    public List obtenerTodosPorCurso(String claveCurso) throws SQLException {
        List<Tema> temas = new ArrayList<>();
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT TEM.fecha, TEM.id_tema, TEM.nombre, TEM.estado FROM Tema AS TEM INNER JOIN Curso AS CUR ON TEM.clave_curso = CUR.clave_curso AND CUR.clave_curso = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, claveCurso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while( resultSet.next() ) {
                Tema tema = new Tema();
                tema.setEstado(Estado.valueOf( resultSet.getString("TEM.estado").toUpperCase() ) );
                tema.setId( resultSet.getInt( "TEM.id_tema") );
                tema.setNombre( resultSet.getString("TEM.nombre") );
                tema.setFecha( resultSet.getString("TEM.fecha") );
                temas.add(tema);
                PreparedStatement preparedStatementMultivalued = conn.prepareStatement("SELECT ACT.actividad FROM Actividad AS ACT WHERE id_tema = ?");
                preparedStatementMultivalued.setInt(1, tema.getId() );
                ResultSet resultSetMultivalued = preparedStatementMultivalued.executeQuery();
                List<Actividad> actividadList = new ArrayList<>();
                while( resultSetMultivalued.next() ) {
                    Actividad actividad = new Actividad();
                    actividad.setNombre( resultSetMultivalued.getString("ACT.actividad") );
                    actividadList.add(actividad);
                }
                tema.setActividades(actividadList);
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return temas;
    }





}
