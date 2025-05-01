package nicolaecaralicea.todolist.dao;

import nicolaecaralicea.todolist.datamodel.ToDoItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ToDoDao class is a Data Access Object that separates low-level data access logic from the business logic
 * Any Database related operations are supposed to be located in this class
 *
 */
@Repository
public class ToDoDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ToDoDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<ToDoItem> findAll() {
        return jdbcTemplate.query(
                "SELECT id, description, completion_status FROM todos",
                (rs, rowNum) -> ToDoItem.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .completionStatus(rs.getInt("completion_status")).build()
        );
    }

    public Optional<ToDoItem> find(int id) {
        List<ToDoItem> result = jdbcTemplate.query(
                "SELECT id, description, completion_status FROM todos WHERE id = ? LIMIT 1",
                preparedStmt -> preparedStmt.setObject(1, id),
                (rs, rowNum) -> ToDoItem.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .completionStatus(rs.getInt("completion_status")).build()
        );
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.empty();
    }

    public int insert(ToDoItem toDoItem) {
        String insertStmt =
                "INSERT INTO todos (id, description, completion_status) VALUES (:id, :description, :completion_status)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", toDoItem.getId())
                .addValue("description", toDoItem.getDescription())
                .addValue("completion_status", toDoItem.getCompletionStatus());
        return namedParameterJdbcTemplate.update(insertStmt, params);
    }

    public int update(ToDoItem toDoItem) {
        String updateStmt =
                "UPDATE todos SET description = :description, completion_status = :completion_status WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", toDoItem.getId())
                .addValue("description", toDoItem.getDescription())
                .addValue("completion_status", toDoItem.getCompletionStatus());
        return namedParameterJdbcTemplate.update(updateStmt, params);
    }

    public int delete(int id) {
        String insertStmt =
                "DELETE FROM todos WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.update(insertStmt, params);
    }

}
