package domain.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T> {
    List<T> obtenerTodos() throws SQLException;
}
