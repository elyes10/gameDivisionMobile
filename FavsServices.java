/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
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
import gui.Cart;
import gui.EditUserForm;
import gui.GamesForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 *
 * @author LENOVO
 */
public class FavsServices {
    public static  FavsServices instance;
     
    public boolean resultOK;
    private final  ConnectionRequest req;

    /**
     *
     */
    public ArrayList<Product> tasks1;
    public Map<String,Integer>Likes=new HashMap<>();
     public ArrayList<Product> tasks;
     public int activuser;
     
     public void setactiveuserId(int i)
     {
         
       this.activuser=i;
       
     }
  
    public FavsServices() {
         req = new ConnectionRequest();
        
    }

    public static FavsServices getInstance() 
    {
        if (instance == null) 
        {
            instance = new FavsServices();
        }
        return instance;
    }
    public void showMerchLikes()
    {
        getAllMerchLikes().show();
    }     
   public Map<String,Integer> parseMerchLikes(String jsonText)
    {
          Map<String,Integer>MapLikes=new HashMap<>();
        try {
           
             Map<String,Object> tasksListJson;
                     tasksListJson = new HashMap<>();
             List<Map<String,Object>> list=new ArrayList<>();
            JSONParser j = new JSONParser();
// Instanciation d'un objet JSONParser permettant le parsing du résultat json
            tasksListJson  =j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            System.out.println("mapsize=>"+tasksListJson.size());
            list = (List<Map<String,Object>>)tasksListJson.get("root");

            for (Map<String,Object> obj : list) {
                
               float count1 = Float.parseFloat(obj.get("count").toString());
                int count=(int)count1;
                String NameObj=obj.get("product").toString();
                MapLikes.put(NameObj, count);
                
            }
        }
        catch (IOException ex) 
        {
            System.out.println("error");
        }
        return MapLikes;
    }
    public Form getAllMerchLikes(){
        String url = Statics.BASE_URL+"/ratingmobile";
         //Map<String,Integer>Likes=new HashMap<>();
        ConnectionRequest conn = new ConnectionRequest();
        conn.setUrl(url);
        conn.setPost(false);
        Form hi = new Form("Hi World", BoxLayout.y());
        Style s = UIManager.getInstance().getComponentStyle("TitleCommand");
FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s);
//hi.getToolbar().addCommandToSideMenu("addtofavourites", icon, (e) ->this.AddItemstolist(selectedproducts));        
hi.getToolbar().addCommandToSideMenu("view my list of Favourite products", icon,e->showfavourites());
hi.getToolbar().addCommandToSideMenu("view products by likes", icon, (e) -> Log.p("Clicked"));
        conn.addResponseListener((NetworkEvent evt) -> {
            String response = new String(conn.getResponseData());
            Likes=parseMerchLikes(response);
            //System.out.println(response);
            for (Map.Entry<String, Integer> entry :    Likes.entrySet()) {
    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
    Label l = new Label();
    l.setText(entry.getKey()+"------>"+entry.getValue()+"\n");
    hi.add(l);   
}
        });
        
