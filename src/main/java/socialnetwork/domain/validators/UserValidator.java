package socialnetwork.domain.validators;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.ValidationException;

import java.util.Objects;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String error = "";
        if (entity.getId() < 0)
            error += "Id invalid\n";
        if (Objects.equals(entity.getLastName(), ""))
            error += "Nume invalid\n";
        if (Objects.equals(entity.getFirstName(), ""))
            error += "Prenume invalid\n";
        if (!error.equals(""))
            throw new ValidationException(error);
    }
}
