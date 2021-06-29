package app.forms;

import core.Facades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StaffsHome {


    @FXML
    void onGetInfo(ActionEvent event) {
        Facades.fm.open("Thông tin cá nhân","personal-info");
    }

    @FXML
    void onLogout(ActionEvent event) {
        Facades.fm.closeAll();
        Facades.session.clear();
        Facades.fm.open("Cafeshop","login");
    }
}

