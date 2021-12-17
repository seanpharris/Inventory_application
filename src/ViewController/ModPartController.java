package ViewController;

import Model.InHouse;
import Model.Inv;
import Model.OutSource;
import Model.Part;
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
 * modify part controller
 */
public class ModPartController implements Initializable{

    @FXML
    public TextField priceTxt;
    public ToggleGroup source;
    Inv inv;
    Part part;

    @FXML
    private RadioButton inHouseButton;
    @FXML
    private RadioButton outSourceButton;
    @FXML
    private Label compTitle;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField stockTxt;
    @FXML
    private TextField minTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField compTxt;

    public ModPartController(Inv inv,Part part){
        this.inv=inv;
        this.part=part;
    }

    /**
     * in house button - machine id as label and text field
     *
     * @param event
     */
    @FXML
    private void selInHouseButton(MouseEvent event){
        compTitle.setText("Machine ID");
    }

    /**
     * out sourced button changes it to company name as label and text field
     *
     * @param event
     */
    @FXML
    private void selOutSourceButton(MouseEvent event){
        compTitle.setText("Company Name");

    }

    /**
     * initialize is the first actions to happen once scene is open.
     * that being
     * -part selected information will be set to textfields from inhouse/outsourced pending upon part
     * -radio button will be selected for kind of part
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url,ResourceBundle rb){
        /**
         * part instanceof InHouse sets textfields and radio button for a inhouse part
         */
        if(part instanceof InHouse){
            InHouse inPart=(InHouse) part;
            inHouseButton.setSelected(true);
            compTitle.setText("Machine ID");
            nameTxt.setText(inPart.getName());
            idTxt.setText((Integer.toString(inPart.getId())));
            stockTxt.setText((Integer.toString(inPart.getStock())));
            priceTxt.setText((Double.toString(inPart.getPrice())));
            minTxt.setText((Integer.toString(inPart.getMin())));
            maxTxt.setText((Integer.toString(inPart.getMax())));
            compTxt.setText(Integer.toString(inPart.getMachID()));
        }
        /**
         * part instanceof InHouse sets textfields and radio button for a outsourced part
         */
        if(part instanceof OutSource){
            OutSource outPart=(OutSource) part;
            outSourceButton.setSelected(true);
            compTitle.setText("Company Name");
            nameTxt.setText(outPart.getName());
            idTxt.setText((Integer.toString(outPart.getId())));
            stockTxt.setText((Integer.toString(outPart.getStock())));
            priceTxt.setText((Double.toString(outPart.getPrice())));
            minTxt.setText((Integer.toString(outPart.getMin())));
            maxTxt.setText((Integer.toString(outPart.getMax())));
            compTxt.setText(outPart.getCompName());
        }

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
        if(result.isPresent()&&result.get()==ButtonType.OK) try{
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

    /**
     * saveModPart is a mouse event. Once "save" is clicked it will go through a series of input validations to ensure it is the correct value types. After saved, the MainForm window will appear.
     *
     * @param event
     */
    @FXML
    private void saveModPart(MouseEvent event){
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
        if(inHouseButton.isSelected()&part instanceof InHouse||inHouseButton.isSelected()&part instanceof OutSource){
            inv.updatePart(new InHouse(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()),Integer.parseInt(compTxt.getText())));

        }
        if(outSourceButton.isSelected()&part instanceof OutSource||outSourceButton.isSelected()&part instanceof InHouse){
            inv.updatePart(new OutSource(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()),compTxt.getText()));
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





