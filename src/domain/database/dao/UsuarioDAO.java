package domain.database.dao;

import domain.usuarios.Directivo;
import domain.usuarios.Docente;
import domain.usuarios.Encargado;
import domain.usuarios.Personal;
import domain.usuarios.Usuario;
import domain.database.Database;
import domain.enumeration.PerfilProfesional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsuarioDAO implements IDAO {
    private final Database database;

    public UsuarioDAO() {
        database = new Database();
    }

    public Personal obtenerPersonalPorUsuarioYContraseña(String nombreUsuario, String contraseña) throws SQLException {
        Personal personal = null;
        try( Connection conn = database.getConnection() ) {
            String statement = "CALL obtener_cuenta_acceso(?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, nombreUsuario);
            callableStatement.setString(2, contraseña);
            ResultSet resultSet = callableStatement.executeQuery();
            if( resultSet.next() ) {
                personal = obtenerInstanceDePersonal(resultSet);
                personal.setNumeroPersonal( resultSet.getString( "US.numero_personal") );
                personal.setCorreo( resultSet.getString("PERS.correo") );
                personal.setNombre( resultSet.getString("PERS.nombre") );
                personal.setRfc( resultSet.getString("PERS.rfc") );
                personal.setFechaNacimiento( resultSet.getString("PERS.fecha_nacimiento") );
                personal.setNombre( resultSet.getString("PERS.nombre") );
                Usuario usuario = new Usuario();
                usuario.setContraseña( resultSet.getString("US.contraseña") );
                usuario.setUsuario( resultSet.getString("US.nombre_usuario") ) ;
                personal.setUsuario(usuario);
            }
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return personal;
    }

    @Override
    public List obtenerTodos() throws SQLException {
        return null;
    }

    private Personal obtenerInstanceDePersonal(ResultSet resultSet) throws SQLException {
        Personal personal = new Personal();
        if( resultSet.getString("DIC.sindicato") != null ) {
            Directivo directivo = new Directivo();
            directivo.setSindicato( resultSet.getString("DIC.sindicato") );
            personal = directivo;
        } else if( resultSet.getString("ENC.area") != null ) {
            Encargado encargado = new Encargado();
            encargado.setArea( resultSet.getString("ENC.area") );
            personal = encargado;
        } else if( resultSet.getString("DOC.anos_experiencia") != null ) {
            Docente docente = new Docente();
            docente.setAnosExperiencia( resultSet.getInt("DOC.anos_experiencia") );
            docente.setPerfilProfesional( PerfilProfesional.valueOf( resultSet.getString("DOC.perfil_profesional").toUpperCase() ) );
            personal = docente;
        }
        return personal;
    }

}
