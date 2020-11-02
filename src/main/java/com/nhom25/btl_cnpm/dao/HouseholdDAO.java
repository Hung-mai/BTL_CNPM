/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.dao;

import com.nhom25.btl_cnpm.entity.Household;
import java.sql.SQLException;

/**
 *
 * @author hungn
 */
public class HouseholdDAO {
    // lẫy toàn bộ dữ liệu từ database về thông qua connectionController
    // và truyền vào list
    public static void main(String[] args) throws SQLException {
         ConnectionController conn = new ConnectionController();
         Household a = new Household(2460,"a",3,3);
         conn.delete(a);
         
    }
   
    
    // viết các hàm thêm, sửa, xóa, lấy dữ liệu
    
    // class này để thao tác thay đổi trực tiếp lên database
    
}
