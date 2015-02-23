package monopoly;

/**
 * A class that holds all the information about each player.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Player {
    private int id, money, location, railroadsOwned, utilitiesOwned, turnsInJail;
    private String name, tokenImage, ownedImage;
    private int locationsOwned[] = new int[40];
    private boolean isJailed, hasFreeJailCard;
    
    /**
     * The player constructor. Creates a new player object.
     * 
     * @param sentId The player's number.
     * @param sentMoney The player's starting money.
     * @param sentName The player's name.
     * @param sentTokenImage The player's image URL.
     * @param sentOwnedImage The image that will be used to display this player owns a property.
     */
    public Player(int sentId, int sentMoney, String sentName, String sentTokenImage, String sentOwnedImage) {
        id = sentId;
        name = sentName;
        tokenImage = sentTokenImage;
        ownedImage = sentOwnedImage;
        money = sentMoney;
        location = 0;
        railroadsOwned = 0;
        utilitiesOwned = 0;
        turnsInJail = 0;
        isJailed = false;
        hasFreeJailCard = false;

        //Sets the array to default of the player not owning any property.
        for (int i = 0; i < locationsOwned.length; i++) {
            locationsOwned[i] = 0;
        } 
    }
    
    /**
     * Gets the player number.
     * @return Int - The player's number.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * A getter for the variable PlayerName.
     * 
     * @return The player's name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * A getter for the variable PlayerLocation.
     * 
     * @return The location where the player is. Will be an index of an array.
     */
    public int getLocation() {
        return this.location;
    }
    
    /**
     * A getter for the variable PlayerImage.
     * 
     * @return The location of the image for the player.
     */
    public String getTokenImage() {
        return this.tokenImage;
    }
    
    public String getOwnedImage() {
        return this.ownedImage;
    }
    
    /**
     * A getter for the variable PlayerMoney.
     * 
     * @return The amount of money the player has.
     */
    public int getMoney() {
        return this.money;
    }
    
    /**
     * A getter for the array LocationOwned.
     * 
     * @return An array of all the properties. 0 means they do not own, 1 means they own.
     */
    public int[] getLocationsOwned() {
        return locationsOwned;
    }
    
    /**
     * A getter for the variable RailroadsOwned.
     * 
     * @return The number of railroads that this player owns.
     */
    public int getRailroadsOwned() {
        return railroadsOwned;
    }
    
    
    /**
     * A getter for the variable TurnsInJail.
     * @return How many turns the player has spent in jail.
     */
    public int getTurnsInJail() {
        return this.turnsInJail;
    }
    
    
    /**
     * A getter for the getHasFreeJailCard variable. Tells the program if they have
     * a get out of jail free card.
     * @return A boolean saying if the player has a get out of jail card.
     */
    public boolean getHasFreeJailCard() {
        return this.hasFreeJailCard;
    }
    
    
    /**
     * A getter for the variable getIsJailed. Tells the program if the player is 
     * in jail or not.
     * @return A boolean saying if the player is in jail or not.
     */
    public boolean getIsJailed() {
        return this.isJailed;
    }
    
    
    /**
     * Returns the tax amount multiplier for railroads.
     * 
     * @return The multiplier for the railroad tax.
     */
    public int findRailroadMultiplier() {
        int multiplier = 0;
        
        if (this.railroadsOwned == 1) {
            multiplier = 1;
        }
        else if (this.railroadsOwned == 2) {
            multiplier = 2;
        }
        else if (this.railroadsOwned == 3) {
            multiplier = 4;
        }
        else if (this.railroadsOwned == 4) {
            multiplier = 8;
        }
        else {
            multiplier = 0;
        }
        
        return multiplier;
    }
    
    
    /**
     * Returns the tax multiplier for utilities.
     * 
     * @return The multiplier for the utility tax. Utility tax is calculated from roll x this number.
     */
    public int findUtilityMultiplier() {
        int multiplier = 0;
        
        if (this.utilitiesOwned == 1) {
            multiplier = 4;
        }
        else if (this.utilitiesOwned == 2) {
            multiplier = 10;
        }
        else {
            multiplier = 0;
        }
        
        return multiplier;
    }
    
    
    /**
     * Sets the location of the player. Each space is an index in an array.
     * Sets the location of the player to one of those indexes. If the player passes
     * go give them $200 and keep moving them around the board.
     * 
     * @param sentLocation The amount of spaces that the player will move forward.
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String setLocation(int sentLocation) {
        String passedGo = "NO";
        
        this.location += sentLocation;
        
        //Checks to see if the player has passed go.
        if (this.location > 39) {
            passedGo = "YES";
            this.location -= 40;
            this.increaseMoney(200);
        }
        
        return passedGo;
    }
    
    
    /**
     * Moves the player right to the go space. Finds how many spots the player 
     * needs to move in order to reach go.
     */
    public void sendPlayerToGo() {
        this.setLocation(40 - this.location);
    }
    
    
    /**
     * Sends the player to a specific location on the board. Finds the number of spaces
     * they need to move and moves them that amount.
     * 
     * @param sentLocation The place on the board the player will be moving to.
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String sendPlayerToSpecific(int sentLocation) {
        int amountToMove = getAmountToMove(sentLocation);
        
        return this.setLocation(amountToMove);
    }
    
    /**
     * Sends the player to the nearest utility. Finds how many spaces the player
     * needs to move in order to reach each utility space. Finds the lowest number
     * that they need to move. Moves the player to that utility.
     * 
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String sendPlayerToUtility() {
        int amountToMoveElectric = getAmountToMove(12);
        int amountToMoveWater = getAmountToMove(28);
        
        //Decides which is closer.
        if (amountToMoveElectric > amountToMoveWater) {
            return this.setLocation(amountToMoveWater);
        }
        else {
            return this.setLocation(amountToMoveElectric);
        }
    }
    
    
    /**
     * Sends the player to the nearest railroad. Finds how many spaces the player needs to move
     * in order to reach each railroad. Finds the lowest number that they
     * need to move. Moves the player to that railroad.
     * 
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String sendPlayerToRailroad() {
        int amountToMove[] = {getAmountToMove(5), getAmountToMove(15), getAmountToMove(25), getAmountToMove(35)};
        int shortestAmount = 0;        
        
        for (int i = 0; i < amountToMove.length; i++) {
            if (amountToMove[shortestAmount] > amountToMove[i]) {
                shortestAmount = i;
            }
        }
        
        return setLocation(amountToMove[shortestAmount]);
    }
    
    
    /**
     * Finds the amount of spaces that the current player needs to move forward
     * in order to land on a specific location.
     * 
     * @param propertyLocation The property the player will be landing on.
     * @return The number that the player needs to move.
     */
    private int getAmountToMove(int propertyLocation) {
        int amountToMove = 0;
        
        amountToMove = propertyLocation - this.location;
        
        if (amountToMove < 0) {
            amountToMove = propertyLocation + (40 - this.location);
        }
        
        return amountToMove;
    }
    
    /**
     * A setter for the getHasFreeJailCard variable. Gives the user a get out of
     * jail free card (Sets variable to true).
     */
    public void awardFreeJailCard() {
        this.hasFreeJailCard = true;
    }
    
    
    /**
     * When the player is sent to jail. Sets the location of the player to be at 
     * jail, start the counter for how many turns they have been in jail, 
     * and sets the getIsJailed variable to true.
     */
    public void goToJail() {
        this.location = 10;
        this.turnsInJail = 0;
        this.isJailed = true;
    }
    
    /**
     * When the player exits jail. Set the getIsJailed variable to false.
     */
    public void exitJail() {
        this.isJailed = false;
    }
    
    
    /**
     * Each turn the player is in jail. Add one to the number of turns they 
     * have been in jail.
     */
    public void stillInJail() {
        this.turnsInJail += 1;
    }
    
    /**
     * Subtracts money from the player's object.
     * 
     * @param Money The amount of money to subtract.
     */
    public void subtractMoney(int sentMoney) {
        this.money -= sentMoney;
    }
    
    /**
     * Adds money to the player's object.
     * 
     * @param Money The amount of money to add.
     */
    public void increaseMoney(int sentMoney) {
        this.money += sentMoney;
    }
    
    /**
     * When the player buys any type of property. Subtracts money from their object
     * for the cost of the property. Set the index of the properties owned array for the
     * space they are currently at, to represent that they own this property.
     * 
     * @param Cost The cost of the property that the player is buying.
     */
    public void buyProperty(int sentCost) {
        locationsOwned[this.location] = 1;
        this.subtractMoney(sentCost);
    }
    
    public void buyPropertyFromPlayer(int sentCost, int sentLocation) {
        locationsOwned[sentLocation] = 1;
        this.subtractMoney(sentCost);
    }
    
    public void sellPropertyToPlayer(int sentCost, int sentLocation) {
        locationsOwned[sentLocation] = 0;
        this.increaseMoney(sentCost);
    }
    
    /**
     * When the player buys a railroad. Adds one to the tax multiplier for
     * railroads.
     */
    public void buyRailroad() {
        this.railroadsOwned += 1;
    }
    
    /**
     * When the player buys an utility type property. Add one to the tax 
     * multiplier for utilities.
     */
    public void buyUtility() {
        this.utilitiesOwned += 1;
    }
}
