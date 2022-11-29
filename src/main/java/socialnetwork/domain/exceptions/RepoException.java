package socialnetwork.domain.exceptions;

public class RepoException extends RuntimeException {
    public RepoException(String message) {
        super(message);
    }

    public String toString() {
        return this.getMessage();
    }
}
