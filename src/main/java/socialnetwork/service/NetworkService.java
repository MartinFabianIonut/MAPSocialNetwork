package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.graph.UndirectedGraph;
import socialnetwork.domain.validators.Validator;
import socialnetwork.observer.Observable;
import socialnetwork.observer.Observer;
import socialnetwork.repository.Repository0;

import java.time.LocalDateTime;
import java.util.*;

public class NetworkService implements Observable {
    private final Repository0<Long, User> repoUsers;
    private final Repository0<Long, Friendship> repoFriendships;

    private final Validator<User> validatorUser;
    private final Validator<Friendship> validatorFriendship;

    public NetworkService(Repository0<Long, User> repoUsers,
                          Repository0<Long, Friendship> repoFriendships,
                          Validator<User> validatorUser,
                          Validator<Friendship> validatorFriendship) {
        this.repoUsers = repoUsers;
        this.repoUsers.loadData();
        this.repoFriendships = repoFriendships;
        this.repoFriendships.loadData();
        this.validatorUser = validatorUser;
        this.validatorFriendship = validatorFriendship;
    }

    /**
     * @return all existing users
     */
    public Iterable<User> getAllUsers() {
        return repoUsers.findAll();
    }

    public Iterable<Friendship> getAllFriendships() {
        return repoFriendships.findAll();
    }


    public User findByName(String name) throws RepoException {
        Iterable<User> allUsers = getAllUsers();
        User user = null;
        for (User u : allUsers) {
            if ((u.getLastName() + " " + u.getFirstName()).equals(name)) {
                user = repoUsers.findOne(u.getId());
                break;
            }
        }
        return user;
        //throw new RepoException("Non existing user!");
    }

    /**
     * @param lastName  is the last name of a new user to add to memory, String type,
     *                  if "", validator will throw exception, which will go in cascade
     * @param firstName is the first name of a new user to add to memory, String type,
     *                  if "", validator will throw exception, which will go in cascade
     */
    public void addUser(String lastName, String firstName) throws RepoException {
        User user = new User(lastName, firstName);
        Iterable<User> users = repoUsers.findAll();
        Long maxi = 0L;
        boolean exist = false;
        for (User u : users) {
            if (u.getId() > maxi)
                maxi = u.getId();
            if (Objects.equals(u.getLastName(), lastName) && Objects.equals(u.getFirstName(), firstName))
                exist = true;
        }
        if (exist)
            throw new RepoException("This user already exists");
        user.setId(maxi + 1);
        validatorUser.validate(user);
        repoUsers.save(user);
        notifyObservers();
    }

    /**
     * @param id id type Long is the id of a user we want to delete
     */
    public void deleteUser(Long id) {
        User user = repoUsers.delete(id);
        Collection<Friendship> toBeErased = new ArrayList<>();
        for (Friendship friendship : repoFriendships.findAll())
            if (user.getId() == friendship.getFirstFriendId() || user.getId() == friendship.getSecondFriendId())
                toBeErased.add(friendship);
        for (Friendship friendship : toBeErased)
            repoFriendships.delete(friendship.getId());
        for (User userSearch : repoUsers.findAll())
            userSearch.getFriends().remove(user);
        notifyObservers();
    }

    /**
     * @param u1 first user, I want to see if he is friend with u2
     * @param u2 second user, I want to see if he is friend with u1
     * @return true, if friends, false, else
     */
    public boolean alreadyFriends(User u1, User u2) {
        List<User> l1 = u1.getFriends();
        for (User f : l1) {
            if (f == u2)
                return true;
        }
        return false;
    }


    /**
     * @param id1 id of first user, I want to add friendship between him and second user, identified by id2
     * @param id2 id of second user, I want to add friendship between him and first user, identified by id1
     * @throws RepoException if they were already friends or if same person
     */
    public void addFriendship(Long id1, Long id2, boolean admin) throws RepoException, ValidationException {
        User[] users = getTwoUsersById(id1, id2);
        User u1 = users[0];
        User u2 = users[1];
        if (u1 != u2) {
            if (alreadyFriends(u1, u2))
                if(!admin)
                    throw new RepoException(u1 + " is already a friend of " + u2 + "!");
                else
                {
                    acceptFriendship(u2,u1);
                    return;
                }
            Friendship friendship;
            if(admin)
                friendship = new Friendship(LocalDateTime.now(),u1,u2,"accepted");
            else
                friendship = new Friendship(u1, u2);
            validatorFriendship.validate(friendship);
            Iterable<Friendship> friendships = repoFriendships.findAll();
            Long maxi = 0L;
            for (Friendship f : friendships) {
                if (f.getId() > maxi)
                    maxi = f.getId();
            }
            friendship.setId(maxi + 1);
            repoFriendships.save(friendship);

            u1.getFriends().add(u2);
            u2.getFriends().add(u1);
            repoUsers.update(u1);
            repoUsers.update(u2);

            notifyObservers();
        } else {
            throw new RepoException("Same user, no efect!");
        }
    }

