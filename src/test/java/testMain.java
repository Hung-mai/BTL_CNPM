
import com.nhom25.btl_cnpm.dao.ConnectionController;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bùi Minh Tu?n
 */
public class testMain {
    public static void main(String[] args) throws SQLException {
       ConnectionController conn = new ConnectionController();
       Household household = new Household("Nguyễn Tiến Dũng", 8);
//       conn.insertHousehold(household);
       conn.insertFee("Bảo vệ môi trường");
    }
}
