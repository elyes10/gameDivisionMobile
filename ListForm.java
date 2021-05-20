/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import services.FavsServices;
import services.FavsServices1;
import services.PrdServices;



/**
 *
 * @author LENOVO
 */
public class ListForm extends Form{

    ListForm() 
    {
        FavsServices fS=new FavsServices();
               FavsServices1 fS1=new FavsServices1();
              
               fS.setactiveuserId(fS1.getactiveuserfromdb());
      
              
               System.out.println(fS1.getactiveuserfromdb());
               fS.getAllTasks().show();
         // getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());
    }

   

   
   
}
