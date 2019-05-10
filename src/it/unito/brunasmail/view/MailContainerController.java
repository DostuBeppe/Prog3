package it.unito.brunasmail.view;

import it.unito.brunasmail.MainApp;
import it.unito.brunasmail.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private TableColumn<Mail, LocalDateTime> inDateColumn;
    @FXML
    private TableColumn<Mail, String> outReceiverColumn;
    @FXML
    private TableColumn<Mail, String> outSubjectColumn;
    @FXML
    private TableColumn<Mail, LocalDateTime> outDateColumn;
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


    public MailContainerController() {
    }

    public void setDate(TableColumn<Mail, LocalDateTime> d) {
        d.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        d.setCellFactory(col -> new TableCell<Mail, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {

                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm")));
            }
        });
    }

    @FXML
    private void initialize() {
        inSenderColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
        inSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        setDate(inDateColumn);
        outReceiverColumn.setCellValueFactory(cellData -> cellData.getValue().receiversStringProperty());
        outSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        setDate(outDateColumn);


        showMailDetails(null);
        inTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showMailDetails(newValue)));
        outTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showMailDetails(newValue)));
    }

    @FXML
    private void handleForward() {
        mainApp.showSendMailDialog(
                new Mail(
                        mainApp.getUserMail(),
                        "[FWD] " + selectedMail.getSubject(),
                        "",
                        0L,
                        selectedMail.getMessage() + "\n---------------------\nForwarded from " + selectedMail.getSender()
                ),
                "Forward Email");
    }

    @FXML
    private void handleReply() {
        if (!selectedMail.getSender().equals(mainApp.getUserMail())) {
            mainApp.showSendMailDialog(
                    new Mail(mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getSender(),
                            0L,
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage()
                    ),
                    "Reply Email");
        } else {
            mainApp.showSendMailDialog(
                    new Mail(
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getReceiversString(),
                            0L,
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage()
                    ),
                    "Reply Email");
        }
    }

    @FXML
    private void handleReplyAll() {
        if (!selectedMail.getSender().equals(mainApp.getUserMail())) {
            selectedMail.removeFromReceivers(mainApp.getUserMail());
            mainApp.showSendMailDialog(
                    new Mail(
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getSender() + "; " + selectedMail.getReceiversString(),
                            0,
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage()
                    ),
                    "Reply Email");
        } else {
            mainApp.showSendMailDialog(
                    new Mail(
                            mainApp.getUserMail(),
                            "[RE] " + selectedMail.getSubject(),
                            selectedMail.getReceiversString(),
                            0L,
                            "\n\n-----------------------\n" + selectedMail.getSender() + ":\n\n" + selectedMail.getMessage()
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

    private void showMailDetails(Mail mail) {
        if (mail != null) {
            this.selectedMail = mail;
            subjectLabel.setText(mail.getSubject());
            senderLabel.setText("from: " + mail.getSender());
            dateLabel.setText(mail.getFormattedDate());
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
