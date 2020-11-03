/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 *
 * @author James Nguyen
 */
public class Household {
    private int hId;
    public String householder;
    public int numOfPeople;
    public int money;
    public Map<Integer, Integer> listOfFee;

    public Household(int mId, String householder, int numOfPeople, int money) {
        this.hId = mId;
        this.householder = householder;
        this.numOfPeople = numOfPeople;
        this.money = money;
    } 
    
    public Household(String householder, int numOfPeople){
        this.householder = householder;
        this.numOfPeople = numOfPeople;
        this.listOfFee = new HashMap<>();
    }
    
    public int gethId() {
        return hId;
    }

    public void sethId(int hId) {
        this.hId = hId;
    }

    public String getHouseholder() {
        return householder;
    }

    public void setHouseholder(String householder) {
        this.householder = householder;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    /**
     * tự động tính tổng số tiền có sẵn trong list
     * @return 
     */
    public int getMoney() {
        money = 6000 * this.getNumOfPeople();
        Set<Integer> setMoney = this.listOfFee.keySet();
        setMoney.forEach(key -> {
            money += this.listOfFee.get(key);
        });
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Integer> getListOfFee() {
        return (List) listOfFee.keySet();
    }
    
    public Map<Integer, Integer> getMapOfFee(){
        return listOfFee;
    }
    /**
     * Charge to a fee
     * @param fee
     * @param money 
     */
    public void addContribute(Fee fee, int money){
        listOfFee.put(fee.getfId(), money);
        this.money += money;
        
        fee.listOfHousehold.put(this.hId, money);
        fee.totalMoney += money;                
    }

    /**
     * Remove charge to a fee
     * @param fee 
     */
    public boolean removeContribute(Fee fee){
        if(listOfFee.containsKey(fee.getfId())){
            int money = listOfFee.get(fee.getfId());
            listOfFee.remove(fee.getfId());
            fee.totalMoney -= money;
            fee.listOfHousehold.remove(hId);
            return true;
        }
        
        return false;
                    
    }
}
