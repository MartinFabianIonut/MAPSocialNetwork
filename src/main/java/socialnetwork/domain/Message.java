package socialnetwork.domain;

import java.util.Objects;

public class Message extends Entity<Long>{
    private final String message;
    private final Integer from, to;

    public String getMessage() {
        return message;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Message(String message, Integer from, Integer to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return message ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message that)) return false;
        return getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getFrom(), getTo());
    }
}
