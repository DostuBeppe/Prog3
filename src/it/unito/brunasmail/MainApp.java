package it.unito.brunasmail;

import it.unito.brunasmail.model.Mail;
import it.unito.brunasmail.view.LoginController;
import it.unito.brunasmail.view.MailContainerController;
import it.unito.brunasmail.view.NewMessageController;
import it.unito.brunasmail.view.RootLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class MainApp extends Application {
    private Stage primaryStage;
    private Stage dialogStage;
    private BorderPane rootLayout;

    private ObservableList<Mail> inbox = FXCollections.observableArrayList();
    private ObservableList<Mail> outbox = FXCollections.observableArrayList();

    private String userMail = "";
    private ClientHandler clientHandler;

    public ClientHandler getClientHandler(){
        return clientHandler;
    }

    public MainApp() {
        clientHandler = new ClientHandler(this);
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public ObservableList<Mail> getInbox() {
        return inbox;
    }

    public ObservableList<Mail> getOutbox() { return outbox; }

    public void addInbox(List<Mail> in){
        inbox.addAll(in);
    }
    public void addOutbox(List<Mail> out){
        outbox.addAll(out);
    }
    public void addOutbox(Mail out){ outbox.add(out); }
    public void delete(Mail mail){
        if(mail.isSent()){
            outbox.remove(mail);
        } else {
            inbox.remove(mail);
        }
    }

    public String getUserMail() {
        return userMail;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Brunas Mail");
        initRootLayout();

        showMailContainer();
        showLoginDialog();

        new Thread(this::refresh).start();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showMailContainer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MailContainer.fxml"));
            AnchorPane mailContainer = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(mailContainer);
            MailContainerController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showSendMailDialog(Mail mail, String title) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NewMassage.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NewMessageController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMail(mail);
            controller.setMainApp(this);

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showNewMailPopup(int mail){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Hooray");
        if(mail>1){
            alert.setContentText("You received " + mail + " new messages");
        } else {
            alert.setContentText("You received a new message");
        }
        alert.showAndWait();
    }



    private void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                }
            });
            LoginController loginController = loader.getController();
            loginController.setMainApp(this, dialogStage);


            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void refresh() {
        while (true) {

            try {
                Thread.sleep(5000);
                if(userMail.length()>0) {
                    Platform.runLater(clientHandler::requestInbox);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
