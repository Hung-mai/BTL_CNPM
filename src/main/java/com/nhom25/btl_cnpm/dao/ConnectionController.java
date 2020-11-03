/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.dao;

import com.nhom25.btl_cnpm.entity.Household;
import java.sql.*;
import java.util.Set;

/**
 *
 * @author hungn
 */
public class ConnectionController {
    // viết phương thức liên kết đến database
    // lưu ý là cần thêm dependency của mysql vào file pom.xml
    // lên gg search mysql mvn. tìm đến đường đẫn đầu tiên. chọn đúng phiên bản mysql đang
    // sử dụng và cop dependency vào pom.xml
    private final Connection conn;
    private final Statement stat;
    
    public ConnectionController() throws SQLException{
        this.conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql_se20201", "root", "");
        this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }
    
    /**
     * chỉ áp dụng để thêm hộ dân mới
     * chỉ thêm khi đã có danh sách các khoản thu phí trong listOfFee của hộ đó
     * @param household
     * @throws SQLException 
     */
    
    public void insertHousehold(Household household) throws SQLException{
        String insertHousehold = "INSERT INTO household(householder, numberOfPeople, money) VALUES ('" 
                + household.getHouseholder() + "'," + household.getNumOfPeople() + "," + household.getMoney() + ")";
        this.stat.executeUpdate(insertHousehold);
        
        
        int hId = 0;
        ResultSet rset = this.stat.executeQuery("SELECT hId FROM household WHERE householder = '" + household.getHouseholder() + "'");
        while(rset.next()){
            hId = rset.getInt("hId");
        }        
        String listFeee = "INSERT INTO listFee VALUES (" + hId + "," + 1 + "," + 6000*household.getNumOfPeople() + ")";
        this.stat.executeUpdate(listFeee);        
        Set<Integer> fId = household.getMapOfFee().keySet();
        for(Integer key : fId){
            String listFee = "INSERT INTO listFee VALUES (" + hId + "," + key + "," + household.getMapOfFee().get(key) + ")";
            this.stat.executeUpdate(listFee);
        }
                
        rset = this.stat.executeQuery("SELECT * FROM fee");
        while(rset.next()){
            int feeId = rset.getInt("fId");
            int totalMoney = rset.getInt("totalMoney");
            int number = rset.getInt("numberOfHousehold");
            if(feeId == 1){
                totalMoney += household.getNumOfPeople()*6000;
                number++;
                rset.updateInt("totalMoney", totalMoney);
                rset.updateInt("numberOfHousehold", number);
                rset.updateRow();
            }
            else{         
                for(Integer key : fId){
                    if(key == feeId){
                        totalMoney += household.getMapOfFee().get(key);
                        number++;
                        rset.updateInt("totalMoney", totalMoney);
                        rset.updateInt("numberOfHousehold", number);
                        rset.updateRow();
                    }
                }
            }
        }
    }
    /**
     * thêm tên một loại đóng góp mới
     * giá trị mặc định là 0
     * @param name
     * @throws SQLException 
     */
    public void insertFee(String name) throws SQLException{
        String insertFee = "INSERT INTO fee (name, totalMoney, numberOfHousehold) VALUES ('" + name + "', 0, 0)";
        this.stat.executeUpdate(insertFee);
        System.out.println("Insert fee success");
    }
    
    /**
     * thêm phí hoàn toàn mới của 1 hộ dân theo id hộ dân có và id của loại phí đó
     * @param hId
     * @param fId
     * @param money
     * @throws SQLException 
     */
    
