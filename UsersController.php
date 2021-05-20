<?php

namespace App\Controller;

use App\Entity\Users;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use MercurySeries\FlashyBundle\FlashyNotifier;
use Gregwar\CaptchaBundle\Type\CaptchaType;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Form\Extension\Core\Type\FormType;
use Symfony\Component\Form\SubmitButton;
use Symfony\Component\HttpFoundation\File\Exception\FileNotFoundException;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Knp\Component\Pager\PaginatorInterface;
use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\Encoding\Encoding;
use Endroid\QrCode\ErrorCorrectionLevel\ErrorCorrectionLevelHigh;
use Endroid\QrCode\Label\Alignment\LabelAlignmentCenter;
use Endroid\QrCode\Label\Font\NotoSans;
use Endroid\QrCode\RoundBlockSizeMode\RoundBlockSizeModeMargin;
use Endroid\QrCode\Writer\PngWriter;
use Symfony\Component\Serializer\SerializerInterface;

class UsersController extends AbstractController
{
    /**
     * @Route("/users", name="users")
     */
    public function index(): Response
    {
        return $this->render('users/singup.html.twig', [
            'controller_name' => 'UsersController',
        ]);
    }
    /**
     * @Route("/userslist", name="users_list")
     */
    public function afficher(Request $request ,PaginatorInterface $paginator)
    {
        $allusers=$this->getDoctrine()->getRepository(Users::class)->findAll();
        // Paginate the results of the query
        $usersView= $paginator->paginate(
        // Doctrine Query, not results
            $allusers,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            3
        );

        return $this->render('users/afficher.html.twig', [
            'usersView' =>$usersView
        ]);
    }

