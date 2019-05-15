package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import it.unito.brunasmail.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewMessageController {
    @FXML
    private TextField receiversField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea messageBodyArea;

    private Stage dialogStage;
    private Mail mail;
    private boolean okClicked = false;
    private MainApp mainApp;

    @FXML
    private void initialize(){}

    public void setDialogStage(Stage dialogStage){ this.dialogStage=dialogStage;}

    public void setMail(Mail mail){
        this.mail = mail;

        if(mail!=null){
            receiversField.setText(this.mail.getReceiversString());
            subjectField.setText(this.mail.getSubject());
            messageBodyArea.setText(this.mail.getMessage());
        } else {
            this.mail = new Mail("","",null,0L,"");
            receiversField.setText(this.mail.getReceiversString());
            subjectField.setText(this.mail.getSubject());
            messageBodyArea.setText(this.mail.getMessage());
        }
    }
    public boolean isOkClicked(){
        return okClicked;
    }

    @FXML
    private void handleOk(){
        mail.setReceivers(receiversField.getText());
        mail.setSubject(subjectField.getText());
        mail.setMessage(messageBodyArea.getText());
        if(isInputValid(mail)){
            okClicked = true;
            Thread thread = new Thread(()->MainApp.sendMail(mail, this.mainApp));
            thread.start();
            new Thread(this.mainApp::requestMail).start();
            dialogStage.close();
        }

    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    private boolean isInputValid(Mail mail){
        String errorMessage = "";
        if(receiversField.getText()==null||receiversField.getText().length()==0){
            errorMessage += "Missing Receivers\n";
        }
        if(mail.getReceiversString().length()==0){
            errorMessage += "Wrong Email Format\n";
        }
        if(subjectField.getText()==null||subjectField.getText().length()==0){
            errorMessage += "Missing Subject\n";
        }
        if(receiversField.getText()==null||receiversField.getText().length()==0){
            errorMessage += "Empty Message Body\n";
        }
        if(errorMessage.length()==0){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Errors detected in the following fields:");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }

    }


}
