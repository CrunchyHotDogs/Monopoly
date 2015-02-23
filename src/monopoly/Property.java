package monopoly;

/**
 * A class that holds all the information needed for every space on the Monopoly
 * board.
 * 
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Property {
    private String name, type;
    private int purchaseCost, houses, houseCost, owner;  
    private int taxes[] = new int[6];
    private boolean isOwned;
    
    public Property(String sentName, String sentType, int sentTaxes[], int sentPurchaseCost, int sentHouseCost) {
        this.name = sentName;
        this.purchaseCost = sentPurchaseCost;
        this.taxes[0] = sentTaxes[0];
        this.taxes[1] = sentTaxes[1];
        this.taxes[2] = sentTaxes[2];
        this.taxes[3] = sentTaxes[3];
        this.taxes[4] = sentTaxes[4];
        this.taxes[5] = sentTaxes[5];
        this.type = sentType;
        this.houses = 0;
        this.houseCost = sentHouseCost;
        this.isOwned = false;
        this.owner = -1;
    }
    
    public String getName() {
        return this.name;
    }
    public int getPurchaseCost() {
        return this.purchaseCost;
    }
    public int getTaxes() {
        return this.taxes[this.houses];
    }
    public String getType() {
        return this.type;
    }
    public boolean getIsOwned() {
        return this.isOwned;
    }
    public int getOwner() {
        return this.owner;
    }
    public int getHouseCost() {
        return this.houseCost;
    }
    public int getHouses() {
        return this.houses;
    }
    
    public void setPropOwned(boolean sentOwned) {
        this.isOwned = sentOwned;
    }
    public void setOwner(int sentOwner) {
        this.owner = sentOwner;
    }
    public void setHouses(int sentHouses) {
        this.houses = sentHouses;
    }
    
    /**
     * When a user buys a house on the property.
     * 
     * @return int - The cost of each house.
     */
    public int buyHouse() {
        this.houses += 1;
        
        return this.houseCost;
    }
}
