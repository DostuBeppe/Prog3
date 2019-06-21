package it.unito.brunasmail;

import it.unito.brunasmail.model.Mail;
import javafx.application.Platform;
import sun.applet.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler {
    private MainApp mainApp;


    private static final String host = "192.168.137.1";

    public ClientHandler(MainApp mainApp){
        this.mainApp = mainApp;
    }

    public String getMaxTimestamp(List<Mail> inbox){
        long maxTimestamp = 0;
        for(Mail m: inbox){
            if(m.getMillis()>maxTimestamp){
                maxTimestamp = m.getMillis();
            }
        }
        return "" + maxTimestamp;
    }

    public void requestInbox() {
        try {
            try (Socket s = new Socket(host, 8189)) {
                System.out.println("Socket opened");
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                System.out.println("Receiving data from server!");
                out.writeObject("inbox");
                out.writeObject(mainApp.getUserMail());
                out.writeObject(getMaxTimestamp(mainApp.getInbox()));
                List<Mail> resIn = (List<Mail>) in.readObject();
                in.close();
                out.close();
                if (resIn != null) {
                    if (resIn.size() > 0) {
                        mainApp.addInbox(resIn);
                        mainApp.showNewMailPopup(resIn.size());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean requestAll(){
        try {
            try (Socket s = new Socket(host, 8189)) {
                System.out.println("Socket opened");
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                out.writeObject("all");
                out.writeObject(mainApp.getUserMail());
                List<Mail> resIn = (List<Mail>) in.readObject();
                List<Mail> resOut = (List<Mail>) in.readObject();
                in.close();
                out.close();
                if (resIn != null&&resOut!=null) {
                    if (resIn.size() > 0) {
                        mainApp.addInbox(resIn);
                        mainApp.addOutbox(resOut);
                    }
                } else {
                    return false;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void sendMail(Mail mail, MainApp mainApp) {
        mainApp.setMailSent(false);
        try (Socket s = new Socket(host, 8189)) {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            out.writeObject("send");
            out.writeObject(mail);
            Mail m = (Mail)in.readObject();
            in.close();
            out.close();
            if (m!=null) {
                mainApp.setMailSent(true);
                Platform.runLater(()->mainApp.addOutbox(m));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void deleteMail(Mail mail, MainApp mainApp) {
        try (Socket s = new Socket(host, 8189)) {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject("delete");
            out.writeObject(mainApp.getUserMail());
            out.writeObject(mail);
            Platform.runLater(()->mainApp.delete(mail));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
