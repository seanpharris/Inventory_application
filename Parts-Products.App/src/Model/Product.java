package Model;
/**
 * products class
 */

import java.util.ArrayList;

public class Product{

    private ArrayList<Part> relParts=new ArrayList<Part>();
    private int prodID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;
    private double cost;

    public Product(int prodID,String name,double price,int inStock,int min,int max){
        this.prodID=prodID;
        this.name=name;
        this.price=price;
        this.inStock=inStock;
        this.min=min;
        this.max=max;
    }

    /** removes related parts table on product pages
     *
     * @param partToRemove
     * @return
     */
    public boolean remRelPart(int partToRemove){
        int i;
        for(i=0; i<relParts.size(); i++){
            if(relParts.get(i).getId()==partToRemove){
                relParts.remove(i);
                return true;
            }
        }

        return false;
    }

    /** locates related parts for product pages
     *
     * @param partToSearch
     * @return
     */
    public Part findRelPart(int partToSearch){
        for(Part relPart : relParts){
            if(relPart.getId()==partToSearch){
                return relPart;
            }
        }
        return null;
    }

    public int getPartsListSize(){
        return relParts.size();
    }

    public ArrayList<Part> getRelParts(){
        return relParts;
    }

    public void setRelParts(ArrayList<Part> relParts){
        this.relParts=relParts;
    }

    /** returns product IDs
     *
     * @return
     */
    public int getProdID(){
        return prodID;
    }

    /** Id to set
     *
     * @param prodID
     */
    public void setProdID(int prodID){
        this.prodID=prodID;
    }

    /** gets name for product
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /** sets name for product
     *
     * @param name
     */
    public void setName(String name){
        this.name=name;
    }

    /** gets price
     *
     * @return
     */
    public double getPrice(){
        return price;
    }

    /** sets price
     *
     * @param price
     */
    public void setPrice(double price){
        this.price=price;
    }

    /** gets in stock
     *
     * @return
     */
    public int getInStock(){
        return inStock;
    }

    /** sets stock
     *
     * @param inStock
     */
    public void setInStock(int inStock){
        this.inStock=inStock;
    }

    /** gets min
     *
     * @return
     */
    public int getMin(){
        return min;
    }

    /** sets min
     *
     * @param min
     */
    public void setMin(int min){
        this.min=min;
    }

    /** gets max
     *
     * @return
     */
    public int getMax(){
        return max;
    }

    /** sets max
     *
     * @param max
     */
    public void setMax(int max){
        this.max=max;
    }

    /** gets cost
     *
     * @return
     */
    public double getCost(){
        return cost;
    }

    /** sets cost
     *
     * @param cost
     */
    public void setCost(double cost){
        this.cost=cost;
    }

    public void addRelPart(Part partToAdd){
        relParts.add(partToAdd);
    }
}

