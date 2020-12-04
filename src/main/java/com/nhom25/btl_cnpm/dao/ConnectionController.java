/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.dao;

import java.util.Set;
import com.nhom25.btl_cnpm.entity.Fee;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        this.conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/mysql_se20201", "root", "");
        this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }
    
    /**
     * hàm tìm kiếm toàn bộ hộ dân, khoản phí và hàm trả về khoản phí theo hộ dân, hộ dân theo khoản phí
     * @return 
     */
    public List<Household> findAllHousehold(){
        List<Household> householdList = new ArrayList<>();

        return householdList;
    }
    
    public List<Fee> findAllFee(){
        List<Fee> feeList = new ArrayList<>();
        
        try{
            String sql = "select * from fee";            
            ResultSet rs = stat.executeQuery(sql);
            
            while(rs.next()){
                Fee f = new Fee(rs.getInt("fId"), rs.getString("name"), 
                        rs.getInt("totalMoney"), rs.getInt("numberOfHousehold"));
                f.listOfHousehold = findHouseholdOfFee(f);
                feeList.add(f);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if(stat != null){
                try {
                    stat.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return feeList;
    }
    
    public Map<Integer, Integer> findFeeOfHousehold(Household h) throws SQLException{
        Map<Integer, Integer> listOfFee = new HashMap<>();
        String sql = "select fId, money from listfee where hId = '" + h.hId + "'";
        Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet res = state.executeQuery(sql);
        while(res.next()){
            listOfFee.put(res.getInt("fId"), res.getInt("money"));
            h.money += res.getInt("money");
        }
        return listOfFee;
    }
    public Map<Integer, Integer> findHouseholdOfFee(Fee f) throws SQLException{
        Map<Integer, Integer> listOfHousehold = new HashMap<>();
        String sql = "select hId, money from listfee where fId = " + f.getfId();
        Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = state.executeQuery(sql);
        while(rs.next()){
            listOfHousehold.put(rs.getInt("hId"), rs.getInt("money"));
        }
        return listOfHousehold;
    }
    
    
    /**
     * chỉ áp dụng để thêm hộ dân mới
     * chỉ thêm khi đã có danh sách các khoản thu phí trong listOfFee của hộ đó
     * @param household
     * @throws SQLException 
     */
    
    public void insertHousehold(Household household) throws SQLException{
        String insertHousehold = "INSERT INTO household(householder, numberOfPeople, money) VALUES ('" 
                + household.getHouseholder() + "'," + household.getNumOfPeople() + "," + household.getNumOfPeople()*6 + ")";
        this.stat.executeUpdate(insertHousehold);
        
        
        int hId = 0;
        ResultSet rset = this.stat.executeQuery("SELECT hId FROM household WHERE householder = '" + household.getHouseholder() + "'");
        while(rset.next()){
            hId = rset.getInt("hId");
        }        
        String listFeee = "INSERT INTO listfee VALUES (" + hId + "," + 1 + "," + 6*household.getNumOfPeople() + ")";
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
                totalMoney += household.getNumOfPeople()*6;
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
    public void modifyNumberOfPeople(int hId, int numberOfPeople) throws SQLException{
        int money = numberOfPeople*6;
        int number = 0;
        ResultSet rset = this.stat.executeQuery("SELECT * FROM household where hId = " + hId);
        while(rset.next()){
           number = rset.getInt("numberOfPeople");
           //int hmoney = rset.getInt("money");
           //hmoney += money - number*6;
           rset.updateInt("money", money);
           rset.updateInt("numberOfPeople", numberOfPeople);
           rset.updateRow();
        }
        
        rset = this.stat.executeQuery("SELECT * FROM fee where fId = 1");
        while(rset.next()){ 
            int totalMoney = rset.getInt("totalMoney");
            totalMoney = totalMoney + money - number*6;
            //System.out.println("So tien thay doi: " + (money - number*6));
            rset.updateInt("totalMoney", totalMoney);
            rset.updateRow();
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
    public void modifyHouseholder(int hId, String name) throws SQLException{
        ResultSet rset = this.stat.executeQuery("SELECT * FROM household");
        while(rset.next()){
           int houseId = rset.getInt("hId");
           if(houseId == hId){
               rset.updateString("householder", name);
               rset.updateRow();
           }
        }
    }
    
    public void modifyFee(int fId, String name) throws SQLException{
        ResultSet rset = this.stat.executeQuery("SELECT * FROM fee");
        while(rset.next()){
           int feeId = rset.getInt("fId");
           if(feeId == fId){
               rset.updateString("name", name);
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
    public void modifyFeeHouseholder(int hId, int fId, int money) throws SQLException{
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
        
       
    public Household showH(int hId) throws SQLException {
        String show = "SELECT * FROM household WHERE hId ="+hId+";";
        ResultSet rs = this.stat.executeQuery(show);
        rs.next();
        String hH = rs.getString("houseHolder");
        int num = rs.getInt("numOfPerson");
        int money = rs.getInt("money");
        Household A = new Household(hId,hH,num,money);
        
        return A;
        }

    public Fee showF(int fId) throws SQLException {
        String show = "SELECT * FROM fee WHERE fId ="+fId+";";
         ResultSet rs = this.stat.executeQuery(show);
        rs.next();
        String name = rs.getString("name");
        int money = rs.getInt("totalMoney");
        int num = rs.getInt("numberOfHousehold");
        Fee A = new Fee(fId,name,money,num);   
        return A;
        }
//    public void repair(Household household1,Household household2) throws SQLException {
//        String test = "SELECT * FROM `household` WHERE hId ="+household.gethId();
//               ResultSet setID = this.stat.executeQuery(test);
//               if(setID.next()){
//                   String repair = 
//                   this.stat.executeUpdate(repair);
//               }
//    }
    public void insert(Household household) throws SQLException {
           String test = "SELECT * FROM `household` WHERE hId ="+household.gethId();
           ResultSet setID = this.stat.executeQuery(test);
           if(!setID.next()){
                String insert = "INSERT INTO `household` (`hId`, `houseHolder`, `numOfPerson`,`money`) VALUES "
                    + "(NULL, '"+household.getHouseholder()+"', '"+household.getNumOfPeople()+"', '"+household.getMoney()+"');";
                this.stat.executeUpdate(insert);
                Set <Integer> fId = (Set) household.getListOfFee();
                for(Integer key : fId){
                    String listFee = "INSERT INTO `listfee` (`hId`, `fId`, `money`) VALUES ('"+household.gethId()+"', '"+key
                            +"', '"+household.getListOfFee() +"');";
                    this.stat.executeUpdate(listFee);
                }
                ResultSet rs = this.stat.executeQuery("SELECT * FROM fee");
                
           }
    }

    
     public void deleteHousehold(int hId) throws SQLException{
        String str = "SELECT * FROM listfee WHERE hId = " + hId;
        ResultSet rset = this.stat.executeQuery(str);
        Map<Integer, Integer> listOfFee = new HashMap<>();
        while(rset.next()){
            listOfFee.put(rset.getInt("fId"), rset.getInt("money"));
        }
        str = "DELETE FROM listfee WHERE hId = " + hId;
        this.stat.executeUpdate(str);
       
        str = "SELECT * FROM household WHERE hId = " + hId;
        rset = this.stat.executeQuery(str);
        int numberOfPeople = 0;
        while(rset.next()){
            numberOfPeople = rset.getInt("numberOfPeople");
        }
        if(numberOfPeople != 0){
            listOfFee.put(1, numberOfPeople*6);
        }
        str = "DELETE FROM household WHERE hId = " + hId;
        this.stat.executeUpdate(str);
        
        str = "SELECT * FROM fee";
        rset = this.stat.executeQuery(str);
        while(rset.next()){
            int fId = rset.getInt("fId");
            if(listOfFee.containsKey(fId)){
                int totalMoney = rset.getInt("totalMoney");
                int numberOfHousehold = rset.getInt("numberOfHousehold");
                totalMoney -= listOfFee.get(fId);
                numberOfHousehold--;
                rset.updateInt("totalMoney", totalMoney);
                rset.updateInt("numberOfHousehold", numberOfHousehold);
                rset.updateRow();
            }
        }
    }
    
     public void deleteFee(int fId)throws SQLException {
        String test = "SELECT * FROM listfee WHERE listfee.fId =" + fId +";";
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);        
        ResultSet testF = st.executeQuery(test);
        while(testF.next()){
            int hId = testF.getInt("hId");
            deleteFeeOfHousehold(fId, hId);
        }
        this.stat.executeUpdate("DELETE FROM fee WHERE fee.fId =" +  fId +";");
    }
     
          public void deleteFeeOfHousehold(int fId,int hId)throws SQLException {
        String test = "SELECT * FROM listfee WHERE listfee.fId =" + fId +" AND listfee.hId = "+ hId+";";
        ResultSet testLF = this.stat.executeQuery(test);
            if(testLF.next()){
                int money = testLF.getInt("money");
                this.stat.executeUpdate("DELETE FROM listfee WHERE (listfee.fId=" +fId+" AND listfee.hId = "+hId+");");
                
                testLF = this.stat.executeQuery("SELECT * FROM household WHERE hId =" +hId);
                testLF.next();
                int totalMoney = testLF.getInt("money");
                totalMoney -= money;
                this.stat.executeUpdate("UPDATE household SET money = " + totalMoney + " WHERE household.hId = "+ hId +";");
                
                testLF = this.stat.executeQuery("SELECT * FROM fee WHERE fId =" +fId);
                testLF.next();
                totalMoney = testLF.getInt("totalMoney");
                totalMoney -= money;
                int num = testLF.getInt("numberOfHousehold");
                num -= 1;
                this.stat.executeUpdate("UPDATE fee SET totalMoney = " + totalMoney + " WHERE fee.fId = "+ fId +";");
                this.stat.executeUpdate("UPDATE fee SET numberOfHousehold = " + num + " WHERE fee.fId = "+ fId +";");
            }
     }
    
      public int[] findH(String f) throws SQLException {
        
        String sfind = "SELECT * FROM household WHERE householder LIKE '%"+f+"%';";
        ResultSet find = this.stat.executeQuery(sfind);
        int i = find.getType();
        int a = 0;
        int[] A = new int[i];
        while(find.next()){
            A[a] = find.getInt("hId");
            a++;
        }
        return A;    
    }
      
      public int[] findF(String f) throws SQLException {
        
        String sfind = "SELECT * FROM fee WHERE name LIKE '%"+f+"%';";
        ResultSet find = this.stat.executeQuery(sfind);
        int i = find.getType();
        int a = 0;
        int[] A = new int[i];
        while(find.next()){
            A[a] = find.getInt("fId");
            a++;
        }
        return A;    
    }
    
       public Fee findFee(int fId) throws SQLException{
           String s = "Select * from fee where fid = '" + fId + "'";
           ResultSet rs = this.stat.executeQuery(s);
           while(rs.next()){
               Fee f = new Fee(rs.getInt("fId"), rs.getString("name"), 
                       rs.getInt("totalMoney"), rs.getInt("numberOfHousehold"));
               f.setListOfHousehold(findHouseholdOfFee(f));
               return f;
           }
           return null;
       }
       
       public Household findHousehold(int hId) throws SQLException{
           String s = "Select * from household where hid = '" + hId + "'";
           ResultSet rs = this.stat.executeQuery(s);
           while(rs.next()){
               Household hd = new Household(rs.getInt("hId"), rs.getString("householder"), 
                        rs.getInt("numberOfPeople"), rs.getInt("money"));
                hd.setListOfFee(findFeeOfHousehold(hd));
                return hd;
           }
           return null;
       }
       
       public List<Fee> findFeeNotChargeYet(int hId) throws SQLException{
           List<Fee> result = new ArrayList<>();
           String sql = "select fId from fee except (select fId from listfee where hId = " + hId + ")";
           Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet res = state.executeQuery(sql);
           while(res.next()){
               result.add(findFee(res.getInt("fId")));
           }
           return result;
       }
       
       public List<Household> findHouseholdNotChargeYet(int fId) throws SQLException{
           List<Household> result = new ArrayList<>();
           String sql = "select hId from household except (select hId from listfee where fId = " + fId + ")";
           Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           ResultSet res = state.executeQuery(sql);
           while(res.next()){
               result.add(findHousehold(res.getInt("hId")));
           }
           return result;
       }
}
    


