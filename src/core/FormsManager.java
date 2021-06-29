package core;

import javafx.scene.control.Alert;
import java.util.HashMap;

public class FormsManager {
    HashMap<String,View> views = new HashMap<>();

    public void showAlert(String title,String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);

        alert.show();
    }

    public void showAlert(String title,String content,String typeAlert){
        Alert alert;
        switch (typeAlert){
            case "info":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            default:
                alert = new Alert(Alert.AlertType.WARNING);
                break;
        }
        alert.setTitle(title);
        alert.setContentText(content);

        alert.show();
    }

    public FormsManager open(String title,String nameView){
        views.put(nameView,new View(title,nameView));
        return this;
    }

    public View get(String nameView){
        return views.get(nameView);
    }


    public FormsManager closeAll(){
        views.forEach((name,view)->{
            view.close();
        });

        views.clear();

        return this;
    }
}
