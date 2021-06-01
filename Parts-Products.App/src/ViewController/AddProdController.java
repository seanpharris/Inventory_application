package ViewController;

import Model.Inv;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * add product controller
 */
public class AddProdController implements Initializable{
    /**
     * these are the product list involved
     */
    private final ObservableList<Part> partInv=FXCollections.observableArrayList();
    private final ObservableList<Part> partsInvSearch=FXCollections.observableArrayList();
    private final ObservableList<Part> relPartList=FXCollections.observableArrayList();
    Inv inv;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField partSearchTxt;
    @FXML
    private TableColumn<Part,Integer> relPartStockColumn;
    @FXML
    private TableColumn<Part,String> relPartNameColumn;
    @FXML
    private TableColumn<Part,Integer> relPartIDColumn;
    @FXML
    private TableColumn<Part,Integer> partCountColumn;
    @FXML
    private TableColumn<Part,String> partNameColumn;
    @FXML
    private TableColumn<Part,Integer> partIDColumn;
    @FXML
    private TableColumn<Product,Double> partPriceColumn;
    @FXML
    private TableColumn<Product,Double> relPartPriceColumn;
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
    private TableView<Part> partFindTable;
    @FXML
    private TableView<Part> relPartsTable;

    public AddProdController(Inv inv){
        this.inv=inv;
    }
    /**
     * initialize is the first actions to happen once scene is open.
     * that being
     * -an Id created for a new product
     * -the information of the top table will be set when window is opened
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url,ResourceBundle rb){
        idTxt.setText(createProdID());
        /**
         * sets top table
         */
        partInv.setAll(inv.getAllParts());
        partFindTable.setItems(inv.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partCountColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partFindTable.setItems(partInv);
        partFindTable.refresh();
    }

    /**
     *  prodIDCounter is the starting number of 1 and increase by 1 for every new part.
     */
    private static long prodIDCounter = 1001;
    /**
     *  createProdID assigns a unique id to new part
     */
    public static synchronized String createProdID()
    {
        return String.valueOf(prodIDCounter++);
    }

    /**
     * searchForPart is a keyevent, once text field is typed into it will begin to search the table for parts by ID or partial/full name
     * @param keyEvent
     */
    public void searchForPart(KeyEvent keyEvent){
        if(!partSearchTxt.getText().isEmpty()){
            partsInvSearch.clear();
            for(Part part : inv.getAllParts()){
                if(part.getName().contains(partSearchTxt.getText())||((String.valueOf(part.getId()).contains(partSearchTxt.getText())))){
                    partsInvSearch.add(part);
                }
            }
            partFindTable.setItems(partsInvSearch);
            partFindTable.refresh();
            if (partsInvSearch.isEmpty()){
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Notice:");
                alert.setHeaderText("Search not found");
                alert.setContentText("Search for another part");
                alert.showAndWait();
            }
        }
        if(partSearchTxt.getText().isEmpty()){
            partFindTable.setItems(inv.getAllParts());
        }
    }


    /**
     * adds part from list ensures no duplicate. If duplicate is attempted it will prompt a warning letting user know.
     *
     * @param event
     */
    @FXML
    private void addPart(MouseEvent event){
        Part addPart=partFindTable.getSelectionModel().getSelectedItem();
        boolean multRelPart=false;

        if(addPart!=null){
            int id=addPart.getId();
            for(Part part : relPartList){
                if(part.getId()==id){
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid:");
                    alert.setHeaderText("This part has already been associated.");
                    alert.showAndWait();
                    multRelPart=true;
                }
            }

            if(!multRelPart){
                relPartList.add(addPart);
            }
            relPartsTable.setItems(relPartList);
        }
    }

    /**
     * deletes parts from selected parts and deletion confirmation is prompted
     *
     * @param event
     */
    @FXML
    private void delPart(MouseEvent event){
        Part remPart=relPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted=false;
        if(null!=remPart){
            boolean remove=true;
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText("Deleting");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result=alert.showAndWait();
            relPartList.remove(remPart);
            relPartsTable.refresh();
        }else{
            return;
        }
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(null);
        alert.setContentText("Invalid action");

    }

    /**
     * saveAddProd is a mouse event. Once "save" is clicked it will go through a series of input validations to ensure it is the correct value types as well as at least one part is assigned to it. After saved, the MainForm window will appear.
     *
     * @param event
     */

    @FXML
    private void saveAddProd(MouseEvent event){
        if(relPartList.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("No part was Associated to the product");
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
        try{
            Product prod=new Product(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()));
            for(Part part : relPartList){
                prod.addRelPart(part);
            }

            inv.addProd(prod);
        }catch(NumberFormatException e){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Fill in all fields using valid values");
            alert.showAndWait();
            return;
        }
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/viewController/MainForm.fxml"));
            MainFormController controller=new MainFormController(inv);

            loader.setController(controller);
            Parent root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * goes back to main form once cancel is clicked and user will be prompted for cancellation of adding a product.
     *
     * @param event
     */
    public void backToMainForm(MouseEvent event){
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
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