    public void acceptFriendship(User u1, User u2) throws RepoException{
        Iterable<Friendship> friendshipIterable = getAllFriendships();
        for (Friendship f : friendshipIterable) {
            if (f.getFirstFriend() == u2 && f.getSecondFriend() == u1) {
                if (Objects.equals(f.getStatus(), "accepted")) {
                    throw new RepoException("Already friends!");
                } else {
                    f.setStatus("accepted");
                    repoFriendships.update(f);
                    notifyObservers();
                }
            }
        }
    }

    public void refuseFriendship(User u1, User u2) throws RepoException{
        Iterable<Friendship> friendshipIterable = getAllFriendships();
        for (Friendship f : friendshipIterable) {
            if (f.getFirstFriend() == u2 && f.getSecondFriend() == u1) {
                if (Objects.equals(f.getStatus(), "accepted")) {
                    throw new RepoException("Already friends!");
                } else {
                    deleteFriendship(u1.getId(),u2.getId());
                    notifyObservers();
                    break;
                }
            }
        }
    }

    /**
     * @param id1 id of first user
     * @param id2 id of second user
     * @return a vector of two users if both of them exists
     */
    private User[] getTwoUsersById(Long id1, Long id2) {
        User u1 = repoUsers.findOne(id1);
        User u2 = repoUsers.findOne(id2);
        if (u1 == null && u2 == null)
            throw new RepoException("Non existing users!");
        else if (u1 == null) throw new RepoException("First user does not exist!");
        else if (u2 == null) throw new RepoException("Second user does not exist!");
        return new User[]{u1, u2};
    }

    /**
     * @param id1 id of first user, I want to delete friendship between him and second user, identified by id2
     * @param id2 id of second user, I want to delete friendship between him and first user, identified by id1
     *            if not friends at all, exception
     *            if same person, exception
     */
    public void deleteFriendship(Long id1, Long id2) {
        User[] users = getTwoUsersById(id1, id2);
        User u1 = users[0];
        User u2 = users[1];
        if (u1 != u2) {
            if (alreadyFriends(u1, u2)) {
                u1.getFriends().remove(u2);
                u2.getFriends().remove(u1);
                repoUsers.update(u1);
                repoUsers.update(u2);
                for (Friendship f : repoFriendships.findAll()) {
                    Long idf1 = f.getFirstFriendId();
                    Long idf2 = f.getSecondFriendId();
                    if ((Objects.equals(idf1, id1) && Objects.equals(idf2, id2)) ||
                            (Objects.equals(idf1, id2) && Objects.equals(idf2, id1))) {
                        repoFriendships.delete(f.getId());
                        notifyObservers();
                        break;
                    }
                }
            } else throw new RepoException(u1 + " was never a friend of " + u2 + "!");
        } else {
            throw new RepoException("Same user, no efect!");
        }
    }

    /**
     * @return un UndirectedGraph based on users and their friendships
     */
    private UndirectedGraph createGraph() {
        Map<Long, HashSet<Long>> adjMap = new HashMap<>();
        for (User user : getAllUsers()) {
            adjMap.putIfAbsent(user.getId(), new HashSet<>());
            for (Long friendId : user.getFriendsIds()) {
                Long userId = user.getId();
                adjMap.putIfAbsent(userId, new HashSet<>());
                adjMap.putIfAbsent(friendId, new HashSet<>());
                adjMap.get(userId).add(friendId);
                adjMap.get(friendId).add(userId);
            }
        }
        return new UndirectedGraph(adjMap);
    }

    /**
     * @return the most sociable community as an Iterable<Utilizator>
     */
    public Iterable<User> getMostSociableCommunity() {
        UndirectedGraph graph = createGraph();
        Collection<User> mostSociableNetwork = new ArrayList<>();
        Iterable<Long> connectedComponent = graph.getConnectedComponentWithLongestRoad();
        for (Long vertex : connectedComponent) {
            mostSociableNetwork.add(repoUsers.findOne(vertex));
        }
        return mostSociableNetwork;
    }

    /**
     * @return number of communities
     */
    public int numberOfCommunities() {
        UndirectedGraph graph = createGraph();
        return graph.getConnectedComponentsCount();
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }
    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}

