package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MessageDBRepository extends AbstractDBRepository<Long, Message> {
    private final String url;
    private final String userName;
    private final String password;

    public MessageDBRepository(String url, String userName, String password, Validator<Message> validator) {
        super(validator);
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected Iterable<Message> extractEntity() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url,userName, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages");
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String message = resultSet.getString("message");
                Integer from = resultSet.getInt("message_from");
                Integer to = resultSet.getInt("message_to");
                Message newMessage = new Message(message,from,to);
                newMessage.setId(id);
                messages.add(newMessage);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Execute the necessary statement to add a Message to messages table from the database
     * @param entity added
     */
    @Override
    protected void addToDatabase(Message entity) {
        String sql = "INSERT INTO messages (id,message,message_from,message_to) VALUES (?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,entity.getId().intValue());
            ps.setString(2,entity.getMessage());
            ps.setInt(3,entity.getFrom());
            ps.setInt(4,entity.getTo());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to delete a Message from messages table from the database
     * @param aLong for entity to be deleted
     */
    @Override
    protected void removeFromDatabase(Long aLong) {
        int id = aLong.intValue();
        String sql = "DELETE FROM messages WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to modify a Message in messages table from the database
     * @param entity modified
     */
    @Override
    protected void updateInDatabase(Message entity) {
        int id = entity.getId().intValue();
        String sql = "UPDATE messages SET message='"+entity.getMessage() +
                "', message_from ="+ entity.getFrom()+
                ", message_to = "+entity.getTo()+ " WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
