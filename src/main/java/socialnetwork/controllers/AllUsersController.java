package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.service.NetworkService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AllUsersController extends AbstractController {

    ObservableList<UserDTO> modelGrade = FXCollections.observableArrayList();
    //List<String> modelTema;


    @FXML
    TableView<UserDTO>tableViewUsers;
    @FXML
    TableColumn<UserDTO, String>tableColumnId;
    @FXML
    TableColumn<UserDTO, String>tableColumnName;
    @FXML
    TableColumn<UserDTO, String>tableColumnFriends;


    @FXML
    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("idd"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("name"));
        tableColumnFriends.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("friends"));

        tableViewUsers.setItems(modelGrade);
    }

    public void setService(NetworkService service, User user){
        super.init(service,user);
        modelGrade.setAll(getAllUsersList());
    }

    @Override
    public void update() {
        modelGrade.setAll(getAllUsersList());
    }
}
