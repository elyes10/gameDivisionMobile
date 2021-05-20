/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.entities.Product;
import com.entities.Statics;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 *
 * @author LENOVO
 */
public class FavsServices1 {
     public static FavsServices instance=null;
     
    public boolean resultOK;
   private final  ConnectionRequest req;
     public ArrayList<Product> tasks;
     
    
  
    public FavsServices1() {
         req = new ConnectionRequest();
        
    }
    public void deletefromfavourites(int i,int j)
    {
        String url = Statics.BASE_URL + "/deletefavsmobile/" + i + "/" + j;
         ConnectionRequest conn = new ConnectionRequest();                   
        conn.setUrl(url);// Insertion de l'URL de notre demande de connexion
        conn.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) 
            {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); 
            }
        });
        try
        {
            NetworkManager.getInstance().addToQueueAndWait(conn);
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
    }
    public static FavsServices getInstance() 
    {
        if (instance == null) 
        {
            instance = new FavsServices();
        }
        return instance;
    }
    public ArrayList<Product> parseTasks(String jsonText) 
    {
        try {
            tasks=new ArrayList<>();
             Map<String,Object> tasksListJson;
             List<Map<String,Object>> list;
            JSONParser j = new JSONParser();
// Instanciation d'un objet JSONParser permettant le parsing du résultat json
            tasksListJson  =j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            System.out.println("mapsize=>"+tasksListJson.size());
            list = (List<Map<String,Object>>)tasksListJson.get("root");
            for (Object keys : tasksListJson.values())
{
   System.out.println("values->"+keys);
}
//            System.out.println(list.size());
            for (Map<String,Object> obj : list) {
                Product p = new Product();
                float id = Float.parseFloat(obj.get("id").toString());
                p.setProduct_id((int)id);
                float price = Float.parseFloat(obj.get("pr").toString());
                p.setPrice((double)price);
                p.setProduct_name(obj.get("n").toString());
                p.setCategory(obj.get("c").toString());
                float quantity = Float.parseFloat(obj.get("q").toString());
                p.setQuantity((int)quantity);
                float Tid = Float.parseFloat(obj.get("t").toString());
                p.setTeam_id((int)Tid);
                p.setImg(obj.get("i").toString());
                System.out.println("&^p^&=>"+p.toString());
                tasks.add(p);
            }
        }
        catch (IOException ex) 
        {
            System.out.println("error");
        }
        return tasks;
    }
    public int getactiveuserfromdb()
    {
         String url = Statics.BASE_URL + "/getactivusemob";
          ConnectionRequest conn = new ConnectionRequest();       
                 
        conn.setUrl(url);
        
      
        try{
        NetworkManager.getInstance().addToQueueAndWait(conn);
        }
        catch(Exception e)
        {
            System.out.println("error");
        } 
        return Integer.parseInt(new String(conn.getResponseData()));
        
    }

public Form getAllTasks(){
    int activuser=getactiveuserfromdb();
        System.out.println("user logged in f1="+activuser);
        Form hi = new Form("Hi World", BoxLayout.y());
        FavsServices f1=new FavsServices();
       
         hi.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e->{
             f1.setactiveuser();
             f1.setactiveuserId(activuser);
             f1.getAllTasks().show();
             
                 });
        String url = Statics.BASE_URL+"/listfavmob/"+activuser;
        tasks=new ArrayList<>();
        req.setUrl(url);
        req.setPost(false);
       
        Style s = UIManager.getInstance().getComponentStyle("TitleCommand");
FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s);
//hi.getToolbar().addCommandToSideMenu("addtofavourites", icon, (e) ->this.AddItemstolist(selectedproducts));        

hi.getToolbar().addCommandToSideMenu("Mail", icon, (e) -> Log.p("Clicked"));
        req.addResponseListener((NetworkEvent evt) -> {
            String response = new String(req.getResponseData());
            tasks = parseTasks(response);
            System.out.println(response);
            for (Product p : tasks)
            {
                hi.add(addItemcontainer(p));
                
            }            // req.removeResponseListener(this);
        });
        
        System.out.println("tasks=>"+tasks);
        NetworkManager.getInstance().addToQueueAndWait(req);
        return hi;
    }

    public Container addItemcontainer(Product p) {

        Container cell = new Container(BoxLayout.y());
        
        //ImageViewer image = new ImageViewer(theme.getImage(p.getImage()));
        Container labelsCtn = new Container(BoxLayout.y());
        Label name = new Label(p.getProduct_name());
        Label Categ = new Label(p.getCategory());
        Label price  = new Label(p.getPrice()+"$");
        Label Quantity;
       Quantity = new Label(p.getQuantity()+"");
        Label tId=new Label(p.getTeam_id()+"");
        Label Img=new Label(p.getImg());
        Label id=new Label(p.getProduct_id()+"");
        //Button mailBtn = new Button(p.getMail());
        labelsCtn.addAll(name,price,id);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(500, 500 , 0xffff0001), true);
URLImage background = URLImage.createToStorage(placeholder,"file:///C:/wamp641/www/Products_management_GD/public/uploads/images/"+p.getImg(),
        "file:///C:/wamp641/www/Products_management_GD/public/uploads/images/"+p.getImg());
cell.add(background);

        cell.add(labelsCtn);
       Button B =new Button("delete product from favourites list");
        
//        System.out.println(activuser);
        //B.addActionListener(e->this.addtofavourites(activuser, p.getProduct_id()));
       B.addActionListener((ActionListener) (ActionEvent evt) -> {
          getactiveuserfromdb();
          this.deletefromfavourites(getactiveuserfromdb(),p.getProduct_id());
          getAllTasks().show();
           //this.addtofavourites(activuser, p.getProduct_id());
        });
       cell.add(B);
       
      
       // cell.add(background);
        return cell;
    }
    
    
}
