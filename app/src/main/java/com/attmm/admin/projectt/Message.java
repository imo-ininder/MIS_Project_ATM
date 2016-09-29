package com.attmm.admin.projectt;

/**
 * Created by imo on 2016/7/15.
 */
public class Message {
    private String author,message,action;
    public Message(){}
    public Message(String author,String message,String action){
        this.author = author;
        this.message = message;
        this.action = action;
    }
    public String getAuthor(){
        return author;
    }
    public String getMessage(){
        return message;
    }
    public String getAction(){
        return action;
    }
}
