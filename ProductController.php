<?php

namespace App\Controller;

use App\Entity\Products;
use App\Form\ProductFormType;
use App\Form\FormNameType;

use App\Repository\ProductRepository;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\QueryBuilder;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\HttpFoundation\File\Exception\FileNotFoundException;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\HttpFoundation\Request;
use assets\images;
use Symfony\Component\Asset\Package;
use Symfony\Component\Asset\VersionStrategy\EmptyVersionStrategy;
use Symfony\Component\Mailer\MailerInterface;
use Omines\DataTablesBundle\Adapter\Doctrine\ORMAdapter;
use Omines\DataTablesBundle\Adapter\ArrayAdapter;
use Omines\DataTablesBundle\Column\TextColumn;
use Omines\DataTablesBundle\DataTableFactory;
use Omines\DataTablesBundle\Adapter\Doctrine\ORMAdapterEvents;
use MercurySeries\FlashyBundle\FlashyNotifier;
use Symfony\Component\Mime\Email;

use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\SerializerInterface;


class ProductController extends AbstractController

{

    /**
     * @Route("/product")
     */

    public function index(Request $request)
    {

        $repository=$this->getDoctrine()->getRepository(Products::class);
        $em= $this->getDoctrine()->getManager();




        //return $this->render('list.html.twig', ['datatable' => $table]);

        return $this->render('back.html.twig', [
            'controller_name' => 'ProductController'
        ]);
    }


