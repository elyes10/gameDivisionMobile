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
import java.util.ArrayList;
import services.CartServices;

/**
 *
 * @author HP OMEN
 */
public class Cart extends Form{

    public Cart() {
        //form init
        this.setTitle("Cart");
        this.setLayout(BoxLayout.y());
        //widgets
        SpanLabel sp=new SpanLabel();
         Button ordersbtn = new Button("Orders History");
         //ArrayList m=CartServices.getInstance().getcarts();
      // sp.setText(m.toString());
          ordersbtn.addActionListener(e -> new orders().show());
        //add to form
       Form f =CartServices.getInstance().getAllcarts();
          //  this.add(sp);
          this.add(ordersbtn);
          this.add(f);
        
    }
    
}