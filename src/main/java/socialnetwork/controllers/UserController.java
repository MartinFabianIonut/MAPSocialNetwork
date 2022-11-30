package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.FriendshipDTO;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.service.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController extends AbstractController{

    ObservableList<UserDTO> modelGradeUserDTO = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelGradeFriendshipDTO = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelGradeFriendshipDTORequests = FXCollections.observableArrayList();
    @FXML
    TableView<FriendshipDTO> tableMyFriends;
    @FXML
    TableColumn<FriendshipDTO,String> friendWith, friendFrom;
    /*----------------------------------------*/
    @FXML
    TableView<UserDTO> tableAllUsers;
    @FXML
    TableColumn<UserDTO,String> userName;
    /*----------------------------------------*/
    @FXML
    TableView<FriendshipDTO> tableFriendRequests;
    @FXML
    TableColumn<FriendshipDTO,String> sendByMe, receivedByMe;
    /*----------------------------------------*/
    @FXML
    ComboBox<String> comboBoxOptions;
    /*----------------------------------------*/
    @FXML
    Button deleteFriendButton, addFriendButton, acceptFriendshipButton,
            refuseFriendshipButton, signOutButton, deleteAccountButton;
    /*----------------------------------------*/
    @FXML
    Text showSelectedUser, showResult;

    @FXML
    public void initialize(){
        tableMyFriends.setVisible(false);
        tableAllUsers.setVisible(false);
        tableFriendRequests.setVisible(false);

        acceptFriendshipButton.setVisible(false);
        refuseFriendshipButton.setVisible(false);

        friendFrom.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("friendFrom"));
        friendWith.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("friendWith")); //TODO

        userName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));

        sendByMe.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("sendByMe"));
        receivedByMe.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("receivedByMe"));

        tableMyFriends.setItems(modelGradeFriendshipDTO);
        tableAllUsers.setItems(modelGradeUserDTO);
        tableFriendRequests.setItems(modelGradeFriendshipDTORequests);
        comboBoxOptions.getSelectionModel().selectedItemProperty().addListener(
                (x,y,z)->handleFilter()
        );
    }

    private void handleFilter(){
        String selectedItem = comboBoxOptions.getSelectionModel().getSelectedItem();
        switch (selectedItem) {
            case "See my friends" -> {
                tableMyFriends.setVisible(true);
                tableFriendRequests.setVisible(false);
                tableAllUsers.setVisible(false);
                acceptFriendshipButton.setVisible(false);
                refuseFriendshipButton.setVisible(false);
            }
            case "See all users" -> {
                tableMyFriends.setVisible(false);
                tableFriendRequests.setVisible(false);
                tableAllUsers.setVisible(true);
                acceptFriendshipButton.setVisible(false);
                refuseFriendshipButton.setVisible(false);
            }
            case "See friend requests" -> {
                tableMyFriends.setVisible(false);
                tableFriendRequests.setVisible(true);
                tableAllUsers.setVisible(false);
                acceptFriendshipButton.setVisible(true);
                refuseFriendshipButton.setVisible(true);
            }
        }
    }

    private List<FriendshipDTO> getAllFriendshipsForThisUser(){
        Iterable<Friendship> list =  service.getAllFriendships();
        List<FriendshipDTO> friendshipDTOList = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getFirstFriendId(), currentUser.getId())
                        && Objects.equals(x.getStatus(), "accepted")))
                .map(f -> new FriendshipDTO(f.getDate().toString(),
                        f.getSecondFriend().getLastName() + " " + f.getSecondFriend().getFirstName(), null, null)).toList();
        List<FriendshipDTO> friendshipDTOList2 = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getSecondFriendId(), currentUser.getId())
                        && Objects.equals(x.getStatus(), "accepted")))
                .map(f -> new FriendshipDTO(f.getDate().toString(),
                        f.getFirstFriend().getLastName() + " " + f.getFirstFriend().getFirstName(), null, null)).toList();
        List<FriendshipDTO> friendshipDTOListf = new ArrayList<>();
        friendshipDTOListf.addAll(friendshipDTOList);
        friendshipDTOListf.addAll(friendshipDTOList2);
        return friendshipDTOListf;
    }

    private List<FriendshipDTO> getAllFriendshipRequests(){
        Iterable<Friendship> list =  service.getAllFriendships();
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
        while(i1.hasNext() && i2.hasNext()){
            friendshipsRequests.add(new FriendshipDTO(i1.next(),i2.next()));
        }
        while(i1.hasNext()){
            friendshipsRequests.add(new FriendshipDTO(i1.next(),""));
        }
        while(i2.hasNext()){
            friendshipsRequests.add(new FriendshipDTO("",i2.next()));
        }
        return friendshipsRequests;
    }

    public void setService(NetworkService service, User user){
        super.init(service,user);
        modelGradeUserDTO.setAll(getAllUsersList());
        modelGradeFriendshipDTO.setAll(getAllFriendshipsForThisUser());
        modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests());
        comboBoxOptions.getItems().setAll("See my friends","See all users", "See friend requests");
    }

    @FXML
    public void addFriend(){
        if(Objects.equals(showSelectedUser.getText(), "")){
            showResult.setText("You haven't selected any user yet!");
        }
        else{
            User toBeFriendWith = service.findByName(showSelectedUser.getText());
            Long id = toBeFriendWith.getId();
            try{
                service.addFriendship(currentUser.getId(),id);
                showResult.setText("Friendship with "+ showSelectedUser.getText() + " added successfully!");
            }catch (RepoException e){
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void deleteFriend(){
        if(Objects.equals(showSelectedUser.getText(), "")){
            showResult.setText("You haven't selected any user yet!");
        }
        else{
            User toDeleteFriendship = service.findByName(showSelectedUser.getText());
            Long id = toDeleteFriendship.getId();
            try{
                service.deleteFriendship(currentUser.getId(),id);
                showResult.setText("Friendship with "+ showSelectedUser.getText() + " deleted successfully!");
            }catch (RepoException e){
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void showUserTableAllUsers(){
        UserDTO user = tableAllUsers.getSelectionModel().getSelectedItem();
        showSelectedUser.setText(user.getName());
    }

    @FXML
    public void showUserTableFriendRequests(){
        FriendshipDTO user = tableFriendRequests.getSelectionModel().getSelectedItem();
        String longForm = user.getReceivedByMe();
        String[] parts = longForm.split(";",2);
        showSelectedUser.setText(parts[0]);
    }

    @FXML
    public void showUserTableMyFriends(){
        FriendshipDTO user = tableMyFriends.getSelectionModel().getSelectedItem();
        showSelectedUser.setText(user.getFriendWith());
    }

    @FXML
    public void acceptFriendshipAction(){
        try{
            service.acceptFriendship(currentUser,service.findByName(showSelectedUser.getText()));
            showResult.setText("Friendship with "+ showSelectedUser.getText() + " accepted successfully!");
        }catch (RepoException e){
            showResult.setText(e.toString());
        }
    }

    @FXML
    public void refuseFriendshipAction(){
        try{
            service.refuseFriendship(currentUser,service.findByName(showSelectedUser.getText()));
            showResult.setText("Friendship with "+ showSelectedUser.getText() + " accepted successfully!");
        }catch (RepoException e){
            showResult.setText(e.toString());
        }
    }

    @FXML
    public void signOutAction() throws IOException {
        Stage previousStage = (Stage) signOutButton.getScene().getWindow();
        super.signOutAction(previousStage);
    }

    @FXML
    public void deleteAccountAction(){
        try{
            service.deleteUser(currentUser.getId());
            signOutAction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update() {
        modelGradeUserDTO.setAll(getAllUsersList());
        modelGradeFriendshipDTO.setAll(getAllFriendshipsForThisUser());
        modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests());
    }
}
