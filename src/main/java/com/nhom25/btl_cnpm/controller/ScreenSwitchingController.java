/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nhom25.btl_cnpm.controller;

import com.nhom25.btl_cnpm.bean.DanhMucBean;
import com.nhom25.btl_cnpm.view.pages.FeeManagerJP;
import com.nhom25.btl_cnpm.view.pages.HouseholdManageJP;
import com.nhom25.btl_cnpm.view.pages.InfoJP;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author hungn
 */
public class ScreenSwitchingController {
    
    private final JPanel root;
    private String kindSelected = "";
    List<DanhMucBean> listItem = null;

    public ScreenSwitchingController(JPanel root) {
        this.root = root;
    }
    
    public void setView(JPanel jpnItem, JLabel jlbItem){
        this.kindSelected = "houseManager";
        jpnItem.setBackground(new Color(0, 102, 0));
        jlbItem.setBackground(new Color(0, 102, 0));
        
        root.removeAll();    
        root.setLayout(new BorderLayout());
        root.add(new HouseholdManageJP());
        root.validate();
        root.repaint();
    }
    
   
    public void setEvent(List<DanhMucBean> listItem) {
        this.listItem = listItem;
        listItem.forEach(item -> {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));
        });
    }
    
//    public void setAction(List<DanhMucBean> listItem) {
//        this.listItem = listItem;
//        for(DanhMucBean item : listItem){
//            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));
//        }
//    }


    
    class LabelEvent implements MouseListener{
        
        private JPanel node;
        
        public String kind;
        private final JPanel jpnItem;
        private final JLabel jlbItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jlbItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlbItem = jlbItem;
        }
        
        

        @Override
        public void mouseClicked(MouseEvent e) {
            switch(kind){
                case "housejpn": 
                    node = new HouseholdManageJP();
                    break;
                case "feejpn":
                    node = new FeeManagerJP();
                    break;
                case "infojpn": 
                    node = new InfoJP();
                    break;
                    
                default:break;
            }
            
            root.removeAll();
            root.setLayout(new BorderLayout());
            root.add(node);
            root.validate();
            root.repaint();
            setChangeBackground(kind);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            jpnItem.setBackground(new Color(0, 102, 0));
            jlbItem.setBackground(new Color(0, 102, 0));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            jpnItem.setBackground(new Color(0, 102, 0));
            jlbItem.setBackground(new Color(0, 102, 0));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!kindSelected.equalsIgnoreCase(kind)){
                jpnItem.setBackground(new Color(0, 153, 0));
                jlbItem.setBackground(new Color(0, 153, 0));
            }
        }
        
        
    
    }
    
    public void setChangeBackground(String kind){
        for(DanhMucBean item : listItem){
            if(item.getKind().equalsIgnoreCase(kind)){
                item.getJpn().setBackground(new Color(0, 102, 0));
                item.getJpn().setBackground(new Color(0, 102, 0));
            }else{
                 item.getJpn().setBackground(new Color(0, 153, 0));
                item.getJpn().setBackground(new Color(0, 153, 0));
            }
        }
    }
}
    
    

