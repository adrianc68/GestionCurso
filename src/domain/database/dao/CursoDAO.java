package domain.database.dao;

import domain.Curso;
import domain.Plan;
import domain.database.Database;
import domain.enumeration.EstadoPlan;
import domain.enumeration.PerfilProfesional;
import domain.usuarios.Docente;
import util.Autentication;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO implements IDAO {
    private final Database database;

    public CursoDAO() {
        this.database = new Database();
    }

    public boolean agregarCurso(Curso curso) throws SQLException {
        boolean cursoAgregado;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL agregarCurso(?,?,?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, curso.getClaveCurso() );
            callableStatement.setString(2, curso.getNombre() );
            callableStatement.setString(3, curso.getDescripcion());
            callableStatement.setString(4, curso.getSeccion() );
            callableStatement.setString(5, curso.getDocente().getNumeroPersonal() );
            callableStatement.setString(6, Autentication.getInstance().getCurrentUser().getNumeroPersonal() );
            callableStatement.setString(7, curso.getHorario() );
            callableStatement.setString(8, String.valueOf( curso.getDuracion() ) );
            callableStatement.execute();
            cursoAgregado = !callableStatement.wasNull();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return cursoAgregado;
    }

    @Override
    public List obtenerTodos() throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL obtener_cursos()";
            CallableStatement callableStatement = conn.prepareCall(statement);
            ResultSet resultSet = callableStatement.executeQuery();
            while( resultSet.next() ) {
                Docente docente = null;
                if( resultSet.getString("PERS.nombre") != null) {
                    docente = new Docente();
                    docente.setNombre( resultSet.getString("PERS.nombre") );
                    docente.setAnosExperiencia( resultSet.getInt("DOC.anos_experiencia") );
                    docente.setCorreo( resultSet.getString("PERS.correo") );
                    docente.setNumeroPersonal( resultSet.getString("DOC.numero_personal") );
                    docente.setPerfilProfesional( PerfilProfesional.valueOf( resultSet.getString("DOC.perfil_profesional").toUpperCase() ) );
                }
                Plan plan = null;
                if( resultSet.getString("PL.avance") != null) {
                    plan = new Plan();
                    plan.setAvance( resultSet.getFloat("PL.avance") );
                    plan.setEstado( EstadoPlan.valueOf( resultSet.getString("PL.estado").toUpperCase() ) );
                    plan.setFechaActualizacion( resultSet.getString("PL.fecha_actualizacion") );
                    plan.setFechaElaboracion( resultSet.getString("PL.fecha_elaboracion") );
                    plan.setId( resultSet.getInt("PL.id_plan") );
                }
                Curso curso = new Curso();
                curso.setNombre( resultSet.getString("CUR.nombre") );
                curso.setDocente(docente);
                curso.setDescripcion( resultSet.getString("CUR.descripcion") );
                curso.setHorario( resultSet.getString("CUR.horario") );
                curso.setPlanCurso(plan);
                curso.setClaveCurso( resultSet.getString("CUR.clave_curso") );
                curso.setSeccion( resultSet.getString("CUR.seccion") );
                cursos.add(curso);
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return cursos;
    }

    public boolean existeCursoAgregado(String claveCurso) throws SQLException {
        boolean cursoAgregado = false;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL obtener_cursos_agregado(?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, claveCurso );
            ResultSet resultSet = callableStatement.executeQuery();
            if( resultSet.next() ) {
                cursoAgregado = true;
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return cursoAgregado;
    }

    public List obtenerPorDocente(String numeroPersonal) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL obtener_cursos_por_profesor(?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, numeroPersonal);
            ResultSet resultSet = callableStatement.executeQuery();
            while( resultSet.next() ) {
                Docente docente = null;
                if( resultSet.getString("PERS.nombre") != null) {
                    docente = new Docente();
                    docente.setNombre( resultSet.getString("PERS.nombre") );
                    docente.setAnosExperiencia( resultSet.getInt("DOC.anos_experiencia") );
                    docente.setCorreo( resultSet.getString("PERS.correo") );
                    docente.setNumeroPersonal( resultSet.getString("DOC.numero_personal") );
                    docente.setPerfilProfesional( PerfilProfesional.valueOf( resultSet.getString("DOC.perfil_profesional").toUpperCase() ) );
                }
                Plan plan = null;
                if( resultSet.getString("PL.avance") != null) {
                    plan = new Plan();
                    plan.setAvance( resultSet.getFloat("PL.avance") );
                    plan.setEstado( EstadoPlan.valueOf( resultSet.getString("PL.estado").toUpperCase() ) );
                    plan.setFechaActualizacion( resultSet.getString("PL.fecha_actualizacion") );
                    plan.setFechaElaboracion( resultSet.getString("PL.fecha_elaboracion") );
                    plan.setId( resultSet.getInt("PL.id_plan") );
                }
                Curso curso = new Curso();
                curso.setNombre( resultSet.getString("CUR.nombre") );
                curso.setDocente(docente);
                curso.setDescripcion( resultSet.getString("CUR.descripcion") );
                curso.setHorario( resultSet.getString("CUR.horario") );
                curso.setPlanCurso(plan);
                curso.setClaveCurso( resultSet.getString("CUR.clave_curso") );
                curso.setSeccion( resultSet.getString("CUR.seccion") );
                cursos.add(curso);
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return cursos;
    }

    public List obtenerCursosSinPlan(String numeroPersonal) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL obtener_cursos_sin_plan(?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, numeroPersonal);
            callableStatement.executeQuery();
            ResultSet resultSet = callableStatement.executeQuery();
            while( resultSet.next() ) {
                Docente docente = new Docente();
                docente.setNombre( resultSet.getString("PERS.nombre") );
                docente.setAnosExperiencia( resultSet.getInt("DOC.anos_experiencia") );
                docente.setCorreo( resultSet.getString("PERS.correo") );
                docente.setNumeroPersonal( resultSet.getString("DOC.numero_personal") );
                docente.setPerfilProfesional( PerfilProfesional.valueOf( resultSet.getString("DOC.perfil_profesional").toUpperCase() ) );
                Curso curso = new Curso();
                curso.setNombre( resultSet.getString("CUR.nombre") );
                curso.setDocente(docente);
                curso.setDescripcion( resultSet.getString("CUR.descripcion") );
                curso.setHorario( resultSet.getString("CUR.horario") );
                curso.setClaveCurso( resultSet.getString("CUR.clave_curso") );
                curso.setSeccion( resultSet.getString("CUR.seccion") );
                cursos.add(curso);
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return cursos;
    }



}
