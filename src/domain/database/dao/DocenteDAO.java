package domain.database.dao;

import domain.database.Database;
import domain.enumeration.PerfilProfesional;
import domain.usuarios.Docente;
import domain.usuarios.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO implements IDAO {
    private final Database database;

    public DocenteDAO() {
        database = new Database();
    }

    public boolean agregarDocente(Docente docente) throws SQLException {
        boolean docenteAgregado = false;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL agregarDocente(?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, docente.getNumeroPersonal() );
            callableStatement.setString(2, docente.getNombre() );
            callableStatement.setString(3, docente.getRfc() );
            callableStatement.setString(4, docente.getCorreo() );
            callableStatement.setString(5, docente.getFechaNacimiento() );
            callableStatement.setString(6, String.valueOf( docente.getAnosExperiencia() ) );
            callableStatement.setString(7, docente.getPerfilProfesional().getId() );
            callableStatement.setString(8, docente.getEncargado().getNumeroPersonal() );
            callableStatement.execute();
            docenteAgregado = !callableStatement.wasNull();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return docenteAgregado;
    }

    @Override
    public List obtenerTodos() throws SQLException {
        List<Docente> docentes = new ArrayList<>();
        try( Connection conn = database.getConnection() ) {
            String statement = "CALL obtener_docentes()";
            CallableStatement callableStatement = conn.prepareCall(statement);
            ResultSet resultSet = callableStatement.executeQuery();
            while( resultSet.next() ) {
                Docente docente = new Docente();
                docente.setNumeroPersonal( resultSet.getString( "US.numero_personal") );
                docente.setCorreo( resultSet.getString("PERS.correo") );
                docente.setNombre( resultSet.getString("PERS.nombre") );
                docente.setRfc( resultSet.getString("PERS.rfc") );
                docente.setFechaNacimiento( resultSet.getString("PERS.fecha_nacimiento") );
                docente.setNombre( resultSet.getString("PERS.nombre") );
                Usuario usuario = new Usuario();
                usuario.setContraseña( resultSet.getString("US.contraseña") );
                usuario.setUsuario( resultSet.getString("US.nombre_usuario") ) ;
                docente.setAnosExperiencia( resultSet.getInt("DOC.anos_experiencia") );
                docente.setPerfilProfesional( PerfilProfesional.valueOf( resultSet.getString("DOC.perfil_profesional").toUpperCase() ) );
                docente.setUsuario(usuario);
                docentes.add(docente);
            }
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return docentes;
    }

    public boolean existeDocenteRegistrado(String numeroPersonal) throws SQLException {
        boolean docenteAgregado = false;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL obtener_docente_registrado(?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, numeroPersonal );
            ResultSet resultSet = callableStatement.executeQuery();
            if( resultSet.next() ) {
                docenteAgregado = true;
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return docenteAgregado;
    }


}