    /**
     * @Route("/displayproducts", name="display")

     */
    public function list(Request $request,FlashyNotifier $flashy): Response
    {
        //$flashy->success('added succesfully');
        $repository=$this->getDoctrine()->getRepository(Products::class);
        $products=$repository->findAll();
        $product= new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted())
        {



            $product=$form->getData();
            $em= $this->getDoctrine()->getManager();
            $cover = $form->get('img')->getData();

            if ($cover) {
                $originalFilename = pathinfo($cover->getClientOriginalName(), PATHINFO_FILENAME);
                // this is needed to safely include the file name as part of the URL
                $newFilename = $originalFilename.uniqid().'.'.$cover->guessExtension();

                // Move the file to the directory where brochures are stored
                $cover->move(
                    $this->getParameter('upload_directory') ,
                    $newFilename
                );
                // updates the 'brochureFilename' property to store the PDF file name
                // instead of its contents
                $product->setImg($newFilename);

            }
            $em->persist($product);
                $em->flush();
            $this->addFlash('success', '               product Created!');
            return $this->redirectToRoute('display');

        }
        return $this->render('product/display.html.twig',
            ['controller_name' => 'ProductController','form'=>$form->createView(),'products'=> $products]);

    }
    /**
     * @Route("/order3", name="order3")
     */
    public function OrderById(Request $request)
    {
        $em= $this->getDoctrine()->getManager();
        $query=$em->createQuery('select p from App\Entity\Products p order by p.productId');
        $products=$query->getResult();
        $em= $this->getDoctrine()->getManager();
        $qb = $em->createQueryBuilder();
        $result =
            $qb->select('count(p.productId)')
                ->addSelect('p.category')
                ->from('App\Entity\Products','p')
                ->groupBy('p.category')
        ;
        $query = $result->getQuery();
        $product= new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted())
        {



            $product=$form->getData();
            $em= $this->getDoctrine()->getManager();



            $em->persist($product);
            $em->flush();
            return $this->redirectToRoute('display');

        }
        $result1 = $query->getResult();
        return $this->render('product/display.html.twig',
            ['controller_name' => 'ProductController','products'=> $products,'result'=>$result1,'form'=>$form->createView()]);

    }
    /**
     * @Route("/stats", name="stats")
     */
    public function gotostats()
    {
        $countbycatg=$this->countbycategory();
        foreach ($countbycatg as $c)
        {
            $categories[]=$c['category'];
            $number[]=$c['count'];
        }
        $countbyteam=$this->countbyTeam();
        foreach ($countbyteam as $cbt)
        {
            $teams[]=$cbt['team'];
            $number1[]=$cbt['count'];
        }
        $countbyFav=$this->countbyFavs();
        foreach ($countbyFav as $cbf)
        {
            $favs[]=$cbf['User'];
            $number2[]=$cbf['count'];
        }
        $countbyLikes=$this->countbyLikes();
        foreach ($countbyLikes as $cbl)
        {
            $products[]=$cbl['product'];
            $number3[]=$cbl['count'];
        }
        return $this->render
        ('product/stats.html.twig',
            ['controller_name' => 'ProductController',
                'categories'=>json_encode($categories),
                'nb'=>json_encode($number),
                'teams'=>json_encode($teams),
                'number1'=>json_encode($number1),
                'favs'=>json_encode($favs),
                'number2'=>json_encode($number2),
                'number3'=>json_encode($number3),
                'products'=>json_encode($products)
            ]
        );
    }
    /**
     * @Route("/stats2", name="stats2")
     */

    public function countbyTeam()
    {
        //$repository=$this->getDoctrine()->getRepository(Products::class);


        $em= $this->getDoctrine()->getManager();

        $query = $em->createQuery('
        select
        count(p.productId) as count,
        t.teamName as team
        from App\Entity\Products p
        LEFT JOIN App\Entity\Teams t WITH p.teamId=t.teamId
        group by p.teamId
        
        ');
        $result1 = $query->getResult();
        return $result1;
        /*return $this->render('product/tryout.html.twig',
            ['controller_name' => 'ProductController',
                'result1'=>$result1,
            ]);*/
    }
    /**
     * @Route("/stats3", name="stats3")
     */
    public function countbyFavs()
    {
        $em= $this->getDoctrine()->getManager();
        $query = $em->createQuery('
        select
        count(f.productId) as count,
        u.userName as User
        from App\Entity\Favourites f
        LEFT JOIN App\Entity\Users u WITH u.userId=f.userId
        group by f.userId 
        ');
        $result1 = $query->getResult();
        return $result1;
       /* return $this->render('product/tryout.html.twig',
            ['controller_name' => 'ProductController',
                'result1'=>$result1,
            ]);*/
    }
    public function countbycategory()
    {
        $repository=$this->getDoctrine()->getRepository(Products::class);

        $em= $this->getDoctrine()->getManager();
        $qb = $em->createQueryBuilder();
        $result =
            $qb->select('count(p.productId) as count')
                ->addSelect('p.category as category')
            ->from('App\Entity\Products','p')
            ->groupBy('p.category')
            ;
        $query = $result->getQuery();
        $result1 = $query->getResult();
        return $result1;

    }
    public function countbyLikes()
    {
        //$repository=$this->getDoctrine()->getRepository(Products::class);
        $em= $this->getDoctrine()->getManager();

        $query = $em->createQuery
        ('
        select
        count(f.productId) as count,
        p.productName as product
        from App\Entity\Favourites f
        LEFT JOIN App\Entity\Products p WITH p.productId=f.productId
        group by f.productId
        ');

        $result1 = $query->getResult();
        return $result1;

    }
    /**
     * @Route("/order1", name="order1")
     */

    public function OrderByprice(Request $request)
    {
        $em= $this->getDoctrine()->getManager();
        $query=$em->createQuery('select p from App\Entity\Products p order by p.price');
        $products=$query->getResult();
        $em= $this->getDoctrine()->getManager();
        $qb = $em->createQueryBuilder();
        $result =
            $qb->select('count(p.productId)')
                ->addSelect('p.category')
                ->from('App\Entity\Products','p')
                ->groupBy('p.category')
        ;
        $query1 = $result->getQuery();
        $result1 = $query1->getResult();
        $product= new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted()) {


            $product = $form->getData();
            $em = $this->getDoctrine()->getManager();


            $em->persist($product);
            $em->flush();
            return $this->redirectToRoute('display');
        }
        return $this->render('product/display.html.twig',
            ['controller_name' => 'ProductController','products'=> $products,'result'=> $result1,'form'=>$form->createView(),'products'=> $products]);
    }
    /**
     * @Route("/order2", name="order2")
     */
    public function OrderByQuantity(Request $request)
    {
        $em= $this->getDoctrine()->getManager();
        $query=$em->createQuery('select p from App\Entity\Products p order by p.quantity');
        $products=$query->getResult();
        $em= $this->getDoctrine()->getManager();
        $qb = $em->createQueryBuilder();
        $result =
            $qb->select('count(p.productId)')
                ->addSelect('p.category')
                ->from('App\Entity\Products','p')
                ->groupBy('p.category')
        ;
        $query1 = $result->getQuery();
        $result1 = $query1->getResult();
        $product= new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted()) {


            $product = $form->getData();
            $em = $this->getDoctrine()->getManager();


            $em->persist($product);
            $em->flush();
            return $this->redirectToRoute('display');
        }
        return $this->render('product/display.html.twig',
            ['controller_name' => 'ProductController','products'=> $products,'result'=> $result1,'form'=>$form->createView(),'products'=> $products]);
    }
    /**
     * @Route("/displayproductcatg/{}", name="discatg")
     */
    public function DisplayByCategory(Request $request)
    {
        $category="hardware";
        $repository=$this->getDoctrine()->getRepository(Products::class);
        $products=$repository->findBy(['category' => $category]);
        $product=new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted())
        {

            $file=$product->getImg();
            $filename=md5(uniqid()).'.'.$file->guessExtension();
            $class=$form->getData();
            $em= $this->getDoctrine()->getManager();
            $product->setImg($filename);
            $em->persist($class);
            $em->flush();
            return $this->redirectToRoute('display');

        }
        return $this->render('product/displaybycatgh.html.twig',
            ['controller_name' => 'ProductController','form'=>$form->createView(),'products'=> $products]);
    }
    /**
     * @Route("/displayproductcatg2/{}", name="discatg2")
     */
    public function DisplayByCategory2(Request $request)
    {
        $category="clothes";
        $repository=$this->getDoctrine()->getRepository(Products::class);
        $products=$repository->findBy(['category' => $category]);
        $product=new Products();
        $form= $this->createForm(FormNameType::class,$product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);
        if($form->isSubmitted())
        {
            $file=$product->getImg();
            $filename=md5(uniqid()).'.'.$file->guessExtension();
            $class=$form->getData();
            $em= $this->getDoctrine()->getManager();
            $product->setImg($filename);
            $em->persist($class);
            $em->flush();
            return $this->redirectToRoute('display');
        }
        return $this->render('product/displaybycatgc.html.twig',
            ['controller_name' => 'ProductController','form'=>$form->createView(),'products'=> $products]);
    }
    /**
     * @Route("/add", name="add")
    */
    public function add(Request $request): Response
    {
        $package = new Package(new EmptyVersionStrategy());
        $product = new Products();
        $form = $this->createForm(FormNameType::class, $product);
        $form->add("add",SubmitType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $cover = $form->get('img')->getData();

            if ($cover) {
                $originalFilename = pathinfo($cover->getClientOriginalName(), PATHINFO_FILENAME);
                // this is needed to safely include the file name as part of the URL
                $newFilename = $originalFilename .'-'.uniqid().'.'.$cover->guessExtension();

                // Move the file to the directory where brochures are stored
                $cover->move(
                    $this->getParameter('upload_directory') ,
                    $newFilename
                );
                // updates the 'brochureFilename' property to store the PDF file name
                // instead of its contents
                $product->setImg($newFilename);
            }


            $entityManager->persist($product);
            $entityManager->flush();
            return $this->redirectToRoute('display');
        }
        return $this->render('product/add.html.twig',
            ['controller_name' => 'ProductController','form'=>$form->createView(),]);
    }
    /**
     * @Route("/delete/{id}", name="del")
     */
    public function delete($id): Response
    {   $repository=$this->getDoctrine()->getRepository(Products::class);
        $Products=$repository->find($id);
        $em= $this->getDoctrine()->getManager();
        $em->remove($Products);
        $em->flush();
        return $this->redirectToRoute('display');
    }
    /**
     * @Route("/update/{id}", name="upd")
     */
    public function update($id,Request $request,FlashyNotifier $flashy): Response
    {
        $repository = $this->getDoctrine()->getRepository(Products::class);
        $Products = $repository->find($id);
        //$em = $this->getDoctrine()->getManager();
        $form= $this->createFormBuilder($Products)
            ->add('productName')
            ->add('teamId')
            ->add('price')
            ->add('category',ChoiceType::class,['choices'=>[  'hardware'=>'hardware', 'clothes'=>'clothes','items'=>'items']])
            ->add('quantity')

            ->getForm();
        $form->add("update", SubmitType::class);
        $form->handleRequest( $request);
        if ($form->isSubmitted()) {
            $class=$form->getData();
            $em= $this->getDoctrine()->getManager();
            $em->remove($class);
            $em->persist($class);
            $em->flush();
            $this->addFlash('success', 'Product Updated!');
            return $this->redirectToRoute('display');
        }
        return $this->render('product/update.html.twig', [
            'controller_name' => 'ProductController','form'=>$form->createView(),
        ]);
    }
    /**
     * @Route ("/listGroupeJson", name="listGroupeJson"  )
     */
    public function getallproducts(ProductRepository $repo,SerializerInterface $serializer)
    {
        $productslist=$repo->findAll();
        $json=$serializer->serialize($productslist,'json',['groups'=>'Products']);
        $formatted = $serializer->normalize($productslist);
        return new JsonResponse($formatted);
    }

    /**
     * @Route("/addmobile/{name}/{tid}/{price}/{category}/{quantity}/{img}", name="addmobile" )
     */
    public function addtomobile($name,$tid,$price,$category,$quantity,$img)
    {
       $Product = new Products();
       $Product->setProductName($name);
       $Product->setTeamId($tid);
       $Product->setPrice($price);
       $Product->setCategory($category);
       $Product->setQuantity($quantity);
       $Product->setImg($img);
        $em=$this->getDoctrine()->getManager();
        $em->persist($Product);
        $em->flush();
        return new Response('Groupe added');

    }


    /**
     * @Route("/updatetomobile/{id}/{name}/{tid}/{price}/{category}/{quantity}/{img}", name="updmobile")
     */
    public function updateJson(Request $request,$id,$name,$tid,$price,$category,$quantity,$img)
    {
        $content=$request->getContent();
        $em=$this->getDoctrine()->getManager();
        $repository=$this->getDoctrine()->getRepository(Products::class);
        $Products=$repository->find($id);
        $em->remove($Products);
        //  $Products->setProductId($id);
        $Products->setProductName($name);
        $Products->setTeamId($tid);
        $Products->setPrice($price);
        $Products->setCategory($category);
        $Products->setQuantity($quantity);
        $Products->setImg($img);
        $em->persist($Products);
        $em->flush();
        return new Response('Groupe Modifier');
    }
    /**
     * @Route ("/DeleteJson/{id}", name="DeleteJson")
     */
    public function DeleteGroupeJson($id)
    {
        $em=$this->getDoctrine()->getManager();
        $repo=$em->getRepository(Products::class)->find($id);
        $em->remove($repo);
        $em->flush();
        return new Response('Group deleted');
    }


}
