/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 * A class that holds all the information needed for every space on the Monopoly
 * board.
 * 
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Property {
    private String PropertyName, PropertyType;
    private int PropCost, Houses, BuildingCost, Owner; //PropCost = Cost to first purchase property. Houses = Number of houses.
                                                       //BuildingCost = Cost for each house. Owner = The players number who owns this.
    private int PropTax[] = new int[6];
    private boolean PropOwned;
    
    public Property(String PName, String PType, int PTax[], int PCost, int BCost) {
        this.PropertyName = PName;
        this.PropCost = PCost;
        this.PropTax[0] = PTax[0];
        this.PropTax[1] = PTax[1];
        this.PropTax[2] = PTax[2];
        this.PropTax[3] = PTax[3];
        this.PropTax[4] = PTax[4];
        this.PropTax[5] = PTax[5];
        this.PropertyType = PType;
        this.Houses = 0;
        this.BuildingCost = BCost;
        this.PropOwned = false;
        this.Owner = -1;
    }
    
    public String GetPropertyName() {
        return this.PropertyName;
    }
    public int GetPropertyCost() {
        return this.PropCost;
    }
    public int GetPropertyTax() {
        return this.PropTax[this.Houses];
    }
    public String GetPropertyType() {
        return this.PropertyType;
    }
    public boolean GetPropOwned() {
        return this.PropOwned;
    }
    public int GetOwner() {
        return this.Owner;
    }
    public int GetBuildingCost() {
        return this.BuildingCost;
    }
    public int GetHouses() {
        return this.Houses;
    }
    
    public void setPropOwned(boolean POwned) {
        this.PropOwned = POwned;
    }
    public void setOwner(int CurrentOwner) {
        this.Owner = CurrentOwner;
    }
    public void setHouses(int PHouses) {
        this.Houses = PHouses;
    }
    
    /**
     * When a user buys a house on the property.
     * 
     * @return int - The cost of each house.
     */
    public int buyHouse() {
        this.Houses += 1;
        
        return this.BuildingCost;
    }
}
