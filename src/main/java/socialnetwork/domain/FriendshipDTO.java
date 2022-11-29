package socialnetwork.domain;

public class FriendshipDTO extends Entity<Long>{
    private final String friendFrom;
    private final String friendWith;

    private final String sendByMe;
    private final String receivedByMe;

    public FriendshipDTO(String friendFrom, String friendWith, String s, String r) {
        this.friendFrom = friendFrom;
        this.friendWith = friendWith;
        this.sendByMe=s;
        this.receivedByMe=r;
    }

    public FriendshipDTO(String s, String r){
        this.friendFrom = null;
        this.friendWith = null;
        this.sendByMe=s;
        this.receivedByMe=r;
    }

    public String getFriendFrom() {
        return friendFrom;
    }

    public String getFriendWith() {
        return friendWith;
    }

    public String getSendByMe() {
        return sendByMe;
    }

    public String getReceivedByMe() {
        return receivedByMe;
    }

    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "friendFrom='" + friendFrom + '\'' +
                ", friendWith='" + friendWith + '\'' +
                ", sendByMe=" + sendByMe +
                ", receivedByMe=" + receivedByMe +
                '}';
    }
}
