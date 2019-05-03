package it.unito.brunasmail.client.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.List;

public class Mail {
    private final IntegerProperty id;
    private final StringProperty sender;
    private final StringProperty subject;
    private final List<String> receivers;
    private final ObjectProperty<LocalDateTime> date;
    private final StringProperty message;
    private final Boolean viewed;

    public Mail(Integer id, String sender, String subject, List<String> receivers, String date, String message, Boolean viewed) {
        this.id = new SimpleIntegerProperty(id);
        this.sender = new SimpleStringProperty(sender);
        this.subject = new SimpleStringProperty(subject);
        this.receivers = receivers;
        this.date = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.of(1999, 2, 21,0,0,0,0));
        this.message = new SimpleStringProperty(message);
        this.viewed = viewed;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getSender() {
        return sender.get();
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public Boolean getViewed() {
        return viewed;
    }


}
