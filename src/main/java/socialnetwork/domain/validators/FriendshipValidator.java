package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.ValidationException;


public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getFirstFriend().getId() < 1 || entity.getFirstFriend().getId() > 9999 ||
                entity.getSecondFriend().getId() < 1 || entity.getSecondFriend().getId() > 9999)
            throw new ValidationException("Invalid friendship data");
    }
}