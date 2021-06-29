package app.forms;

import app.models.Dishes;
import app.models.Orders;
import app.models.OrdersDishes;
import core.Facades;
import core.Models;
import core.QueryBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.HashMap;

public class Bill {

    private String id;
    private int sum = 0;

    @FXML
    private Label orderIdLabel;

    @FXML
    private TableView<OrdersDishes> dishTable;

    @FXML
    private Label preSumTextField;

    @FXML
    private Label promotionTextField;

    @FXML
    private Label sumTextField;

    @FXML
    private Label payMoneyTextField;

    @FXML
    private Label restMoneyTextField;

    @FXML
    private Label timeLabel;

    @FXML
    public void initialize(){
        id = (String) Facades.session.getParamFXMLController();
        renderTable();
        renderText();
    }

    private void renderText(){
        Orders order = new Orders(id);
        order.find();

        int sumAll = sum * (100-Integer.parseInt(order.get("promotion"))) / 100;
        int restMoney = Integer.parseInt(order.get("customer_pay")) - sumAll;

        orderIdLabel.setText("Số hoá đơn:"+id);
        preSumTextField.setText("Tạm tính:"+String.valueOf(sum));
        promotionTextField.setText("Giảm giá:"+order.get("promotion")+"%");
        sumTextField.setText("Thành tiền:"+String.valueOf(sumAll));
        payMoneyTextField.setText("Khách trả:"+order.get("customer_pay") + "đ");
        restMoneyTextField.setText("Trả lại:"+String.valueOf(restMoney)+"đ");
        timeLabel.setText(order.get("time"));
    }

    private void renderTable(){
        TableColumn<OrdersDishes,String> nameColumn = new TableColumn<>("Tên món");
        TableColumn<OrdersDishes,String> priceColumn = new TableColumn<>("Đơn giá");
        TableColumn<OrdersDishes,String> numbersColumn = new TableColumn<>("Số lượng");

        nameColumn.setCellValueFactory((arg)->{
            Dishes dish = new Dishes(arg.getValue().get("dishes_id"));
            dish.find();
            if(dish.exist())
                return new SimpleStringProperty(dish.get("name"));
            else return new SimpleStringProperty("Món đã bị xoá khỏi thực đơn");
        });

        priceColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("price"));
        });

        numbersColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("numbers"));
        });

        dishTable.getColumns().addAll(nameColumn,priceColumn,numbersColumn);

        ArrayList<HashMap<String,String>> dishes = Models.db.excuteQuery(QueryBuilder.table("ordersdishes").select("*").where("orders_id","=",id).get()).get();
        dishes.forEach((dish)->{
            dishTable.getItems().add(new OrdersDishes(dish));
            sum+= Integer.parseInt(dish.get("numbers")) * Integer.parseInt(dish.get("price"));
        });
    }
}
