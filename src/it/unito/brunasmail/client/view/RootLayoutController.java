package it.unito.brunasmail.client.view;

import it.unito.brunasmail.client.MainApp;
import it.unito.brunasmail.client.model.Mail;
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
                new Mail(-1,
                        mainApp.getUserMail(),
                        "",
                        null,
                        0L,
                        "",
                        false
                ),
                "Send New Email");
    }
    public RootLayoutController(){}

}
