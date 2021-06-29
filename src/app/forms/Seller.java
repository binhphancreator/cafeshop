package app.forms;

import app.models.Dishes;
import app.models.Orders;
import app.models.OrdersDishes;
import core.Facades;
import core.Models;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Seller {
    private int sum = 0;
    private List<Dishes> dishes = new Dishes("").all().getModels();

    @FXML
    private TableView<Dishes> table;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Dishes> orderTable;

    @FXML
    private TextField preSumTextField;

    @FXML
    private TextField promotionTextField;

    @FXML
    private TextField sumTextField;

    @FXML
    private TextField payMoneyTextField;

    @FXML
    private TextField restMoneyTextField;

    @FXML
    private Button payBtn;

    @FXML
    public void initialize(){
        this.dishes = dishes.stream().filter(dish->{
            return dish.get("active").equals("1");
        }).collect(Collectors.toList());
        initTable();
        renderTable();
        renderOrderTable();
    }

    @FXML
    void onSearch(ActionEvent event) {
        table.getItems().clear();
        table.getItems().addAll(
                dishes.stream().filter(dish->{
                    return dish.get("name").toLowerCase().contains(searchTextField.getText().toLowerCase());
                }).collect(Collectors.toList())
        );
    }

    private void initTable(){
        TableColumn<Dishes, String> nameColumn = new TableColumn<>("Tên");
        TableColumn<Dishes, ImageView> imageColumn = new TableColumn<>("Hình ảnh");


        nameColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("name"));
        });

        imageColumn.setCellValueFactory((dish)->{
            return new SimpleObjectProperty<ImageView>(dish.getValue().getImage());
        });

        table.getColumns().addAll(nameColumn,imageColumn);

        table.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldDish, newDish) -> {
            AtomicReference<Boolean> selected = new AtomicReference<>(false);

            orderTable.getItems().forEach((dish)->{
                if(newDish.get("id").equals(dish.get("id")))
                    selected.set(true);
            });

            if(!selected.get() && newDish!=null){
                orderTable.getItems().add((Dishes) new Dishes(newDish.get("id")).find());
                payBtn.setDisable(false);
                setSum(sum+Integer.parseInt(newDish.get("price")));
            }

        }));
    }

    public void renderTable(){
        table.getItems().addAll(dishes);
    }

    public void renderOrderTable(){
        TableColumn<Dishes, String> nameColumn = new TableColumn<>("Tên");
        TableColumn<Dishes, String> priceColumn = new TableColumn<>("Đơn giá");
        TableColumn<Dishes, String> unitColumn = new TableColumn<>("Đơn vị tính");
        TableColumn<Dishes, String> numberColumn = new TableColumn<>("Số lượng");
        TableColumn<Dishes, Boolean> modifyColumn = new TableColumn<>("Chỉnh sửa");

        nameColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("name"));
        });

        priceColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("price")+"đ");
        });

        unitColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(dish.getValue().get("unit"));
        });

        numberColumn.setCellValueFactory((dish)->{
            return new SimpleStringProperty(String.valueOf(dish.getValue().getNumbers()));
        });

        modifyColumn.setMinWidth(150);
        modifyColumn.setCellFactory((callback)->{
            TableCell<Dishes,Boolean> cell = new TableCell<>(){
                HBox hBox = new HBox();
                Button deleteBtn = new Button("Xoá");
                Button addBtn = new Button("Thêm");
                Button substractBtn = new Button("Bớt");

                {
                    hBox.getChildren().addAll(addBtn,substractBtn,deleteBtn);
                    deleteBtn.setOnAction(e->{
                        getTableView().getItems().remove(getIndex()).resetNumbers();
                        if(getTableView().getItems().isEmpty()) payBtn.setDisable(true);
                    });
                    addBtn.setOnAction(e->{
                        getTableView().getItems().get(getIndex()).plusNumbers();
                        setSum(sum+Integer.parseInt(getTableView().getItems().get(getIndex()).get("price")));
                        getTableView().refresh();
                    });

                    substractBtn.setOnAction(e->{
                        Dishes dish = getTableView().getItems().get(getIndex());
                        if(getTableView().getItems().get(getIndex()).substractNumbers()==0) getTableView().getItems().remove(getIndex()).resetNumbers();
                        setSum(sum-Integer.parseInt(dish.get("price")));
                        getTableView().refresh();
                        if(getTableView().getItems().isEmpty()) payBtn.setDisable(true);
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

        orderTable.getColumns().addAll(nameColumn,priceColumn,unitColumn,numberColumn,modifyColumn);
    }

    @FXML
    void onPay(ActionEvent event) {
        int percent =0;
        int payMoney = 0;

        try{ payMoney = Integer.parseInt(payMoneyTextField.getText()); } catch(NumberFormatException e){ payMoney = 0; }
        try{ percent = Integer.parseInt(promotionTextField.getText()); } catch(NumberFormatException e){ percent =0; }

        if(payMoney < (sum*(100-percent)/100)){
            Facades.fm.showAlert("Có lỗi","Số tiền khách trả nhỏ hơn giá trị hoá đơn");
            return;
        }


        HashMap<String,String> order = new HashMap<>();
        order.put("user_id", Facades.session.get("id"));
        order.put("customer_pay",String.valueOf(payMoney));
        order.put("promotion",promotionTextField.getText());

        new Orders(order).insert();

        String id = Models.db.getLastID();

        orderTable.getItems().forEach((dish)->{
            HashMap<String,String> orderDish = new HashMap<>();
            orderDish.put("orders_id",id);
            orderDish.put("dishes_id",dish.get("id"));
            orderDish.put("price",dish.get("price"));
            orderDish.put("numbers",String.valueOf(dish.getNumbers()));

            new OrdersDishes(orderDish).insert();
        });

        refreshAll();

        Facades.session.setParamsFXMLController(id);
        Facades.fm.open("Hoá đơn","bill");
    }

    @FXML
    void onChangePromotion(KeyEvent event) {
        setSum(sum);
    }

    private void setSum(int value){
        sum = value;
        preSumTextField.setText(String.valueOf(sum));

        int percent =0;
        int payMoney = 0;
        int finalSum = 0;

        try{ payMoney = Integer.parseInt(payMoneyTextField.getText()); } catch(NumberFormatException e){ payMoney = 0; }
        try{ percent = Integer.parseInt(promotionTextField.getText()); } catch(NumberFormatException e){ percent =0; }

        finalSum = sum*(100-percent)/100;
        sumTextField.setText(String.valueOf(finalSum));
        restMoneyTextField.setText(String.valueOf(payMoney-finalSum));
    }

    @FXML
    void onChangePayMoney(KeyEvent event) {
        setSum(sum);
    }

    @FXML
    void onRefresh(ActionEvent event) {
        refreshAll();
    }

    private void refreshAll(){
        orderTable.getItems().clear();

        table.getItems().clear();

        table.getItems().addAll(dishes);

        payMoneyTextField.setText("0");
        setSum(0);

        payBtn.setDisable(true);
    }
}
