package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.service.NetworkService;

import java.io.IOException;
import java.util.Objects;

public class AllUsersController extends AbstractController {
    ObservableList<UserDTO> modelGrade = FXCollections.observableArrayList();
    @FXML
    TableView<UserDTO>tableViewUsers;
    @FXML
    TableColumn<UserDTO, String>tableColumnId, tableColumnName, tableColumnFriends;
    @FXML
    Button signOutButton, addUserButton, deleteUserButton;
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
    }

    public void setService(NetworkService service, User user){
        super.init(service,user);
        modelGrade.setAll(getAllUsersList());
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

    @Override
    public void update() {
        modelGrade.setAll(getAllUsersList());
    }
}
