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
import com.entities.Product;
import services.PrdServices;

/**
 *
 * @author LENOVO
 */
public class EditForm extends Form {
        

    EditForm(Form previous) {
        setTitle("Add Merch");
        setLayout(BoxLayout.y());
        TextField t0 = new TextField("","Product_ID");
        TextField t1 = new TextField("","Product_Name");
        TextField t2= new TextField("", "Team_id");
        TextField t3= new TextField("", "Price");
        TextField t4= new TextField("", "Category");
        TextField t5= new TextField("", "quantity");
        TextField t6= new TextField("", "img");
        Button b=new Button("Edit Merch");
        addAll(t0,t1,t2,t3,t4,t5,t6,b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               Product p=new Product(t1.getText(),Integer.parseInt(t2.getText()),
               Double.parseDouble(t3.getText()),t4.getText(),Integer.parseInt(t5.getText()),
               t6.getText());
               PrdServices pr =new PrdServices();
               pr.editItem(Integer.parseInt(t0.getText()),p);
            }
         });
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());
        
    }
    
}
