package com.example.admin.projectt;

/**
 * Created by imo on 2016/7/13.
 */
public class UserData {
    private String email,password,passwordHint,name,id,gender;
    public UserData(){ }
    public UserData(String email,String password,String passwordHint,String name,String id,String gender){
        this.email = email;
        this.password = password;
        this.passwordHint = passwordHint;
        this.name = name;
        this.id = id;
        this.gender = gender;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getPasswordHint(){
        return passwordHint;
    }
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getGender(){
        return gender;
    }
}
