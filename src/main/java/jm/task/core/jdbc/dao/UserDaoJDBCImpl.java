package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS new_schema.new_table" +
                    "(id mediumint not null auto_increment," +
                    "name VARCHAR(50)," +
                    "lastname VARCHAR(50)," +
                    "age tinyint," +
                    "PRIMARY KEY (id))");
            System.out.println("table createt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS new_schema.new_table");
            System.out.println("table delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastname, byte age) {
        String sql = "INSERT INTO new_schema.new_table(name, lastname, age) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastname);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User with name - " + name + " added in bd");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM new_schema.new_table WHERE id = " + id;
            statement.executeUpdate(sql);
            System.out.println("User delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {

        List<User> allUser = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age FROM new_schema.new_table";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                allUser.add(user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        for (User list: allUser) {
            System.out.println(list);
        }
        return allUser;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE new_schema.new_table";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("the table has been cleaned");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to clear");
        }
    }
}
