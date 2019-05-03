package it.unito.brunasmail.client;

import it.unito.brunasmail.client.model.Mail;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Mail> inbox = FXCollections.observableArrayList();
    public MainApp(){
        ArrayList<String> dest = new ArrayList<>();
        dest.add("bruno@b.it");
        dest.add("bru@b.it");
        inbox.add(new Mail(0,"bruno@bruni.it","Importante",dest,null,"Ciao",false));
        inbox.add(new Mail(0,"bruno@bruni.it","Importantissima",dest,null,"Ciaoooooo",false));
    }

    public ObservableList<Mail> getInbox(){
        return inbox;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
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
        } catch (IOException e) {
            e.printStackTrace();
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
