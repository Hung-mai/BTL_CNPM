/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.service;

import com.nhom25.btl_cnpm.entity.*;
import java.util.*;

/**
 *
 * @author hungn
 */

public interface HouseholdService{
    // outer service
    List<Household> findAll();
    
    List<Household> findByName(String name); // name of owner
    
    Household insert(Household data); // insert and return
    
    boolean delete(int id); // delete by id
    
    boolean update(Household data);
    
    List<Household> sortByMoney(boolean asc); // sort by asc or des by total
    
    // inner service
    List<Fee> findAllFee();
    
    List<Fee> findFeeByName(String name);
    
    List<Fee> sortFeeByMoney(boolean asc);
    
    boolean updateFee(List<Fee> data); // update the fee
}
