package socialnetwork.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.FriendshipDTO;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.observer.Observer;
import socialnetwork.service.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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

    protected List<FriendshipDTO> getAllFriendshipRequests(User currentUser) {
        Iterable<Friendship> list = service.getAllFriendships();
        List<String> sendByMe = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getFirstFriendId(), currentUser.getId())))
                .map(f -> f.getSecondFriend().getLastName() + " " + f.getSecondFriend().getFirstName() + "; " +
                        "in: " + f.getDate().toString() + "; status = " + f.getStatus()).toList();
        List<String> receivedByMe = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getSecondFriendId(), currentUser.getId())))
                .map(f -> f.getFirstFriend().getLastName() + " " + f.getFirstFriend().getFirstName() + "; " +
                        "in: " + f.getDate().toString() + "; status = " + f.getStatus()).toList();
        Iterator<String> i1 = sendByMe.iterator();
        Iterator<String> i2 = receivedByMe.iterator();
        List<FriendshipDTO> friendshipsRequests = new ArrayList<>();
        while (i1.hasNext() && i2.hasNext()) {
            friendshipsRequests.add(new FriendshipDTO(i1.next(), i2.next()));
        }
        while (i1.hasNext()) {
            friendshipsRequests.add(new FriendshipDTO(i1.next(), ""));
        }
        while (i2.hasNext()) {
            friendshipsRequests.add(new FriendshipDTO("", i2.next()));
        }
        return friendshipsRequests;
    }


    @FXML
    public void signOutAction(Stage previousStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/authentication.fxml"));
        AnchorPane root = loader.load();

        AuthenticationController authenticationController = loader.getController();
        authenticationController.init(service,null);

        previousStage.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("Hello, this is my Social network app!");
        stage.show();
    }

}