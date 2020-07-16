package domain.database.dao;

import domain.Actividad;
import domain.Plan;
import domain.Tema;
import domain.database.Database;
import domain.enumeration.Estado;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlanDAO implements IDAO{
    private final Database database;

    public PlanDAO() {
        database = new Database();
    }

    @Override
    public List obtenerTodos() throws SQLException {
        return null;
    }

    public boolean agregarPlanCurso(Plan plan, String claveCurso) throws SQLException{
        boolean planAgregado;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL agregarPlan(?,?,?,?,?,?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, plan.getFechaElaboracion() );
            callableStatement.setString(2, plan.getEstado().toString() );
            callableStatement.setString(3, String.valueOf( plan.getAvance() ) );
            callableStatement.setString(4, plan.getFechaActualizacion() );
            callableStatement.setString(5, plan.getDocente().getNumeroPersonal() );
            callableStatement.setString(6, claveCurso);
            callableStatement.execute();
            planAgregado = !callableStatement.wasNull();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet resultSet = preparedStatement.executeQuery("SELECT LAST_INSERT_ID()");
            resultSet.next();
            plan.setId( resultSet.getInt(1) );
            Iterator iterator = plan.getListaTemas().iterator();
            PreparedStatement preparedStatementMultivalued = conn.prepareStatement("INSERT INTO Tema(nombre, estado, clave_curso, fecha) VALUES(?,?,?,?)");
            PreparedStatement preparedStatementActividad = conn.prepareStatement("INSERT INTO Actividad(actividad, id_tema) VALUES(?,?)");
            while( iterator.hasNext()  ) {
                Tema tema = (Tema) iterator.next();
                preparedStatementMultivalued.setString(1, tema.getNombre() );
                preparedStatementMultivalued.setString(2, tema.getEstado().toString() );
                preparedStatementMultivalued.setString(3, claveCurso );
                preparedStatementMultivalued.setString(4, tema.getFecha() );
                preparedStatementMultivalued.executeUpdate();
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                tema.setId( resultSet.getInt(1) );
                System.out.println(tema);
                if(tema.getActividades() != null ) {
                    Iterator iterator2 = tema.getActividades().iterator();
                    while( iterator2.hasNext() ) {
                        Actividad actividad = (Actividad) iterator2.next();
                        preparedStatementActividad.setString(1, actividad.getNombre() );
                        preparedStatementActividad.setString(2, String.valueOf( tema.getId() ) );
                        preparedStatementActividad.executeUpdate();
                        System.out.println(actividad);
                    }
                }

            }
            conn.commit();
            return planAgregado;
        } catch (SQLException sqlException) {
            throw sqlException;
        }
    }

}
