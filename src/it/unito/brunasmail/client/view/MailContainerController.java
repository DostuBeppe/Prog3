package it.unito.brunasmail.client.view;

import it.unito.brunasmail.client.MainApp;
import it.unito.brunasmail.client.model.Mail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MailContainerController {
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<Mail> inTable;
    @FXML
    private TableView<Mail> outTable;
    @FXML
    private TableColumn<Mail, String> inSenderColumn;
    @FXML
    private TableColumn<Mail, String> inSubjectColumn;
    @FXML
    private TableColumn<Mail, String> inDateColumn;
    @FXML
    private TableColumn<Mail, String> outReceiverColumn;
    @FXML
    private TableColumn<Mail, String> outSubjectColumn;
    @FXML
    private TableColumn<Mail, String> outDateColumn;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label senderLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label receiversLabel;
    @FXML
    private TextArea bodyTextArea;
    @FXML
    private Button buttonReply;
    @FXML
    private Button buttonReplyAll;
    @FXML
    private Button buttonForward;
    @FXML
    private Button buttonDelete;

    private MainApp mainApp;

    private Mail selectedMail;


    public MailContainerController() { }

    @FXML
    private void initialize(){
        inSenderColumn.setCellValueFactory(cellData->cellData.getValue().senderProperty());
        inSubjectColumn.setCellValueFactory(cellData->cellData.getValue().subjectProperty());
        //inDateColumn.setCellValueFactory(cellData->cellData.getValue().getDate());
        outReceiverColumn.setCellValueFactory(cellData->cellData.getValue().receiversStringProperty());
        outSubjectColumn.setCellValueFactory(cellData->cellData.getValue().subjectProperty());
        //inDateColumn.setCellValueFactory(cellData->cellData.getValue().getDate());

        showMailDetails(null);
        inTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showMailDetails(newValue)));
        outTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showMailDetails(newValue)));
    }

    @FXML
    private void handleForward(){
        mainApp.showSendMailDialog(
                new Mail(-1,
                        mainApp.getUserMail(),
                        "[FWD] " + selectedMail.getSubject(),
                        "",
                        "",
                        selectedMail.getMessage() + "\n---------------------\nForwarded from " + selectedMail.getSender(),
                        false
                ),
                "Forward Email");
    }

    @FXML
    private void handleReply(){
        if(!selectedMail.getSender().equals(mainApp.getUserMail())){
            mainApp.showSendMailDialog(
                    new Mail(-1,
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getSender(),
                            "",
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage(),
                            false
                    ),
                    "Reply Email");
        } else {
            mainApp.showSendMailDialog(
                    new Mail(-1,
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getReceiversString(),
                            "",
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage(),
                            false
                    ),
                    "Reply Email");
        }
    }
    @FXML
    private void handleReplyAll(){
        if(!selectedMail.getSender().equals(mainApp.getUserMail())){
            selectedMail.removeFromReceivers(mainApp.getUserMail());
            mainApp.showSendMailDialog(
                    new Mail(-1,
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getSender() + "; " + selectedMail.getReceivers(),
                            "",
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage(),
                            false
                    ),
                    "Reply Email");
        } else {
            mainApp.showSendMailDialog(
                    new Mail(-1,
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getReceiversString(),
                            "",
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage(),
                            false
                    ),
                    "Reply Email");
        }

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        inTable.setItems(mainApp.getInbox());
        outTable.setItems(mainApp.getOutbox());
    }

    private void showMailDetails(Mail mail){
        if(mail!=null) {
            this.selectedMail = mail;
            subjectLabel.setText(mail.getSubject());
            senderLabel.setText("from: " + mail.getSender());
            dateLabel.setText("6/9/1969");
            receiversLabel.setText("to: " + mail.getReceiversString());
            bodyTextArea.setText(mail.getMessage());
            buttonDelete.setDisable(false);
            buttonReply.setDisable(false);
            buttonReplyAll.setDisable(false);
            buttonForward.setDisable(false);
        } else {
            subjectLabel.setText("");
            senderLabel.setText("");
            dateLabel.setText("");
            receiversLabel.setText("");
            bodyTextArea.setText("Seleziona Un Messaggio");
        }
    }




}
