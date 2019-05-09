package it.unito.brunasmail.client.view;

import it.unito.brunasmail.client.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.util.ArrayList;

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
            this.mail = new Mail(-1,"","",null,"","",false);
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
        if(isInputValid()){
            mail.setReceivers(receiversField.getText());
            mail.setSubject(subjectField.getText());
            mail.setMessage(messageBodyArea.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    private boolean isInputValid(){
        String errorMessage = "";
        if(receiversField.getText()==null||receiversField.getText().length()==0){
            errorMessage += "Missing Receivers\n";
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