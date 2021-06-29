package app.forms;

import app.models.Roles;
import core.Facades;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PersonalInfo {

    @FXML
    private Label roleLabel;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField birthTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    public void initialize(){
        roleLabel.setText(new Roles(Facades.session.get("role_id")).find().get("name"));
        fullnameTextField.setText(Facades.session.get("fullname"));
        emailTextField.setText(Facades.session.get("email"));
        birthTextField.setText(Facades.session.get("birth"));
        addressTextField.setText(Facades.session.get("address"));
        phoneTextField.setText(Facades.session.get("phone"));
    }

}
