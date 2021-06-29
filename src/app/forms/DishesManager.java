package app.forms;

import app.models.Dishes;
import core.Facades;
import core.FileSystem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DishesManager {
    private FileChooser fileChooser = new FileChooser();
    private File file;

    @FXML
    private TableView<Dishes> tableDish;

    @FXML
    private TextField nameDish;

    @FXML
    private TextField idDish;

    @FXML
    private TextField groupDish;

    @FXML
    private TextField priceDish;

    @FXML
    private TextField unitDish;

    @FXML
    private Label imageText;

    @FXML
    public void initialize(){
        configFileChooser();
        initTable();
        renderTable();
    }

    private void configFileChooser(){
        fileChooser.setTitle("Chọn ảnh đồ uống");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void initTable(){
        TableColumn<Dishes, String> idColumn = new TableColumn<>("Trạng thái");
        TableColumn<Dishes, String> nameColumn = new TableColumn<>("Tên");
        TableColumn<Dishes, String> priceColumn = new TableColumn<>("Đơn giá");
        TableColumn<Dishes, ImageView> imageColumn = new TableColumn<>("Hình ảnh");
        TableColumn<Dishes, Boolean> modifyColumn = new TableColumn<>("Thay đổi");

        idColumn.setCellValueFactory((dish)->{
            if(dish.getValue().get("active").equals("1"))
                return new SimpleStringProperty("Hiện trên menu");
            else return new SimpleStringProperty("Ẩn khỏi menu");
        });

        nameColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("name"));
        });

        priceColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("price"));
        });

        imageColumn.setCellValueFactory((dish)->{
            return new SimpleObjectProperty<ImageView>(dish.getValue().getImage());
        });

        modifyColumn.setCellFactory(callback->{
            TableCell<Dishes,Boolean> cell = new TableCell<>(){
                HBox hBox = new HBox();
                Button deleteBtn = new Button("Ẩn");
                Button showBtn = new Button("Hiện");
                Button updateBtn = new Button("Sửa");

                {
                    hBox.getChildren().addAll(deleteBtn,showBtn,updateBtn);
                    deleteBtn.setOnAction(e->{
                        getTableView().getItems().get(getIndex()).softDelete();
                        getTableView().refresh();
                    });
                    showBtn.setOnAction(e->{
                        getTableView().getItems().get(getIndex()).set("active","1").update();
                        getTableView().refresh();
                    });
                    updateBtn.setOnAction(e->{
                        ArrayList<Object> arrayList = new ArrayList<>();
                        arrayList.add(getTableView().getItems().get(getIndex()));
                        arrayList.add(getTableView());

                        Facades.session.setParamsFXMLController(arrayList);
                        Facades.fm.open("Cập nhật thông tin đồ uống","modify-dish");
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


        tableDish.getColumns().addAll(idColumn,nameColumn,priceColumn,imageColumn,modifyColumn);
    }

    private void renderTable(){
        tableDish.getItems().addAll(new Dishes("").all().getModels());
    }

    @FXML
    void onAddDish(ActionEvent event) {
        if(file == null){
            Facades.fm.showAlert("Có lỗi","Vui lòng chọn file ảnh upload","warn");
            return;
        }

        if(nameDish.getText().isEmpty() || idDish.getText().isEmpty() || groupDish.getText().isEmpty() || priceDish.getText().isEmpty() || unitDish.getText().isEmpty()){
            Facades.fm.showAlert("Có lỗi","Không được để trống bất kỳ trường nào","warn");
            return;
        }

        if(new Dishes(idDish.getText()).find().exist()){
            Facades.fm.showAlert("Có lỗi","ID món đã tồn tại trong CSDL","warn");
            return;
        }

        HashMap<String,String> data = new HashMap<>();
        data.put("id",idDish.getText());
        data.put("name",nameDish.getText());
        data.put("type",groupDish.getText());
        data.put("price",priceDish.getText());
        data.put("unit",unitDish.getText());
        data.put("image",file.getName());

        FileSystem.copy(file.getAbsolutePath(),file.getName());

        tableDish.getItems().add((Dishes) new Dishes(data).insert());
    }

    @FXML
    void onUpload(ActionEvent event) throws IOException {
        File file = fileChooser.showOpenDialog(Facades.fm.get("admin-home"));
        if(file!=null){
            this.file = file;
            imageText.setText(file.getName());
        }
    }

}
