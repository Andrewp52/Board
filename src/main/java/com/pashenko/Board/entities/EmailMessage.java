package com.pashenko.Board.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EmailMessage {
    private String from;
    private String to;
    private String subject;
    private String body;
    private List<File> attached;

    private EmailMessage(String from, String to, String subject, String body, List<File> attached) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attached = attached;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public List<File> getAttached() {
        return attached;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
    private String from;
    private String to;
    private String subject;
    private String body;
    private List<File> attached = new ArrayList<>();

        private Builder() {
        }

        public Builder mailFrom(String from){
        this.from = from;
        return this;
    }

    public Builder rcptTo(String to){
        this.to = to;
        return this;
    }

    public Builder subject(String subject){
        this.subject = subject;
        return this;
    }

    public Builder body(String body){
        this.body = body;
        return this;
    }

    public Builder attachedFiles(List<File> attached){
        this.attached.addAll(attached);
        return this;
    }

    public Builder attachFile(File file){
        this.attached.add(file);
        return this;
    }

    public EmailMessage build(){
        return new EmailMessage(this.from, this.to, this.subject, this.body, this.attached);
    }

    private boolean validate(){
        return !((this.from == null || this.to == null) || (this.from.isBlank() || this.to.isBlank()));
    }
}
}
