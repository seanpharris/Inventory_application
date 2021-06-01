package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * in house parts class
 */
public class InHouse extends Part{
    IntegerProperty machID;

    public InHouse(int id,String name,double price,int stock,int min,int max,int machID){
        super(id,name,price,stock,min,max);
        this.machID=new SimpleIntegerProperty(machID);
    }

    /**
     * getter for machine id
     *
     * @return machine id
     */
    public int getMachID(){
        return this.machID.get();
    }

    /**
     * setter for machine id
     *
     * @param id for part
     */
    public void setMachID(int id){
        this.machID.set(id);
    }
}
