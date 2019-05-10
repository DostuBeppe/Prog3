package it.unito.brunasmail.client;

import it.unito.brunasmail.client.model.Mail;
import it.unito.brunasmail.client.view.MailContainerController;
import it.unito.brunasmail.client.view.NewMessageController;
import it.unito.brunasmail.client.view.RootLayoutController;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Mail> inbox = FXCollections.observableArrayList();
    private ObservableList<Mail> outbox = FXCollections.observableArrayList();

    private String userMail = "stefanococomazzi@brunasmail.com";

    public MainApp(){
        inbox.add(new Mail(0,"bruno@bruni.it","Importante","dest1@brunasmail.it; dost111@brunasmail.it",179250540110L,"Ciao",false));
        inbox.add(new Mail(0,"bruno@bruni.it","Importantissima","singolo@mail.it",147925042110L,"Ciaoooooo",false));
        inbox.add(new Mail(0,"bruno@bruni.it","Importantissima","stefanococomazzi@brunasmail.com; ahaia.io; hgihs; ahishisahsaihsai; blablabla.it",247925054010L,"Ciaoooooo",false));
        outbox.add(new Mail(0,"brno@bruni.it","Importantiima","viojisja@jsjaks.i;;;",147925540210L,"Ciaoooiiiiiiiooo",false));
        outbox.add(new Mail(0,"bno@bruni.it","Importassima","hsidshduish@ui.it; jsiasj; ais@gj.io",147935540110L,"YEET",false));
    }

    public ObservableList<Mail> getInbox(){
        return inbox;
    }
    public ObservableList<Mail> getOutbox(){ return outbox; }
    public String getUserMail() { return userMail; }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Brunas Mail");
        initRootLayout();
        showMailContainer();
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

    public boolean showSendMailDialog(Mail mail, String title){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NewMassage.fxml"));
            AnchorPane page = (AnchorPane)loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NewMessageController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMail(mail);

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