    /**
     * @Route("/usersadd", name="users_add")
     */
    public function ajouterUser(Request $req){

        $user= new Users();
        $form= $this->createFormBuilder($user)
            ->add('User_name',TextType::class)
            ->add('User_lastname',TextType::class)
            ->add('User_email',TextType::class)
            ->add('User_phone',TextType::class)
            ->add('User_password',PasswordType::class)
            ->add('User_photo',FileType::class)
            ->add('User_gender',ChoiceType::class,['choices'=>[  'Female'=>'female', 'Male'=>'male']])
            ->add('Ajouter',SubmitType::class)
            ->add('captcha', CaptchaType::class)
            ->getForm();
        $form->handleRequest($req);
        if( $form->isSubmitted() && $form->isValid()){
            $user->setUserRole(0);
            $file=$user->getUserPhoto();
            $fileName=md5(uniqid()).'.'.$file->guessExtension();
            $user=$form->getData();
            $user->setUserPhoto($fileName);
            try{
                $file->move(
                    $this->getParameter('UserImage_directory'),$fileName
                );
            }
            catch(FileNotFoundException $e){}
            $em= $this->getDoctrine()->getManager();

            $em->persist($user);
            $em->flush();
            return $this->redirectToRoute('users_list');
        }
        return $this->render('users/ajouter.html.twig',['form' => $form->createView()]);

    }
    /**
     * @Route("/loginn", name="loginn")
     */
    public function login()
    {

        return $this->render('users/loginn.html.twig', [
            'controller_name' => 'UsersController',]);
    }
    /**
     * @Route("/showactivuser", name="activ_user", methods={"GET","POST"})
     */
    public function getactivuser()
    {
        $e= $_POST['Email'];
        $p= $_POST['password'];
        $em= $this->getDoctrine()->getManager();
        $query1 = $em->createQuery('select max(u.userConnected)+1 as cn from App\Entity\Users u  ');

        $result1 = $query1->getResult();
        $connected=$result1;
        $cn=-1;
        foreach ($connected as $c)
        {
            $cn=$c['cn'];
            break;

        }
        $query = $em->createQuery('
        update App\Entity\Users u
        set u.userConnected = :_conn
        where u.User_email=:_mail 
        and
        u.User_password=:_pass
        ');
        $query->setParameter(":_conn", $cn[0]);
        $query->setParameter(":_mail", $e);
        $query->setParameter(":_pass", $p);
        $query->getResult();
        $query1 = $em->createQuery('select max(u.userConnected) as cn from App\Entity\Users u 
         ');

        $result1 = $query1->getResult();
        $connected=$result1;
        $cn1=0;
        foreach ($connected as $c)
        {
            $cn1=$c['cn'];
            break;

        }
        $query = $em->createQuery('select u.User_id as ID  from App\Entity\Users u 
         where u.userConnected = :_conn and u.User_email=:_mail 
        and
        u.User_password=:_pass ');
        $query->setParameter(":_conn", $cn1);
        $query->setParameter(":_mail", $e);
        $query->setParameter(":_pass", $p);
        $result = $query->getResult();
        $id=0;
        foreach ($result as $r)
        {

            $id=$r['ID'];
            break;

        }
       // return $this->redirectToRoute('faidvs');
        // return $c;
        return $this->render('users/a.html.twig', [
            'controller_name' => 'UsersController','e'=>$e,'p'=>$p,'id'=>$id
        ]);
    }
    /**
     * @Route("/signup", name="signup")
     */
    public function singup(Request $req ,FlashyNotifier $flashy){

        $user= new Users();
        $form= $this->createFormBuilder($user)
            ->add('User_name',TextType::class)
            ->add('User_lastname',TextType::class)
            ->add('User_email',TextType::class)
            ->add('User_phone',TextType::class)
            ->add('User_password',PasswordType::class)
            ->add('User_photo',FileType::class)
            ->add('User_gender',ChoiceType::class,['choices'=>[  'Female'=>'female', 'Male'=>'male']])
            ->add('Ajouter',SubmitType::class)
            ->add('captcha', CaptchaType::class)
            ->getForm();
        $form->handleRequest($req);
        if( $form->isSubmitted() && $form->isValid()){
            $user->setUserRole(0);
            $file=$user->getUserPhoto();
            $fileName=md5(uniqid()).'.'.$file->guessExtension();
            $user=$form->getData();
            $user->setUserPhoto($fileName);
            try{
                $file->move(
                    $this->getParameter('UserImage_directory'),$fileName
                );
            }
            catch(FileNotFoundException $e){}
            $em= $this->getDoctrine()->getManager();

            $em->persist($user);
            $em->flush();
            $result = Builder::create()
                ->writer(new PngWriter())
                ->writerOptions([])
                ->data('User_name: '.$user->getUserName())
                ->encoding(new Encoding('UTF-8'))
                ->errorCorrectionLevel(new ErrorCorrectionLevelHigh())
                ->size(300)
                ->margin(10)
                ->roundBlockSizeMode(new RoundBlockSizeModeMargin())
                ->labelText('Scanner le code QR')
                ->labelFont(new NotoSans(20))
                ->labelAlignment(new LabelAlignmentCenter())
                ->build();
            header('Content-Type: '.$result->getMimeType());

            $result->saveToFile($this->getParameter('UserImage_directory').'/'.$user->getUserName().'.png');
            $flashy->success('utilisateur ', 'http://your-awesome-link.com');
            return $this->redirectToRoute('signup');

        }
        return $this->render('users/singup.html.twig',['form' => $form->createView()]);


    }
    /**
     * @Route("/delete/{id}", name="users_delete")
     */
    public function deleteUser($id){
        $em=$this->getDoctrine()->getManager();
        $singleUser= $em->getRepository(Users::class)->find($id);
        $em->remove($singleUser);
        $em->flush();
        return $this->redirectToRoute('users_list');
    }
    /**
     * @Route("/modifier/{id}", name="users_edit")
     */
    public function editUser(Request $req,$id){
        $user= $this->getDoctrine()->getRepository(Users::class)->find($id);
        $form= $this->createFormBuilder($user)
            ->add('User_name',TextType::class)
            ->add('User_lastname',TextType::class)
            ->add('User_email',TextType::class)
            ->add('User_phone',TextType::class)
            ->add('User_password',PasswordType::class)
            ->add('User_gender',ChoiceType::class,['choices'=>[  'Female'=>'female', 'Male'=>'male']])
            ->add('modifier',SubmitType::class)
            ->getForm();
        $form->handleRequest($req);
        if( $form->isSubmitted() && $form->isValid()){

            $em= $this->getDoctrine()->getManager();

            $em->merge($user);
            $em->flush();
            return $this->redirectToRoute('users_list');
        }
        return $this->render('users/modifier.html.twig',['form' => $form->createView()]);


    }
    /**
     * @Route("/find/{email}/{password}", name="find")
     */
    public function finduserbyEmailandpassword($email,$password,SerializerInterface $serializer)
    {
        $em= $this->getDoctrine()->getManager();
        $query = $em->createQuery('select u.userId as ID  from App\Entity\Users u 
         where   u.userEmail=:_mail 
        and
        u.userPassword=:_pass ');

        $query->setParameter(":_mail", $email);
        $query->setParameter(":_pass", $password);
        $result = $query->getResult();
        $id=-1;
        foreach ($result as $r)
        {

            $id=$r['ID'];
            break;

        }
        $formatted = $serializer->normalize($id);
        return new JsonResponse($formatted);
    }

    /**
     * @Route("/addusermobile/{User_name}/{User_lastname}/{User_email}/{User_phone}/{User_password}/{User_photo}/{User_gender}/{deliveryaddress}", name="addmobile")
     */
    public function addusertomobile($User_name,$User_lastname,$User_email,$User_password,$User_gender,$User_photo,$User_phone,$deliveryaddress)
    {
        /*u.getName()+"/"+u.getLastname()
        +"/"+u.getEmail()+"/"+u.getPassword()+"/"
        +u.getGender()+"/"+u.getPhoto()+"/"
        +u.getPhone()+"/"+u.getBirthday();*/
        $Users = new Users();
        $Users->setUserName($User_name);
        $Users->setUserLastname($User_lastname);
        $Users->setUserEmail($User_email);
        $Users->setUserPhone($User_phone);
        $Users->setUserPassword($User_password);
        $Users->setUserPhoto($User_photo);
        $Users->setUserGender($User_gender);
        $Users->setUserConnected(0);
        $Users->setAddresslivraison($deliveryaddress);

        // $Users->setBirthday($birthday);
        $em=$this->getDoctrine()->getManager();
        $em->persist($Users);
        $em->flush();
        return new Response('Groupe added');

    }
    /**
     * @Route("/updatetomobile/{User_id}/{User_name}/{User_lastname}/{User_email}/{User_phone}/{User_password}/{User_photo}/{User_gender}", name="updmobile")
     */
    public function updateJson(Request $request,$User_id,$User_name,$User_lastname,$User_email,$User_password,$User_gender,$User_photo,$User_phone)
    {
        $content=$request->getContent();
        $em=$this->getDoctrine()->getManager();
        $repository=$this->getDoctrine()->getRepository(Users::class);
        $Users=$repository->find($User_id);
        $em->remove($Users);
        //  $Products->setProductId($id);
        $Users->setUserName($User_name);
        $Users->setUserLastname($User_lastname);
        $Users->setUserEmail($User_email);
        $Users->setUserPhone($User_phone);
        $Users->setUserPassword($User_password);
        $Users->setUserPhoto($User_photo);
        $Users->setUserGender($User_gender);
        $em->persist($Users);
        $em->flush();
        return new Response('User Modifier');
    }



}
