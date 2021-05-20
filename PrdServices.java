
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;

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
public class PrdServices {
  
   public ArrayList<Product> tasks;
    
    public static PrdServices instance=null;
    public boolean resultOK;
    private ConnectionRequest req;

    public PrdServices() {
         req = new ConnectionRequest();
    }

    public static PrdServices getInstance() 
    {
        if (instance == null) 
        {
            instance = new PrdServices();
        }
        return instance;
    }
    public boolean deleteItem(int id) {
        String url = Statics.BASE_URL + "/DeleteJson/"+id;
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
        return resultOK;
    }
    public boolean editItem(int id,Product p) {
        String url = Statics.BASE_URL + "/updatetomobile/"+id+"/"+p.getProduct_name() + "/"
                + p.getTeam_id() + "/" + p.getPrice() + "/" +p.getCategory()+ "/" +p.getQuantity()+ "/" 
                +p.getImg();
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
        return resultOK;
    }
    
    public boolean addItem(Product p) {
        String url = Statics.BASE_URL + "/addmobile/" + p.getProduct_name() + "/"
                + p.getTeam_id() + "/" + p.getPrice() + "/" +p.getCategory()+ "/" +p.getQuantity()+ "/" 
               +p.getImg();
                ;
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
        return resultOK;
    }
    public ArrayList<Product> parseTasks(String jsonText) 
    {
        try {
            tasks=new ArrayList<>();
            JSONParser j = new JSONParser();
// Instanciation d'un objet JSONParser permettant le parsing du résultat json
            Map<String,Object> tasksListJson  =j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            System.out.println("mapsize=>"+tasksListJson.size());
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for (Object keys : tasksListJson.values())
{
   System.out.println("values->"+keys);
}
            System.out.println(list.size());
            //Parcourir la liste des tâches Json
            for(Map<String,Object> obj : list)
            {
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
         /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
        */
        return tasks;
        
    }

    public Form getAllTasks(){
        String url = Statics.BASE_URL+"/listGroupeJson";
        req.setUrl(url);
        req.setPost(false);
        Form hi = new Form("Hi World", BoxLayout.y());
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String response = new String(req.getResponseData());
                tasks = parseTasks(response);
                System.out.println(response);
                for (Product p : tasks) 
                {
                 hi.add(addItemcontainer(p));   
                }            // req.removeResponseListener(this);
            }
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
        //Button mailBtn = new Button(p.getMail());
        labelsCtn.addAll(name,price);
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(500, 500 , 0xffff0001), true);
URLImage background = URLImage.createToStorage(placeholder,"file:///C:/wamp641/www/Products_management_GD/public/uploads/images/"+p.getImg(),
        "file:///C:/wamp641/www/Products_management_GD/public/uploads/images/"+p.getImg());
cell.add(background);

        cell.add(labelsCtn);
       
      
       // cell.add(background);
        return cell;
    }
    
}
 
    

