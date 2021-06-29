package app.forms;

import core.Facades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminHome {
    @FXML
    void onLogOut(ActionEvent event) {
        Facades.fm.closeAll();
        Facades.session.clear();
        Facades.fm.open("Cafeshop","login");
    }

    @FXML
    void onOpenRevenueStatistics(ActionEvent event) {
        Facades.fm.get("admin-home").replaceMainPane("revenue-statistics");
    }

    @FXML
    void onOpenSeller(ActionEvent event) {
        Facades.fm.get("admin-home").replaceMainPane("seller");
    }

    @FXML
    void onOpenDishManager(ActionEvent event) {
        Facades.fm.get("admin-home").replaceMainPane("dishes-manager");
    }

    @FXML
    void onShowPersonalInfo(ActionEvent event) {
        Facades.fm.open("Thông tin cá nhân","personal-info");
    }

    @FXML
    void onOpenStaffManager(ActionEvent event) {
        Facades.fm.get("admin-home").replaceMainPane("staffs-manager");
    }
}
