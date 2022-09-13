package controller;

import model.Customer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerCrudController {
    public static ArrayList<String> getCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM Customer");
        ArrayList<String> id = new ArrayList<>();
        while (resultSet.next()){
            id.add(resultSet.getString(1));
        }
        return id;
    }

    public static Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer WHERE id = ?",id);
        if(resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }
}
