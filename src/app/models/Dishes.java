package app.models;

import core.Models;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;

public class Dishes extends Models {
    private int numbers = 1;
    public Dishes(String id){
        this.id = id;
    }

    public Dishes(HashMap<String,String> data){
        this.data = data;
    }

    public ImageView getImage(){
        String path ="file:///" + System.getProperty("user.dir")+"/img/"+get("image");
        return new ImageView(new Image(path));
    }

    public int getNumbers(){
        return numbers;
    }

    public int plusNumbers(){
        return ++numbers;
    }

    public int substractNumbers(){
        return --numbers;
    }

    public void resetNumbers(){
        numbers = 1;
    }
}
