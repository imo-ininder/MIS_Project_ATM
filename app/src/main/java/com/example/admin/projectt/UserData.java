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

    public void setEmail(String email){this.email=email;}
    public void setPassword(String password){this.password=password;}
    public void setPasswordHint(String passwordHint){this.passwordHint=passwordHint;}
    public void setId(String id){this.id=id;}
    public void setName(String name){this.name=name;}

}
