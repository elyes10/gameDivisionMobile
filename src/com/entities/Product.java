/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;





/**
 *
 * @author LENOVO
 */
public class Product {
    private int Product_id;
    private String Product_name;
    private int Team_id;

    public Product() {
    }
   
    
    @Override
    public String toString() {
        return      Product_name + ", Team_id=" + Team_id + ", price=" + price + ", Category=" + Category + ", Quantity=" + Quantity + "\n" + "\n"+ '}';
    }

    public int getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(int Product_id) {
        this.Product_id = Product_id;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String Product_name) {
        this.Product_name = Product_name;
    }

    public int getTeam_id() {
        return Team_id;
    }

    public void setTeam_id(int Team_id) {
        this.Team_id = Team_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    private double price;
    private String Category;
    private int Quantity;
    private String img;
//    final private ImageView imageView;
   
  
    public Product(String n,int t,double p,String c,int q,String i)
    {
        
        this.Product_name=n;
        this.Team_id=t;
        this.price=p;
        this.Category=c;
        this.Quantity=q;
        this.img=i;
       
     
    }
}
