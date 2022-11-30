package socialnetwork.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.User;

import java.io.IOException;
import java.util.Objects;

public class AuthenticationController extends AbstractController {
    @FXML
    Button userButton, adminButton, submitAdminAccess;
    @FXML
    Text passwordAdminAccess;
    @FXML
    PasswordField passwordAdminField;

    @FXML
    public void initialize(){
        submitAdminAccess.setVisible(false);
        passwordAdminField.setVisible(false);
    }

    @FXML
    public void giveAdminAccess() throws IOException {
        if(Objects.equals(passwordAdminField.getText(), adminPswd)){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/allUsersView.fxml"));
            AnchorPane root = loader.load();

            AllUsersController controller = loader.getController();
            User admin = new User("Admin","Admin");
            admin.setId(0L);
            controller.setService(service,admin);

            Stage previousStage = (Stage) adminButton.getScene().getWindow();
            previousStage.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Admin window");
            stage.show();
        }
        else {
            passwordAdminAccess.setText("Access denied!");

            //lastNameField Last name: deleteUserButton
        }
    }
    @FXML
    public void adminPage(){
        passwordAdminAccess.setText("Provide the password for administrator acces:");
        submitAdminAccess.setVisible(true);
        passwordAdminField.setVisible(true);
    }

    @FXML
    public void userPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/logInView.fxml"));
        AnchorPane root = loader.load();

        LogInController controller = loader.getController();
        controller.init(service,null);

        Stage previousStage = (Stage) userButton.getScene().getWindow();
        previousStage.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1200, 800));
        stage.setTitle("User window");
        stage.show();
    }

    @Override
    public void update() {}
}
