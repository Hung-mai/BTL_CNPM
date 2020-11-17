/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.controller;

import com.nhom25.btl_cnpm.dao.ConnectionController;
import com.nhom25.btl_cnpm.entity.Fee;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James Nguyen
 */
public class FeeController {
    ConnectionController controller;
    
    public FeeController() throws SQLException{
        this.controller = new ConnectionController();
        
    }
    
    public boolean addFee(String name){
        if(name.equals("")){
            return false;
        }
        try {
            controller.insertFee(name);
        } catch (SQLException ex) {
            Logger.getLogger(FeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean modifyFee(Fee f, String name) throws SQLException{
        if(name.equals("")){
            return false;
        }
        controller.modifyFee(f.getfId(), name);
        return true;
    }
    
    public List<Fee> findFee(String s){ 
        if(s.length() <= 0){
            return controller.findAllFee();
        }
        try {
            List<Fee> result = new LinkedList<Fee>();
            int listfId[] = controller.findF(s);
            for(int i = 0; i!= listfId.length; i++){
                result.add(controller.findFee(listfId[i]));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            controller.modifyFeeHouseholder(hId, fId, Integer.parseInt(num));
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
    
    /*
    public boolean removeFee(Fee fee){
        try{
            controller.delete(fee);
        }catch (SQLException ex) {
            Logger.getLogger(HouseholdController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    */
}
