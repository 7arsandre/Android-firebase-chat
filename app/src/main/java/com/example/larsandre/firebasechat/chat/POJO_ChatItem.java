package com.example.larsandre.firebasechat.chat;

/**
 * Created by larsandre on 17.02.17.
 */

public class POJO_ChatItem {
    private String id,name,message,date;

    public POJO_ChatItem(){

    }
    public POJO_ChatItem(String id,String name,String message,String date){
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {return date;}

    public void setId(String id) {this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {return id;}

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}