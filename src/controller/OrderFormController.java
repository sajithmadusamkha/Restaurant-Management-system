package controller;

import DBConnection.DBConnection;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.DineInOrder;
import model.Item;
import model.OrderDetails;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import view.tm.CartTm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderFormController {
    public Label lblDate;
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtName;
    public JFXComboBox<String> cmbFoodCode;
    public JFXTextField txtFoodName;
    public JFXTextField txtStock;
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn colFoodName;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotalCost;
    public TableColumn colDate;
    public TableColumn colBtn;
    public Label lblTotal;
    public JFXTextField txtTblId;
    public JFXTextField txtEmployeeId;
    public JFXTextField txtOrderId;

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory("code"));
        colFoodName.setCellValueFactory(new PropertyValueFactory("foodName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory("totalCost"));
        colBtn.setCellValueFactory(new PropertyValueFactory("btn"));

        loadDate();
        setCustomerId();
        setFoodCode();
        try {
            setOrderId();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerDetails(newValue);
            try {
                setOrderId();
            }catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

        cmbFoodCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setFoodDetails(newValue);
        });

    }

    private void setOrderId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(id) FROM Orders");
        resultSet.next();
        resultSet.getString("MAX(id)");
        if(resultSet.getString("MAX(id)") == null){
            txtOrderId.setText("D001");
        } else {
            Long id = Long.parseLong(resultSet.getString("MAX(id)").substring(2,resultSet.getString("MAX(id)").length()));
            id++;
            txtOrderId.setText("D"+String.format("%03d",id));
        }
    }

    private void setFoodDetails(String selectedFoodCode) {
        try {
            Item i = FoodCrudController.getFood(selectedFoodCode);
            if(i != null){
                txtFoodName.setText(i.getFoodName());
                txtPrice.setText(String.valueOf(i.getPrice()));
                txtStock.setText(String.valueOf(i.getQtyOnHand()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCustomerDetails(String selectedCustomer) {
        try {
            Customer c = CustomerCrudController.getCustomer(selectedCustomer);
            if(c != null){
                txtName.setText(c.getName());
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDate() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void setCustomerId(){
        try {
            ObservableList<String> cIdObList = FXCollections.observableArrayList(
                    CustomerCrudController.getCustomerId()
                            );
            cmbCustomerId.setItems(cIdObList );
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFoodCode() {
        try {
            cmbFoodCode.setItems(FXCollections.observableArrayList(FoodCrudController.getFoodCode()));
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    ObservableList<CartTm> tmList = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {
         double unitPrice = Double.parseDouble(txtPrice.getText());
         int qty = Integer.parseInt(txtQty.getText());
         double totalCost = unitPrice*qty;
         String date = lblDate.getText();

         CartTm isExists = isExists(cmbFoodCode.getValue());

         if(isExists != null){
             for (CartTm temp:tmList) {
                 if(temp.equals(isExists)){
                     temp.setQty((temp.getQty()+qty));
                     temp.setTotalCost(temp.getTotalCost()+totalCost);
                 }
             }
         } else {
             Button btn = new Button("Delete");
             CartTm tm = new CartTm(
                     cmbFoodCode.getValue(),
                     txtFoodName.getText(),
                     unitPrice,
                     qty,
                     date,
                     totalCost,
                     btn
             );

             btn.setOnAction(e -> {
                         tmList.remove(tm);
                         calculateTotal();
             });

             tmList.add(tm);
             tblCart.setItems(tmList);
         }
         tblCart.refresh();
         calculateTotal();
    }

    private CartTm isExists(String foodCode){
        for (CartTm tm:tmList) {
            if(tm.getCode().equals(foodCode)){
                return tm;
            }
        }
        return null;
    }

    private void calculateTotal(){
        double total = 0;
        for (CartTm tm : tmList) {
            total += tm.getTotalCost();
        }
        lblTotal.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {
        DineInOrder order = new DineInOrder(
                txtOrderId.getText(),
                cmbCustomerId.getValue(),
                txtEmployeeId.getText(),
                txtTblId.getText()
        );
        ArrayList<OrderDetails> details = new ArrayList<>();
        for (CartTm tm:tmList) {
            details.add(new OrderDetails(
                    txtOrderId.getText(),
                    tm.getCode(),
                    tm.getDate(),
                    tm.getQty(),
                    tm.getPrice()
            ));
        }

        Connection connection = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isOrderSaved = new OrderCrudController().saveOrder(order);
            if(isOrderSaved){
                boolean isDetailsSaved = new OrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved){
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Try Again!").show();
                }
            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e){
            System.out.println(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void printInvoiceOnAction(ActionEvent actionEvent) {
        String subTotal = lblTotal.getText();

        HashMap paramMap = new HashMap();
        paramMap.put("total",subTotal);

        try {
            JasperReport compile = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/Invoice.jasper"));
            ObservableList<CartTm> cartTm = tblCart.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compile,paramMap,new JRBeanCollectionDataSource(cartTm));
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException e) {
            e.printStackTrace();
        }

        lblTotal.setText("0");
        tblCart.getItems().clear();
        clearAllText();
    }

    public void clearAllText(){
        txtName.clear();
        txtTblId.clear();
        txtEmployeeId.clear();
        txtFoodName.clear();
        txtStock.clear();
        txtPrice.clear();
        txtQty.clear();
        txtOrderId.clear();
    }
}
