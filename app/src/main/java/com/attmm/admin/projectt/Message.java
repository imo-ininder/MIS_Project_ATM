package com.attmm.admin.projectt;

/**
 * Created by imo on 2016/7/15.
 */
public class Message {
    private String author,message;
    public Message(){}
    public Message(String author,String message){
        this.author = author;
        this.message = message;
    }
    public String getAuthor(){
        return author;
    }
    public String getMessage(){
        return message;
    }
}
