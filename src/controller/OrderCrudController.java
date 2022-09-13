package controller;

import model.DineInOrder;
import model.OrderDetails;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderCrudController {
    public boolean saveOrder(DineInOrder order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Orders VALUES (?,?,?,?)",order.getId(),order.getCusId(),order.getEmpId(),order.getTableId());
    }

    public boolean saveOrderDetails(ArrayList<OrderDetails>details) throws SQLException, ClassNotFoundException {
        for (OrderDetails det: details) {
            boolean isDetailSaved = CrudUtil.execute("INSERT INTO OrderDetail VALUES (?,?,?,?,?)",
                    det.getOrderId(),det.getFoodCode(),det.getDate(),det.getQty(),det.getUnitPrice());
            if (isDetailSaved){
                if(!updateQty(det.getFoodCode(),det.getQty())){
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String foodCode, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Food SET qtyOnHand = qtyOnHand-? WHERE code = ?",qty,foodCode);
    }
}
