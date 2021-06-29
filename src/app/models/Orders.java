package app.models;

import core.Models;

import java.util.HashMap;

public class Orders extends Models {
    public Orders(String id){
        this.id = id;
    }

    public Orders(HashMap<String,String> data){
        this.data = data;
    }
}
