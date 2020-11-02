/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.dao;

import com.nhom25.btl_cnpm.entity.Fee;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public ConnectionController() throws SQLException {

            String url = "jdbc:mysql://localhost:3306/se_project";// your db name
            String user = "root"; // your db username
            String password = ""; // your db password
            this.conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connect success!");
            } 
            this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);        

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
                Set <Integer> fId = household.getListOfFee().keySet();
                for(Integer key : fId){
                    String listFee = "INSERT INTO `listfee` (`hId`, `fId`, `money`) VALUES ('"+household.gethId()+"', '"+key
                            +"', '"+household.getListOfFee() +"');";
                    this.stat.executeUpdate(listFee);
                }
                ResultSet rs = this.stat.executeQuery("SELECT * FROM fee");
                
           }
    }

    public void delete(Household household) throws SQLException {
        String test = "SELECT * FROM household WHERE hId ="+household.gethId();
           ResultSet setID = this.stat.executeQuery(test);
           if(setID.next()){
               String delete = "DELETE FROM household WHERE household.hId = "+household.gethId()+";";
               this.stat.executeUpdate(delete);
               this.stat.executeUpdate("INSERT INTO removedhousehold (hId, houseHolder,numOfPerson,money) VALUES "
                 + "('"+household.gethId()+"','"+household.getHouseholder()+"','"+household.getNumOfPeople()+"','"+household.getMoney()+"');");
           }
           
    }
      public int[] findH(String f) throws SQLException {
        
        String sfind = "SELECT * FROM household WHERE hId like '%"+f+"%';";
        ResultSet find = this.stat.executeQuery(sfind);
        int i = find.getRow();
        int a = 0;
        int[] A = new int[i];
        while(find.next()&&a<i){
            A[a] = find.getInt("hId");
            a++;
        }
        return A;
    
    }
      public int[] findF(String f) throws SQLException {
        
        String sfind = "SELECT * FROM fee WHERE fId like '%"+f+"%';";
        ResultSet find = this.stat.executeQuery(sfind);
        int i = find.getRow();
        int a = 0;
        int[] A = new int[i];
        while(find.next()&&a<i){
            A[a] = find.getInt("fId");
            a++;
        }
        return A;
    
    }
           
  
}
    


