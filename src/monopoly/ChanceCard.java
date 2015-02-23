package monopoly;

/**
 * Holds all of the chance cards. Contains the description of what they
 * do, the name of the chance card, and the card key (Determines what action to perform).
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class ChanceCard {
    private String CDescription, COutputText;
    private String CKey;
    
    
    public ChanceCard(String CD, String CK, String COT) {
        this.CDescription = CD;
        this.CKey = CK;
        this.COutputText = COT;
    }
    
    public String GetCDescription() {
        return this.CDescription;
    }
    
    public String GetCKey() {
        return this.CKey;
    }
    
    public String GetCOutputText() {
        return this.COutputText;
    }
}
