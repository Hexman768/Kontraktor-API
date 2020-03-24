package com.pedigo.Kontraktor.dao;

import com.pedigo.Kontraktor.exception.FailedToInsertException;
import com.pedigo.Kontraktor.exception.UserNotFoundException;
import com.pedigo.Kontraktor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Repository("postgres")
public class UserDataAccessService implements UserDAO {
    private final JdbcTemplate jdbcTemplate;

    private static final String insertSql = "INSERT INTO users (id, firstName, lastName) VALUES (?, ?, ?)";
    private static final String deleteSql = "DELETE FROM users WHERE id = ?";
    private static final String updateSql = "UPDATE users SET firstName = ?, lastName = ? WHERE id = ?";

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertUser(UUID id, User user) throws FailedToInsertException {
        Object[] params = new Object[] { id.toString(), user.getFirstName(), user.getLastName() };
        int[] types = new int[] {Types.OTHER, Types.VARCHAR, Types.VARCHAR };
        int row = jdbcTemplate.update(insertSql, params, types);

        if (row == 0) {
            throw new FailedToInsertException();
        }

        return row;
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * from users;";

        List<User> users = jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            return new User(id, firstName, lastName);
        });

        return users;
    }

    @Override
    public User getUserById(UUID id) throws UserNotFoundException {
        final String sql = "SELECT * FROM users WHERE users.id = '" + id.toString() + "';";

        List<User> user = jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID uuid = UUID.fromString(resultSet.getString("id"));
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            return new User(uuid, firstName, lastName);
        });

        if (user.isEmpty()) {
            throw new UserNotFoundException(id.toString());
        }

        return user.get(0);
    }

    @Override
    public int deleteUser(UUID id) throws UserNotFoundException {
        Object[] params = new Object[] { id.toString() };
        int[] types = new int[] { Types.OTHER };
        int row = jdbcTemplate.update(deleteSql, params, types);

        if (row == 0) {
            throw new UserNotFoundException(id.toString());
        }
        return row;
    }

    @Override
    public int updateUserById(UUID id, User user) throws UserNotFoundException {
        Object[] params = new Object[] { user.getFirstName(), user.getLastName(), id.toString() };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.OTHER };
        int row = jdbcTemplate.update(updateSql, params, types);

        if (row == 0) {
            throw new UserNotFoundException(id.toString());
        }

        return row;
    }
}
