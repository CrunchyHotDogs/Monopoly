package monopoly;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A class that holds all the information about each player.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Player {
    private int PlayerNumber, PlayerMoney, PlayerLocation, RailroadsOwned, UtilitiesOwned, TurnsInJail;
    private String PlayerName, PlayerImage, PlayerOwnedImage;
    private int LocationOwned[] = new int[40];
    private boolean IsJailed, HasFreeJailCard;
    
    /**
     * The player constructor. Creates a new player object.
     * 
     * @param PNum The player's number.
     * @param PMoney The player's starting money.
     * @param PName The player's name.
     * @param PImage The player's image URL.
     */
    public Player(int PNum, int PMoney, String PName, String PImage, String POImage) {
        PlayerNumber = PNum;
        PlayerName = PName;
        PlayerImage = PImage;
        PlayerOwnedImage = POImage;
        PlayerMoney = PMoney;
        PlayerLocation = 0;
        RailroadsOwned = 0;
        UtilitiesOwned = 0;
        TurnsInJail = 0;
        IsJailed = false;
        HasFreeJailCard = false;

        //Sets the array to default of the player not owning any property.
        for (int i = 0; i < LocationOwned.length; i++) {
            LocationOwned[i] = 0;
        } 
    }
    
    /**
     * Gets the player number.
     * @return Int - The player's number.
     */
    public int GetPlayerNumber() {
        return this.PlayerNumber;
    }
    
    /**
     * A getter for the variable PlayerName.
     * 
     * @return The player's name.
     */
    public String GetPlayerName() {
        return PlayerName;
    }
    
    /**
     * A getter for the variable PlayerLocation.
     * 
     * @return The location where the player is. Will be an index of an array.
     */
    public int GetPlayerLocation() {
        return PlayerLocation;
    }
    
    /**
     * A getter for the variable PlayerImage.
     * 
     * @return The location of the image for the player.
     */
    public String GetPlayerImage() {
        return PlayerImage;
    }
    
    public String GetPlayerOwnedImage() {
        return PlayerOwnedImage;
    }
    
    /**
     * A getter for the variable PlayerMoney.
     * 
     * @return The amount of money the player has.
     */
    public int GetPlayerMoney() {
        return PlayerMoney;
    }
    
    /**
     * A getter for the array LocationOwned.
     * 
     * @return An array of all the properties. 0 means they do not own, 1 means they own.
     */
    public int[] GetLocationsOwned() {
        return LocationOwned;
    }
    
    /**
     * A getter for the variable RailroadsOwned.
     * 
     * @return The number of railroads that this player owns.
     */
    public int GetRailroadsOwned() {
        return RailroadsOwned;
    }
    
    /**
     * Returns the tax amount multiplier for railroads.
     * 
     * @return The multiplier for the railroad tax.
     */
    public int GetRailroadMultiplier() {
        int Multiplier = 0;
        
        if (this.RailroadsOwned == 1) {
            Multiplier = 1;
        }
        else if (this.RailroadsOwned == 2) {
            Multiplier = 2;
        }
        else if (this.RailroadsOwned == 3) {
            Multiplier = 4;
        }
        else if (this.RailroadsOwned == 4) {
            Multiplier = 8;
        }
        else {
            Multiplier = 0;
        }
        
        return Multiplier;
    }
    
    /**
     * Returns the tax multiplier for utilities.
     * 
     * @return The multiplier for the utility tax. Utility tax is calculated from roll x this number.
     */
    public int GetUtilityMultiplier() {
        int Multiplier = 0;
        
        if (this.UtilitiesOwned == 1) {
            Multiplier = 4;
        }
        else if (this.UtilitiesOwned == 2) {
            Multiplier = 10;
        }
        else {
            Multiplier = 0;
        }
        
        return Multiplier;
    }
    
    /**
     * A getter for the variable TurnsInJail.
     * @return How many turns the player has spent in jail.
     */
    public int GetTurnsInJail() {
        return this.TurnsInJail;
    }
    
    /**
     * Sets the location of the player. Each space is an index in an array.
     * Sets the location of the player to one of those indexes. If the player passes
     * go give them $200 and keep moving them around the board.
     * 
     * @param PLocation The amount of spaces that the player will move forward.
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String SetPlayerLocation(int PLocation) {
        String PassedGo = "NO";
        
        PlayerLocation += PLocation;
        
        //Checks to see if the player has passed go.
        if (PlayerLocation > 39) {
            PassedGo = "YES";
            PlayerLocation -= 40;
            this.IncreaseMoney(200);
        }
        
        return PassedGo;
    }
    
    /**
     * Moves the player right to the go space. Finds how many spots the player 
     * needs to move in order to reach go.
     */
    public void SendPlayerToGo() {
        this.SetPlayerLocation(40 - this.PlayerLocation);
    }
    
    /**
     * Sends the player to a specific location on the board. Finds the number of spaces
     * they need to move and moves them that amount.
     * 
     * @param LocationNumber The place on the board the player will be moving to.
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String SendPlayerToSpecific(int LocationNumber) {
        int AmountToMove = GetAmountToMove(LocationNumber);
        
        return this.SetPlayerLocation(AmountToMove);
    }
    
    /**
     * Sends the player to the nearest utility. Finds how many spaces the player
     * needs to move in order to reach each utility space. Finds the lowest number
     * that they need to move. Moves the player to that utility.
     * 
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String SendPlayerToUtility() {
        int AmountToMoveElectric = GetAmountToMove(12);
        int AmountToMoveWater = GetAmountToMove(28);
        
        //Decides which is closer.
        if (AmountToMoveElectric > AmountToMoveWater) {
            return this.SetPlayerLocation(AmountToMoveWater);
        }
        else {
            return this.SetPlayerLocation(AmountToMoveElectric);
        }
    }
    
    /**
     * Sends the player to the nearest railroad. Finds how many spaces the player needs to move
     * in order to reach each railroad. Finds the lowest number that they
     * need to move. Moves the player to that railroad.
     * 
     * @return A string saying if the player passed go or not. ("YES" or "NO").
     */
    public String SendPlayerToRailroad() {
        int AmountToMove[] = {GetAmountToMove(5), GetAmountToMove(15), GetAmountToMove(25), GetAmountToMove(35)};
        int ShortestAmount = 0;        
        
        for (int i = 0; i < AmountToMove.length; i++) {
            if (AmountToMove[ShortestAmount] > AmountToMove[i]) {
                ShortestAmount = i;
            }
        }
        
        return SetPlayerLocation(AmountToMove[ShortestAmount]);
    }
    
    /**
     * Finds the amount of spaces that the current player needs to move forward
     * in order to land on a specific location.
     * 
     * @param PropertyLocation The property the player will be landing on.
     * @return The number that the player needs to move.
     */
    private int GetAmountToMove(int PropertyLocation) {
        int AmountToMove = 0;
        
        AmountToMove = PropertyLocation - this.PlayerLocation;
        
        if (AmountToMove < 0) {
            AmountToMove = PropertyLocation + (40 - this.PlayerLocation);
        }
        
        return AmountToMove;
    }
    
    /**
     * A setter for the HasFreeJailCard variable. Gives the user a get out of
     * jail free card (Sets variable to true).
     */
    public void AwardFreeJailCard() {
        this.HasFreeJailCard = true;
    }
    
    /**
     * A getter for the HasFreeJailCard variable. Tells the program if they have
     * a get out of jail free card.
     * @return A boolean saying if the player has a get out of jail card.
     */
    public boolean HasFreeJailCard() {
        return this.HasFreeJailCard;
    }
    
    /**
     * When the player is sent to jail. Sets the location of the player to be at
     * jail, start the counter for how many turns they have been in jail, and sets
     * the IsJailed variable to true.
     */
    public void GoToJail() {
        this.PlayerLocation = 10;
        this.TurnsInJail = 0;
        this.IsJailed = true;
    }
    
    /**
     * When the player exits jail. Set the IsJailed variable to false.
     */
    public void ExitJail() {
        this.IsJailed = false;
    }
    
    /**
     * A getter for the variable IsJailed. Tells the program if the player is 
     * in jail or not.
     * @return A boolean saying if the player is in jail or not.
     */
    public boolean IsJailed() {
        return this.IsJailed;
    }
    
    /**
     * Each turn the player is in jail. Add one to the number of turns they 
     * have been in jail.
     */
    public void StillInJail() {
        this.TurnsInJail += 1;
    }
    
    /**
     * Subtracts money from the player's object.
     * 
     * @param Money The amount of money to subtract.
     */
    public void SubtractMoney(int Money) {
        this.PlayerMoney -= Money;
    }
    
    /**
     * Adds money to the player's object.
     * 
     * @param Money The amount of money to add.
     */
    public void IncreaseMoney(int Money) {
        this.PlayerMoney += Money;
    }
    
    /**
     * When the player buys any type of property. Subtracts money from their object
     * for the cost of the property. Set the index of the properties owned array for the
     * space they are currently at, to represent that they own this property.
     * 
     * @param Cost The cost of the property that the player is buying.
     */
    public void BuyProperty(int Cost) {
        LocationOwned[this.PlayerLocation] = 1;
        this.SubtractMoney(Cost);
    }
    
    public void BuyPropertyFromPlayer(int Cost, int Location) {
        LocationOwned[Location] = 1;
        this.SubtractMoney(Cost);
    }
    
    public void SellPropertyFromPlayer(int Cost, int Location) {
        LocationOwned[Location] = 0;
        this.IncreaseMoney(Cost);
    }
    
    /**
     * When the player buys a railroad. Adds one to the tax multiplier for
     * railroads.
     */
    public void BuyRailroad() {
        this.RailroadsOwned += 1;
    }
    
    /**
     * When the player buys an utility type property. Add one to the tax 
     * multiplier for utilities.
     */
    public void BuyUtility() {
        this.UtilitiesOwned += 1;
    }
}
