package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.UserDTO;

public class UserController extends AbstractController{

    ObservableList<UserDTO> modelGrade = FXCollections.observableArrayList();
    @FXML
    TableView<UserDTO> tableMyFriends;
    @FXML
    TableColumn<UserDTO,String> friendWith;
    @FXML
    TableColumn<UserDTO,String> friendFrom;
    /*----------------------------------------*/
    @FXML
    TableView<UserDTO> tableAllUsers;
    @FXML
    TableColumn<UserDTO,String> userName;
    /*----------------------------------------*/
    @FXML
    TableView<UserDTO> tableFriendRequests;
    @FXML
    TableColumn<UserDTO,String> sendByMe;
    @FXML
    TableColumn<UserDTO,String> receivedByMe;

    @FXML
    public void initialize(){
        tableMyFriends.setVisible(false);
        tableAllUsers.setVisible(false);
        tableFriendRequests.setVisible(false);

        friendWith.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("idd"));
        friendFrom.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));

        userName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));

        sendByMe.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));
        receivedByMe.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));

        tableMyFriends.setItems(modelGrade);
        tableAllUsers.setItems(modelGrade);
        tableFriendRequests.setItems(modelGrade);
    }
}
