/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author James Nguyen
 */
public class Fee {
    private int fId;
    public String name;
    public int totalMoney;
    public int numOfHousehold;
    public Map<Integer, Integer> listOfHousehold; // contains the id of household contributed

    
    public Fee(int mId, String name, int totalMoney, int numOfHousehold) {
        this.fId = mId;
        this.name = name;
        this.totalMoney = totalMoney;
        this.numOfHousehold = numOfHousehold;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getNumOfHousehold() {
        return numOfHousehold;
    }

    public void setNumOfHousehold(int numOfHousehold) {
        this.numOfHousehold = numOfHousehold;
    }

    public void setListOfHousehold(Map<Integer, Integer> listOfHousehold) {
        this.listOfHousehold = listOfHousehold;
    }
    
    public List<Integer> getListOfHousehold() {
        List<Integer> result = new ArrayList(listOfHousehold.keySet());
        return result;
    }

    @Override
    public String toString() {
        return "Fee{" + "fId=" + fId + ", name=" + name + ", totalMoney=" + totalMoney + ", numOfHousehold=" + numOfHousehold + ", listOfHousehold=" + listOfHousehold + '}';
    }
    
}
