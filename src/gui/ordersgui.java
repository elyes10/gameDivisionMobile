/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import services.CartServices;
import services.OrderServices;

/**
 *
 * @author HP OMEN
 */
public class ordersgui extends Form{

    public ordersgui() {
         //form init
        this.setTitle("Orders");
        this.setLayout(BoxLayout.y());
        //widgets
        SpanLabel sp=new SpanLabel();
         Button ordersbtn = new Button("Cart");
          
      
         // ordersbtn.addActionListener(e -> new Cart().show());
        //add to form
       Form f =OrderServices.getInstance().getAllorders();
          //  this.add(sp);
          this.add(ordersbtn);
          this.add(f);
         
        
       
    }
    
}
