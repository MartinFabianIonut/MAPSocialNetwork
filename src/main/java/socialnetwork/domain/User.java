package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<User> friends;

    public User(String lastName, String firstName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<>();
    }

    public User(String lastName, String firstName, List<User> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = friends;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Long> getFriendsIds() {
        return friends.stream().map(Entity::getId).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", friends=" + this.getFriendsIds() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriendsIds());
    }
}