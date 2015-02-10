/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 * Holds all of the community chest cards. Contains the description of what they
 * do, the name of the community chest card, and the card key (Determines what action to perform).
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class CommunityChestCard {
    private String CCDescription, CCOutputText;
    private String CCKey;
    
    public CommunityChestCard(String CCD, String CCK, String CCOT) {
        this.CCDescription = CCD;
        this.CCKey = CCK;
        this.CCOutputText = CCOT;
    }
    
    public String GetCCDescription() {
        return this.CCDescription;
    }
    
    public String GetCCKey() {
        return this.CCKey;
    }
    
    public String GetCCOutputText() {
        return this.CCOutputText;
    }
}
