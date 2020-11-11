/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.controller;

import com.nhom25.btl_cnpm.dao.ConnectionController;
import com.nhom25.btl_cnpm.entity.Household;
import java.sql.SQLException;
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
