package app.forms;

import app.models.Dishes;
import core.Facades;
import core.FileSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class ModifyDish {

    private File file;
    private Dishes dish;
    private TableView<Dishes> tableDish;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField nameDish;

    @FXML
    private TextField idDish;

    @FXML
    private TextField typeDish;

    @FXML
    private TextField priceDish;

    @FXML
    private TextField unitDish;

    @FXML
    private Label imageText;

    @FXML
    public void initialize(){
        dish = (Dishes) ((ArrayList<Object>) Facades.session.getParamFXMLController()).get(0);
        tableDish = (TableView<Dishes>) ((ArrayList<Object>) Facades.session.getParamFXMLController()).get(1);
        configFileChooser();
        renderText();
    }

    private void configFileChooser(){
        fileChooser.setTitle("Chọn ảnh đồ uống");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void renderText(){
        nameDish.setText(dish.get("name"));
        idDish.setText(dish.get("id"));
        typeDish.setText(dish.get("type"));
        priceDish.setText(dish.get("price"));
        unitDish.setText(dish.get("unit"));
        imageText.setText(dish.get("image"));
    }

    @FXML
    void onUpdate(ActionEvent event) {
        if(file!=null){
            FileSystem.copy(file.getAbsolutePath(),file.getName());
        }

        dish
                .set("name",nameDish.getText())
                .set("type",typeDish.getText())
                .set("price",priceDish.getText())
                .set("unit",unitDish.getText())
                .set("image",imageText.getText())
                .update();

        tableDish.refresh();
        Facades.fm.get("modify-dish").close();
    }

    @FXML
    void onUpload(ActionEvent event) {
        File file = fileChooser.showOpenDialog(Facades.fm.get("admin-home"));
        if(file!=null){
            this.file = file;
            imageText.setText(file.getName());
        }
    }

}

