package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBRepository extends AbstractDBRepository<Long,User> {
    private final String url;
    private final String userName;
    private final String password;

    public UserDBRepository(String url, String userName, String password, Validator<User> validator) {
        super(validator);
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Extract all entities of type User
     * @return users
     */
    @Override
    public Iterable<User> extractEntity(){
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url,userName, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");

                User newUser = new User(lastName,firstName);
                newUser.setId(id);
                users.add(newUser);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Execute the necessary statement to add a User to users table from the database
     * @param entity added
     */
    @Override
    protected void addToDatabase(User entity) {
        String sql = "INSERT INTO users (id,firstname, lastname) VALUES (?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,entity.getId().intValue());
            ps.setString(2,entity.getFirstName());
            ps.setString(3,entity.getLastName());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to delete a User from users table from the database
     * @param aLong for entity to be deleted
     */
    @Override
    protected void removeFromDatabase(Long aLong) {
        int id = aLong.intValue();
        String sql = "DELETE FROM users WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to modify a User in users table from the database
     * @param entity modified
     */
    @Override
    protected void updateInDatabase(User entity) {
        int id = entity.getId().intValue();
        String sql = "UPDATE users SET firstname='"+entity.getFirstName()+"'" +
                ", lastname ='"+ entity.getLastName()+"' WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
