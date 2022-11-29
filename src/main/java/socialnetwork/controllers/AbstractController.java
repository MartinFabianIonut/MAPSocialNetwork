package socialnetwork.controllers;

import socialnetwork.domain.User;
import socialnetwork.service.NetworkService;

public abstract class AbstractController  {
    protected NetworkService service;
    //protected User loggedUser;

    public void init(NetworkService service) {
        this.service = service;
        //service.addObserver(this);
        //this.loggedUser = loggedUser;
        //update();
    }
}