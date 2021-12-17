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

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/ViewController/MainForm.fxml"));
        MainFormController controller=new MainFormController(inv);
        loader.setController(controller);
        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}


