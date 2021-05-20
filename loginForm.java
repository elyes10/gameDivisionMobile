/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import services.FavsServices;


/**
 *
 * @author LENOVO
 */
public class loginForm extends Form {
        public loginForm(){
            
        TextField t1 = new TextField("","email");
        TextField t2= new TextField("", "password");
        Button login=new Button("Login");
        Button SignUp=new Button("Sign Up");
        addAll(t1,t2,login,SignUp);
        login.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            FavsServices fs = new FavsServices();
            int x=fs.Searchuserloginform(t1.getText(),t2.getText());
            fs.setactiveuserId(x);
            
            if(x!=-1)
            {
               System.out.println("x***="+x);
               FavsServices fS=new FavsServices();
              // FavsServices1 fS1=new FavsServices1();
               fS.setactiveuserId(x);
             /*  fS1.setactiveuserId1(x);
                System.out.println("1-------------->"+fS1.activuser);*/
               fS.setactiveuser();
               fS.getAllTasks().show();

            }
            
          
         }
     });
        SignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               
                AddUserForm a=new AddUserForm();
                a.show();
            
            }
        });
        
        }
        
}
