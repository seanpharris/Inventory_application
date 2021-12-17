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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * modify product class
 */
public class ModProdController implements Initializable{
    /**
     * these are the product list involved
     */
    private final ObservableList<Part> partInv=FXCollections.observableArrayList();
    private final ObservableList<Part> partsInvSearch=FXCollections.observableArrayList();
    private final ObservableList<Part> relPartList=FXCollections.observableArrayList();
    Inv inv;
    Product prod;
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
    private TableView<Part> relPartsTable;
    @FXML
    private TableView<Part> partFindTable;

    public ModProdController(Inv inv,Product prod){
        this.inv=inv;
        this.prod=prod;
    }
    /**
     * initialize is the first actions to happen once scene is open.
     * that being
     * -selected product textfields will be set with information of the product to be modified
     * -the information of the top table will be set when window is opened
     * =the information of the bottom table will be set when the window is opened
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url,ResourceBundle rb){
        /**
         * sets top table to find parts
         */
        partInv.setAll(inv.getAllParts());
        partFindTable.setItems(inv.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partCountColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partFindTable.setItems(partInv);
        partFindTable.refresh();
        /**
         * sets bottom table of parts associated with the product
         */
        IntStream.range(0,1000).mapToObj(i -> prod.findRelPart(i)).filter(Objects::nonNull).forEach(relPartList::add);
        relPartsTable.setItems(relPartList);
        relPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        relPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        relPartStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        /**
         * fills text fields with data of product to be modified
         */
        nameTxt.setText(prod.getName());
        idTxt.setText((Integer.toString(prod.getProdID())));
        stockTxt.setText((Integer.toString(prod.getInStock())));
        priceTxt.setText((Double.toString(prod.getPrice())));
        minTxt.setText((Integer.toString(prod.getMin())));
        maxTxt.setText((Integer.toString(prod.getMax())));
    }

    /**
     * removes parts from product with a deletion confirmation
     *
     * @param event
     */
    @FXML
    private void delPart(MouseEvent event){
        Part remPart=relPartsTable.getSelectionModel().getSelectedItem();
        if(remPart!=null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText("Deleting");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result=alert.showAndWait();
            if(result.isPresent()&&result.get()==ButtonType.OK){
                prod.remRelPart(remPart.getId());
                relPartList.remove(remPart);
                relPartsTable.refresh();
            }
        }
    }

    /**
     * dds part from list ensures no duplicate. If duplicate is attempted it will prompt a warning letting user know.
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
                    Alert alert=new Alert(AlertType.WARNING);
                    alert.setTitle("Invalid::");
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
     * goes back to main form once cancel is clicked and user will be prompted for cancellation of adding a product.
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
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * modProdSave is a mouse event. Once "save" is clicked it will go through a series of input validations to ensure it is the correct value types as well as at least one part is assigned to it. After saved, the MainForm window will appear.
     *
     * @param //event
     */

    @FXML
    private void modProdSave(MouseEvent event){
        try{
            Product prod=new Product(Integer.parseInt(idTxt.getText()),nameTxt.getText(),Double.parseDouble(priceTxt.getText()),Integer.parseInt(stockTxt.getText()),Integer.parseInt(minTxt.getText()),Integer.parseInt(maxTxt.getText()));
            for(Part part : relPartList){
                prod.addRelPart(part);
            }

            inv.updateProd(prod);
        }catch(NumberFormatException e){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid input:");
            alert.setHeaderText("Fill in all fields using valid values");
            alert.showAndWait();
            return;
        }
        if(nameTxt.getText().isEmpty()||stockTxt.getText().isEmpty()||priceTxt.getText().isEmpty()||minTxt.getText().isEmpty()||maxTxt.getText().isEmpty()){
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
                Alert alert=new Alert(AlertType.WARNING);
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

}
