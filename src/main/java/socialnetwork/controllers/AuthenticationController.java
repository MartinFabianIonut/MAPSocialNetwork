package socialnetwork.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthenticationController extends AbstractController {
    @FXML
    Button userButton;

    @FXML
    Button adminButton;

    @FXML
    public void adminPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/allUsersView.fxml"));
        AnchorPane root = loader.load();

        AllUsersController controller = loader.getController();
        controller.setService(service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Admin window");
        stage.show();
    }

    @FXML
    public void userPage(){

    }

}
