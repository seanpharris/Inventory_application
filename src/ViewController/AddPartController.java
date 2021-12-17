package ViewController;


import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * add part controller for gui
 */
public class AddPartController implements Initializable{

    @FXML
    private TextField priceTxt;
    @FXML
    private RadioButton inHouseButton;
    @FXML
    private RadioButton outSourceButton;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField stockTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField compTxt;
    @FXML
    private Label compTitle;
    @FXML
    private TextField minTxt;
    private ObservableList<Part> allParts=FXCollections.observableArrayList();


    public AddPartController(Inv inv){
        this.inv=inv;
    }

    Inv inv;

    /**
     * in house button - machine id as label and text field
     *
     * @param event
     */
    @FXML
    private void inHouseButton(MouseEvent event){
        compTitle.setText("Machine ID");
        compTxt.setText(" ");
    }

    /**
     * out sourced button changes it to company name as label and text field
     *
     * @param event
     */
    @FXML
    private void outSourceButton(MouseEvent event){
        compTitle.setText("Company Name");
        compTxt.setText(" ");
    }

    /**
     * initialize is the first actions to happen once scene is open.
     * that being
     * -an Id created for a new part
     * -the in house radio button will be selected from beginning
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url,ResourceBundle rb){
        inHouseButton.setSelected(true);
        idTxt.setText(createPartID());
    }

    /**
     * PartIDCounter is the starting number of 1 and increase by 1 for every new part.
     */
    private static long partIDCounter=1;

    /**
     * createPartID assigns a unique id to new part
     */
    public static synchronized String createPartID(){
        return String.valueOf(partIDCounter++);
    }


    /**
     * goes back to main form once cancel is clicked and user will be prompted for cancellation of adding a part.
     *
     * @param event
     */
    @FXML
    private void backToMainForm(MouseEvent event){
        boolean cancel;
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Click ok to confirm");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK){
            try{
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/viewController/MainForm.fxml"));
                MainFormController controller=new MainFormController(inv);

                loader.setController(controller);
                Parent root=loader.load();
                Scene scene=new Scene(root);
                Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * addPartSave is a mouse event. Once "save" is clicked it will go through a series of input validations to ensure it is the correct value types. After saved, the MainForm window will appear.
     *
     * @param event
     */
    @FXML
    private void addPartSave(MouseEvent event){
        if(nameTxt.getText().isEmpty()||stockTxt.getText().isEmpty()||priceTxt.getText().isEmpty()||minTxt.getText().isEmpty()||maxTxt.getText().isEmpty()||compTxt.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Missing information");
            alert.showAndWait();
            return;
        }
        if(!nameTxt.getText().matches("[A-Z a-z,0-9.!?-]*")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Name requires Alphabetical or numerical values");
            alert.showAndWait();
            return;
        }
        if(!stockTxt.getText().matches("[0-9]*")||!priceTxt.getText().matches("[0-9 .]*")||(!minTxt.getText().matches("[0-9]*"))||(!maxTxt.getText().matches("[0-9]*"))){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Inv, Price, Max, Min requires numerical values");
            alert.showAndWait();
            return;
        }
        if(Integer.parseInt(stockTxt.getText())<Integer.parseInt(minTxt.getText())||(Integer.parseInt(stockTxt.getText())>Integer.parseInt(maxTxt.getText()))){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("inventory cannot have less then min or more than max");
            alert.showAndWait();
            return;
        }
        if(Integer.parseInt(minTxt.getText())<=0||Integer.parseInt(minTxt.getText())>=Integer.parseInt(maxTxt.getText())){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("min cannot be less than zero and must be less than max");
            alert.showAndWait();
            return;
        }
        if(inHouseButton.isSelected()&&!compTxt.getText().matches("[0-9]*")){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("MachineID must be numerical");
            alert.showAndWait();
            return;
        }
        if(outSourceButton.isSelected()&&compTxt.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Fill in all fields using valid values");
            alert.showAndWait();
            return;
        }
        if(inHouseButton.isSelected()){
            inv.addPart(new InHouse(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()),(Integer.parseInt(compTxt.getText()))));

        }
        if(outSourceButton.isSelected()){
            inv.addPart(new OutSource(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()),compTxt.getText()));
        }
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/ViewController/MainForm.fxml"));
            MainFormController controller=new MainFormController(inv);

            loader.setController(controller);
            Parent root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}



