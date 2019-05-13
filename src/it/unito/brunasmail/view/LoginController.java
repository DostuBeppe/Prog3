package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private AnchorPane loginPane;

    private MainApp mainApp;
    private Stage stage;

    public void setMainApp(MainApp mainApp, Stage stage){
        this.mainApp=mainApp;
        this.stage = stage;
    }

    @FXML
    private void handleLogin(){
        if(usernameField.getText().length()>0 && usernameField != null){
            mainApp.setUserMail(usernameField.getText());
            new Thread(mainApp::requestMail).start();
            stage.close();
        } else if (usernameField == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Error");
            alert.setHeaderText("Error:");
            alert.setContentText("Email not valid!");
            alert.showAndWait();
        }
    }

}
