package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import it.unito.brunasmail.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RootLayoutController {

    @FXML private Label addressLabel;
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        addressLabel.setText(mainApp.getUserMail());
    }
    @FXML
    private void handleNew(){
        mainApp.showSendMailDialog(
                new Mail(
                        mainApp.getUserMail(),
                        "",
                        null,
                        0L,
                        ""
                ),
                "Send New Email");
    }
    public RootLayoutController(){}

}
