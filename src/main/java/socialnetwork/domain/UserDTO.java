package socialnetwork.domain;

import java.util.List;

public class UserDTO extends Entity<Long> {
    private String idd;
    private String name;
    private List<String>friends;

    public UserDTO(String id, String name, List<String> friends) {
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

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return name;
    }
}