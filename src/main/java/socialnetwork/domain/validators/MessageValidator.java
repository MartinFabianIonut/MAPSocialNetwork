package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        String error = "";
        if(entity.getMessage().isEmpty())
            error+="Message is empty!\n";
        if (!error.equals(""))
            throw new ValidationException(error);
    }
}
