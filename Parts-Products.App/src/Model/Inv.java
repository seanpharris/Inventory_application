package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.stream.IntStream;

/**
 * inventory class for parts and products
 */
public class Inv{

    private ObservableList<Part> allParts=FXCollections.observableArrayList();

    private ObservableList<Product> allProd=FXCollections.observableArrayList();
    /**
     * adds part
     *
     * @param partToAdd
     */
    public void addPart(Part partToAdd){
        if(partToAdd!=null){
            allParts.add(partToAdd);
        }
    }
    /**
     * adds products
     *
     * @param prodToAdd
     */
    public void addProd(Product prodToAdd){
        if(prodToAdd!=null){
            allProd.add(prodToAdd);
        }
    }
    /**
     * gets all products
     *
     * @return
     */
    public ObservableList<Product> getAllProd(){
        return allProd;
    }

    /**
     * gets all parts
     *
     * @return
     */
    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * list of part size
     *
     * @return
     */
    public int partListSize(){
        return allParts.size();
    }

    /**
     * list of product size
     *
     * @return
     */
    public int prodListSize(){
        return allProd.size();
    }

    /**
     * deletes products
     *
     * @param prodToRemove
     */
    public void delProd(int prodToRemove){
        for(int i=0; i<allProd.size(); i++){
            if(allProd.get(i).getProdID()==prodToRemove){
                allProd.remove(i);
                return;
            }
        }
    }


    /**
     * Find products for table and search
     *
     * @param prodToSearch
     * @return
     */
    private Product findProd(int prodToSearch){
        if(!allProd.isEmpty()){
            for(Product allProd : allProd){
                if(allProd.getProdID()==prodToSearch){
                    return allProd;
                }
            }
        }
        return null;
    }

    private ObservableList<Part> findProd(String prodNameToFind){
        return null;
    }

    /**
     * changes product after save
     *
     * @param prod
     */
    public void updateProd(Product prod){
        for(int i=0; i<allProd.size(); i++){
            if(allProd.get(i).getProdID()==prod.getProdID()){
                allProd.set(i,prod);
                break;
            }
        }
    }

    /**
     * removes part
     *
     * @param partToDel
     */
    public void delPart(Part partToDel){
        for(int i=0; i<allParts.size(); i++){
            if(allParts.get(i).getId()==partToDel.getId()){
                allParts.remove(i);
                return;
            }
        }
    }

    /**
     * finds part for list/table
     *
     * @param partToFind
     * @return
     */
    public Part findPart(int partToFind){
        if(!allParts.isEmpty()){
            for(Part allPart : allParts){
                if(allPart.getId()==partToFind){
                    return allPart;
                }
            }

        }
        return null;
    }

    /**
     * list of parts
     *
     * @param partNameToFind
     * @return
     */
    public ObservableList findPart(String partNameToFind){
        if(!allParts.isEmpty()){
            ObservableList<Object> searchPartsList=FXCollections.observableArrayList();
            for(Part p : getAllParts())
                if(p.getName().contains(partNameToFind)) searchPartsList.add(p);
            return searchPartsList;
        }
        return null;
    }

    /**
     * changes parts as needed
     *
     * @param partToUpdate
     */
    public void updatePart(Part partToUpdate){
        IntStream.range(0,allParts.size()).filter(i -> allParts.get(i).getId()==partToUpdate.getId()).findFirst().ifPresent(i -> allParts.set(i,partToUpdate));
    }
}
