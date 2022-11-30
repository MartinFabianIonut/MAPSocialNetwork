package socialnetwork.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;

import java.io.IOException;

public class LogInController extends AbstractController{
    @FXML
    Button signUpButton, logInButton, signOutButton;
    @FXML
    TextField nameField;

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
    }

    @FXML
    public void signOutAction() throws IOException {
        Stage previousStage = (Stage) signOutButton.getScene().getWindow();
        super.signOutAction(previousStage);
    }

    @Override
    public void update() {}
}
