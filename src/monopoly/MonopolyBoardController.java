/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Kyle
 */
public class MonopolyBoardController {
    private ArrayList<Player> playerGroup = new ArrayList<>();
    private int turnCounter;
    
    public MonopolyBoardController(ArrayList<Player> sentPlayers) {
        this.playerGroup = sentPlayers;
        this.turnCounter = 0;
    }
    
    public int getTurnCounter() {
        return this.turnCounter;
    }
    
    public int[] rollDice() {
        Random rand = new Random();
        int[] diceRolls = { rand.nextInt((6 - 1) + 1) + 1,
                            rand.nextInt((6 - 1) + 1) + 1};
        
        return diceRolls;
    }
    
    public int[] moveCharacter(int diceRoll) {
        int[] turnAndLocation = new int[2];
        
        playerGroup.get(turnCounter).setLocation(diceRoll);
        turnAndLocation[0] = turnCounter;
        turnAndLocation[1] = playerGroup.get(turnCounter).getLocation();
        
        return turnAndLocation;
    }
    
    public void performAction(String propertyType) {
        
    }
}
