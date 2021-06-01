package Main;

import Model.*;
import ViewController.MainFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Javadoc is located in a folder with the project folder labeled Javadoc.
 *
 * main method starts application
 */
public class InventoryApplication extends Application{

    public static void main(String[] args){
        launch(args);
    }

    /**
     * start method sets stage for start up scene
     */
    @Override
    public void start(Stage stage) throws Exception{
        Inv inv=new Inv();
        addTestData(inv);

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/ViewController/MainForm.fxml"));
        MainFormController controller=new MainFormController(inv);
        loader.setController(controller);
        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    void addTestData(Inv inv){
        //Add InHouse Parts
        Part a1=new InHouse(1,"Part A1",2.99,10,5,100,101);
        Part b=new InHouse(2,"Part B",3.99,9,5,100,102);
        inv.addPart(a1);
        inv.addPart(b);

        //Add OutSourced Parts
        Part o1=new OutSource(6,"Part O1",2.99,10,5,100,"ACME Co.");
        Part p=new OutSource(7,"Part P",3.99,9,5,100,"ACME Co.");
        inv.addPart(o1);
        inv.addPart(p);
        //Add allProducts
        Product prod1=new Product(100,"Product 1",9.99,20,5,100);
        prod1.addRelPart(a1);
        prod1.addRelPart(o1);
        inv.addProd(prod1);
        Product prod2=new Product(200,"Product 2",9.99,29,5,100);
        prod2.addRelPart(p);
        inv.addProd(prod2);
        Product prod3=new Product(300,"Product 3",9.99,30,5,100);
        prod3.addRelPart(b);
        inv.addProd(prod3);

    }

}


