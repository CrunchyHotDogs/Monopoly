package monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * The whole game of Monopoly. Contains all of the methods that are required for
 * Monopoly.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class MainGame extends javax.swing.JFrame {
    private Property[] boardProperties = new Property[40];
    private JLabel[] propertiesOwned = new JLabel[40];
    private JLabel[] houseLabels = new JLabel[40];
    private CommunityChestCard[] communityChestCards = new CommunityChestCard[17];
    private ChanceCard[] chanceCards = new ChanceCard[16];
    private ArrayList<JLabel[]> boardPositionLocation = new ArrayList<>();
    private ArrayList<JLabel> jailPosition = new ArrayList<>();
    private ArrayList<JLabel> playerNameLabels = new ArrayList<>();
    private ArrayList<JLabel> playerMoney = new ArrayList<>();
    private ArrayList<Player> playerGroup = new ArrayList<>();
    private int currentPlayerTurn = 0, peoplePlaying = 4, currentDiceRoll = 0, rollsInARow = 0, playerPurchasing = 0;
    private String gameStyle = "";
    private int startingMoney = 0;
    private OwnedProperties ownedProperties = null;
    private HomeBuyer homeBuyer = null;
    private BuySellProperties buySellProperties = null;
    private boolean isPlayingMusic = true;
    
    
    /**
     * Creates new form MainMenu
     * @param NumOfPlayers The amount of people playing.
     * @param PlayerNames An array of names for each player.
     * @param PlayerImages An array of the url for player images.
     * @param GameType The type of property/board the player is playing on.
     * @param Money The starting money.
     */
    public MainGame(int NumOfPlayers, String[] PlayerNames, String[] PlayerImages, String GameType, int Money, String[] PlayerOwnedImages) {
        gameStyle = GameType; //Sets the global variable for the gameboard.
        startingMoney = Money;
        peoplePlaying = NumOfPlayers;
        
        initComponents();
        createArray(); //Initialize every array needed for the game. Creates all of the objects needed.
        setPlayers(NumOfPlayers, PlayerNames, PlayerImages, PlayerOwnedImages); //Sets the player list.
        updateMoney(); //Updates the labels for money.
        
        ownedProperties = new OwnedProperties(playerGroup, boardProperties); //Creates a new owned properties object.
        homeBuyer = new HomeBuyer(playerGroup, boardProperties); //Creates a new home buyer object.
        buySellProperties = new BuySellProperties(playerGroup, boardProperties);
        
        try {
            GameBoard.setIcon(new ImageIcon("MonopolyInfo\\" + gameStyle +  "\\Gameboard.jpg")); //Sets the gameboard to which game is being played.
        }
        catch (Exception ex) {
            
        }
        
        //Makes the turn label have proper grammer.
        if (!playerGroup.get(currentPlayerTurn).GetPlayerName().substring(playerGroup.get(currentPlayerTurn).GetPlayerName().length() - 1).equalsIgnoreCase("S")) {
            PlayerTurnLabel.setText("<html>Currently " + playerGroup.get(currentPlayerTurn).GetPlayerName() + "'s turn.</html>"); //Say that it's the first players turn.
        }
        else {
            PlayerTurnLabel.setText("<html>Currently " + playerGroup.get(currentPlayerTurn).GetPlayerName() + "' turn.</html>"); //Say that it's the first players turn.
        }       
    }
    
    
    /**
    * Clears all of images of the current player from every space, so their icon is not in more 
    * than one place at once.
    * 
    * @param  PlayerNumber  The player that will have his spaces cleared.
    * @see         label.setIcon()
    */
    private void clearSpaces(int playerNumber) {
        //Loops through all of the spaces on the board.
        for (int i = 0; i < boardPositionLocation.get(playerNumber).length; i++) {
            boardPositionLocation.get(playerNumber)[i].setIcon(null);
        }
    }
    
    
    /**
     * Sets the current players location. Changes the image on the players location to show their image.
     * 
     * @param PlayerNumber The player that is having their space set.
     * @param PlayerLocation The location that the current player is currently at.
     * @param PlayerImage The image that is being set for the player.
     */
    private void setPlayerLocation(int playerNumber, int playerLocation, String playerImage) {
        boardPositionLocation.get(playerNumber)[playerLocation].setIcon(new ImageIcon(playerImage));
    }
    
    
    /**
     * Moves the current player forward a random amount. Sets the dice image to represent what was rolled. 
     * Calls the method to clear the spaces for the current player. Checks if the current player passed go.
     * Calls the method to set the current location with the player's image.
     * Checks to see which type of spot the current player has landed on and perform
     * a certain action based on the type. Moves to the next player's turn.
     * Calls the method to update the money.
     */
    private void moveCharacters() {
        //Clear the labels in case they aren't used this roll.
        TransactionLabel.setText("");
        InfoLabel.setText("");
        CardLabel.setText("");
        
        //Rolls the two dices. Gets two random numbers.
        Random rand = new Random();
        int rollDice = 0;
        int rollFirstDice = rand.nextInt((6 - 1) + 1) + 1;
        int rollSecondDice = rand.nextInt((6 - 1) + 1) + 1;    
        
        int currentLocation;
        
        //Get the total that the user rolled.
        rollDice = rollFirstDice + rollSecondDice;
        
        //Sets the dice images.
        FirstDiceImageLabel.setIcon(new ImageIcon("MonopolyInfo\\" + gameStyle +  "\\Dice\\" + rollFirstDice + "Dice.png"));
        SecondDiceImageLabel.setIcon(new ImageIcon("MonopolyInfo\\" + gameStyle +  "\\Dice\\" + rollSecondDice + "Dice.png"));
        
        //If the played is in jail.
        if (playerGroup.get(currentPlayerTurn).IsJailed()) {
            //Adds a turn to the time the player has been in jail.
            playerGroup.get(currentPlayerTurn).StillInJail();
            
            //Checks to see if the user rolled doubles.
            if (rollFirstDice == rollSecondDice) {
                //The user leaves jail.
                playerGroup.get(currentPlayerTurn).ExitJail();
                //Removes the image from jail.
                jailPosition.get(currentPlayerTurn).setIcon(null);
            }
            //If the player has been in jail for three turns, kick them out of jail.
            if (playerGroup.get(currentPlayerTurn).GetTurnsInJail() == 3) {
                playerGroup.get(currentPlayerTurn).SubtractMoney(50);
                playerGroup.get(currentPlayerTurn).ExitJail();
                jailPosition.get(currentPlayerTurn).setIcon(null);
            }
        }
        
        //Checks to see if the player is not in jail.
        if (!playerGroup.get(currentPlayerTurn).IsJailed() && ((rollsInARow == 2 && rollFirstDice != rollSecondDice) || (rollsInARow != 2))) {
            //Sets a global variable to what the user rolled, used for utility taxes.
            currentDiceRoll = rollDice;

            //Clears all of the spaces for the certain player.
            clearSpaces(currentPlayerTurn);
            //Checks to see if the player passed go.
            if (playerGroup.get(currentPlayerTurn).SetPlayerLocation(rollDice).equals("YES")) {
                TransactionLabel.setText(playerGroup.get(currentPlayerTurn).GetPlayerName() + " has passed go, receive $200.");
            }

            //Sets the players current location to have their image.
            setPlayerLocation(currentPlayerTurn, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());

            currentLocation = playerGroup.get(currentPlayerTurn).GetPlayerLocation();
            playerPurchasing = currentPlayerTurn;
            
            //A general label that tells the user where the player landed.
            PlaceLandedLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has landed on " + boardProperties[currentLocation].GetPropertyName() + ".</html>");
            //Performs different actions based on the type of space the user landed on.
            performSpaceAction(currentLocation);
        }
        
        
        //Finds which property type the player landed on. Used to keep the Pay Fine button disabled while a player is in jail and it is not their turn.
        String PropertyType = boardProperties[playerGroup.get(currentPlayerTurn).GetPlayerLocation()].GetPropertyType();
        currentLocation = playerGroup.get(currentPlayerTurn).GetPlayerLocation();
        
        //Checks to see if the dice rolls are not doubles.
        if (rollFirstDice != rollSecondDice) {
            //Increase the turn counter.
            currentPlayerTurn += 1;
            rollsInARow = 0;
        }
        //Roll again.
        else {
            //Checks to see if the user has rolled to many times in a row.
            if (rollsInARow == 2) {
                //Sends them to jail. Resets the board and rolls in a row.
                rollsInARow = 0;
                goToJail(currentPlayerTurn);
                enableBoard();
                PropertyNameLabel.setText("");
                BuyPropertyLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has rolled three times in a row! Go right to the " + boardProperties[10].GetPropertyName() + ".</html>");
                currentPlayerTurn += 1;
            }
            //If they are still rolling in a row.
            else {
                rollsInARow += 1;
            }
        }
        
        //Checks to see if the counter needs to go back to the first player.
        if (currentPlayerTurn >= peoplePlaying) {
            currentPlayerTurn = 0;
        }
        
        //Update the money labels.
        updateMoney();
        
        //Checks to see if the player is jailed. Also checks to see if the player before the jailed player is standing on a property they can purchase.
        if (playerGroup.get(currentPlayerTurn).IsJailed() && (PropertyType.equals("P") && boardProperties[currentLocation].GetPropOwned() != false)) {
            //If the player has a get out of jail free card enable the button.
            if (playerGroup.get(currentPlayerTurn).HasFreeJailCard()) {
                JailGetOutFreeButton.setEnabled(true);
            }
            JailPayFineButton.setEnabled(true);
        }
        else {
            JailGetOutFreeButton.setEnabled(false);
            JailPayFineButton.setEnabled(false);
        }
        
        //Says which player the turn belongs to.
        PlayerTurnLabel.setText("Currently " + playerGroup.get(currentPlayerTurn).GetPlayerName() + " gets the next roll.");
    }
    
    /**
     * Performs an action based on the type of space the player has landed on.
     * 
     * @param currentLocation The current location of the current player.
     */
    private void performSpaceAction(int currentLocation) {
        String PropertyType = boardProperties[playerGroup.get(currentPlayerTurn).GetPlayerLocation()].GetPropertyType();
        
        //Checks to see what type of space the player landed on.
        //Chance.
        if (PropertyType.equals("C")) {
            chanceActive(currentPlayerTurn);
        }
        //Community Chest.
        else if (PropertyType.equals("CC")) { 
            communityChestActive(currentPlayerTurn);
        }
        //Free Parking.
        else if (PropertyType.equals("FP")) {

        }
        //Go.
        else if (PropertyType.equals("G")) {
            InfoLabel.setText(playerGroup.get(currentPlayerTurn).GetPlayerName() + " has landed on go. Collect $200.");
        }
        //Go To Jail.
        else if (PropertyType.equals("GTJ")) {
            goToJail(currentPlayerTurn);
        }
        //Income Tax.
        else if (PropertyType.equals("IT")) {
            playerGroup.get(currentPlayerTurn).SubtractMoney(boardProperties[currentLocation].GetPropertyCost());
            InfoLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has landed on " + boardProperties[currentLocation].GetPropertyName() + ". Pay $" + boardProperties[currentLocation].GetPropertyCost() + ".</html>");
        }
        //Luxury Tax.
        else if (PropertyType.equals("LT")) {
            playerGroup.get(currentPlayerTurn).SubtractMoney(boardProperties[currentLocation].GetPropertyCost());
            InfoLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has landed on " + boardProperties[currentLocation].GetPropertyName() + ". Pay $" + boardProperties[currentLocation].GetPropertyCost() + ".</html>");
        }
        //Property.
        else if (PropertyType.equals("P")) {
            //Checks to see if anyone owns the property that was landed on.
            if (boardProperties[currentLocation].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            //If the property is owned, tax the player.
            else {
                taxPlayer();
            }
        }
        //Railroad.
        else if (PropertyType.equals("RR"))  {
            //Checks to see if anyone owns the railroad that was landed on.
            if (boardProperties[currentLocation].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            //If the property is owned, tax the player.
            else {
                taxPlayerRailroad();
            }
        }
        //Utility.
        else if (PropertyType.equals("U")) {
            //Checks to see if anyone owns the utility that was landed on.
            if (boardProperties[currentLocation].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            //If the property is owned, tax the player.
            else {
                taxPlayerUtility();
            }
        }
    }
    
    /**
     * If a player's money goes below 0, this method is called. Removes the player from the game
     * and everything that relates to that player. Removes the player from the player list, reset his properties,
     * reset the owner of the property, remove all the images off the board of the player, 
     * remove the displayed money number, reduce the amount of people playing counter, remove
     * the player's money from the money list, remove the labels for the player from the board position
     * list, keep track of who's turn comes next.
     */
    private void playerLose() {
        int[] PropertiesOwned = new int[40];
        
        //Loops through the player list.
        for (int i = 0; i < playerGroup.size(); i++) {
            //Checks to see if there money is below 0.
            if (playerGroup.get(i).GetPlayerMoney() <= 0) {
                PropertiesOwned = playerGroup.get(i).GetLocationsOwned();
                
                //Loops through all of the properties.
                for (int x = 0; x < PropertiesOwned.length; x++) {
                    //If the player owns the current index property.
                    if (PropertiesOwned[x] == 1) {
                        //Remove the ownership of the property.
                        boardProperties[x].setPropOwned(false);
                        boardProperties[x].setOwner(-1);
                        boardProperties[x].setHouses(0);
                        propertiesOwned[x].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png")));
                        
                        //Checks to see if the space has a house label.
                        if (houseLabels[x] != null) {
                            houseLabels[x].setIcon(null);
                        }
                    }
                }
                
                //Clear the board of all of this players images.
                clearSpaces(i);
                playerMoney.get(i).setText("");
                InfoLabel.setText("<html>" + playerGroup.get(i).GetPlayerName() + " has lost.</html>");
                
                //Subtrack the amount of people playing.
                peoplePlaying -= 1;
                
                //Remove the player's characteristics from all the lists including the player.
                playerGroup.remove(i);
                boardPositionLocation.remove(i);
                playerMoney.remove(i);
                jailPosition.remove(i);
                //Refresh the buttons on the Owner Properties board.
                ownedProperties.refreshButtons();
                homeBuyer.refreshButtons();
                
                //Sets the turn to the next player.
                if (currentPlayerTurn > 0) {
                    currentPlayerTurn -= 1;
                }
            }
        }
        
        //Checks to see if a player has won. Show a JOptionPane if they have won and then close the game.
        if (peoplePlaying == 1) {
            DiceRoll.setVisible(false);
            BuyPropertyButton.setVisible(false);
            DeclinePropertyButton.setVisible(false);
            JailGetOutFreeButton.setVisible(false);
            JailPayFineButton.setVisible(false);
            JOptionPane.showMessageDialog(null, "We have a winner! The winner is " + playerGroup.get(0).GetPlayerName() + ".", "", JOptionPane.ERROR_MESSAGE); 
            System.exit(0);
        }
    }
    
    
    /**
     * Buys the property for the current player. Reduces the players money depending on what was bought.
     * Calls specific functions of the Player object depending on which type of property was bought.
     * Sets the info label to say that the player has bought X property for Y amount of money.
     * Calls a method to enable the board again (allow the user to roll the dice).
     */
    private void buyProperty() {
        int currentLocation, currentPlayer;
        
        currentPlayer = playerPurchasing;
        
        currentLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
        
        //Add the property to the players property owned array.
        playerGroup.get(currentPlayer).BuyProperty(boardProperties[currentLocation].GetPropertyCost());
        //Sets the property to have an owner.
        boardProperties[currentLocation].setPropOwned(true);
        boardProperties[currentLocation].setOwner(currentPlayer);
        
        //Checks to see if the property is a railroad.
        if (boardProperties[currentLocation].GetPropertyType().equals("RR")) {
            playerGroup.get(currentPlayer).BuyRailroad();
        }
        //Checks to see if the property is a utility.
        else if (boardProperties[currentLocation].GetPropertyType().equals("U")) {
            playerGroup.get(currentPlayer).BuyUtility();
        }
        
        //Show the user that a player has purchased a property.
        InfoLabel.setText("<html>" + playerGroup.get(currentPlayer).GetPlayerName() +
                          " has purchased " + boardProperties[currentLocation].GetPropertyName() + 
                          " for $" + boardProperties[currentLocation].GetPropertyCost() + ".</html>");
        
        //Changed the owner label image to the player's color.
        propertiesOwned[currentLocation].setIcon(new ImageIcon(getClass().getResource("/monopoly/Images/Player" + (playerGroup.get(currentPlayer).GetPlayerNumber() + 1) + "Owned.png")));
        enableBoard();
    }
    
    
    /**
     * Reduces the current player's (the one who landed on the property) money.
     * Displays how much the player has lost and how much the owner has gained.
     */
    private void taxPlayer() {
        int currentLocation, currentPlayer;
        
        currentPlayer = currentPlayerTurn;
            
        currentLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
        
        //Subtrack from the player the amount stored in the property object.
        playerGroup.get(currentPlayer).SubtractMoney(boardProperties[currentLocation].GetPropertyTax());
        playerGroup.get(boardProperties[currentLocation].GetOwner()).IncreaseMoney(boardProperties[currentLocation].GetPropertyTax());
        
        //Inform the user of how much the player was taxed.
        TransactionLabel.setText("<html>" + playerGroup.get(currentPlayer).GetPlayerName() +
                          " has lost $" + boardProperties[currentLocation].GetPropertyTax() + ".<br/>" +
                          "" + playerGroup.get(boardProperties[currentLocation].GetOwner()).GetPlayerName() +
                          " has gained $" + boardProperties[currentLocation].GetPropertyTax() + "</html>");
        updateMoney();
    }
    
    
    /**
     * Reduces the current player's (the one who landed on the property) money.
     * Displays how much the player has lost and how much the owner has gained.
     * The money lost/gained is depended on how many railroads the owner owns.
     */
    private void taxPlayerRailroad() {
        int currentLocation, currentPlayer;
        
        currentPlayer = currentPlayerTurn;
            
        currentLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
        
        //Subtract money from the player based on a railroad equation.
        playerGroup.get(currentPlayer).SubtractMoney(boardProperties[currentLocation].GetPropertyTax() * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetRailroadMultiplier());
        playerGroup.get(boardProperties[currentLocation].GetOwner()).IncreaseMoney(boardProperties[currentLocation].GetPropertyTax() * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetRailroadMultiplier());
        
        //Inform the user about money lost and gained.
        TransactionLabel.setText("<html>" + playerGroup.get(currentPlayer).GetPlayerName() +
                          " has lost $" + boardProperties[currentLocation].GetPropertyTax() * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetRailroadMultiplier() + ".<br/>" +
                          "" + playerGroup.get(boardProperties[currentLocation].GetOwner()).GetPlayerName() +
                          " has gained $" + boardProperties[currentLocation].GetPropertyTax() * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetRailroadMultiplier() + "</html>");
        updateMoney();
    }
    
    
    /**
     * Reduces the current player's (the one who landed on the property) money.
     * Displays how much the player has lost and how much the owner has gained.
     * The money lost/gained is depended on what the current player rolled and how many utilities the owner owns.
     */
    private void taxPlayerUtility() {
        int currentLocation, currentPlayer;
        
        currentPlayer = currentPlayerTurn;
            
        currentLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
        
        //Subtracts money from the player based on an utility equation.
        playerGroup.get(currentPlayer).SubtractMoney(currentDiceRoll * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetUtilityMultiplier());
        playerGroup.get(boardProperties[currentLocation].GetOwner()).IncreaseMoney(currentDiceRoll * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetUtilityMultiplier());
        
        //Inform the user about money lost and gained.
        TransactionLabel.setText("<html>" + playerGroup.get(currentPlayer).GetPlayerName() +
                          " has lost $" + currentDiceRoll * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetUtilityMultiplier() + ".<br/>" +
                          "" + playerGroup.get(boardProperties[currentLocation].GetOwner()).GetPlayerName() +
                          " has gained $" + currentDiceRoll * playerGroup.get(boardProperties[currentLocation].GetOwner()).GetUtilityMultiplier() + "</html>");
        updateMoney();
    }
    
    
    /**
     * Calls a method to check if anyone has lost.
     * Updates all of the money labels.
     */
    public void updateMoney() {
        //Checks to see if any player has lost.
        playerLose();
        //Changes the labels of people playing.
        for (int i = 0; i < playerGroup.size(); i++) {
            playerMoney.get(i).setText("$" + playerGroup.get(i).GetPlayerMoney());
        }
    }
    
    /**
     * Enables the buttons that allows the current player to buy a property or go to next turn.
     * Doesn't let the next player roll until the current player decides.
     */
    private void buyPropertyEnable() {
        DiceRoll.setEnabled(false);
        JailPayFineButton.setEnabled(false);
        BuyPropertyButton.setEnabled(true);
        DeclinePropertyButton.setEnabled(true);
        BuyPropertyLabel.setText("<html>Would you like to purchase this property, " + playerGroup.get(currentPlayerTurn).GetPlayerName() + "?</html>" );
        PropertyNameLabel.setText("<html>" + boardProperties[playerGroup.get(currentPlayerTurn).GetPlayerLocation()].GetPropertyName() + " for $" + boardProperties[playerGroup.get(currentPlayerTurn).GetPlayerLocation()].GetPropertyCost() + "</html>");
    }
    
    /**
     * Finds out how much the player is being taxed for the houses/hotels they own.
     * 
     * @param HouseTax The amount per house.
     * @param HotelTax The amount per hotel.
     * @param CurrentPlayer The player being taxed
     * @return 
     */
    public int taxForHouses(int HouseTax, int HotelTax, int CurrentPlayer) {
        int MoneyToTax = 0;
        int[] OwnedProperties = playerGroup.get(CurrentPlayer).GetLocationsOwned();
            
            //Loops through the properties list from the player.
            for (int i = 0; i < OwnedProperties.length; i++) {
                //Checks to see if the player owns the index property.
                if (OwnedProperties[i] == 1) {
                    //Check to see if they own houses on the property.
                    if (boardProperties[i].GetHouses() >= 1 && boardProperties[i].GetHouses() < 4) {
                        MoneyToTax += (HouseTax * boardProperties[i].GetHouses());
                    }
                    //They own a hotel on the property.
                    else if (boardProperties[i].GetHouses() == 4) {
                        MoneyToTax += HotelTax;
                    }
                }
            }
        
        return MoneyToTax;
    }
    
    
    /**
     * Draws a random card from the array of community chest cards. Checks which card was randomly
     * selected and perform an effect based on the card. Displays
     * what has happened in a label.
     * 
     * @param currentPlayer The player that has landed on the community chest location.
     */
    private void communityChestActive(int currentPlayer) {
        Random rand = new Random();
        //Get a a random card from the community chest list.
        int communityChestCard = rand.nextInt((16 - 0) + 1) + 0;
        String card = communityChestCards[communityChestCard].GetCCKey();
        String infoString = "";
        
        //Decide which community chest card was picked. Refer to CommunityChest.txt for individual results.
        if (card.equals("A")) {
            playerGroup.get(currentPlayer).SendPlayerToGo();
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
        }
        else if (card.equals("B")) {
            playerGroup.get(currentPlayer).IncreaseMoney(200);
        }
        else if (card.equals("C")) {
            playerGroup.get(currentPlayer).SubtractMoney(50);
        }
        else if (card.equals("D")) {
            playerGroup.get(currentPlayer).IncreaseMoney(50);
        }
        else if (card.equals("E")) { 
            playerGroup.get(currentPlayer).AwardFreeJailCard();
        }
        else if (card.equals("F")) {
            goToJail(currentPlayer);
        }
        else if (card.equals("G")) {
            recursiveReduceMoney(50, currentPlayer, 0);
        }
        else if (card.equals("H")) {
            playerGroup.get(currentPlayer).IncreaseMoney(100);
        }
        else if (card.equals("I")) {
            playerGroup.get(currentPlayer).IncreaseMoney(20);
        }
        else if (card.equals("J")) {
            recursiveReduceMoney(10, currentPlayer, 0);
        }
        else if (card.equals("K")) {
            playerGroup.get(currentPlayer).IncreaseMoney(100);
        }
        else if (card.equals("L")) {
            playerGroup.get(currentPlayer).SubtractMoney(100);
        }
        else if (card.equals("M")) {
            playerGroup.get(currentPlayer).SubtractMoney(150);
        }
        else if (card.equals("N")) {
            playerGroup.get(currentPlayer).IncreaseMoney(25);
        }
        else if (card.equals("O")) {
            int AmountToTax = taxForHouses(25, 115, currentPlayer);
            playerGroup.get(currentPlayer).SubtractMoney(AmountToTax);
            TransactionLabel.setText(playerGroup.get(currentPlayer).GetPlayerName() + " has been taxed $" + AmountToTax);
        }
        else if (card.equals("P")) {
            playerGroup.get(currentPlayer).IncreaseMoney(10);
        }
        else if (card.equals("Q")) {
            playerGroup.get(currentPlayer).IncreaseMoney(100);
        }
        
        
        infoString = communityChestCards[communityChestCard].GetCCOutputText();
        infoString = infoString.replace("[PLAYER_NAME]", playerGroup.get(currentPlayer).GetPlayerName());
        infoString = infoString.replace("[DESCRIPTION]", communityChestCards[communityChestCard].GetCCDescription());

        CardLabel.setText(infoString);
    }
    
    
    /**
     * Draws a random card from the chance card array. Performs an effect based on which
     * card was drawn. Displays what happened in a label.
     * 
     * @param CurrentPlayer The player that has landed on the chance location.
     */
    private void chanceActive(int currentPlayer) {
        Random rand = new Random();
        //Gets a random chance card.
        int chanceCard = rand.nextInt((15 - 0) + 1) + 0; //14 is house and hotels.
        String card = chanceCards[chanceCard].GetCKey();
        String infoString = "";
        
        //Dfferent actions for each chance card. See MonopolyInfo/SPECIFIC GAME TYPE/Chance.txt for individual results.
        if (card.equals("A")) {
            playerGroup.get(currentPlayer).SendPlayerToGo();
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
        }
        else if (card.equals("B")) {
            moveToSpecificLocation(24, currentPlayer, "Normal");
        }
        else if (card.equals("C")) {
            moveToSpecificLocation(11, currentPlayer, "Normal");
        }
        else if (card.equals("D")) {
            int PlayerLocation;
            
            if (playerGroup.get(currentPlayer).SendPlayerToUtility().equals("YES")) {
                InfoLabel.setText("<html>" + playerGroup.get(currentPlayer).GetPlayerName() + " has passed go, receive $200.</html>");
            }
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
            
            PlayerLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
            if (boardProperties[PlayerLocation].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            else {
                taxPlayerUtility();
            }
        }
        else if (card.equals("E")) {
            int PlayerLocation;
            
            if (playerGroup.get(currentPlayer).SendPlayerToRailroad().equals("YES")) {
                TransactionLabel.setText(playerGroup.get(currentPlayer).GetPlayerName() + " has passed go, receive $200.");
            }
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
            
            PlayerLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
            if (boardProperties[PlayerLocation].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            else {
                taxPlayerRailroad();
            }
        }
        else if (card.equals("F")) {
            playerGroup.get(currentPlayer).IncreaseMoney(50);
        }
        else if (card.equals("G")) {
            playerGroup.get(currentPlayer).AwardFreeJailCard();
        }
        else if (card.equals("H")) {
            int PlayerLocation;
            
            playerGroup.get(currentPlayer).SetPlayerLocation(-3);
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
            
            PlayerLocation = playerGroup.get(currentPlayer).GetPlayerLocation();
            
            performSpaceAction(PlayerLocation);
        }
        else if (card.equals("I")) {
            goToJail(currentPlayer);
        }
        else if (card.equals("J")) {
            int AmountToTax = taxForHouses(25, 100, currentPlayer);
            playerGroup.get(currentPlayer).SubtractMoney(AmountToTax);
            TransactionLabel.setText(playerGroup.get(currentPlayer).GetPlayerName() + " has been taxed $" + AmountToTax);
        }
        else if (card.equals("K")) {
            playerGroup.get(currentPlayer).SubtractMoney(15);
        }
        else if (card.equals("L")) {
            moveToSpecificLocation(5, currentPlayer, "Railroad");
        }
        else if (card.equals("M")) {
            moveToSpecificLocation(39, currentPlayer, "Normal");
        }
        else if (card.equals("N")) {
            recursiveReduceMoney(-50, currentPlayer, 0);
        }
        else if (card.equals("P")) {
            playerGroup.get(currentPlayer).IncreaseMoney(150);
        }
        else if (card.equals("Q")) {
            playerGroup.get(currentPlayer).IncreaseMoney(100);
        }

        infoString = chanceCards[chanceCard].GetCOutputText();
        infoString = infoString.replace("[PLAYER_NAME]", playerGroup.get(currentPlayer).GetPlayerName());
        infoString = infoString.replace("[DESCRIPTION]", chanceCards[chanceCard].GetCDescription());

        CardLabel.setText(infoString);
    }
    
    
    /**
     * Every time the player land son some space that sends them to jail.
     * Sets the Player variable IsJailed to be true. Sets the icon
     * in jail to represent the player's icon. Clears the other icons of the player
     * off of the board.
     * 
     * @param currentPlayer The number of the current player.
     */
    private void goToJail(int currentPlayer) {
        PropertyNameLabel.setText("");
        BuyPropertyLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has been sent to the " + boardProperties[10].GetPropertyName() + ".</html>");
        playerGroup.get(currentPlayer).GoToJail();
        clearSpaces(currentPlayer);
        jailPosition.get(currentPlayer).setIcon(new ImageIcon(getClass().getResource(playerGroup.get(currentPlayer).GetPlayerImage())));
    }
    
    
    /**
     * The user pays a $50 fine in order to escape jail. Subtracts money from the player
     * , removes their player from jail.
     */
    private void payFineJail() {
        playerGroup.get(currentPlayerTurn).SubtractMoney(50);
        jailPosition.get(currentPlayerTurn).setIcon(null);
        playerGroup.get(currentPlayerTurn).ExitJail();
        InfoLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has payed their fines. They may walk free.</html>");
    }
    
    
    /**
     * If the user has a get out of jail free card they can use it while in jail.
     * Removes the player from jail with no penalties.
     */
    private void getOutFree() {
        jailPosition.get(currentPlayerTurn).setIcon(null);
        playerGroup.get(currentPlayerTurn).ExitJail();
        InfoLabel.setText("<html>" + playerGroup.get(currentPlayerTurn).GetPlayerName() + " has used their free jail card. Lucky you!</html>");
    }
    
    
    /**
     * Moves the player to the current location required by the chance card.
     * 
     * @param locationNumber The location in the array that the player will be moving to.
     * @param currentPlayer The current player who's turn it is.
     * @param propType The type of property being landed on.
     */
    private void moveToSpecificLocation(int locationNumber, int currentPlayer, String propType) {
            //Moves the player to a specific property, checks to see if they passed go.
            if (playerGroup.get(currentPlayer).SendPlayerToSpecific(locationNumber).equals("YES")) {
                TransactionLabel.setText(playerGroup.get(currentPlayer).GetPlayerName() + " has passed go, receive $200.");
            }
            
            clearSpaces(currentPlayer);
            setPlayerLocation(currentPlayer, playerGroup.get(currentPlayerTurn).GetPlayerLocation(), playerGroup.get(currentPlayerTurn).GetPlayerImage());
            
            //Checks to see if the property is owned.
            if (boardProperties[locationNumber].GetPropOwned() == false) {
                buyPropertyEnable();
            }
            else {
                //Checks to see which type of property.
                if (propType.equals("Railroad")) {
                    taxPlayerRailroad();
                }
                else if (propType.equals("Normal")) {
                    taxPlayer();
                }
            }
    }
    
    
    /**
     * If one player is gaining/losing and all the other players are gaining/losing (opposite of the one player)
     * , keep calling the method to take money from players until every player is been checked.
     * 
     * @param moneyToTake Amount of money to take away from each player and give to one player.
     * @param currentPlayer Current player that landed on the space. They will either receive money from everyone or give money to everyone.
     * @param playerToReduce Current player that the recursive method is on.
     */
    private void recursiveReduceMoney(int moneyToTake, int currentPlayer, int playerToReduce) {
        //Makes sure the money does not get reduced from the player gaining money.
        if (currentPlayer != playerToReduce) {
            //Subtract money from each player that isn't gaining money. Gives money to the player gaining money.
            playerGroup.get(playerToReduce).SubtractMoney(moneyToTake);
            playerGroup.get(currentPlayer).IncreaseMoney(moneyToTake);
        }
        
        //Checks to see if the recursive function has been through each player.
        if (playerToReduce < peoplePlaying - 1) {
            recursiveReduceMoney(moneyToTake, currentPlayer, playerToReduce + 1);
        }
    }
    
    
    /**
     * Updates the housing labels to represent which properties have houses.
     */
    public void updateHousing() {
        //Loops through all of the properties.
        for (int i = 0; i < boardProperties.length; i++) {
            //Checks to see if the property can have houses.
            if (houseLabels[i] != null) {
                //Checks to see if the property has any houses.
                if (boardProperties[i].GetHouses() != 0) {
                    //Checks to see which side of the board the property is on.
                    if (i <= 10 && i >= 0) {
                        houseLabels[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/House" + boardProperties[i].GetHouses() + "Bottom.png")));
                    }
                    else if (i <= 20 && i > 10) {
                        houseLabels[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/House" + boardProperties[i].GetHouses() + "Left.png")));
                    }
                    else if (i <= 30 && i > 20) {
                        houseLabels[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/House" + boardProperties[i].GetHouses() + "Top.png")));
                    }
                    else if (i <= 39 && i > 30) {
                        houseLabels[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/House" + boardProperties[i].GetHouses() + "Right.png")));
                    }
                }
                //If the property does not have any houses.
                else {
                    houseLabels[i].setIcon(null);
                }
            }
        }
    }
    
    
    /**
     * Adds the houses onto the board where they belong.
     */
    public void updatePropertiesOwned() {
        int[] PropertiesOwned;
        
        ImageIcon[] OwnerLabels = {null, null, null, null};
        
        for (int i = 0; i < playerGroup.size(); i++) {
            OwnerLabels[i] = new ImageIcon(getClass().getResource(playerGroup.get(i).GetPlayerOwnedImage()));
        }
        
        for (int i = 0; i < propertiesOwned.length; i++) {
            if (propertiesOwned[i] != null) {
                propertiesOwned[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png")));
            }
        }
        
        for (int i = 0; i < playerGroup.size(); i++) {
            PropertiesOwned = playerGroup.get(i).GetLocationsOwned();
            
            for (int x = 0; x < PropertiesOwned.length; x++) {
                if (PropertiesOwned[x] == 1) {
                    propertiesOwned[x].setIcon(OwnerLabels[i]);
                }
            }
        }
    }
    
    
    /**
     * Enables the roll dice button, disable the buttons for property purchasing.
     * Update the money of the players.
     */
    private void enableBoard() {
        if (playerGroup.get(currentPlayerTurn).IsJailed()) {
            if (playerGroup.get(currentPlayerTurn).HasFreeJailCard()) {
                JailGetOutFreeButton.setEnabled(true);
            }
            JailPayFineButton.setEnabled(true);
        }
        
        DiceRoll.setEnabled(true);
        BuyPropertyButton.setEnabled(false);
        DeclinePropertyButton.setEnabled(false);
        BuyPropertyLabel.setText("");
        PropertyNameLabel.setText("");
        updateMoney();
    }
    
    
    /**
     * Sets the player list with the number of people playing.
     * Sets their name and image.
     * 
     * @param numOfPlayers The number of people that are playing Monopoly at the start.
     * @param playerNames The array of names for the players.
     * @param playerImages The array of strings containing image location for each player.
     */
    private void setPlayers(int numOfPlayers, String[] playerNames, String[] playerImages, String[] ownerLabels) {
        for (int i = 0; i < numOfPlayers; i++) {
            playerGroup.add(new Player(i, startingMoney, playerNames[i], playerImages[i], ownerLabels[i]));
            setPlayerLocation(i, 0, playerGroup.get(i).GetPlayerImage());
            if (!playerGroup.get(i).GetPlayerName().substring(playerGroup.get(i).GetPlayerName().length() - 1).equalsIgnoreCase("S")) {
                playerNameLabels.get(i).setText("<html>" + playerGroup.get(i).GetPlayerName() + "'s Money</html>");
            }
            else {
                playerNameLabels.get(i).setText("<html>" + playerGroup.get(i).GetPlayerName() + "' Money</html>");
            }
        }    
    }
    
    
    /**
     * Initializes every variable that is needed for the program. Retrieves the cards from txt files and create
     * arrays of objects for them. Initialize the array for board spaces.
     */
    private void createArray() {
        boolean Found = true;
        int FLAG = 0, counter = 0, propTaxCounter = 0, propCost = 0, arrayCounter = 0, buildingCost = 0;
        int propTax[] = new int[6];
        String fileName, fileData, propName = "", propType = "", propOutput = "";
        Scanner fileScan = null;
        
        try {
       	    fileName = "MonopolyInfo\\" + gameStyle + "\\Properties.txt";
       	    fileScan = new Scanner(new File(fileName)); //Sets the file to a variable so the program can read it.
         }
         catch(FileNotFoundException exception) {  //If their is no file by the right name, it will tell the user the file was not found.
            JOptionPane.showMessageDialog(null, "The input file for properties was not found.", "", JOptionPane.ERROR_MESSAGE); 
            System.exit(0);
         }
         
         if (Found) { //Scans the file and places the info into arrays.
             while(fileScan.hasNext()) {
                fileData = fileScan.next();
                if (FLAG == 1) {
                    if (!fileData.equals("*-") && !fileData.equals("||")) {
                        //Checks to see which parameter is being retrieved.
                        if (counter == 0) {
                            propName += fileData + " ";
                        }
                        else if (counter == 1) {
                            propTax[propTaxCounter] = parseInt(fileData);
                            propTaxCounter += 1;
                        }
                        else if (counter == 2) {
                            propCost = parseInt(fileData);
                        }
                        else if (counter == 3) {
                            buildingCost = parseInt(fileData);
                        }
                        else if (counter == 4) {
                            propType = fileData;
                        }
                    }
                }
                
                //Checks to see if the file has found either: the start of an object,
                // the end of an object, or a parameter divider.
                if (fileData.equals("-*")) {
                    FLAG = 1;
                }
                if (fileData.equals("*-")) {
                    FLAG = 2;
                }
                if (fileData.equals("||")) {
                    counter += 1;
                }
                
                //Checks to see if the file is at the end of one of the objects.
                if (FLAG == 2) {
                    boardProperties[arrayCounter] = new Property(propName.trim(), propType.trim(), propTax, propCost, buildingCost);
                    propName = "";
                    counter = 0;
                    propTaxCounter = 0;
                    arrayCounter += 1;
                }   
            }
        }
         
        propName = "";
        arrayCounter = 0;
         
        //Adds all of the community chest cards into an array.
        try { 
       	    fileName = "MonopolyInfo\\" + gameStyle + "\\CommunityChest.txt";
       	    fileScan = new Scanner(new File(fileName)); //Sets the file to a variable so the program can read it.
        }
        catch(FileNotFoundException exception) {  //If their is no file by the right name, it will tell the user the file was not found.
            JOptionPane.showMessageDialog(null, "The input file for community chest cards was not found.", "", JOptionPane.ERROR_MESSAGE); 
            System.exit(0);
        }
         
        if (Found) { //Scans the file and places the info into arrays.
            while(fileScan.hasNext()) {
                fileData = fileScan.next();
                
                if (FLAG == 1) {
                    if (!fileData.equals("*-") && !fileData.equals("||")) {
                        //Checks to see which parameter is being retrieved.
                        if (counter == 0) {
                            propName += fileData + " ";
                        }
                        else if (counter == 1) {
                            propType = fileData;
                        }
                        else if (counter == 2) {
                            propOutput += fileData + " ";
                        }
                    }
                }
                
                //Checks to see if the file has found either: the start of an object,
                // the end of an object, or a parameter divider.
                if (fileData.equals("-*")) {
                    FLAG = 1;
                }
                if (fileData.equals("*-")) {
                    FLAG = 2;
                }
                if (fileData.equals("||")) {
                    counter += 1;
                }
                
                //Checks to see if the file is at the end of one of the objects.
                if (FLAG == 2) {
                    communityChestCards[arrayCounter] = new CommunityChestCard(propName.trim(), propType.trim(), propOutput.trim());
                    propName = "";
                    propOutput = "";
                    counter = 0;
                    arrayCounter += 1;
                }   
            }
        } 
        
         
        propName = "";
        arrayCounter = 0;
         
        //Adds all of the community chest cards into an array.
        try { 
       	    fileName = "MonopolyInfo\\" + gameStyle + "\\Chance.txt";
       	    fileScan = new Scanner(new File(fileName)); //Sets the file to a variable so the program can read it.
        }
        catch(FileNotFoundException exception) {  //If their is no file by the right name, it will tell the user the file was not found.
            JOptionPane.showMessageDialog(null, "The input file for chance cards cards was not found.", "", JOptionPane.ERROR_MESSAGE); 
            System.exit(0);
        }
         
        if (Found) { //Scans the file and places the info into arrays.
            while(fileScan.hasNext()) {
                fileData = fileScan.next();
                //Checks to see which parameter is being retrieved.
                if (FLAG == 1) {
                    if (!fileData.equals("*-") && !fileData.equals("||")) {
                        if (counter == 0) {
                            propName += fileData + " ";
                        }
                        else if (counter == 1) {
                            propType = fileData;
                        }
                        else if (counter == 2) {
                            propOutput += fileData + " ";
                        }
                    }
                }
                
                //Checks to see if the file has found either: the start of an object,
                // the end of an object, or a parameter divider.
                if (fileData.equals("-*")) {
                    FLAG = 1;
                }
                if (fileData.equals("*-")) {
                    FLAG = 2;
                }
                if (fileData.equals("||")) {
                    counter += 1;
                }
               
                //Checks to see if the file is at the end of one of the objects.
                if (FLAG == 2) {
                    chanceCards[arrayCounter] = new ChanceCard(propName.trim(), propType.trim(), propOutput.trim());
                    propName = "";
                    propOutput = "";
                    counter = 0;
                    arrayCounter += 1;
                }   
            }
        } 
         
        playerMoney.add(PlayerOneMoneyLabel);
        playerMoney.add(PlayerTwoMoneyLabel);
        playerMoney.add(PlayerThreeMoneyLabel);
        playerMoney.add(PlayerFourMoneyLabel);
         
        playerNameLabels.add(PlayerOneInfoLabel);
        playerNameLabels.add(PlayerTwoInfoLabel);
        playerNameLabels.add(PlayerThreeInfoLabel);
        playerNameLabels.add(PlayerFourInfoLabel);
        
        jailPosition.add(P1Jail);
        jailPosition.add(P2Jail);
        jailPosition.add(P3Jail);
        jailPosition.add(P4Jail);
        
        //Creates arrays of JLabels in the list of JLabels.
        for (int i = 0; i < 4; i++) {
            boardPositionLocation.add(new JLabel[40]);
        }
        
        //Labels for all of the properties. Shows a color based on who owns the property. 
        //Labels are right outside of their respective property.
        propertiesOwned[0] = null;
        propertiesOwned[1] = boardOwnedLabel1;
        propertiesOwned[2] = null;
        propertiesOwned[3] = boardOwnedLabel2;
        propertiesOwned[4] = null;
        propertiesOwned[5] = boardOwnedLabel3;
        propertiesOwned[6] = boardOwnedLabel4;
        propertiesOwned[7] = null;
        propertiesOwned[8] = boardOwnedLabel5;
        propertiesOwned[9] = boardOwnedLabel6;
        propertiesOwned[10] = null;
        propertiesOwned[11] = boardOwnedLabel7;
        propertiesOwned[12] = boardOwnedLabel8;
        propertiesOwned[13] = boardOwnedLabel9;
        propertiesOwned[14] = boardOwnedLabel10;
        propertiesOwned[15] = boardOwnedLabel11;
        propertiesOwned[16] = boardOwnedLabel12;
        propertiesOwned[17] = null;
        propertiesOwned[18] = boardOwnedLabel13;
        propertiesOwned[19] = boardOwnedLabel14;
        propertiesOwned[20] = null;
        propertiesOwned[21] = boardOwnedLabel15;
        propertiesOwned[22] = null;
        propertiesOwned[23] = boardOwnedLabel16;
        propertiesOwned[24] = boardOwnedLabel17;
        propertiesOwned[25] = boardOwnedLabel18;
        propertiesOwned[26] = boardOwnedLabel19;
        propertiesOwned[27] = boardOwnedLabel20;
        propertiesOwned[28] = boardOwnedLabel21;
        propertiesOwned[29] = boardOwnedLabel22;
        propertiesOwned[30] = null;
        propertiesOwned[31] = boardOwnedLabel23;
        propertiesOwned[32] = boardOwnedLabel24;
        propertiesOwned[33] = null;
        propertiesOwned[34] = boardOwnedLabel25;
        propertiesOwned[35] = boardOwnedLabel26;
        propertiesOwned[36] = null;
        propertiesOwned[37] = boardOwnedLabel27;
        propertiesOwned[38] = boardOwnedLabel28;
        propertiesOwned[39] = boardOwnedLabel29;
        
        
        //Labels for all of the houses. Shows how many houses the proprety has.
        //Houses are shown on the color rectangle of the property.
        houseLabels[0] = null;
        houseLabels[1] = housesLabel1;
        houseLabels[2] = null;
        houseLabels[3] = housesLabel2;
        houseLabels[4] = null;
        houseLabels[5] = null;
        houseLabels[6] = housesLabel3;
        houseLabels[7] = null;
        houseLabels[8] = housesLabel4;
        houseLabels[9] = housesLabel5;
        houseLabels[10] = null;
        houseLabels[11] = housesLabel6;
        houseLabels[12] = null;
        houseLabels[13] = housesLabel7;
        houseLabels[14] = housesLabel8;
        houseLabels[15] = null;
        houseLabels[16] = housesLabel9;
        houseLabels[17] = null;
        houseLabels[18] = housesLabel10;
        houseLabels[19] = housesLabel11;
        houseLabels[20] = null;
        houseLabels[21] = housesLabel12;
        houseLabels[22] = null;
        houseLabels[23] = housesLabel13;
        houseLabels[24] = housesLabel14;
        houseLabels[25] = null;
        houseLabels[26] = housesLabel15;
        houseLabels[27] = housesLabel16;
        houseLabels[28] = null;
        houseLabels[29] = housesLabel17;
        houseLabels[30] = null;
        houseLabels[31] = housesLabel18;
        houseLabels[32] = housesLabel19;
        houseLabels[33] = null;
        houseLabels[34] = housesLabel20;
        houseLabels[35] = null;
        houseLabels[36] = null;
        houseLabels[37] = housesLabel21;
        houseLabels[38] = null;
        houseLabels[39] = housesLabel22;
        
        //Labels for every space on the board. Holds spaces for players 1, 2, 3, and 4.
        boardPositionLocation.get(0)[0] = P1S0;
        boardPositionLocation.get(0)[1] = P1S1;
        boardPositionLocation.get(0)[2] = P1S2;
        boardPositionLocation.get(0)[3] = P1S3;
        boardPositionLocation.get(0)[4] = P1S4;
        boardPositionLocation.get(0)[5] = P1S5;
        boardPositionLocation.get(0)[6] = P1S6;
        boardPositionLocation.get(0)[7] = P1S7;
        boardPositionLocation.get(0)[8] = P1S8;
        boardPositionLocation.get(0)[9] = P1S9;
        boardPositionLocation.get(0)[10] = P1S10;
        boardPositionLocation.get(0)[11] = P1S11;
        boardPositionLocation.get(0)[12] = P1S12;
        boardPositionLocation.get(0)[13] = P1S13;
        boardPositionLocation.get(0)[14] = P1S14;
        boardPositionLocation.get(0)[15] = P1S15;
        boardPositionLocation.get(0)[16] = P1S16;
        boardPositionLocation.get(0)[17] = P1S17;
        boardPositionLocation.get(0)[18] = P1S18;
        boardPositionLocation.get(0)[19] = P1S19;
        boardPositionLocation.get(0)[20] = P1S20;
        boardPositionLocation.get(0)[21] = P1S21;
        boardPositionLocation.get(0)[22] = P1S22;
        boardPositionLocation.get(0)[23] = P1S23;
        boardPositionLocation.get(0)[24] = P1S24;
        boardPositionLocation.get(0)[25] = P1S25;
        boardPositionLocation.get(0)[26] = P1S26;
        boardPositionLocation.get(0)[27] = P1S27;
        boardPositionLocation.get(0)[28] = P1S28;
        boardPositionLocation.get(0)[29] = P1S29;
        boardPositionLocation.get(0)[30] = P1S30;
        boardPositionLocation.get(0)[31] = P1S31;
        boardPositionLocation.get(0)[32] = P1S32;
        boardPositionLocation.get(0)[33] = P1S33;
        boardPositionLocation.get(0)[34] = P1S34;
        boardPositionLocation.get(0)[35] = P1S35;
        boardPositionLocation.get(0)[36] = P1S36;
        boardPositionLocation.get(0)[37] = P1S37;
        boardPositionLocation.get(0)[38] = P1S38;
        boardPositionLocation.get(0)[39] = P1S39;
        
        boardPositionLocation.get(1)[0] = P2S0;
        boardPositionLocation.get(1)[1] = P2S1;
        boardPositionLocation.get(1)[2] = P2S2;
        boardPositionLocation.get(1)[3] = P2S3;
        boardPositionLocation.get(1)[4] = P2S4;
        boardPositionLocation.get(1)[5] = P2S5;
        boardPositionLocation.get(1)[6] = P2S6;
        boardPositionLocation.get(1)[7] = P2S7;
        boardPositionLocation.get(1)[8] = P2S8;
        boardPositionLocation.get(1)[9] = P2S9;
        boardPositionLocation.get(1)[10] = P2S10;
        boardPositionLocation.get(1)[11] = P2S11;
        boardPositionLocation.get(1)[12] = P2S12;
        boardPositionLocation.get(1)[13] = P2S13;
        boardPositionLocation.get(1)[14] = P2S14;
        boardPositionLocation.get(1)[15] = P2S15;
        boardPositionLocation.get(1)[16] = P2S16;
        boardPositionLocation.get(1)[17] = P2S17;
        boardPositionLocation.get(1)[18] = P2S18;
        boardPositionLocation.get(1)[19] = P2S19;
        boardPositionLocation.get(1)[20] = P2S20;
        boardPositionLocation.get(1)[21] = P2S21;
        boardPositionLocation.get(1)[22] = P2S22;
        boardPositionLocation.get(1)[23] = P2S23;
        boardPositionLocation.get(1)[24] = P2S24;
        boardPositionLocation.get(1)[25] = P2S25;
        boardPositionLocation.get(1)[26] = P2S26;
        boardPositionLocation.get(1)[27] = P2S27;
        boardPositionLocation.get(1)[28] = P2S28;
        boardPositionLocation.get(1)[29] = P2S29;
        boardPositionLocation.get(1)[30] = P2S30;
        boardPositionLocation.get(1)[31] = P2S31;
        boardPositionLocation.get(1)[32] = P2S32;
        boardPositionLocation.get(1)[33] = P2S33;
        boardPositionLocation.get(1)[34] = P2S34;
        boardPositionLocation.get(1)[35] = P2S35;
        boardPositionLocation.get(1)[36] = P2S36;
        boardPositionLocation.get(1)[37] = P2S37;
        boardPositionLocation.get(1)[38] = P2S38;
        boardPositionLocation.get(1)[39] = P2S39;
        
        boardPositionLocation.get(2)[0] = P3S0;
        boardPositionLocation.get(2)[1] = P3S1;
        boardPositionLocation.get(2)[2] = P3S2;
        boardPositionLocation.get(2)[3] = P3S3;
        boardPositionLocation.get(2)[4] = P3S4;
        boardPositionLocation.get(2)[5] = P3S5;
        boardPositionLocation.get(2)[6] = P3S6;
        boardPositionLocation.get(2)[7] = P3S7;
        boardPositionLocation.get(2)[8] = P3S8;
        boardPositionLocation.get(2)[9] = P3S9;
        boardPositionLocation.get(2)[10] = P3S10;
        boardPositionLocation.get(2)[11] = P3S11;
        boardPositionLocation.get(2)[12] = P3S12;
        boardPositionLocation.get(2)[13] = P3S13;
        boardPositionLocation.get(2)[14] = P3S14;
        boardPositionLocation.get(2)[15] = P3S15;
        boardPositionLocation.get(2)[16] = P3S16;
        boardPositionLocation.get(2)[17] = P3S17;
        boardPositionLocation.get(2)[18] = P3S18;
        boardPositionLocation.get(2)[19] = P3S19;
        boardPositionLocation.get(2)[20] = P3S20;
        boardPositionLocation.get(2)[21] = P3S21;
        boardPositionLocation.get(2)[22] = P3S22;
        boardPositionLocation.get(2)[23] = P3S23;
        boardPositionLocation.get(2)[24] = P3S24;
        boardPositionLocation.get(2)[25] = P3S25;
        boardPositionLocation.get(2)[26] = P3S26;
        boardPositionLocation.get(2)[27] = P3S27;
        boardPositionLocation.get(2)[28] = P3S28;
        boardPositionLocation.get(2)[29] = P3S29;
        boardPositionLocation.get(2)[30] = P3S30;
        boardPositionLocation.get(2)[31] = P3S31;
        boardPositionLocation.get(2)[32] = P3S32;
        boardPositionLocation.get(2)[33] = P3S33;
        boardPositionLocation.get(2)[34] = P3S34;
        boardPositionLocation.get(2)[35] = P3S35;
        boardPositionLocation.get(2)[36] = P3S36;
        boardPositionLocation.get(2)[37] = P3S37;
        boardPositionLocation.get(2)[38] = P3S38;
        boardPositionLocation.get(2)[39] = P3S39;
        
        boardPositionLocation.get(3)[0] = P4S0;
        boardPositionLocation.get(3)[1] = P4S1;
        boardPositionLocation.get(3)[2] = P4S2;
        boardPositionLocation.get(3)[3] = P4S3;
        boardPositionLocation.get(3)[4] = P4S4;
        boardPositionLocation.get(3)[5] = P4S5;
        boardPositionLocation.get(3)[6] = P4S6;
        boardPositionLocation.get(3)[7] = P4S7;
        boardPositionLocation.get(3)[8] = P4S8;
        boardPositionLocation.get(3)[9] = P4S9;
        boardPositionLocation.get(3)[10] = P4S10;
        boardPositionLocation.get(3)[11] = P4S11;
        boardPositionLocation.get(3)[12] = P4S12;
        boardPositionLocation.get(3)[13] = P4S13;
        boardPositionLocation.get(3)[14] = P4S14;
        boardPositionLocation.get(3)[15] = P4S15;
        boardPositionLocation.get(3)[16] = P4S16;
        boardPositionLocation.get(3)[17] = P4S17;
        boardPositionLocation.get(3)[18] = P4S18;
        boardPositionLocation.get(3)[19] = P4S19;
        boardPositionLocation.get(3)[20] = P4S20;
        boardPositionLocation.get(3)[21] = P4S21;
        boardPositionLocation.get(3)[22] = P4S22;
        boardPositionLocation.get(3)[23] = P4S23;
        boardPositionLocation.get(3)[24] = P4S24;
        boardPositionLocation.get(3)[25] = P4S25;
        boardPositionLocation.get(3)[26] = P4S26;
        boardPositionLocation.get(3)[27] = P4S27;
        boardPositionLocation.get(3)[28] = P4S28;
        boardPositionLocation.get(3)[29] = P4S29;
        boardPositionLocation.get(3)[30] = P4S30;
        boardPositionLocation.get(3)[31] = P4S31;
        boardPositionLocation.get(3)[32] = P4S32;
        boardPositionLocation.get(3)[33] = P4S33;
        boardPositionLocation.get(3)[34] = P4S34;
        boardPositionLocation.get(3)[35] = P4S35;
        boardPositionLocation.get(3)[36] = P4S36;
        boardPositionLocation.get(3)[37] = P4S37;
        boardPositionLocation.get(3)[38] = P4S38;
        boardPositionLocation.get(3)[39] = P4S39;
    }    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelGame = new javax.swing.JPanel();
        LayeredPaneGame = new javax.swing.JLayeredPane();
        P1S0 = new javax.swing.JLabel();
        P2S0 = new javax.swing.JLabel();
        P3S0 = new javax.swing.JLabel();
        P4S0 = new javax.swing.JLabel();
        P1S1 = new javax.swing.JLabel();
        P2S1 = new javax.swing.JLabel();
        P3S1 = new javax.swing.JLabel();
        P4S1 = new javax.swing.JLabel();
        P1S2 = new javax.swing.JLabel();
        P2S2 = new javax.swing.JLabel();
        P3S2 = new javax.swing.JLabel();
        P4S2 = new javax.swing.JLabel();
        P1S3 = new javax.swing.JLabel();
        P2S3 = new javax.swing.JLabel();
        P3S3 = new javax.swing.JLabel();
        P4S3 = new javax.swing.JLabel();
        P1S4 = new javax.swing.JLabel();
        P2S4 = new javax.swing.JLabel();
        P3S4 = new javax.swing.JLabel();
        P4S4 = new javax.swing.JLabel();
        P1S5 = new javax.swing.JLabel();
        P2S5 = new javax.swing.JLabel();
        P3S5 = new javax.swing.JLabel();
        P4S5 = new javax.swing.JLabel();
        P1S6 = new javax.swing.JLabel();
        P2S6 = new javax.swing.JLabel();
        P3S6 = new javax.swing.JLabel();
        P4S6 = new javax.swing.JLabel();
        P1S7 = new javax.swing.JLabel();
        P2S7 = new javax.swing.JLabel();
        P3S7 = new javax.swing.JLabel();
        P4S7 = new javax.swing.JLabel();
        P1S8 = new javax.swing.JLabel();
        P2S8 = new javax.swing.JLabel();
        P3S8 = new javax.swing.JLabel();
        P4S8 = new javax.swing.JLabel();
        P1S9 = new javax.swing.JLabel();
        P2S9 = new javax.swing.JLabel();
        P3S9 = new javax.swing.JLabel();
        P4S9 = new javax.swing.JLabel();
        P1S10 = new javax.swing.JLabel();
        P2S10 = new javax.swing.JLabel();
        P3S10 = new javax.swing.JLabel();
        P4S10 = new javax.swing.JLabel();
        P1S11 = new javax.swing.JLabel();
        P2S11 = new javax.swing.JLabel();
        P3S11 = new javax.swing.JLabel();
        P4S11 = new javax.swing.JLabel();
        P1S12 = new javax.swing.JLabel();
        P2S12 = new javax.swing.JLabel();
        P3S12 = new javax.swing.JLabel();
        P4S12 = new javax.swing.JLabel();
        P1S13 = new javax.swing.JLabel();
        P2S13 = new javax.swing.JLabel();
        P3S13 = new javax.swing.JLabel();
        P4S13 = new javax.swing.JLabel();
        P1S14 = new javax.swing.JLabel();
        P2S14 = new javax.swing.JLabel();
        P3S14 = new javax.swing.JLabel();
        P4S14 = new javax.swing.JLabel();
        P1S15 = new javax.swing.JLabel();
        P2S15 = new javax.swing.JLabel();
        P3S15 = new javax.swing.JLabel();
        P4S15 = new javax.swing.JLabel();
        P1S16 = new javax.swing.JLabel();
        P2S16 = new javax.swing.JLabel();
        P3S16 = new javax.swing.JLabel();
        P4S16 = new javax.swing.JLabel();
        P1S17 = new javax.swing.JLabel();
        P2S17 = new javax.swing.JLabel();
        P3S17 = new javax.swing.JLabel();
        P4S17 = new javax.swing.JLabel();
        P1S18 = new javax.swing.JLabel();
        P2S18 = new javax.swing.JLabel();
        P3S18 = new javax.swing.JLabel();
        P4S18 = new javax.swing.JLabel();
        P1S19 = new javax.swing.JLabel();
        P2S19 = new javax.swing.JLabel();
        P3S19 = new javax.swing.JLabel();
        P4S19 = new javax.swing.JLabel();
        P1S20 = new javax.swing.JLabel();
        P2S20 = new javax.swing.JLabel();
        P3S20 = new javax.swing.JLabel();
        P4S20 = new javax.swing.JLabel();
        P1S21 = new javax.swing.JLabel();
        P2S21 = new javax.swing.JLabel();
        P3S21 = new javax.swing.JLabel();
        P4S21 = new javax.swing.JLabel();
        P1S22 = new javax.swing.JLabel();
        P2S22 = new javax.swing.JLabel();
        P3S22 = new javax.swing.JLabel();
        P4S22 = new javax.swing.JLabel();
        P1S23 = new javax.swing.JLabel();
        P2S23 = new javax.swing.JLabel();
        P3S23 = new javax.swing.JLabel();
        P4S23 = new javax.swing.JLabel();
        P1S24 = new javax.swing.JLabel();
        P2S24 = new javax.swing.JLabel();
        P3S24 = new javax.swing.JLabel();
        P4S24 = new javax.swing.JLabel();
        P1S25 = new javax.swing.JLabel();
        P2S25 = new javax.swing.JLabel();
        P3S25 = new javax.swing.JLabel();
        P4S25 = new javax.swing.JLabel();
        P1S26 = new javax.swing.JLabel();
        P2S26 = new javax.swing.JLabel();
        P3S26 = new javax.swing.JLabel();
        P4S26 = new javax.swing.JLabel();
        P1S27 = new javax.swing.JLabel();
        P2S27 = new javax.swing.JLabel();
        P3S27 = new javax.swing.JLabel();
        P4S27 = new javax.swing.JLabel();
        P1S28 = new javax.swing.JLabel();
        P2S28 = new javax.swing.JLabel();
        P3S28 = new javax.swing.JLabel();
        P4S28 = new javax.swing.JLabel();
        P3S29 = new javax.swing.JLabel();
        P4S29 = new javax.swing.JLabel();
        P1S29 = new javax.swing.JLabel();
        P2S29 = new javax.swing.JLabel();
        P1S30 = new javax.swing.JLabel();
        P2S30 = new javax.swing.JLabel();
        P4S30 = new javax.swing.JLabel();
        P3S30 = new javax.swing.JLabel();
        P1S31 = new javax.swing.JLabel();
        P2S31 = new javax.swing.JLabel();
        P3S31 = new javax.swing.JLabel();
        P4S31 = new javax.swing.JLabel();
        P4S32 = new javax.swing.JLabel();
        P1S32 = new javax.swing.JLabel();
        P3S32 = new javax.swing.JLabel();
        P2S32 = new javax.swing.JLabel();
        P1S33 = new javax.swing.JLabel();
        P3S33 = new javax.swing.JLabel();
        P4S33 = new javax.swing.JLabel();
        P2S33 = new javax.swing.JLabel();
        P3S34 = new javax.swing.JLabel();
        P1S34 = new javax.swing.JLabel();
        P4S34 = new javax.swing.JLabel();
        P2S34 = new javax.swing.JLabel();
        P3S35 = new javax.swing.JLabel();
        P2S35 = new javax.swing.JLabel();
        P4S35 = new javax.swing.JLabel();
        P1S35 = new javax.swing.JLabel();
        P4S36 = new javax.swing.JLabel();
        P3S36 = new javax.swing.JLabel();
        P2S36 = new javax.swing.JLabel();
        P1S36 = new javax.swing.JLabel();
        P4S37 = new javax.swing.JLabel();
        P2S37 = new javax.swing.JLabel();
        P1S37 = new javax.swing.JLabel();
        P3S37 = new javax.swing.JLabel();
        P1S38 = new javax.swing.JLabel();
        P3S38 = new javax.swing.JLabel();
        P4S38 = new javax.swing.JLabel();
        P2S38 = new javax.swing.JLabel();
        P2S39 = new javax.swing.JLabel();
        P4S39 = new javax.swing.JLabel();
        P1S39 = new javax.swing.JLabel();
        P3S39 = new javax.swing.JLabel();
        P1Jail = new javax.swing.JLabel();
        P2Jail = new javax.swing.JLabel();
        P3Jail = new javax.swing.JLabel();
        P4Jail = new javax.swing.JLabel();
        boardOwnedLabel1 = new javax.swing.JLabel();
        boardOwnedLabel2 = new javax.swing.JLabel();
        boardOwnedLabel3 = new javax.swing.JLabel();
        boardOwnedLabel4 = new javax.swing.JLabel();
        boardOwnedLabel5 = new javax.swing.JLabel();
        boardOwnedLabel6 = new javax.swing.JLabel();
        boardOwnedLabel7 = new javax.swing.JLabel();
        boardOwnedLabel8 = new javax.swing.JLabel();
        boardOwnedLabel9 = new javax.swing.JLabel();
        boardOwnedLabel10 = new javax.swing.JLabel();
        boardOwnedLabel11 = new javax.swing.JLabel();
        boardOwnedLabel12 = new javax.swing.JLabel();
        boardOwnedLabel13 = new javax.swing.JLabel();
        boardOwnedLabel14 = new javax.swing.JLabel();
        boardOwnedLabel15 = new javax.swing.JLabel();
        boardOwnedLabel16 = new javax.swing.JLabel();
        boardOwnedLabel17 = new javax.swing.JLabel();
        boardOwnedLabel18 = new javax.swing.JLabel();
        boardOwnedLabel19 = new javax.swing.JLabel();
        boardOwnedLabel20 = new javax.swing.JLabel();
        boardOwnedLabel21 = new javax.swing.JLabel();
        boardOwnedLabel22 = new javax.swing.JLabel();
        boardOwnedLabel23 = new javax.swing.JLabel();
        boardOwnedLabel24 = new javax.swing.JLabel();
        boardOwnedLabel25 = new javax.swing.JLabel();
        boardOwnedLabel26 = new javax.swing.JLabel();
        boardOwnedLabel27 = new javax.swing.JLabel();
        boardOwnedLabel28 = new javax.swing.JLabel();
        boardOwnedLabel29 = new javax.swing.JLabel();
        housesLabel1 = new javax.swing.JLabel();
        housesLabel2 = new javax.swing.JLabel();
        housesLabel3 = new javax.swing.JLabel();
        housesLabel4 = new javax.swing.JLabel();
        housesLabel5 = new javax.swing.JLabel();
        housesLabel6 = new javax.swing.JLabel();
        housesLabel7 = new javax.swing.JLabel();
        housesLabel8 = new javax.swing.JLabel();
        housesLabel9 = new javax.swing.JLabel();
        housesLabel10 = new javax.swing.JLabel();
        housesLabel11 = new javax.swing.JLabel();
        housesLabel12 = new javax.swing.JLabel();
        housesLabel13 = new javax.swing.JLabel();
        housesLabel14 = new javax.swing.JLabel();
        housesLabel15 = new javax.swing.JLabel();
        housesLabel16 = new javax.swing.JLabel();
        housesLabel17 = new javax.swing.JLabel();
        housesLabel18 = new javax.swing.JLabel();
        housesLabel19 = new javax.swing.JLabel();
        housesLabel20 = new javax.swing.JLabel();
        housesLabel21 = new javax.swing.JLabel();
        housesLabel22 = new javax.swing.JLabel();
        GameBoard = new javax.swing.JLabel();
        DiceRoll = new javax.swing.JButton();
        BuyPropertyButton = new javax.swing.JButton();
        DeclinePropertyButton = new javax.swing.JButton();
        BuyPropertyLabel = new javax.swing.JLabel();
        PropertyNameLabel = new javax.swing.JLabel();
        PlayerOneInfoLabel = new javax.swing.JLabel();
        PlayerTwoInfoLabel = new javax.swing.JLabel();
        PlayerThreeInfoLabel = new javax.swing.JLabel();
        PlayerFourInfoLabel = new javax.swing.JLabel();
        PlayerOneMoneyLabel = new javax.swing.JLabel();
        PlayerTwoMoneyLabel = new javax.swing.JLabel();
        PlayerThreeMoneyLabel = new javax.swing.JLabel();
        PlayerFourMoneyLabel = new javax.swing.JLabel();
        InfoLabel = new javax.swing.JLabel();
        PlayerTurnLabel = new javax.swing.JLabel();
        FirstDiceImageLabel = new javax.swing.JLabel();
        TransactionLabel = new javax.swing.JLabel();
        SecondDiceImageLabel = new javax.swing.JLabel();
        JailPayFineButton = new javax.swing.JButton();
        JailGetOutFreeButton = new javax.swing.JButton();
        PlaceLandedLabel = new javax.swing.JLabel();
        PropBuyInstruction = new javax.swing.JLabel();
        InfoInstruction = new javax.swing.JLabel();
        TransactionInstruction = new javax.swing.JLabel();
        PlaceLandedInstructions = new javax.swing.JLabel();
        CardInstruction = new javax.swing.JLabel();
        CardLabel = new javax.swing.JLabel();
        ViewPropertyButton = new javax.swing.JButton();
        BuyHousesButton = new javax.swing.JButton();
        BuySellPropertiesButton = new javax.swing.JButton();
        MusicLabel = new javax.swing.JLabel();
        PlayPauseMusicButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelGame.setPreferredSize(new java.awt.Dimension(800, 800));

        LayeredPaneGame.setPreferredSize(new java.awt.Dimension(800, 800));
        LayeredPaneGame.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        P1S0.setBackground(new java.awt.Color(0, 102, 153));
        P1S0.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S0, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 730, 20, 20));

        P2S0.setBackground(new java.awt.Color(0, 102, 153));
        P2S0.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S0, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 730, 20, 20));
        P2S0.getAccessibleContext().setAccessibleName("PlayerOneSpotOne");

        P3S0.setBackground(new java.awt.Color(0, 102, 153));
        P3S0.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S0, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 760, 20, 20));

        P4S0.setBackground(new java.awt.Color(0, 102, 153));
        P4S0.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S0, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 760, 20, 20));

        P1S1.setBackground(new java.awt.Color(0, 0, 0));
        P1S1.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 730, 20, 20));

        P2S1.setBackground(new java.awt.Color(0, 102, 153));
        P2S1.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 730, 20, 20));

        P3S1.setBackground(new java.awt.Color(0, 102, 153));
        P3S1.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 760, 20, 20));

        P4S1.setBackground(new java.awt.Color(0, 102, 153));
        P4S1.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 760, 20, 20));

        P1S2.setBackground(new java.awt.Color(0, 102, 153));
        P1S2.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 730, 20, 20));

        P2S2.setBackground(new java.awt.Color(0, 102, 153));
        P2S2.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 730, 20, 20));

        P3S2.setBackground(new java.awt.Color(0, 102, 153));
        P3S2.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 760, 20, 20));

        P4S2.setBackground(new java.awt.Color(0, 102, 153));
        P4S2.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 760, 20, 20));

        P1S3.setBackground(new java.awt.Color(0, 102, 153));
        P1S3.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 730, 20, 20));

        P2S3.setBackground(new java.awt.Color(0, 102, 153));
        P2S3.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 730, 20, 20));

        P3S3.setBackground(new java.awt.Color(0, 102, 153));
        P3S3.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 760, 20, 20));

        P4S3.setBackground(new java.awt.Color(0, 102, 153));
        P4S3.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 760, 20, 20));

        P1S4.setBackground(new java.awt.Color(0, 102, 153));
        P1S4.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 730, 20, 20));

        P2S4.setBackground(new java.awt.Color(0, 102, 153));
        P2S4.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 730, 20, 20));

        P3S4.setBackground(new java.awt.Color(0, 102, 153));
        P3S4.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 760, 20, 20));

        P4S4.setBackground(new java.awt.Color(0, 102, 153));
        P4S4.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 760, 20, 20));

        P1S5.setBackground(new java.awt.Color(0, 102, 153));
        P1S5.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 730, 20, 20));

        P2S5.setBackground(new java.awt.Color(0, 102, 153));
        P2S5.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 730, 20, 20));

        P3S5.setBackground(new java.awt.Color(0, 102, 153));
        P3S5.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 760, 20, 20));

        P4S5.setBackground(new java.awt.Color(0, 102, 153));
        P4S5.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 760, 20, 20));

        P1S6.setBackground(new java.awt.Color(0, 102, 153));
        P1S6.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 730, 20, 20));

        P2S6.setBackground(new java.awt.Color(0, 102, 153));
        P2S6.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 730, 20, 20));

        P3S6.setBackground(new java.awt.Color(0, 102, 153));
        P3S6.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 760, 20, 20));

        P4S6.setBackground(new java.awt.Color(0, 102, 153));
        P4S6.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 760, 20, 20));

        P1S7.setBackground(new java.awt.Color(0, 102, 153));
        P1S7.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 730, 20, 20));

        P2S7.setBackground(new java.awt.Color(0, 102, 153));
        P2S7.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 730, 20, 20));

        P3S7.setBackground(new java.awt.Color(0, 102, 153));
        P3S7.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 760, 20, 20));

        P4S7.setBackground(new java.awt.Color(0, 102, 153));
        P4S7.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 760, 20, 20));

        P1S8.setBackground(new java.awt.Color(0, 102, 153));
        P1S8.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 730, 20, 20));

        P2S8.setBackground(new java.awt.Color(0, 102, 153));
        P2S8.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 730, 20, 20));

        P3S8.setBackground(new java.awt.Color(0, 102, 153));
        P3S8.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 760, 20, 20));

        P4S8.setBackground(new java.awt.Color(0, 102, 153));
        P4S8.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 760, 20, 20));

        P1S9.setBackground(new java.awt.Color(0, 102, 153));
        P1S9.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 730, 20, 20));

        P2S9.setBackground(new java.awt.Color(0, 102, 153));
        P2S9.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 730, 20, 20));

        P3S9.setBackground(new java.awt.Color(0, 102, 153));
        P3S9.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 760, 20, 20));

        P4S9.setBackground(new java.awt.Color(0, 102, 153));
        P4S9.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 760, 20, 20));

        P1S10.setBackground(new java.awt.Color(0, 102, 153));
        P1S10.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 710, 20, 20));

        P2S10.setBackground(new java.awt.Color(0, 102, 153));
        P2S10.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 750, 20, 20));

        P3S10.setBackground(new java.awt.Color(0, 102, 153));
        P3S10.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 780, 20, 20));

        P4S10.setBackground(new java.awt.Color(0, 102, 153));
        P4S10.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 780, 20, 20));

        P1S11.setBackground(new java.awt.Color(0, 102, 153));
        P1S11.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 20, 20));

        P2S11.setBackground(new java.awt.Color(0, 102, 153));
        P2S11.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 640, 20, 20));

        P3S11.setBackground(new java.awt.Color(0, 102, 153));
        P3S11.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 670, 20, 20));

        P4S11.setBackground(new java.awt.Color(0, 102, 153));
        P4S11.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 670, 20, 20));

        P1S12.setBackground(new java.awt.Color(0, 102, 153));
        P1S12.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 20, 20));

        P2S12.setBackground(new java.awt.Color(0, 102, 153));
        P2S12.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 570, 20, 20));

        P3S12.setBackground(new java.awt.Color(0, 102, 153));
        P3S12.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 20, 20));

        P4S12.setBackground(new java.awt.Color(0, 102, 153));
        P4S12.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, 20, 20));

        P1S13.setBackground(new java.awt.Color(0, 102, 153));
        P1S13.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 20, 20));

        P2S13.setBackground(new java.awt.Color(0, 102, 153));
        P2S13.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 510, 20, 20));

        P3S13.setBackground(new java.awt.Color(0, 102, 153));
        P3S13.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 20, 20));

        P4S13.setBackground(new java.awt.Color(0, 102, 153));
        P4S13.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 540, 20, 20));

        P1S14.setBackground(new java.awt.Color(0, 102, 153));
        P1S14.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 20, 20));

        P2S14.setBackground(new java.awt.Color(0, 102, 153));
        P2S14.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 20, 20));

        P3S14.setBackground(new java.awt.Color(0, 102, 153));
        P3S14.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 20, 20));

        P4S14.setBackground(new java.awt.Color(0, 102, 153));
        P4S14.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 20, 20));

        P1S15.setBackground(new java.awt.Color(0, 102, 153));
        P1S15.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 20, 20));

        P2S15.setBackground(new java.awt.Color(0, 102, 153));
        P2S15.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 20, 20));

        P3S15.setBackground(new java.awt.Color(0, 102, 153));
        P3S15.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 20, 20));

        P4S15.setBackground(new java.awt.Color(0, 102, 153));
        P4S15.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 20, 20));

        P1S16.setBackground(new java.awt.Color(0, 102, 153));
        P1S16.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 20, 20));

        P2S16.setBackground(new java.awt.Color(0, 102, 153));
        P2S16.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 20, 20));

        P3S16.setBackground(new java.awt.Color(0, 102, 153));
        P3S16.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 20, 20));

        P4S16.setBackground(new java.awt.Color(0, 102, 153));
        P4S16.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 20, 20));

        P1S17.setBackground(new java.awt.Color(0, 102, 153));
        P1S17.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 20, 20));

        P2S17.setBackground(new java.awt.Color(0, 102, 153));
        P2S17.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 20, 20));

        P3S17.setBackground(new java.awt.Color(0, 102, 153));
        P3S17.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 20, 20));

        P4S17.setBackground(new java.awt.Color(0, 102, 153));
        P4S17.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 20, 20));

        P1S18.setBackground(new java.awt.Color(0, 102, 153));
        P1S18.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 20, 20));

        P2S18.setBackground(new java.awt.Color(0, 102, 153));
        P2S18.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 20, 20));

        P3S18.setBackground(new java.awt.Color(0, 102, 153));
        P3S18.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 20, 20));

        P4S18.setBackground(new java.awt.Color(0, 102, 153));
        P4S18.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S18, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 20, 20));

        P1S19.setBackground(new java.awt.Color(0, 102, 153));
        P1S19.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 20, 20));

        P2S19.setBackground(new java.awt.Color(0, 102, 153));
        P2S19.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 20, 20));

        P3S19.setBackground(new java.awt.Color(0, 102, 153));
        P3S19.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 20, 20));

        P4S19.setBackground(new java.awt.Color(0, 102, 153));
        P4S19.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 20, 20));

        P1S20.setBackground(new java.awt.Color(0, 102, 153));
        P1S20.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 20, 20));

        P2S20.setBackground(new java.awt.Color(0, 102, 153));
        P2S20.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 20, 20));

        P3S20.setBackground(new java.awt.Color(0, 102, 153));
        P3S20.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 20, 20));

        P4S20.setBackground(new java.awt.Color(0, 102, 153));
        P4S20.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 20, 20));

        P1S21.setBackground(new java.awt.Color(0, 102, 153));
        P1S21.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S21, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 20, 20));

        P2S21.setBackground(new java.awt.Color(0, 102, 153));
        P2S21.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S21, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 20, 20));

        P3S21.setBackground(new java.awt.Color(0, 102, 153));
        P3S21.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S21, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 20, 20));

        P4S21.setBackground(new java.awt.Color(0, 102, 153));
        P4S21.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S21, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 20, 20));

        P1S22.setBackground(new java.awt.Color(0, 102, 153));
        P1S22.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 20, 20));

        P2S22.setBackground(new java.awt.Color(0, 102, 153));
        P2S22.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S22, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 20, 20));

        P3S22.setBackground(new java.awt.Color(0, 102, 153));
        P3S22.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 20, 20));

        P4S22.setBackground(new java.awt.Color(0, 102, 153));
        P4S22.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S22, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 20, 20));

        P1S23.setBackground(new java.awt.Color(0, 102, 153));
        P1S23.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S23, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 20, 20));

        P2S23.setBackground(new java.awt.Color(0, 102, 153));
        P2S23.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S23, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 20, 20));

        P3S23.setBackground(new java.awt.Color(0, 102, 153));
        P3S23.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S23, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 20, 20));

        P4S23.setBackground(new java.awt.Color(0, 102, 153));
        P4S23.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S23, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 20, 20));

        P1S24.setBackground(new java.awt.Color(0, 102, 153));
        P1S24.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S24, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 20, 20));

        P2S24.setBackground(new java.awt.Color(0, 102, 153));
        P2S24.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S24, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 20, 20));

        P3S24.setBackground(new java.awt.Color(0, 102, 153));
        P3S24.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S24, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 20, 20));

        P4S24.setBackground(new java.awt.Color(0, 102, 153));
        P4S24.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S24, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 20, 20));

        P1S25.setBackground(new java.awt.Color(0, 102, 153));
        P1S25.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S25, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 20, 20));

        P2S25.setBackground(new java.awt.Color(0, 102, 153));
        P2S25.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S25, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 20, 20));

        P3S25.setBackground(new java.awt.Color(0, 102, 153));
        P3S25.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S25, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 20, 20));

        P4S25.setBackground(new java.awt.Color(0, 102, 153));
        P4S25.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S25, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 20, 20));

        P1S26.setBackground(new java.awt.Color(0, 102, 153));
        P1S26.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S26, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 20, 20));

        P2S26.setBackground(new java.awt.Color(0, 102, 153));
        P2S26.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S26, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, 20, 20));

        P3S26.setBackground(new java.awt.Color(0, 102, 153));
        P3S26.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S26, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 20, 20));

        P4S26.setBackground(new java.awt.Color(0, 102, 153));
        P4S26.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S26, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, 20, 20));

        P1S27.setBackground(new java.awt.Color(0, 102, 153));
        P1S27.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S27, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 20, 20));

        P2S27.setBackground(new java.awt.Color(0, 102, 153));
        P2S27.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S27, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 20, 20));

        P3S27.setBackground(new java.awt.Color(0, 102, 153));
        P3S27.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S27, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 20, 20));

        P4S27.setBackground(new java.awt.Color(0, 102, 153));
        P4S27.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S27, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 20, 20));

        P1S28.setBackground(new java.awt.Color(0, 102, 153));
        P1S28.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S28, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 20, 20));

        P2S28.setBackground(new java.awt.Color(0, 102, 153));
        P2S28.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S28, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 20, 20));

        P3S28.setBackground(new java.awt.Color(0, 102, 153));
        P3S28.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S28, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 20, 20));

        P4S28.setBackground(new java.awt.Color(0, 102, 153));
        P4S28.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S28, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 20, 20));

        P3S29.setBackground(new java.awt.Color(0, 102, 153));
        P3S29.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S29, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 20, 20));

        P4S29.setBackground(new java.awt.Color(0, 102, 153));
        P4S29.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S29, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 20, 20));

        P1S29.setBackground(new java.awt.Color(0, 102, 153));
        P1S29.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S29, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 20, 20));

        P2S29.setBackground(new java.awt.Color(0, 102, 153));
        P2S29.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S29, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 20, 20));

        P1S30.setBackground(new java.awt.Color(0, 102, 153));
        P1S30.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 20, 20));

        P2S30.setBackground(new java.awt.Color(0, 102, 153));
        P2S30.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S30, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 20, 20));

        P4S30.setBackground(new java.awt.Color(0, 102, 153));
        P4S30.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S30, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 50, 20, 20));

        P3S30.setBackground(new java.awt.Color(0, 102, 153));
        P3S30.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 20, 20));

        P1S31.setBackground(new java.awt.Color(0, 102, 153));
        P1S31.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S31, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 110, 20, 20));

        P2S31.setBackground(new java.awt.Color(0, 102, 153));
        P2S31.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S31, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 110, 20, 20));

        P3S31.setBackground(new java.awt.Color(0, 102, 153));
        P3S31.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S31, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 140, 20, 20));

        P4S31.setBackground(new java.awt.Color(0, 102, 153));
        P4S31.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S31, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 140, 20, 20));

        P4S32.setBackground(new java.awt.Color(0, 102, 153));
        P4S32.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S32, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 210, 20, 20));

        P1S32.setBackground(new java.awt.Color(0, 102, 153));
        P1S32.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S32, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 180, 20, 20));

        P3S32.setBackground(new java.awt.Color(0, 102, 153));
        P3S32.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S32, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 210, 20, 20));

        P2S32.setBackground(new java.awt.Color(0, 102, 153));
        P2S32.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S32, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 180, 20, 20));

        P1S33.setBackground(new java.awt.Color(0, 102, 153));
        P1S33.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S33, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 240, 20, 20));

        P3S33.setBackground(new java.awt.Color(0, 102, 153));
        P3S33.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S33, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 270, 20, 20));

        P4S33.setBackground(new java.awt.Color(0, 102, 153));
        P4S33.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S33, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 270, 20, 20));

        P2S33.setBackground(new java.awt.Color(0, 102, 153));
        P2S33.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S33, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 240, 20, 20));

        P3S34.setBackground(new java.awt.Color(0, 102, 153));
        P3S34.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S34, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 340, 20, 20));

        P1S34.setBackground(new java.awt.Color(0, 102, 153));
        P1S34.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S34, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 310, 20, 20));

        P4S34.setBackground(new java.awt.Color(0, 102, 153));
        P4S34.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S34, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 340, 20, 20));

        P2S34.setBackground(new java.awt.Color(0, 102, 153));
        P2S34.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S34, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 310, 20, 20));

        P3S35.setBackground(new java.awt.Color(0, 102, 153));
        P3S35.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S35, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 400, 20, 20));

        P2S35.setBackground(new java.awt.Color(0, 102, 153));
        P2S35.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S35, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 370, 20, 20));

        P4S35.setBackground(new java.awt.Color(0, 102, 153));
        P4S35.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S35, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 400, 20, 20));

        P1S35.setBackground(new java.awt.Color(0, 102, 153));
        P1S35.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S35, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 370, 20, 20));

        P4S36.setBackground(new java.awt.Color(0, 102, 153));
        P4S36.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S36, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 470, 20, 20));

        P3S36.setBackground(new java.awt.Color(0, 102, 153));
        P3S36.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S36, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 470, 20, 20));

        P2S36.setBackground(new java.awt.Color(0, 102, 153));
        P2S36.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S36, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 440, 20, 20));

        P1S36.setBackground(new java.awt.Color(0, 102, 153));
        P1S36.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S36, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 440, 20, 20));

        P4S37.setBackground(new java.awt.Color(0, 102, 153));
        P4S37.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S37, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 540, 20, 20));

        P2S37.setBackground(new java.awt.Color(0, 102, 153));
        P2S37.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S37, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 510, 20, 20));

        P1S37.setBackground(new java.awt.Color(0, 102, 153));
        P1S37.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S37, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 510, 20, 20));

        P3S37.setBackground(new java.awt.Color(0, 102, 153));
        P3S37.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S37, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 540, 20, 20));

        P1S38.setBackground(new java.awt.Color(0, 102, 153));
        P1S38.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S38, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 570, 20, 20));

        P3S38.setBackground(new java.awt.Color(0, 102, 153));
        P3S38.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S38, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 600, 20, 20));

        P4S38.setBackground(new java.awt.Color(0, 102, 153));
        P4S38.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S38, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 600, 20, 20));

        P2S38.setBackground(new java.awt.Color(0, 102, 153));
        P2S38.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S38, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 570, 20, 20));

        P2S39.setBackground(new java.awt.Color(0, 102, 153));
        P2S39.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2S39, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 640, 20, 20));

        P4S39.setBackground(new java.awt.Color(0, 102, 153));
        P4S39.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4S39, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 670, 20, 20));

        P1S39.setBackground(new java.awt.Color(0, 102, 153));
        P1S39.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1S39, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 640, 20, 20));

        P3S39.setBackground(new java.awt.Color(0, 102, 153));
        P3S39.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3S39, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 670, 20, 20));

        P1Jail.setBackground(new java.awt.Color(0, 102, 153));
        P1Jail.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P1Jail, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 710, 20, 20));

        P2Jail.setBackground(new java.awt.Color(0, 102, 153));
        P2Jail.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P2Jail, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 710, 20, 20));

        P3Jail.setBackground(new java.awt.Color(0, 102, 153));
        P3Jail.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P3Jail, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 740, 20, 20));

        P4Jail.setBackground(new java.awt.Color(0, 102, 153));
        P4Jail.setPreferredSize(new java.awt.Dimension(50, 50));
        LayeredPaneGame.add(P4Jail, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 740, 20, 20));

        boardOwnedLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 680, 10, 10));

        boardOwnedLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 680, 10, 10));

        boardOwnedLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 680, 10, 10));

        boardOwnedLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 680, 10, 10));

        boardOwnedLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 680, 10, 10));

        boardOwnedLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 680, 10, 10));

        boardOwnedLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 650, 10, 10));

        boardOwnedLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 600, 10, 10));

        boardOwnedLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 530, 10, 10));

        boardOwnedLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, 10, 10));

        boardOwnedLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 390, 10, 10));

        boardOwnedLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, 10, 10));

        boardOwnedLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 10, 10));

        boardOwnedLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 10, 10));

        boardOwnedLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 10, 10));

        boardOwnedLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 10, 10));

        boardOwnedLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 10, 10));

        boardOwnedLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 10, 10));

        boardOwnedLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 10, 10));

        boardOwnedLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 10, 10));

        boardOwnedLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 120, 10, 10));

        boardOwnedLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 120, 10, 10));

        boardOwnedLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 140, 10, 10));

        boardOwnedLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 200, 10, 10));

        boardOwnedLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 330, 10, 10));

        boardOwnedLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 400, 10, 10));

        boardOwnedLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, 10, 10));

        boardOwnedLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 590, 10, 10));

        boardOwnedLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/NotOwned.png"))); // NOI18N
        LayeredPaneGame.add(boardOwnedLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 660, 10, 10));
        LayeredPaneGame.add(housesLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 700, 60, 20));
        LayeredPaneGame.add(housesLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 700, 60, 20));
        LayeredPaneGame.add(housesLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 700, 70, 20));
        LayeredPaneGame.add(housesLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 700, 70, 20));
        LayeredPaneGame.add(housesLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 700, 60, 20));
        LayeredPaneGame.add(housesLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 630, 20, 60));
        LayeredPaneGame.add(housesLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 20, 60));
        LayeredPaneGame.add(housesLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, 20, 60));
        LayeredPaneGame.add(housesLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 20, 60));
        LayeredPaneGame.add(housesLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, 20, 60));
        LayeredPaneGame.add(housesLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 20, 60));
        LayeredPaneGame.add(housesLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 60, 20));
        LayeredPaneGame.add(housesLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 90, 60, 20));
        LayeredPaneGame.add(housesLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 60, 20));
        LayeredPaneGame.add(housesLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, 60, 20));
        LayeredPaneGame.add(housesLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 60, 20));
        LayeredPaneGame.add(housesLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 90, 60, 20));
        LayeredPaneGame.add(housesLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 110, 20, 60));
        LayeredPaneGame.add(housesLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 180, 20, 60));
        LayeredPaneGame.add(housesLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 310, 20, 60));
        LayeredPaneGame.add(housesLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 500, 20, 60));
        LayeredPaneGame.add(housesLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 630, 20, 60));

        GameBoard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/Gameboard.jpg"))); // NOI18N
        LayeredPaneGame.add(GameBoard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 800));

        DiceRoll.setText("Roll Dice");
        DiceRoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiceRollActionPerformed(evt);
            }
        });

        BuyPropertyButton.setText("Buy Property");
        BuyPropertyButton.setEnabled(false);
        BuyPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuyPropertyButtonActionPerformed(evt);
            }
        });

        DeclinePropertyButton.setText("Decline");
        DeclinePropertyButton.setEnabled(false);
        DeclinePropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeclinePropertyButtonActionPerformed(evt);
            }
        });

        BuyPropertyLabel.setMaximumSize(new java.awt.Dimension(75, 75));
        BuyPropertyLabel.setMinimumSize(new java.awt.Dimension(75, 75));

        PlayerOneInfoLabel.setText("Player 1 Money");

        PlayerTwoInfoLabel.setText("Player 2 Money");

        PlayerThreeInfoLabel.setText("Player 3 Money");

        PlayerFourInfoLabel.setText("Player 4 Money");

        PlayerOneMoneyLabel.setText("$$$");

        PlayerTwoMoneyLabel.setText("$$$");

        PlayerThreeMoneyLabel.setText("$$$");

        PlayerFourMoneyLabel.setText("$$$");

        PlayerTurnLabel.setText("Player's Turn");

        JailPayFineButton.setText("Pay Fine");
        JailPayFineButton.setEnabled(false);
        JailPayFineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JailPayFineButtonActionPerformed(evt);
            }
        });

        JailGetOutFreeButton.setText("Jail Card");
        JailGetOutFreeButton.setEnabled(false);
        JailGetOutFreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JailGetOutFreeButtonActionPerformed(evt);
            }
        });

        PropBuyInstruction.setText("Property To Buy");

        InfoInstruction.setText("Various Information");

        TransactionInstruction.setText("Transactions");

        PlaceLandedInstructions.setText("Place Landed");

        CardInstruction.setText("Community Chest & Chance");

        javax.swing.GroupLayout PanelGameLayout = new javax.swing.GroupLayout(PanelGame);
        PanelGame.setLayout(PanelGameLayout);
        PanelGameLayout.setHorizontalGroup(
            PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGameLayout.createSequentialGroup()
                .addComponent(LayeredPaneGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGameLayout.createSequentialGroup()
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelGameLayout.createSequentialGroup()
                                        .addComponent(FirstDiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(SecondDiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(PlayerTurnLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PlaceLandedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BuyPropertyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PropBuyInstruction)
                                    .addComponent(PlaceLandedInstructions)))
                            .addGroup(PanelGameLayout.createSequentialGroup()
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(JailGetOutFreeButton)
                                    .addGroup(PanelGameLayout.createSequentialGroup()
                                        .addComponent(DiceRoll)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JailPayFineButton)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PropertyNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGameLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InfoInstruction)
                                    .addComponent(InfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TransactionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CardInstruction))))
                        .addContainerGap())
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelGameLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(PlayerThreeMoneyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(PlayerFourMoneyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelGameLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(PlayerOneMoneyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(PlayerTwoMoneyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelGameLayout.createSequentialGroup()
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(PlayerOneInfoLabel)
                                        .addComponent(PlayerThreeInfoLabel))
                                    .addComponent(DeclinePropertyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelGameLayout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(PlayerTwoInfoLabel)
                                            .addComponent(PlayerFourInfoLabel)))
                                    .addGroup(PanelGameLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BuyPropertyButton)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(TransactionInstruction)
                        .addGap(157, 157, 157))))
        );
        PanelGameLayout.setVerticalGroup(
            PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGameLayout.createSequentialGroup()
                .addComponent(LayeredPaneGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
            .addGroup(PanelGameLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addComponent(PlayerTurnLabel)
                        .addGap(26, 26, 26)
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FirstDiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SecondDiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addComponent(PlaceLandedInstructions)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PlaceLandedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PropBuyInstruction)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BuyPropertyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DiceRoll)
                            .addComponent(JailPayFineButton)))
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(PropertyNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JailGetOutFreeButton)
                    .addComponent(InfoInstruction))
                .addGap(18, 18, 18)
                .addComponent(InfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DeclinePropertyButton)
                    .addComponent(BuyPropertyButton)
                    .addComponent(TransactionInstruction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TransactionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PlayerOneInfoLabel)
                    .addComponent(PlayerTwoInfoLabel)
                    .addComponent(CardInstruction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelGameLayout.createSequentialGroup()
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PlayerOneMoneyLabel)
                            .addComponent(PlayerTwoMoneyLabel))
                        .addGap(66, 66, 66)
                        .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PlayerThreeInfoLabel)
                            .addComponent(PlayerFourInfoLabel)))
                    .addComponent(CardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(PanelGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PlayerThreeMoneyLabel)
                    .addComponent(PlayerFourMoneyLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ViewPropertyButton.setText("Owned Props");
        ViewPropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewPropertyButtonActionPerformed(evt);
            }
        });

        BuyHousesButton.setText("Buy Houses");
        BuyHousesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuyHousesButtonActionPerformed(evt);
            }
        });

        BuySellPropertiesButton.setText("Buy/Sell Properties");
        BuySellPropertiesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuySellPropertiesButtonActionPerformed(evt);
            }
        });

        MusicLabel.setText("Music Options");

        PlayPauseMusicButton.setText("Play/Pause");
        PlayPauseMusicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayPauseMusicButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(ViewPropertyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BuyHousesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BuySellPropertiesButton)
                .addGap(129, 129, 129)
                .addComponent(MusicLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PlayPauseMusicButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelGame, javax.swing.GroupLayout.PREFERRED_SIZE, 1327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewPropertyButton)
                    .addComponent(BuyHousesButton)
                    .addComponent(BuySellPropertiesButton)
                    .addComponent(MusicLabel)
                    .addComponent(PlayPauseMusicButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelGame, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DiceRollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiceRollActionPerformed
        moveCharacters();
    }//GEN-LAST:event_DiceRollActionPerformed

    private void BuyPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuyPropertyButtonActionPerformed
        buyProperty();
        enableBoard();
    }//GEN-LAST:event_BuyPropertyButtonActionPerformed

    private void DeclinePropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeclinePropertyButtonActionPerformed
        enableBoard();
    }//GEN-LAST:event_DeclinePropertyButtonActionPerformed

    private void JailPayFineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JailPayFineButtonActionPerformed
        payFineJail();
        moveCharacters();
    }//GEN-LAST:event_JailPayFineButtonActionPerformed

    private void JailGetOutFreeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JailGetOutFreeButtonActionPerformed
        getOutFree();
        moveCharacters();
    }//GEN-LAST:event_JailGetOutFreeButtonActionPerformed

    private void ViewPropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewPropertyButtonActionPerformed
        ownedProperties.setVisible(true);
    }//GEN-LAST:event_ViewPropertyButtonActionPerformed

    private void BuyHousesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuyHousesButtonActionPerformed
        homeBuyer.setVisible(true);
    }//GEN-LAST:event_BuyHousesButtonActionPerformed

    private void BuySellPropertiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuySellPropertiesButtonActionPerformed
        buySellProperties.setVisible(true);
    }//GEN-LAST:event_BuySellPropertiesButtonActionPerformed

    private void PlayPauseMusicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayPauseMusicButtonActionPerformed
        isPlayingMusic = Monopoly.PauseMusic(isPlayingMusic);
    }//GEN-LAST:event_PlayPauseMusicButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BuyHousesButton;
    private javax.swing.JButton BuyPropertyButton;
    private javax.swing.JLabel BuyPropertyLabel;
    private javax.swing.JButton BuySellPropertiesButton;
    private javax.swing.JLabel CardInstruction;
    private javax.swing.JLabel CardLabel;
    private javax.swing.JButton DeclinePropertyButton;
    private javax.swing.JButton DiceRoll;
    private javax.swing.JLabel FirstDiceImageLabel;
    private javax.swing.JLabel GameBoard;
    private javax.swing.JLabel InfoInstruction;
    private javax.swing.JLabel InfoLabel;
    private javax.swing.JButton JailGetOutFreeButton;
    private javax.swing.JButton JailPayFineButton;
    private javax.swing.JLayeredPane LayeredPaneGame;
    private javax.swing.JLabel MusicLabel;
    private javax.swing.JLabel P1Jail;
    private javax.swing.JLabel P1S0;
    private javax.swing.JLabel P1S1;
    private javax.swing.JLabel P1S10;
    private javax.swing.JLabel P1S11;
    private javax.swing.JLabel P1S12;
    private javax.swing.JLabel P1S13;
    private javax.swing.JLabel P1S14;
    private javax.swing.JLabel P1S15;
    private javax.swing.JLabel P1S16;
    private javax.swing.JLabel P1S17;
    private javax.swing.JLabel P1S18;
    private javax.swing.JLabel P1S19;
    private javax.swing.JLabel P1S2;
    private javax.swing.JLabel P1S20;
    private javax.swing.JLabel P1S21;
    private javax.swing.JLabel P1S22;
    private javax.swing.JLabel P1S23;
    private javax.swing.JLabel P1S24;
    private javax.swing.JLabel P1S25;
    private javax.swing.JLabel P1S26;
    private javax.swing.JLabel P1S27;
    private javax.swing.JLabel P1S28;
    private javax.swing.JLabel P1S29;
    private javax.swing.JLabel P1S3;
    private javax.swing.JLabel P1S30;
    private javax.swing.JLabel P1S31;
    private javax.swing.JLabel P1S32;
    private javax.swing.JLabel P1S33;
    private javax.swing.JLabel P1S34;
    private javax.swing.JLabel P1S35;
    private javax.swing.JLabel P1S36;
    private javax.swing.JLabel P1S37;
    private javax.swing.JLabel P1S38;
    private javax.swing.JLabel P1S39;
    private javax.swing.JLabel P1S4;
    private javax.swing.JLabel P1S5;
    private javax.swing.JLabel P1S6;
    private javax.swing.JLabel P1S7;
    private javax.swing.JLabel P1S8;
    private javax.swing.JLabel P1S9;
    private javax.swing.JLabel P2Jail;
    private javax.swing.JLabel P2S0;
    private javax.swing.JLabel P2S1;
    private javax.swing.JLabel P2S10;
    private javax.swing.JLabel P2S11;
    private javax.swing.JLabel P2S12;
    private javax.swing.JLabel P2S13;
    private javax.swing.JLabel P2S14;
    private javax.swing.JLabel P2S15;
    private javax.swing.JLabel P2S16;
    private javax.swing.JLabel P2S17;
    private javax.swing.JLabel P2S18;
    private javax.swing.JLabel P2S19;
    private javax.swing.JLabel P2S2;
    private javax.swing.JLabel P2S20;
    private javax.swing.JLabel P2S21;
    private javax.swing.JLabel P2S22;
    private javax.swing.JLabel P2S23;
    private javax.swing.JLabel P2S24;
    private javax.swing.JLabel P2S25;
    private javax.swing.JLabel P2S26;
    private javax.swing.JLabel P2S27;
    private javax.swing.JLabel P2S28;
    private javax.swing.JLabel P2S29;
    private javax.swing.JLabel P2S3;
    private javax.swing.JLabel P2S30;
    private javax.swing.JLabel P2S31;
    private javax.swing.JLabel P2S32;
    private javax.swing.JLabel P2S33;
    private javax.swing.JLabel P2S34;
    private javax.swing.JLabel P2S35;
    private javax.swing.JLabel P2S36;
    private javax.swing.JLabel P2S37;
    private javax.swing.JLabel P2S38;
    private javax.swing.JLabel P2S39;
    private javax.swing.JLabel P2S4;
    private javax.swing.JLabel P2S5;
    private javax.swing.JLabel P2S6;
    private javax.swing.JLabel P2S7;
    private javax.swing.JLabel P2S8;
    private javax.swing.JLabel P2S9;
    private javax.swing.JLabel P3Jail;
    private javax.swing.JLabel P3S0;
    private javax.swing.JLabel P3S1;
    private javax.swing.JLabel P3S10;
    private javax.swing.JLabel P3S11;
    private javax.swing.JLabel P3S12;
    private javax.swing.JLabel P3S13;
    private javax.swing.JLabel P3S14;
    private javax.swing.JLabel P3S15;
    private javax.swing.JLabel P3S16;
    private javax.swing.JLabel P3S17;
    private javax.swing.JLabel P3S18;
    private javax.swing.JLabel P3S19;
    private javax.swing.JLabel P3S2;
    private javax.swing.JLabel P3S20;
    private javax.swing.JLabel P3S21;
    private javax.swing.JLabel P3S22;
    private javax.swing.JLabel P3S23;
    private javax.swing.JLabel P3S24;
    private javax.swing.JLabel P3S25;
    private javax.swing.JLabel P3S26;
    private javax.swing.JLabel P3S27;
    private javax.swing.JLabel P3S28;
    private javax.swing.JLabel P3S29;
    private javax.swing.JLabel P3S3;
    private javax.swing.JLabel P3S30;
    private javax.swing.JLabel P3S31;
    private javax.swing.JLabel P3S32;
    private javax.swing.JLabel P3S33;
    private javax.swing.JLabel P3S34;
    private javax.swing.JLabel P3S35;
    private javax.swing.JLabel P3S36;
    private javax.swing.JLabel P3S37;
    private javax.swing.JLabel P3S38;
    private javax.swing.JLabel P3S39;
    private javax.swing.JLabel P3S4;
    private javax.swing.JLabel P3S5;
    private javax.swing.JLabel P3S6;
    private javax.swing.JLabel P3S7;
    private javax.swing.JLabel P3S8;
    private javax.swing.JLabel P3S9;
    private javax.swing.JLabel P4Jail;
    private javax.swing.JLabel P4S0;
    private javax.swing.JLabel P4S1;
    private javax.swing.JLabel P4S10;
    private javax.swing.JLabel P4S11;
    private javax.swing.JLabel P4S12;
    private javax.swing.JLabel P4S13;
    private javax.swing.JLabel P4S14;
    private javax.swing.JLabel P4S15;
    private javax.swing.JLabel P4S16;
    private javax.swing.JLabel P4S17;
    private javax.swing.JLabel P4S18;
    private javax.swing.JLabel P4S19;
    private javax.swing.JLabel P4S2;
    private javax.swing.JLabel P4S20;
    private javax.swing.JLabel P4S21;
    private javax.swing.JLabel P4S22;
    private javax.swing.JLabel P4S23;
    private javax.swing.JLabel P4S24;
    private javax.swing.JLabel P4S25;
    private javax.swing.JLabel P4S26;
    private javax.swing.JLabel P4S27;
    private javax.swing.JLabel P4S28;
    private javax.swing.JLabel P4S29;
    private javax.swing.JLabel P4S3;
    private javax.swing.JLabel P4S30;
    private javax.swing.JLabel P4S31;
    private javax.swing.JLabel P4S32;
    private javax.swing.JLabel P4S33;
    private javax.swing.JLabel P4S34;
    private javax.swing.JLabel P4S35;
    private javax.swing.JLabel P4S36;
    private javax.swing.JLabel P4S37;
    private javax.swing.JLabel P4S38;
    private javax.swing.JLabel P4S39;
    private javax.swing.JLabel P4S4;
    private javax.swing.JLabel P4S5;
    private javax.swing.JLabel P4S6;
    private javax.swing.JLabel P4S7;
    private javax.swing.JLabel P4S8;
    private javax.swing.JLabel P4S9;
    private javax.swing.JPanel PanelGame;
    private javax.swing.JLabel PlaceLandedInstructions;
    private javax.swing.JLabel PlaceLandedLabel;
    private javax.swing.JButton PlayPauseMusicButton;
    private javax.swing.JLabel PlayerFourInfoLabel;
    private javax.swing.JLabel PlayerFourMoneyLabel;
    private javax.swing.JLabel PlayerOneInfoLabel;
    private javax.swing.JLabel PlayerOneMoneyLabel;
    private javax.swing.JLabel PlayerThreeInfoLabel;
    private javax.swing.JLabel PlayerThreeMoneyLabel;
    private javax.swing.JLabel PlayerTurnLabel;
    private javax.swing.JLabel PlayerTwoInfoLabel;
    private javax.swing.JLabel PlayerTwoMoneyLabel;
    private javax.swing.JLabel PropBuyInstruction;
    private javax.swing.JLabel PropertyNameLabel;
    private javax.swing.JLabel SecondDiceImageLabel;
    private javax.swing.JLabel TransactionInstruction;
    private javax.swing.JLabel TransactionLabel;
    private javax.swing.JButton ViewPropertyButton;
    private javax.swing.JLabel boardOwnedLabel1;
    private javax.swing.JLabel boardOwnedLabel10;
    private javax.swing.JLabel boardOwnedLabel11;
    private javax.swing.JLabel boardOwnedLabel12;
    private javax.swing.JLabel boardOwnedLabel13;
    private javax.swing.JLabel boardOwnedLabel14;
    private javax.swing.JLabel boardOwnedLabel15;
    private javax.swing.JLabel boardOwnedLabel16;
    private javax.swing.JLabel boardOwnedLabel17;
    private javax.swing.JLabel boardOwnedLabel18;
    private javax.swing.JLabel boardOwnedLabel19;
    private javax.swing.JLabel boardOwnedLabel2;
    private javax.swing.JLabel boardOwnedLabel20;
    private javax.swing.JLabel boardOwnedLabel21;
    private javax.swing.JLabel boardOwnedLabel22;
    private javax.swing.JLabel boardOwnedLabel23;
    private javax.swing.JLabel boardOwnedLabel24;
    private javax.swing.JLabel boardOwnedLabel25;
    private javax.swing.JLabel boardOwnedLabel26;
    private javax.swing.JLabel boardOwnedLabel27;
    private javax.swing.JLabel boardOwnedLabel28;
    private javax.swing.JLabel boardOwnedLabel29;
    private javax.swing.JLabel boardOwnedLabel3;
    private javax.swing.JLabel boardOwnedLabel4;
    private javax.swing.JLabel boardOwnedLabel5;
    private javax.swing.JLabel boardOwnedLabel6;
    private javax.swing.JLabel boardOwnedLabel7;
    private javax.swing.JLabel boardOwnedLabel8;
    private javax.swing.JLabel boardOwnedLabel9;
    private javax.swing.JLabel housesLabel1;
    private javax.swing.JLabel housesLabel10;
    private javax.swing.JLabel housesLabel11;
    private javax.swing.JLabel housesLabel12;
    private javax.swing.JLabel housesLabel13;
    private javax.swing.JLabel housesLabel14;
    private javax.swing.JLabel housesLabel15;
    private javax.swing.JLabel housesLabel16;
    private javax.swing.JLabel housesLabel17;
    private javax.swing.JLabel housesLabel18;
    private javax.swing.JLabel housesLabel19;
    private javax.swing.JLabel housesLabel2;
    private javax.swing.JLabel housesLabel20;
    private javax.swing.JLabel housesLabel21;
    private javax.swing.JLabel housesLabel22;
    private javax.swing.JLabel housesLabel3;
    private javax.swing.JLabel housesLabel4;
    private javax.swing.JLabel housesLabel5;
    private javax.swing.JLabel housesLabel6;
    private javax.swing.JLabel housesLabel7;
    private javax.swing.JLabel housesLabel8;
    private javax.swing.JLabel housesLabel9;
    // End of variables declaration//GEN-END:variables
}
