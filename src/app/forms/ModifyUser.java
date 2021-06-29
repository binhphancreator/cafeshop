package app.forms;

import app.models.User;
import core.Facades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ModifyUser {

    private User user;
    private TableView<User> table;
    private boolean emailExist = false;

    @FXML
    private TextField passwordText;

    @FXML
    private TextField emailText;

    @FXML
    private TextField fullnameText;

    @FXML
    private TextField addressText;

    @FXML
    private DatePicker birthText;

    @FXML
    private TextField phoneText;

    @FXML
    void onUpdate(ActionEvent event) {
        if(emailText.getText().isEmpty() || passwordText.getText().isEmpty()){
            Facades.fm.showAlert("Có lỗi xảy ra","Email và mật khẩu không được để trống");
            return;
        }

        emailExist = false;
        table.getItems().forEach(user->{
            if(user.get("email").equals(emailText.getText()) && !user.get("id").equals(this.user.get("id"))) emailExist = true;
        });

        if(emailExist){
            Facades.fm.showAlert("Có lỗi xảy ra","Email đã được đăng ký bởi người dùng khác");
            return;
        }

        user.set("email",emailText.getText())
                .set("password",passwordText.getText())
                .set("fullname",fullnameText.getText())
                .set("address",addressText.getText())
                .set("phone",phoneText.getText());

        if(birthText.getValue()!=null) user.set("birth",birthText.getValue().format(DateTimeFormatter.ISO_DATE));

        user.update();
        table.refresh();
        Facades.fm.get("modify-user").close();
    }

    @FXML
    public void initialize(){
        user = (User) ((ArrayList<Object>) Facades.session.getParamFXMLController()).get(0);
        table = (TableView<User>) ((ArrayList<Object>) Facades.session.getParamFXMLController()).get(1);
        renderText();
    }

    private void renderText(){
        emailText.setText(user.get("email"));
        passwordText.setText(user.get("password"));
        fullnameText.setText(user.get("fullname"));
        addressText.setText(user.get("address"));
        if(user.get("birth")!=null) birthText.setValue(LocalDate.parse(user.get("birth"),DateTimeFormatter.ISO_DATE));
        phoneText.setText(user.get("phone"));
    }
}
