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
//public class HouseholdService {
//    // lấy dữ liệu từ DAO về đây và thực hiện các chức năng- dịch vụ
//    
//    // các hàm đó là 
//    // tìm kiếm
//    // sắp xếp
//    // lọc
//    // vân vân tùy vào các dịch vụ mình muốn cung cấp cho người dùng
//    
//    // sap tra lai id ho dan tu tien it den tien nhieu
//    public static int[] sortAscending(Household household){
//        int id[] = new int[household.listOfFee.size()];
//        int money[] = new int[household.listOfFee.size()];
//        int count = 0;
//        for(Integer index : household.listOfFee.keySet()){
//            id[count] = index;
//            money[count] = household.listOfFee.get(index);
//            count++;
//        }
//        for(int i = 0; i < household.listOfFee.size() - 1; i++){
//            for(int j = i; j < household.listOfFee.size(); j++){
//                if(money[i] > money[j]){
//                    int temp = money[i];
//                    money[i] = money[j];
//                    money[j] = temp;
//                    temp = id[i];
//                    id[i] = id[j];
//                    id[j] = temp;
//                }
//            }
//        }
//        return id;
//    }
//}

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
