package controller;

import model.Item;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodCrudController {
    public static ArrayList<String> getFoodCode() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Food");
        ArrayList<String> codeList = new ArrayList<>();
        while(resultSet.next()){
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static Item getFood(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Food WHERE code=?",code);
        if(resultSet.next()){
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;
    }
}
