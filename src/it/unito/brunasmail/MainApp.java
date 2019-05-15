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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp extends Application {
    private Stage primaryStage;
    private Stage dialogStage;
    private BorderPane rootLayout;

    private ObservableList<Mail> inbox = FXCollections.observableArrayList();
    private ObservableList<Mail> outbox = FXCollections.observableArrayList();

    private String userMail;

    public MainApp() {
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public ObservableList<Mail> getInbox() {
        return inbox;
    }

    public ObservableList<Mail> getOutbox() {
        return outbox;
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
        new Thread(this::connectToServer).start();
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

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void connectToServer() {

        try {
            Socket s = new Socket("localhost", 8189);

            System.out.println("Ho aperto il socket verso il server");

            try {
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                OutputStream outStream = s.getOutputStream();
                PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
                Scanner stin = new Scanner(System.in);

                System.out.println("Sto per ricevere dati dal socket server!");

                String line = in.nextLine(); // attenzione: se il server non scrive nulla questo resta in attesa...
                System.out.println(line);

                boolean done = false;
                while (!done) /* && in.hasNextLine()) */ {

                    String lineout = stin.nextLine();
                    out.println(lineout);

                    line = in.nextLine();
                    System.out.println(line);
                    if (lineout.equals("BYE"))
                        done = true;
                }
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean requestMail() {
        boolean request = false;
        try {
            Socket s = new Socket("localhost", 8189);

            System.out.println("Socket opened");

            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                System.out.println("Receiving data from server!");
                out.writeObject("receive");
                out.writeObject(userMail);
                List<Mail> resIn = (List<Mail>) in.readObject();
                List<Mail> resOut = (List<Mail>) in.readObject();
                in.close();
                out.close();
                if (resIn!=null&&resOut!=null){
                    inbox.addAll(resIn);
                    outbox.addAll(resOut);
                    request = true;
                }
                return request;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void showLoginDialog() {
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

    public static void sendMail(Mail mail){
        try {
            Socket s = new Socket("localhost", 8189);

            System.out.println("Socket opened");

            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                //ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                System.out.println("Receiving data from server!");
                out.writeObject("send");
                out.writeObject(mail);
                System.out.println(mail.getSender());
                //String res = (String) in.readObject();
                //in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
