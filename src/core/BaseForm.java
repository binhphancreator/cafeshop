package core;

import javafx.application.Application;
import javafx.stage.Stage;

public class BaseForm extends Application {
    @Override
    public void start(Stage stage) {
        if(Facades.session.logined()) {
            String nameView = "admin-home";

            if(Facades.session.get("role_id").equals("1")) nameView = "staffs-home";

            Facades.fm.open("Cafeshop", nameView);
            Facades.fm.get(nameView).addComponents("seller");
        }
        else Facades.fm.open("Cafeshop","login");
    }
}
