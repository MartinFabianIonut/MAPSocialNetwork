package socialnetwork.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.RepoException;

import java.io.IOException;

public class LogInController extends AbstractController{
    public javafx.scene.text.Text messageText;
    @FXML
    Text lastNameText,firstNameText;
    @FXML
    Button signUpButton, logInButton, signOutButton, createButton;
    @FXML
    TextField nameField, lastNameField, firstNameField;

    @FXML
    AnchorPane anchorPane;

    @FXML
    public void initialize(){
        anchorPane.setVisible(false);
    }
    @FXML
    public void logIn() throws IOException {
        User user = service.findByName(nameField.getText());
        if(user!=null)
        {
            this.currentUser = user;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/userView.fxml"));
            AnchorPane root = loader.load();

            UserController controller = loader.getController();
            controller.setService(service,user);

            Stage previousStage = (Stage) logInButton.getScene().getWindow();
            previousStage.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Wow window");
            stage.show();
        }
        else {
            logInButton.setText("No such");
        }
    }

    public void signUp() {
        anchorPane.setVisible(true);
    }

    @FXML
    public void signOutAction() throws IOException {
        Stage previousStage = (Stage) signOutButton.getScene().getWindow();
        super.signOutAction(previousStage);
    }

    @Override
    public void update() {}

    public void createAccount() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        if(lastName.isEmpty() || firstName.isEmpty()) {
            if (firstName.isEmpty() && lastName.isEmpty())
                messageText.setText("Last and first name field is empty!");
            else{
                if (lastName.isEmpty())
                    messageText.setText("Last name field is empty!");
                if (firstName.isEmpty())
                    messageText.setText("First name field is empty!");
            }
        }
        else {
            try {
                service.addUser(lastName,firstName);
                messageText.setText("User "+lastName+ " "+firstName+" added successfully! Now, you can log in into your account!");
            }catch (RepoException e){
                messageText.setText(e.toString());
            }
        }
    }
}
