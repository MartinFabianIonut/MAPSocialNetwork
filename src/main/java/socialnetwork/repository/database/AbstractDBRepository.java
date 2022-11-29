package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository0;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository0<ID, E> {


    public AbstractDBRepository(Validator<E> validator) {
        super(validator);
    }

    /**
     * Loads data by extracting all entities of type E (User/Friendship), by calling the abstract method
     * extractEntity(), which is implemented in both UserDBRepository and FriendshipDBRepository
     */
    public void loadData() {
        Iterable<E> allEntity = extractEntity();
        allEntity.forEach(super::save);
    }

    /**
     * Abstract method for getting all entities of type E (User/Friendship), method that is
     * implemented in both UserDBRepository and FriendshipDBRepository
     * @return an iterable collection of entities of type E (User/Friendship)
     */
    protected abstract Iterable<E> extractEntity();

    /**
     * Saves a new entity both in memory and in database
     * @param entity entity must be not null
     * @return saved entity
     */
    @Override
    public E save(E entity){
        E e = super.save(entity);
        if (e == null)
            addToDatabase(entity);
        return e;
    }

    /**
     * Delete an entity by id, applying changes both in memory and in database
     * @param id id must be not null
     * @return deleted entity
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if (e != null)
            removeFromDatabase(id);
        return e;
    }

    /**
     * Updates an entity, applying changes both in memory and in database
     * @param entity entity must not be null
     * @return modified entity
     */
    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if (e != null)
            updateInDatabase(entity);
        return e;
    }

    /**
     * Adds to certain database entity of type E (User/Friendship)
     * @param entity added
     */
    protected void addToDatabase (E entity) { }

    /**
     * Deletes an entity identified by id from a certain database
     * Obs: the type of entity, E (User/Friendship), is deduced in delete method, which calls its superclass
     * @param id for entity to be deleted
     */
    protected void removeFromDatabase (ID id) { }

    /**
     * Modifies an entity, applying changes to a certain database entity of type E (User/Friendship)
     * @param entity modified
     */
    protected void updateInDatabase (E entity) { }

}
