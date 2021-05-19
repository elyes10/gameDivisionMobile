/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.components.ImageViewer;
import com.codename1.components.StorageImage;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.messaging.Message;
import com.codename1.payment.Product;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import entities.cart;
import gui.ordersgui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import gui.Cart;
import utils.Statics;

/**
 *
 * @author HP OMEN
 */
public class CartServices {
        //var
   public static String listeproduit="";
    boolean resultOK;
    ConnectionRequest req;
      ConnectionRequest req1,req3,req4,req5;
    static CartServices instance;
    ArrayList<cart> carts = new ArrayList<>();
     ArrayList<cart> allcarts=new ArrayList<>();
    
    //constructor
    private CartServices() {
        req = new ConnectionRequest();
        req1 = new ConnectionRequest();
          req3 = new ConnectionRequest();
             req4 = new ConnectionRequest();
             req5 = new ConnectionRequest();
    }
    
    //SINGLETON
    public static CartServices getInstance(){
        
        if (instance == null) {
            instance = new CartServices();
        }
        
        return instance;
    }
  
  
    
public ArrayList<cart> parseJSONAction(String textJson){
        
        JSONParser j = new JSONParser();
        
        try {
            
  Map<String, Object> cartsListJson = j.parseJSON(new CharArrayReader(textJson.toCharArray()));
            ArrayList<Map<String,Object>> cartsList = (ArrayList<Map<String,Object>>) cartsListJson.get("root");
            
            for (Map<String, Object> obj : cartsList) {
                
                cart t = new cart();
                float id = Float.parseFloat(obj.get("productId").toString());
                t.setProductid((int) id);
                float quantite = Float.parseFloat(obj.get("quantite").toString());
                t.setQuantity((int) quantite);
                float cid = Float.parseFloat(obj.get("cartId").toString());
                t.setCart_id((int)cid);
                float userid = Float.parseFloat(obj.get("userId").toString());
                t.setUser_id((int) userid);
                int idd=(int)id;
               
                carts.add(t);

            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return carts;  
    }
       //GET TASKS
   public Form getAllcarts(){
        String url = Statics.BASE_URL+"/affichejs";
        req.setUrl(url);
        req.setPost(false);
        Form hi = new Form("Cart", BoxLayout.y());
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req.getResponseData());
                carts = parseJSONAction(response);
                System.out.println(response);
                 Container labelTitles = new Container(BoxLayout.x());
                   Label nametitle = new Label("Product");
                   
        Label quantitetitle = new Label("    Quantite");
          // Label imagetitle = new Label("Image");
           Label Pricetitle = new Label("           Price");
            labelTitles.addAll(nametitle,quantitetitle,Pricetitle);
            hi.add(labelTitles);
                         
                  for (cart p : carts) 
                {   
                    
                   
                     p.setProduct_name(prodname(p.getProductid()));
                 listeproduit=listeproduit+","+prodname(p.getProductid());
                     hi.add(addItemcontainer(p));  
                    
                 
                 
                } req.removeResponseListener(this);
            }
        });
         
        System.out.println("carts=>"+carts);
        NetworkManager.getInstance().addToQueueAndWait(req);
        return hi;
    }
   
   
   public Container addItemcontainer(cart p) {
       
        
        Container cell = new Container(BoxLayout.y());
        //ImageViewer image = new ImageViewer(theme.getImage(p.getImage()));
       
        Container labelsCtn = new Container(BoxLayout.x());
        
        String idpr =p.getProduct_name();
        
        Label name = new Label(idpr);
        Label Categ = new Label("   "+Integer.toString(p.getQuantity()));
     Label price = new Label("      "+prodprice(p.getProductid()));
    //image test
     

    //end image
        //Button mailBtn = new Button(p.getMail());
         Button quantbutButtonplus=new Button(" +");
                    Button quantButtonmoin=new Button(" -");
        Button removeprod=new Button("    Del");
        quantbutButtonplus.addActionListener(e -> {quantiteplus(p.getCart_id());p.setQuantity(p.getQuantity()+1);Categ.setText("   "+Integer.toString(p.getQuantity()));});
        quantButtonmoin.addActionListener(e -> {quantitemoin(p.getCart_id());p.setQuantity(p.getQuantity()-1);Categ.setText   ("   "+Integer.toString(p.getQuantity()));  });
        removeprod.addActionListener(e -> {deleteItem(p.getCart_id());});
        labelsCtn.addAll(name,Categ,quantbutButtonplus,quantButtonmoin,price,removeprod);
    
        
        cell.add(labelsCtn);
       
      
       // cell.add(background);
        return cell;
    }
 
   public boolean deleteItem(int id) {
        String url = Statics.BASE_URL + "/deletcartjson/"+id;
                 //création de l'URL
        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); 
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        resultOK=true;
        return resultOK;
    }
   
      public boolean quantiteplus(int id) {
        String url = Statics.BASE_URL + "/quantitychangejson/"+id;
                 //création de l'URL
        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); 
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        resultOK=true;
        return resultOK;
    }
      
         public boolean quantitemoin(int id) {
        String url = Statics.BASE_URL + "/quantitychangeminusjson/"+id;
                 //création de l'URL
        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); 
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        resultOK=true;
        return resultOK;
    }
       
         
         
         static String n="jj";
    
        
        
        public String prodname(int id){
       String name="";
       
       String url = Statics.BASE_URL+"/prodname/"+id;
        req1.setUrl(url);
        req1.setPost(false);
        Form hi = new Form("Cart", BoxLayout.y());
        req1.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req1.getResponseData());
                
              n= response;
               req1.removeResponseListener(this);
            }
        });
         NetworkManager.getInstance().addToQueueAndWait(req1);
       return n;
       }  
        
        public String prodprice(int id){
       String name="";
       
       String url = Statics.BASE_URL+"/prodprice/"+id;
        req3.setUrl(url);
        req3.setPost(false);
        Form hi = new Form("Cart", BoxLayout.y());
        req3.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req3.getResponseData());
               
              n= response;
               req3.removeResponseListener(this);
            }
        });
         NetworkManager.getInstance().addToQueueAndWait(req3);
       return n;
       } 
        
        public String prodimg(int id){
       String name="";
       
       String url = Statics.BASE_URL+"/prodimage/"+id;
        req3.setUrl(url);
        req3.setPost(false);
        Form hi = new Form("Cart", BoxLayout.y());
        req3.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req3.getResponseData());
           
              n= response;
               req3.removeResponseListener(this);
            }
        });
         NetworkManager.getInstance().addToQueueAndWait(req3);
       return n;
       } 
        
        public void add2cart(int id){
        String url = Statics.BASE_URL+"/addcartjson/"+id;
        req4.setUrl(url);
        req4.setPost(false);
        NetworkManager.getInstance().addToQueueAndWait(req4);
        }
         public void addorder(String list){
        String url = Statics.BASE_URL+"/addordersjson/"+(listeproduit.toString());
        req5.setUrl(url);
        req5.setPost(false);
        NetworkManager.getInstance().addToQueueAndWait(req5);
        
        }
         
         public void mail(){
         
         Message m = new Message("order confirmed");
m.getAttachments().put("product lists", listeproduit) ;

Display.getInstance().sendMessage(new String[] {"elyes.zarrad@esprit.com"}, "orders confirmation", m);
         }
}
