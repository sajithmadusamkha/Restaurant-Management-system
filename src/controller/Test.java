package controller;

import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
   public void test() throws SQLException, ClassNotFoundException {
       ResultSet set = CrudUtil.execute("SELECT * FROM Customer");
       while (set.next()){

       }
   }
}
