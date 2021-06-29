package app.models;

import core.Models;

import java.util.HashMap;

public class User extends Models {
    public User(String id){
        this.id = id;
    }
    public User(HashMap<String,String> data){
        this.data = data;
    }
}
