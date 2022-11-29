package socialnetwork.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id3;

    public ID getId() {
        return id3;
    }

    public void setId(ID id) {
        this.id3 = id;
    }
}