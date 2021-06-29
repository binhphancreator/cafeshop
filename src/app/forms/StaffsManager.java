package app.forms;

import app.models.User;
import core.Facades;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class StaffsManager {

    private boolean emailExist = false;

    @FXML
    private TableView<User> table;

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
    void onAddStaffs(ActionEvent event) {
        if(emailText.getText().isEmpty() || passwordText.getText().isEmpty()){
            Facades.fm.showAlert("Có lỗi xảy ra","Email và mật khẩu không được để trống");
            return;
        }
        emailExist = false;
        table.getItems().forEach(user->{
            if(user.get("email").equals(emailText.getText())) emailExist = true;
        });

        if(emailExist){
            Facades.fm.showAlert("Có lỗi xảy ra","Email đã được đăng ký bởi người dùng khác");
            return;
        }

        HashMap<String,String> userHashMap = new HashMap<>();
        userHashMap.put("email",emailText.getText());
        userHashMap.put("password",passwordText.getText());
        userHashMap.put("fullname",fullnameText.getText());
        userHashMap.put("address",addressText.getText());
        if(birthText.getValue()!=null) userHashMap.put("birth",birthText.getValue().format(DateTimeFormatter.ISO_DATE));
        userHashMap.put("phone",phoneText.getText());

        table.getItems().add((User) new User(userHashMap).insert());

    }

    @FXML
    public void initialize(){
        initTable();
        renderTable();
    }

    private void initTable(){
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        TableColumn<User, String> nameColumn = new TableColumn<>("Tên");
        TableColumn<User, String> addressColumn = new TableColumn<>("Địa chỉ");
        TableColumn<User, String> phoneColumn = new TableColumn<>("Số điện thoại");
        TableColumn<User, Boolean> modifyColumn = new TableColumn<>("Chức năng");

        emailColumn.setCellValueFactory((user)->{
            return new SimpleStringProperty(user.getValue().get("email"));
        });

        nameColumn.setCellValueFactory((user)->{
            return new SimpleStringProperty(user.getValue().get("fullname"));
        });

        addressColumn.setCellValueFactory((user)->{
            return new SimpleStringProperty(user.getValue().get("address"));
        });

        phoneColumn.setCellValueFactory((user)->{
            return new SimpleStringProperty(user.getValue().get("phone"));
        });

        modifyColumn.setCellFactory(callback->{
            TableCell<User,Boolean> cell = new TableCell<>(){
                HBox hBox = new HBox();
                Button deleteBtn = new Button("Xoá");
                Button updateBtn = new Button("Sửa");

                {
                    hBox.getChildren().addAll(deleteBtn,updateBtn);
                    deleteBtn.setOnAction(e->{
                        getTableView().getItems().remove(getIndex()).delete();
                    });
                    updateBtn.setOnAction(e->{
                        ArrayList<Object> arrayList = new ArrayList<>();
                        arrayList.add(getTableView().getItems().get(getIndex()));
                        arrayList.add(getTableView());

                        Facades.session.setParamsFXMLController(arrayList);
                        Facades.fm.open("Cập nhật thông tin nhân viên","modify-user");
                    });
                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        });

        table.getColumns().addAll(emailColumn,nameColumn,addressColumn,phoneColumn,modifyColumn);
    }

    private void renderTable(){
        table.getItems().addAll(new User("").all().getModels());
    }

}
