package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * out sourced parts class
 */
public class OutSource extends Part{
    StringProperty compName;

    public OutSource(int id,String name,double price,int stock,int min,int max,String compName){
        super(id,name,price,stock,min,max);
        this.compName=new SimpleStringProperty(compName);
    }

    /**
     * getter for company name
     *
     * @return
     */
    public String getCompName(){
        return this.compName.get();
    }

    /**
     * setter for company name
     *
     * @param name
     */
    public void setCompName(String name){
        this.compName.set(name);
    }
}
