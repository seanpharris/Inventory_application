package ViewController;

import Model.Inv;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * main form controller class
 */
public class MainFormController implements Initializable{

    private final ObservableList<Part> partInv=FXCollections.observableArrayList();
    private final ObservableList<Product> prodInv=FXCollections.observableArrayList();
    private final ObservableList<Part> partInvSearch=FXCollections.observableArrayList();
    private final ObservableList<Product> prodInvSearch=FXCollections.observableArrayList();
    Inv inv;
    @FXML
    private TableColumn<Part,Integer> partIdColumn;
    @FXML
    private TableColumn<Part,String> partNameColumn;
    @FXML
    private TableColumn<Part,Integer> partStockColumn;
    @FXML
    private TableColumn<Part,Double> partPriceColumn;
    @FXML
    private TableColumn<Product,Integer> prodIdColumn;
    @FXML
    private TableColumn<Product,String> prodNameColumn;
    @FXML
    private TableColumn<Product,Integer> prodStockColumn;
    @FXML
    private TableColumn<Product,Double> prodPriceColumn;
    @FXML
    private TextField partSearchTxt;
    @FXML
    private TextField prodSearchTxt;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> prodTable;

    public MainFormController(Inv inv){
        this.inv=inv;
    }
    /**
     * initialize is the first actions to happen once scene is open.
     * that being
     *
     * -the information of the parts table will be set when window is opened
     * -the information of the products table will be set when window is opened
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url,ResourceBundle rb){
        /**
         * loads all parts for table
         */
        partInv.setAll(inv.getAllParts());
        partsTable.setItems(inv.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(partInv);
        partsTable.refresh();
        /**
         * loads all product for table
         */
        prodInv.setAll(inv.getAllProd());
        prodTable.setItems(inv.getAllProd());
        prodIdColumn.setCellValueFactory(new PropertyValueFactory<>("prodID"));
        prodNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        prodPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodTable.setItems(prodInv);
        prodTable.refresh();

    }

