package app.forms;

import app.models.Revenue;
import core.Facades;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RevenueStatistics {

    private int sum = 0;
    private List<Revenue> arrRevenue = new Revenue("").all().getModels();

    @FXML
    private TableView<Revenue> table;

    @FXML
    private TextField monthTextField;

    @FXML
    private Label numbersOrdersLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    void onDoStatistics(ActionEvent event) {
        table.getItems().clear();
        renderTable();
    }

    @FXML
    public void initialize(){
        initTable();
        renderTable();
    }

    private void initTable(){
        TableColumn<Revenue,String> idColumn = new TableColumn<>("Mã hoá đơn");
        TableColumn<Revenue,String> timeColumn = new TableColumn<>("Thời gian");
        TableColumn<Revenue,String> promotionColumn = new TableColumn<>("Giảm giá");
        TableColumn<Revenue,String> revenueColumn = new TableColumn<>("Tổng tiền");
        TableColumn<Revenue,Boolean> buttonColumn = new TableColumn<>("Chức năng");

        idColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("id"));
        });

        timeColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("time"));
        });

        promotionColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("promotion")+"%");
        });

        revenueColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(arg.getValue().get("sum_money"));
        });

        buttonColumn.setCellFactory(callback->{
            TableCell<Revenue,Boolean> cell = new TableCell<>(){
                Button viewBtn = new Button("Xem hoá đơn");
                {
                    viewBtn.setOnAction(e->{
                        Facades.session.setParamsFXMLController(getTableView().getItems().get(getIndex()).get("id"));
                        Facades.fm.open("Hoá đơn","bill");
                    });
                }
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewBtn);
                    }
                }
            };

            return cell;
        });

        table.getColumns().addAll(idColumn,timeColumn,promotionColumn,revenueColumn,buttonColumn);
    }

    private void renderTable(){
        monthTextField.setText(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        table.getItems().addAll(
                arrRevenue.stream().filter(revenue -> {
                    return revenue.get("time").contains(monthTextField.getText());
                }).collect(Collectors.toList())
        );
        renderText();
    }

    private void renderText(){
        sum = 0;
        numbersOrdersLabel.setText("Số đơn:"+table.getItems().size());
        table.getItems().forEach(revenue -> {
            sum+=Integer.parseInt(revenue.get("sum_money"));
        });
        revenueLabel.setText("Doanh thu:"+String.valueOf(sum)+"đ");
    }

}
