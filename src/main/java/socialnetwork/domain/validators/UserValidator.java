package socialnetwork.domain.validators;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.ValidationException;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String error = "";
        if (entity.getId() < 0)
            error += "Id is invalid!\n";
        if (Objects.equals(entity.getLastName(), "")
                || entity.getLastName().startsWith(" ")
                || entity.getLastName().endsWith(" "))
            error += "Last name is invalid!\n";
        if (Objects.equals(entity.getFirstName(), "")
                || entity.getFirstName().startsWith(" ")
                || entity.getFirstName().endsWith(" "))
            error += "First name is invalid!\n";
        if (!error.equals(""))
            throw new ValidationException(error);
    }
}