    public void insertFeeHousehold(int hId, int fId, int money) throws SQLException{
        String listFee = "INSERT INTO listFee VALUES (" + hId + "," + fId + "," + money + ")";
            this.stat.executeUpdate(listFee);
        ResultSet rset = this.stat.executeQuery("SELECT * FROM household");
        while(rset.next()){
           int houseId = rset.getInt("hId");
           int hmoney = rset.getInt("money");
           if(houseId == hId){
               hmoney += money;
               rset.updateInt("money", hmoney);
               rset.updateRow();
           }
        }
        
        rset = this.stat.executeQuery("SELECT * FROM fee");
        while(rset.next()){
            int feeId = rset.getInt("fId");
            int totalMoney = rset.getInt("totalMoney");
            int number = rset.getInt("numberOfHousehold");         
            if(fId == feeId){
                totalMoney += money;
                number++;
                rset.updateInt("totalMoney", totalMoney);
                rset.updateInt("numberOfHousehold", number);
                rset.updateRow();
            }
        }
    }
    
    
    /**
     * chỉnh sửa số người trong một hộ khi đã biết Id của hộ đó
     * @param hId
     * @param numberOfPeople
     * @throws SQLException 
     */
    public void adjustNumberOfPeople(int hId, int numberOfPeople) throws SQLException{
        int money = numberOfPeople*6000;
        ResultSet rset = this.stat.executeQuery("SELECT * FROM household");
        while(rset.next()){
           int houseId = rset.getInt("hId");
           int number = rset.getInt("numberOfPeople");
           int hmoney = rset.getInt("money");
           if(houseId == hId){
               hmoney += money - number*6000;
               rset.updateInt("money", hmoney);
               rset.updateInt("numberOfPeople", numberOfPeople);
               rset.updateRow();
           }
        }
        
        rset = this.stat.executeQuery("SELECT * FROM fee");
        while(rset.next()){
            int feeId = rset.getInt("fId");     
            if(feeId == 1){
                rset.updateInt("totalMoney", money);
                rset.updateInt("numberOfHousehold", numberOfPeople);
                rset.updateRow();
            }
        }
        
        String updateFee = "";
        rset = this.stat.executeQuery("SELECT * FROM listFee");
        while(rset.next()){
            int houseId = rset.getInt("hId");
            int feeId = rset.getInt("fId");
            if(feeId == 1 && houseId == hId){
                updateFee += "UPDATE listFee SET money = " + money + " WHERE hId = " + houseId + " AND fId = 1";
            }
        } 
        this.stat.executeUpdate(updateFee);
    }
    
    /**
     * Chỉnh sửa tên của một hộ
     * @param hId
     * @param name
     * @throws SQLException 
     */
    public void adjustHouseholder(int hId, String name) throws SQLException{
        ResultSet rset = this.stat.executeQuery("SELECT * FROM household");
        while(rset.next()){
           int houseId = rset.getInt("hId");
           if(houseId == hId){
               rset.updateString("householder", name);
               rset.updateRow();
           }
        }
    }
    
    /**
     * Chỉnh sửa số tiền đóng của một khoản phí xác định.
     * @param hId
     * @param fId
     * @param money
     * @throws SQLException 
     */
    public void adjustFeeHouseholder(int hId, int fId, int money) throws SQLException{
        int adjustMoney = 0;
        ResultSet rset = this.stat.executeQuery("SELECT * FROM listFee");
        while(rset.next()){
            int houseId = rset.getInt("hId");
            int feeId = rset.getInt("fId");
            if(feeId == fId && hId == houseId){
                adjustMoney = rset.getInt("money");
            }
        }
        String updateFee = "UPDATE listFee SET money = " + money + " WHERE hId = " + hId + " AND fId = " + fId;
        this.stat.executeUpdate(updateFee);
        
        adjustMoney = money - adjustMoney;
        rset = this.stat.executeQuery("SELECT * FROM fee");
        while(rset.next()){
            int feeId = rset.getInt("fId");
            int totalMoney = rset.getInt("totalMoney");      
            if(feeId == fId){
                totalMoney += adjustMoney;
                rset.updateInt("totalMoney", totalMoney);
                rset.updateRow();
            }
        }
        
        rset = this.stat.executeQuery("SELECT * FROM household");
        while(rset.next()){
           int houseId = rset.getInt("hId");
           int hmoney = rset.getInt("money");
           if(houseId == hId){
               hmoney += adjustMoney;
               rset.updateInt("money", hmoney);
               rset.updateRow();
           }
        }
        
    }
        
}
