package com.justin.cheapestproduct;

import org.w3c.dom.Text;

import java.util.Date;

public class Price {

//    private int id;
    private String barcode;
    private String price1;
    private String update_date;
    private int store_id;
    private int area_id;
    private String verify;
    private String user_id;
    private String remark;

//    public int getId(){
//        return id;
//    }
//
//    public void setId(int id){
//        this.id = id;
//    }

    public String getBarcode(){
        return barcode;
    }

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getPrice1(){
        return price1;
    }

    public void setPrice1(String price1){
        this.price1 = price1;
    }

    public String getUpdate_date(){
        return update_date;
    }

    public void setUpdate_date(String update_date){
        this.update_date = update_date;
    }

    public int getStore_id(){
        return store_id;
    }

    public void setStore_id(int store_id){
        this.store_id = store_id;
    }

    public int getArea_id(){
        return area_id;
    }

    public void setArea_id(int area_id){
        this.area_id=area_id;
    }

    public String getVerify(){
        return verify;
    }

    public void setVerify(String verify){
        this.verify=verify;
    }

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String user_id){
        this.user_id=user_id;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark=remark;
    }

}
