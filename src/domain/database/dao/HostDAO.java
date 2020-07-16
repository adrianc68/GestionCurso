package domain.database.dao;

import domain.database.Database;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HostDAO implements IDAO {
    private final Database database;

    public HostDAO() {
        this.database = new Database();
    }

    public int getAttemptsByMacAddress(String address) throws SQLException {
        int attempts = -1;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "SELECT attempts FROM Host WHERE mac_address = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ) {
                attempts = resultSet.getInt("attempts");
            }
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return attempts;
    }

    public boolean sendActualMacAddress(String address) throws SQLException {
        boolean executed;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "CALL sendAddress(?)";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, address);
            callableStatement.execute();
            executed = !callableStatement.wasNull();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return executed;
    }

    public boolean resetAttempts(String address) throws SQLException {
        boolean executed;
        try(Connection conn = database.getConnection() ){
            conn.setAutoCommit(false);
            String statement = "UPDATE Host SET attempts = 0 WHERE mac_address = ?";
            CallableStatement callableStatement = conn.prepareCall(statement);
            callableStatement.setString(1, address);
            callableStatement.execute();
            executed = !callableStatement.wasNull();
            conn.commit();
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        return executed;
    }

    @Override
    public List obtenerTodos() throws SQLException {
        return null;
    }

}
