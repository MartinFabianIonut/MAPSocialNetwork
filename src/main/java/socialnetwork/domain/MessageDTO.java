package socialnetwork.domain;

import java.util.List;

public class MessageDTO extends Entity<Long>{
    private String messageSent;
    private String messageReceived;

    public MessageDTO(String messageSent, String messageReceived) {
        this.messageSent = messageSent;
        this.messageReceived = messageReceived;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(String messageReceived) {
        this.messageReceived = messageReceived;
    }

}
