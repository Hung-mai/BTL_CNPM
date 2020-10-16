/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.entity;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author James Nguyen
 */
public class Fee {
    private int mId;
    public String name;
    public int totalMoney;
    public int numOfHousehold;
    public HashMap<Integer, Integer> listOfHousehold; // contains the id of household contributed

    public Fee(int mId, String name, int totalMoney, int numOfHousehold) {
        this.mId = mId;
        this.name = name;
        this.totalMoney = totalMoney;
        this.numOfHousehold = numOfHousehold;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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
    
}
