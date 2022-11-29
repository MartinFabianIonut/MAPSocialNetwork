package socialnetwork.controllers;

import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.observer.Observer;
import socialnetwork.service.NetworkService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractController implements Observer {
    protected NetworkService service;
    protected User currentUser;

    protected String adminPswd="martin";

    public void init(NetworkService service, User currentUser) {
        this.service = service;
        this.currentUser = currentUser;
        service.addObserver(this);
        update();
    }

    protected List<UserDTO> getAllUsersList(){
        Iterable<User> list =  service.getAllUsers();
        List<UserDTO> users = StreamSupport.stream(list.spliterator(), false)
                .map(u -> new UserDTO(u.getId().toString(), u.getLastName()+ " "+u.getFirstName(), u.getFriendsAsString()))
                .collect(Collectors.toList());
        return users;
    }
}