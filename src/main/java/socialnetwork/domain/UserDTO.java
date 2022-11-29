package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDTO extends Entity<Long> {
    private String idd;
    private String name;
    private List<Long>friends;

    public UserDTO(String id, String name, List<Long> friends) {
        this.idd = id;
        this.name = name;
        this.friends = friends;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "idd='" + idd + '\'' +
                ", name='" + name + '\'' +
                ", friends=" + friends +
                '}';
    }
}