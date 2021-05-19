/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import services.PrdServices;

/**
 *
 * @author LENOVO
 */
public class ListForm extends Form{

    ListForm(Form previous) 
    {
       PrdServices p=new PrdServices(); 
       p.getAllTasks().show();
          getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());
    }

    public ListForm() {
    }

   
   
}
