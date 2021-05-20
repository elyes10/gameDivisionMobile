
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 *
 * @author LENOVO
 */
public class products {

    private int Product_id;
    private String Product_name;
    private int Team_id;
    private double Price;
    private String Category;
    private int Quantity;
    private String img;
    private ImageView imagev;

    public products() {
    }

    public products(int Product_id, String Product_name, int Team_id, double Price, String Category, int Quantity, String img) {
        this.Product_id = Product_id;
        this.Product_name = Product_name;
        this.Team_id = Team_id;
        this.Price = Price;
        this.Category = Category;
        this.Quantity = Quantity;
        this.img = img;

    }

    @Override
    public String toString() {
        return this.getProduct_name() + '-' + this.getQuantity() + '-' + this.getTeam_id() + '-' + this.getPrice() + '-' + this.getCategory() + '\n';
    }

    /**
     * @return the Product_id
     */
    public int getProduct_id() {
        return Product_id;
    }

    /**
     * @param Product_id the Product_id to set
     */
    public void setProduct_id(int Product_id) {
        this.Product_id = Product_id;
    }

    /**
     * @return the Product_name
     */
    public String getProduct_name() {
        return Product_name;
    }

    /**
     * @param Product_name the Product_name to set
     */
    public void setProduct_name(String Product_name) {
        this.Product_name = Product_name;
    }

    /**
     * @return the Team_id
     */
    public int getTeam_id() {
        return Team_id;
    }

    /**
     * @param Team_id the Team_id to set
     */
    public void setTeam_id(int Team_id) {
        this.Team_id = Team_id;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return Price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.Price = price;
    }

    /**
     * @return the Category
     */
    public String getCategory() {
        return Category;
    }

    /**
     * @param Category the Category to set
     */
    public void setCategory(String Category) {
        this.Category = Category;
    }

    /**
     * @return the Quantity
     */
    public int getQuantity() {
        return Quantity;
    }

    /**
     * @param Quantity the Quantity to set
     */
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the imagev
     */
    public ImageView getImagev() {
        return imagev;
    }

    /**
     * @param imagev the imagev to set
     */
    public void setImagev(ImageView imagev) {
        this.imagev = imagev;
    }

}
