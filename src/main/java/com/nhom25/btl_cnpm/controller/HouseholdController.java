/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.controller;

import com.nhom25.btl_cnpm.dao.ConnectionController;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hungn
 */
public class HouseholdController {
    ConnectionController controller;
    
    public HouseholdController() throws SQLException{
        this.controller = new ConnectionController();
        
    }
    
// thử ghi xem ra cái gì ko?
    // viết các hàm để thực hiện các dịch vụ từ các class service, trả về dữ liệu
    // và trả các dữ liệu đó ra view
    public boolean addHousehold(String name, String num){
        try{
            if(num.equals("")|| Integer.parseInt(num) < 1 || name.equals("")){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        Household household = new Household(name, Integer.parseInt(num));
        try{
            controller.insertHousehold(household);
        }catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean removeHousehold(Household household){
        try{
            controller.deleteHousehold(household.gethId());
            return true;
        }catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }
    
    public boolean removeFeeOfHousehold(int hId, int fId){
        try{
            controller.deleteFeeOfHousehold(fId, hId); // chèn hàm xoá trong controller vào đây
            return true;
        }catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }
    
    public boolean changeFee(int hId, int fId, String num){
        try{
            if(num.equals("") || Integer.parseInt(num) <= 0){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        
        try {
            controller.modifyFeeHouseholder(hId, fId, Integer.parseInt(num)/1000);
        } catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean addFee(int hId, int fId, String num){
        try{
            if(num.equals("") || Integer.parseInt(num) <= 0){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        
        try {
            controller.insertFeeHousehold(hId, fId, Integer.parseInt(num)/1000);
        } catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean modifyHousehold(Household h, String name, String num) throws SQLException{
        try{
            if(num.equals("")|| Integer.parseInt(num) < 1 || name.equals("")){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        controller.modifyHouseholder(h.gethId(), name);
        controller.modifyNumberOfPeople(h.gethId(), Integer.parseInt(num));
        return true;
    }
    
    public List<Household> findHousehold(String s){
        if(s.length() == 0){
            return controller.findAllHousehold();
        }
        try {
            List<Household> result = new LinkedList<Household>();
            int listhId[] = controller.findH(s);
            for(int i = 0; i!= listhId.length; i++){
                result.add(controller.findHousehold(listhId[i]));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
