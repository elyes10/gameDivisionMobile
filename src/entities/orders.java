/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;

/**
 *
 * @author HP OMEN
 */
public class orders {

    private String order_id;
    private int user_id;
    private String adr_livraison;
    private String country;
    private int post_code;
    private String date_commande;
    private double total_price;
    private String status;
    private String liste_produit;

    public orders() {
    }

    public orders(String order_id, int user_id, String adr_livraison, String country, int post_code, String date_commande, double total_price, String status, String liste_produit) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.adr_livraison = adr_livraison;
        this.country = country;
        this.post_code = post_code;
        this.date_commande = date_commande;
        this.total_price = total_price;
        this.status = status;
        this.liste_produit = liste_produit;
    }

  

    
    /**
     * @return the order_id
     */
    public String getOrder_id() {
        return order_id;
    }

    /**
     * @param order_id the order_id to set
     */
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    /**
     * @return the user_id
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the adr_livraison
     */
    public String getAdr_livraison() {
        return adr_livraison;
    }

    /**
     * @param adr_livraison the adr_livraison to set
     */
    public void setAdr_livraison(String adr_livraison) {
        this.adr_livraison = adr_livraison;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the post_code
     */
    public int getPost_code() {
        return post_code;
    }

    /**
     * @param post_code the post_code to set
     */
    public void setPost_code(int post_code) {
        this.post_code = post_code;
    }

    /**
     * @return the date_commande
     */
    public String getDate_commande() {
        return date_commande;
    }

    /**
     * @param date_commande the date_commande to set
     */
    public void setDate_commande(String date_commande) {
        this.date_commande = date_commande;
    }

    /**
     * @return the total_price
     */
    public double getTotal_price() {
        return total_price;
    }

    /**
     * @param total_price the total_price to set
     */
    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "order_id: " + " " + this.order_id + " " + "user_id: " + " " + this.user_id + " " + "adresse: " + " " + this.adr_livraison + " " + "country: " + " " + this.country + " " + "post_code: " + " " + this.post_code + " " + "date: " + " " + this.date_commande +" " + "total_price: " + " " + this.total_price +" " + "status: " + " " + this.status +'\n';
    }

    /**
     * @return the liste_produit
     */
    public String getListe_produit() {
        return liste_produit;
    }

    /**
     * @param liste_produit the liste_produit to set
     */
    public void setListe_produit(String liste_produit) {
        this.liste_produit = liste_produit;
    }
}
