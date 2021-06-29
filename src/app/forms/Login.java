package app.forms;

import core.Facades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    void login(ActionEvent event) {
        Facades.session.loginWith(emailTextField.getText(),passwordTextField.getText());
        if(Facades.session.logined()) {
            Facades.fm.closeAll();
            String nameView = "admin-home";
            if(Facades.session.get("role_id").equals("1")) nameView = "staffs-home";
            Facades.fm.open("Cafeshop", nameView);
            Facades.fm.get(nameView).addComponents("seller");
        }
        else if(Facades.session.logined()) Facades.fm.showAlert("Thông báo","Sai tài khoản hoặc mật khẩu");
    }

}
