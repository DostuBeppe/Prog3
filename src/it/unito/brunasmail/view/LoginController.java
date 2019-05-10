package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class LoginController {

    @FXML
    private TextField usernameField;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp){
        this.mainApp=mainApp;

    }

    @FXML
    private void handleLogin(){
        if(usernameField.getText().length()>0){
            mainApp.setUserMail(usernameField.getText());
            new Thread(mainApp::requestMail).start();

        } /*else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner();
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Errors detected in the following fields:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }*/
    }

}
