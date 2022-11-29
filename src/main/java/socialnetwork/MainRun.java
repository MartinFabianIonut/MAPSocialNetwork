package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.controllers.AuthenticationController;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.database.FriendshipDBRepository;
import socialnetwork.repository.database.UserDBRepository;
import socialnetwork.service.NetworkService;

import java.io.IOException;

public class MainRun extends Application {

    public static Validator<User> validatorUser = new UserValidator();
    public static Validator<Friendship> validatorFriendship = new FriendshipValidator();
    public static String url = "jdbc:postgresql://localhost:5432/socialNetwork";
    public static UserDBRepository userDBRepository = new UserDBRepository(url,"postgres","postgres", validatorUser);
    public static FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url,"postgres","postgres", userDBRepository, validatorFriendship);
    public static NetworkService service = new NetworkService(userDBRepository, friendshipDBRepository, validatorUser, validatorFriendship);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/authentication.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);

        AuthenticationController authenticationController =fxmlLoader.getController();
        authenticationController.init(service);

        stage.setTitle("Hello, this is my Social network app!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}