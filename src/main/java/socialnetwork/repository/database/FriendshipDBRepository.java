package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepository extends AbstractDBRepository<Long,Friendship>  {
    private final String url;
    private final String userName;
    private final String password;

    private final Repository0<Long, User> userDBRepository;

    public FriendshipDBRepository(String url, String userName, String password, Repository0<Long, User> repository0, Validator<Friendship> validator) {
        super(validator);
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.userDBRepository = repository0;
    }

    /**
     * Extract all entities of type Friendship
     * @return friendships
     */
    @Override
    public Iterable<Friendship> extractEntity(){
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url,userName, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long firstfriend = resultSet.getLong("firstfriend");
                Long secondfriend = resultSet.getLong("secondfriend");
                String date = resultSet.getString("date");
                User u1 = userDBRepository.findOne(firstfriend);
                User u2 = userDBRepository.findOne(secondfriend);
                u1.getFriends().add(u2);
                u2.getFriends().add(u1);
                LocalDateTime dateTime = LocalDateTime.parse(date);
                Friendship newFriendship = new Friendship(dateTime,u1, u2);
                newFriendship.setId(id);
                friendships.add(newFriendship);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }

    /**
     * Execute the necessary statement to add a Friendship to friendships table from the database
     * @param entity added
     */
    @Override
    protected void addToDatabase(Friendship entity) {
        String sql = "INSERT INTO friendships (id,firstfriend, secondfriend,date) VALUES (?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,entity.getId().intValue());
            String date = entity.getDate().toString();

            ps.setString(4,date);
            ps.setInt(2,entity.getFirstFriend().getId().intValue());
            ps.setInt(3,entity.getSecondFriend().getId().intValue());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to delete a Friendship from friendships table from the database
     * @param aLong for entity to be deleted
     */
    @Override
    protected void removeFromDatabase(Long aLong) {
        int id = aLong.intValue();
        String sql = "DELETE FROM friendships WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Execute the necessary statement to modify a Friendship in friendships table from the database
     * @param entity modified
     */
    @Override
    protected void updateInDatabase(Friendship entity) {
        int id = entity.getId().intValue();
        LocalDateTime localDateTime = entity.getDate();
        String date = localDateTime.toString();
        String sql = "UPDATE friendships SET date="+date+"" +
                ", firstfriend ="+ entity.getFirstFriend().getId().intValue()+
                ", secondfriend = "+entity.getSecondFriend().getId().intValue()+" WHERE id="+id;
        try(Connection connection = DriverManager.getConnection(url,userName, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
