package socialnetwork.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import socialnetwork.domain.User;

public class LogInController extends AbstractController{
    @FXML
    Button signUpButton;
    @FXML
    Button logInButton;

    @FXML
    TextField nameField;

    @FXML
    public void logIn(){
        User user = service.findByName(nameField.getText());
        if(user!=null)
        {
            logInButton.setText("Bravo");
        }
        else {
            logInButton.setText("Rau");
        }
        //logInButton.setText("Bravo");
    }

    public void signUp() {
    }
}
