/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import services.PrdServices;

/**
 *
 * @author LENOVO
 */
public class DeleteForm extends Form{


   public DeleteForm(Form previous) {
        setTitle("Delete Merch");
        setLayout(BoxLayout.y());
        TextField t1 = new TextField("","Product_ID");
         Button btnValide = new Button("Delete Merch");
        addAll(t1,btnValide);
        btnValide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PrdServices pr=new PrdServices();
                pr.deleteItem(Integer.parseInt(t1.getText()));
            }
        });
           
     getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());   
    }
    
}
