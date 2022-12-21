package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
import java.util.stream.StreamSupport;

public class AllUsersController extends AbstractController {
    ObservableList<UserDTO> modelGrade = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelGradeFriendshipDTORequests = FXCollections.observableArrayList();
    @FXML
    TableView<UserDTO>tableViewUsers;
    @FXML
    TableColumn<UserDTO, String>tableColumnId, tableColumnName, tableColumnFriends;

    @FXML
    TableView<FriendshipDTO> tableFriendRequests;
    @FXML
    TableColumn<FriendshipDTO,String> sendByMe, receivedByMe;

    @FXML
    ComboBox<String> userComboBox;

    @FXML
    Button signOutButton, addUserButton, deleteUserButton,deleteFriendButton,addFriendButton;
    @FXML
    Text controlPanel;
    @FXML
    TextField lastNameField, firstNameField;

    @FXML
    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idd"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnFriends.setCellValueFactory(new PropertyValueFactory<>("friends"));
        tableViewUsers.setItems(modelGrade);

        sendByMe.setCellValueFactory(new PropertyValueFactory<>("sendByMe"));
        receivedByMe.setCellValueFactory(new PropertyValueFactory<>("receivedByMe"));
        tableFriendRequests.setItems(modelGradeFriendshipDTORequests);

        userComboBox.getSelectionModel().selectedItemProperty().addListener(
                (x,y,z)->handleFilter()
        );
    }

    private void handleFilter(){
        String selectedItem = userComboBox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null)
        {
            User selectedUser = service.findByName(selectedItem);
            modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests(selectedUser));
        }
    }

    public void setService(NetworkService service, User user){
        super.init(service,user);
        modelGrade.setAll(getAllUsersList());
        for(UserDTO u :getAllUsersList())
        {
            String name = u.getName();
            userComboBox.getItems().add(name);
        }
    }

    @FXML
    public void signOutAction() throws IOException {
        Stage previousStage = (Stage) signOutButton.getScene().getWindow();
        super.signOutAction(previousStage);
    }

    @FXML
    public void addUserAction(){
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        if(lastName.isEmpty() || firstName.isEmpty()) {
            if (firstName.isEmpty() && lastName.isEmpty())
                controlPanel.setText("Last and first name field is empty!");
            else{
                if (lastName.isEmpty())
                    controlPanel.setText("Last name field is empty!");
                if (firstName.isEmpty())
                    controlPanel.setText("First name field is empty!");
            }
        }
        else {
            try {
                service.addUser(lastName,firstName);
                controlPanel.setText("User "+lastName+ " "+firstName+" added successfully!");
            }catch (RepoException e){
                controlPanel.setText(e.toString());
            }
        }
    }

    @FXML
    public void deleteUserAction(){
        UserDTO userDTO = tableViewUsers.getSelectionModel().getSelectedItem();
        if(userDTO==null){
            controlPanel.setText("You haven't selected any user yet!");
        }
        else{
            String userToBeDeletedString = userDTO.getName();
            User userToBeDeleted = service.findByName(userToBeDeletedString);
            Long id = userToBeDeleted.getId();
            try{
                service.deleteUser(id);
                controlPanel.setText("User "+ userToBeDeletedString + " deleted successfully!");
            }catch (RepoException e){
                controlPanel.setText(e.toString());
            }
        }
    }

    @FXML
    public void addFriend(){
        if(userComboBox.getSelectionModel().isEmpty()){
            controlPanel.setText("You haven't selected any user yet!");
        }
        else{
            UserDTO selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
            User toBeFriendWith = service.findByName(userComboBox.getSelectionModel().getSelectedItem());
            Long id = toBeFriendWith.getId();
            try{
                service.addFriendship(service.findByName(selectedUser.getName()).getId(),id,true);
                controlPanel.setText("Friendship with "+ userComboBox.getSelectionModel().getSelectedItem() + " added successfully!");
            }catch (RepoException e){
                controlPanel.setText(e.toString());
            }catch (Exception e){
                controlPanel.setText("You haven't selected a user to deal with!");
            }
        }
    }

    @FXML
    public void deleteFriend(){
        if(userComboBox.getSelectionModel().isEmpty()){
            controlPanel.setText("You haven't selected any user yet!");
        }
        else{
            UserDTO selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
            User toDeleteFriendship = service.findByName(userComboBox.getSelectionModel().getSelectedItem());
            Long id = toDeleteFriendship.getId();
            try{
                service.deleteFriendship(service.findByName(selectedUser.getName()).getId(),id);
                controlPanel.setText("Friendship with "+ userComboBox.getSelectionModel().getSelectedItem() + " deleted successfully!");
            }catch (RepoException e){
                controlPanel.setText(e.toString());
            }catch (Exception e){
                controlPanel.setText("You haven't selected a user to deal with!");
            }
        }
    }

    @Override
    public void update() {
        modelGrade.setAll(getAllUsersList());
        if(!userComboBox.getSelectionModel().isEmpty())
        {
            String selectedItem = userComboBox.getSelectionModel().getSelectedItem();
            User selectedUser = service.findByName(selectedItem);
            modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests(selectedUser));
        }
    }
}
