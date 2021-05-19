/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.components.SpanLabel;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import entities.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import static services.CartServices.instance;
import entities.orders;
import utils.Statics;
/**
 *
 * @author HP OMEN
 */
public class OrderServices {
    boolean resultOK;
    ConnectionRequest req;
      ConnectionRequest req1,req3,req4,req5;
    static OrderServices instance;
    ArrayList<orders> orders = new ArrayList<>();
     ArrayList<orders> allorders=new ArrayList<>();

    public OrderServices() {
         req = new ConnectionRequest();
        req1 = new ConnectionRequest();
          req3 = new ConnectionRequest();
             req4 = new ConnectionRequest();
             req5 = new ConnectionRequest();
    }
     
       //SINGLETON
    public static OrderServices getInstance(){
        
        if (instance == null) {
            instance = new OrderServices();
        }
        
        return instance;
    }
    
    
    public ArrayList<orders> parseJSONAction(String textJson){
        
        JSONParser j = new JSONParser();
        
        try {
            
  Map<String, Object> ordersListJson = j.parseJSON(new CharArrayReader(textJson.toCharArray()));
            ArrayList<Map<String,Object>> ordersList = (ArrayList<Map<String,Object>>) ordersListJson.get("root");
            
            for (Map<String, Object> obj : ordersList) {
                
                orders o= new orders();
              String adress = obj.get("adrLivraison").toString();
                o.setAdr_livraison(adress);
                String country = obj.get("country").toString();
                o.setCountry(country);
                
               
                String status = obj.get("status").toString();
                o.setStatus(status);
                 String listeProduit = obj.get("listeProduit").toString();
               o.setListe_produit(listeProduit);
               
                orders.add(o);
              

            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return orders;  
    }
    
    //GET TASKS
   public Form getAllorders(){
        String url = Statics.BASE_URL+"/ordersjson";
        req.setUrl(url);
        req.setPost(false);
        Form hi = new Form("orders", BoxLayout.y());
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req.getResponseData());
                orders = parseJSONAction(response);
                System.out.println(response+"im here");
                 Container labelTitles = new Container(BoxLayout.x());
                   Label nametitle = new Label("Address ");
                   
        
          // Label imagetitle = new Label("Image");
           Label Pricetitle = new Label("status");
           Label Products = new Label("products");
            labelTitles.addAll(nametitle,Pricetitle,Products);
            hi.add(labelTitles);
                   
                  for (orders p : orders) 
                {   
                    
                   
                    
                    
                     hi.add(addItemcontainer(p));  
                   
                 
                 
                } req.removeResponseListener(this);
            }
        });
         
        System.out.println("orders=>"+orders);
        NetworkManager.getInstance().addToQueueAndWait(req);
        return hi;
    }
    
    public Container addItemcontainer(orders p) {
       
        
        Container cell = new Container(BoxLayout.y());
        //ImageViewer image = new ImageViewer(theme.getImage(p.getImage()));
       
        Container labelsCtn = new Container(BoxLayout.x());
        
        String idpr =p.getAdr_livraison();
        
        SpanLabel address = new SpanLabel(idpr);
        SpanLabel postcode = new  SpanLabel("   "+Integer.toString(p.getPost_code()));
     SpanLabel status = new SpanLabel(p.getStatus());
     SpanLabel produclist = new SpanLabel(p.getListe_produit());
    //image test
     
       
    //end image
        //Button mailBtn = new Button(p.getMail());

        labelsCtn.addAll(address,postcode,status,produclist);
    
        
        cell.add(labelsCtn);
       
      
       // cell.add(background);
        return cell;
    }
    
    
}