    /**
     * closeProg is the exit button on the MainForm. Once clicked it will prompt for confirmation.
     *
     * @param event
     */
    @FXML
    private void closeProg(MouseEvent event){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("You're about to close the application");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK) Platform.exit();
    }

    /**
     * searchForProd is a keyevent, once text field is typed into it will begin to search the table for products by ID or partial/full name
     */
    public void searchForProd(KeyEvent keyEvent){
        if(!prodSearchTxt.getText().isEmpty()){
            prodInvSearch.clear();
            for(Product product : inv.getAllProd()){
                if(product.getName().contains(prodSearchTxt.getText())||((String.valueOf(product.getProdID()).contains(prodSearchTxt.getText())))){
                    prodInvSearch.add(product);
                }
            }
            prodTable.setItems(prodInvSearch);
            prodTable.refresh();
            if (prodInvSearch.isEmpty()){
                Alert alert=new Alert(AlertType.WARNING);
                alert.setTitle("Notice:");
                alert.setHeaderText("Search not found");
                alert.setContentText("Search for another product");
                alert.showAndWait();
            }
        }
        if(prodSearchTxt.getText().isEmpty()){
            prodTable.setItems(inv.getAllProd());
        }
    }

    /**
     * searchForPart is a keyevent, once text field is typed into it will begin to search the table for parts by ID or partial/full name
     */
    public void searchForPart(KeyEvent keyEvent){
        if(!partSearchTxt.getText().isEmpty()){
            partInvSearch.clear();
            for(Part part : inv.getAllParts()){
                if(part.getName().contains(partSearchTxt.getText())||((String.valueOf(part.getId()).contains(partSearchTxt.getText())))){
                    partInvSearch.add(part);
                }
            }
            partsTable.setItems(partInvSearch);
            partsTable.refresh();
            if (partInvSearch.isEmpty()){
                Alert alert=new Alert(AlertType.WARNING);
                alert.setTitle("Notice:");
                alert.setHeaderText("Search not found");
                alert.setContentText("Search for another part");
                alert.showAndWait();
            }
        }

        if(partSearchTxt.getText().isEmpty()) partsTable.setItems(inv.getAllParts());
    }






    /**
     *
     * addPart is the event of the add button on the mainform being clicked which will then go to the addpart window
     * @param event
     */
    @FXML
    private void addPart(MouseEvent event) throws IOException{

        FXMLLoader loader=new FXMLLoader(getClass().getResource("AddPart.fxml"));
        AddPartController controller=new AddPartController(inv);

        loader.setController(controller);
        Parent root=loader.load();
        Scene scene=new Scene(root);
        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * modPart is the event of the add button on the mainform being clicked which will then go to the modpart window
     *
     * @param event
     */
    @FXML
    private void modPart(MouseEvent event) throws IOException{
        Part selected=partsTable.getSelectionModel().getSelectedItem();
        if(partInv.isEmpty()){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Notice:");
            alert.setHeaderText("Nothing in stock");
            alert.setContentText("Add a part");
            alert.showAndWait();
            return;
        }
        if(selected==null){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Nothing has been selected");
            alert.setHeaderText("Invalid:");
            alert.setContentText("Make a selection");
            alert.showAndWait();
        }else{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("ModPart.fxml"));
            ModPartController controller=new ModPartController(inv,selected);

            loader.setController(controller);
            Parent root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    /**
     * deletes part from table and prompts deletion confirmation
     *
     * @param event
     */
    @FXML
    private void delPart(MouseEvent event){
        Part remPart=partsTable.getSelectionModel().getSelectedItem();
        if(partInv.isEmpty()){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Notice:");
            alert.setHeaderText("Nothing in stock");
            alert.setContentText("Add a part");
            alert.showAndWait();
            return;
        }
        if(!partInv.isEmpty()&&remPart==null){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Nothing has been selected");
            alert.setHeaderText("Invalid:");
            alert.setContentText("Make a selection");
            alert.showAndWait();
            return;
        }else{
            boolean confirm=true;
            Alert alert=new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deleting part");
            alert.setHeaderText("Deleting:");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result=alert.showAndWait();
            if(result.isPresent()){
                result.get();
                if(!confirm){
                    return;
                }
                inv.delPart(remPart);
                partInv.remove(remPart);
                partsTable.refresh();

            }
        }
    }

    /**
     * deletes product from table after deletion confirmation
     *
     * @param event
     */
    @FXML
    private void delProd(MouseEvent event){
        boolean deleted=false;
        Product delProd=prodTable.getSelectionModel().getSelectedItem();
        if(prodInv.isEmpty()){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Notice:");
            alert.setHeaderText("Nothing in stock");
            alert.setContentText("Add a part");
            alert.showAndWait();
            return;
        }
        if(!prodInv.isEmpty()&&delProd==null){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Nothing has been selected");
            alert.setHeaderText("Invalid:");
            alert.setContentText("Make a selection");
            alert.showAndWait();
            return;
        }
        if(delProd.getPartsListSize()==0){
            boolean confirm=true;
            Alert alert=new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deleting product");
            alert.setHeaderText("Deleting");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result=alert.showAndWait();
            if(result.isEmpty()||result.get()!=ButtonType.OK){
                return;
            }
        }else{
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText("Cannot be deleted due to have associated parts. Delete parts first");
            alert.showAndWait();
            return;
        }
        inv.delProd(delProd.getProdID());
        prodInv.remove(delProd);
        prodTable.setItems(prodInv);
        prodTable.refresh();
    }

    /**
     * open modify product gui and alarms for nothing in stock to click or alarms for nothing clicked
     * if these alarms were not implemented then runtime error occurs when clicking modify but nothing selected
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void modProd(MouseEvent event) throws IOException{
        Product prodSelected=prodTable.getSelectionModel().getSelectedItem();
        if(prodInv.isEmpty()){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Notice:");
            alert.setHeaderText("Nothing in stock");
            alert.setContentText("Add a part");
            alert.showAndWait();
        }
        if(!prodInv.isEmpty()&&prodSelected==null){
            Alert alert=new Alert(AlertType.WARNING);
            alert.setTitle("Nothing has been selected");
            alert.setHeaderText("Invalid:");
            alert.setContentText("Make a selection");
            alert.showAndWait();

        }else{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/viewController/ModProd.fxml"));
            ModProdController controller=new ModProdController(inv,prodSelected);

            loader.setController(controller);
            Parent root=loader.load();
            Scene scene=new Scene(root);
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    /**
     * addProd is the event of the add button on the mainform being clicked which will then go to the addprod window
     *
     * @param event
     */
    @FXML
    private void addProd(MouseEvent event){
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/ViewController/AddProd.fxml"));
            AddProdController controller=new AddProdController(inv);

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