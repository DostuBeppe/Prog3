package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import javafx.application.Platform;
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
            mainApp.setUserMail(usernameField.getText()+"@brunasmail.it");
            boolean successfulLogin = mainApp.getClientHandler().requestAll();
            if (successfulLogin){
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("Error");
                alert.setHeaderText("Error:");
                alert.setContentText("Cannot login, server offline, try again later");
                alert.showAndWait();
                Platform.exit();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Error");
            alert.setHeaderText("Error:");
            alert.setContentText("Email not valid!");
            alert.showAndWait();
        }
    }

    @FXML
    public void closeStage(){
        stage.close();
    }

}
