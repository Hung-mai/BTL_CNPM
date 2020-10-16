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
 * @author James Nguyen
 */
public interface FeeService {
    // outer service
    List<Fee> findAll();
    
    List<Fee> findByName(String name);
    
    boolean insert(Fee data);
    
    boolean delete(int id);
    
    boolean update(Fee data);
    
    List<Fee> sortByMoney(boolean asc);
    
    // inner service
    List<Household> findAllHousehold();
    
    List<Household> findHouseholdByName(String name); // name of owner
    
    List<Household> sortHouseholdByMoney(boolean asc); // sort by asc or des by total
    
    boolean update(List<Household> data); // update the fee w/list of household
}