        System.out.println("Likes=>"+Likes);
        NetworkManager.getInstance().addToQueueAndWait(conn);
        return hi;
    }
    public void showfavourites()
    {
       FavsServices1 f1=new FavsServices1();
       f1.getAllTasks().show();
    }
    public void  setactiveuser()
    {
      String url = Statics.BASE_URL + "/setactiveuser/" + activuser;
                
                 
        req.setUrl(url);
      
        try{
        NetworkManager.getInstance().addToQueueAndWait(req);
        }
        catch(Exception e)
        {
            System.out.println("error");
        } 
        
    }
   
    public void addtofavourites(int i,int j)
    {
      String url = Statics.BASE_URL + "/addtofavsmobile/" + i + "/" + j;
         ConnectionRequest conn = new ConnectionRequest();        
                 //création de l'URL
        conn.setUrl(url);// Insertion de l'URL de notre demande de connexion
        conn.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
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
    public int Searchuserloginform(String email,String password)
    {
            String url = Statics.BASE_URL + "/find/"+email+"/"+password;
                 //création de l'URL
        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
       
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                 req.getResponseData().toString(); //Code HTTP 200 OK
                req.removeResponseListener(this); 
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        String response = new String(req.getResponseData());
        return Integer.parseInt(response);
    }
public ArrayList<Product> parseTasks(String jsonText) 
    {
        try {
            tasks=new ArrayList<>();
             Map<String,Object> tasksListJson;
                     tasksListJson = new HashMap<>();
             List<Map<String,Object>> list=new ArrayList<>();
            JSONParser j = new JSONParser();
// Instanciation d'un objet JSONParser permettant le parsing du résultat json
            tasksListJson  =j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            System.out.println("mapsize=>"+tasksListJson.size());
            list = (List<Map<String,Object>>)tasksListJson.get("root");

            for (Map<String,Object> obj : list) {
                Product p = new Product();
                float id = Float.parseFloat(obj.get("productId").toString());
                p.setProduct_id((int)id);
                float price = Float.parseFloat(obj.get("price").toString());
                p.setPrice((double)price);
                p.setProduct_name(obj.get("productName").toString());
                p.setCategory(obj.get("category").toString());
                float quantity = Float.parseFloat(obj.get("quantity").toString());
                p.setQuantity((int)quantity);
                float Tid = Float.parseFloat(obj.get("teamId").toString());
                p.setTeam_id((int)Tid);
                p.setImg(obj.get("img").toString());
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
public void pdf(int activuser)
{
    Form hi = new Form("PDF Viewer", BoxLayout.y());
Button devGuide = new Button("Show PDF");
devGuide.addActionListener(e -> {
    FileSystemStorage fs = FileSystemStorage.getInstance();
    String fileName = "file:///C:/wamp641/www/Products_management_GD/public/FavsPdf.pdf";
    if(!fs.exists(fileName)) {
        Util.downloadUrlToFile("http://127.0.0.1:8000/pdf/"+activuser, fileName, true);
    }
    Display.getInstance().execute(fileName);
});
hi.add(devGuide);

hi.show();
}

public Form getAllTasks(){
        System.out.println("user logged in="+activuser);
        String url = Statics.BASE_URL+"/listGroupeJson";
        tasks=new ArrayList<>();
        req.setUrl(url);
        req.setPost(false);
        CartServices CS=new CartServices();
        Form hi = new Form("Hi World", BoxLayout.y());
        Style s = UIManager.getInstance().getComponentStyle("TitleCommand");
FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_WARNING, s);
//hi.getToolbar().addCommandToSideMenu("addtofavourites", icon, (e) ->this.AddItemstolist(selectedproducts));        
hi.getToolbar().addCommandToSideMenu("view my list of Favourite products", icon,e->showfavourites());
hi.getToolbar().addCommandToSideMenu("view products by likes", icon, (e) -> showMerchLikes());
hi.getToolbar().addCommandToSideMenu("view my cart", icon,e->new Cart().show());
hi.getToolbar().addCommandToSideMenu("view games", icon,e->new GamesForm());
hi.getToolbar().addCommandToSideMenu("edit my profile", icon,e->new EditUserForm().show());
hi.getToolbar().addCommandToSideMenu("pdf", icon,e->pdf(activuser));

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
       Button B =new Button("add product to favourites");
        Button B1 =new Button("add to cart");
        System.out.println(activuser);
        //B.addActionListener(e->this.addtofavourites(activuser, p.getProduct_id()));
       B.addActionListener((ActionListener) (ActionEvent evt) -> {
         addtofavourites(activuser, p.getProduct_id());
        });
       B1.addActionListener((ActionListener) (ActionEvent evt) -> {
           CartServices Cs=new CartServices();
           Cs.addItem(p.getProduct_id(), activuser);
         
        });
       cell.addAll(B,B1);
       
      
       // cell.add(background);
        return cell;
    }
    
    
}
