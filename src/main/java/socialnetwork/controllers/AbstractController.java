package socialnetwork.controllers;

import socialnetwork.service.NetworkService;

public abstract class AbstractController  {
    protected NetworkService service;

    public void init(NetworkService service) {
        this.service = service;
    }
}