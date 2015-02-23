/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

/**
 *
 * @author Kyle
 */
public class Card {
    private String description, outputText, key;
    
    public Card(String sentDescription, String sentKey, String sentOutputText) {
        this.description = sentDescription;
        this.key = sentKey;
        this.outputText = sentOutputText;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getOutputText() {
        return this.outputText;
    }
}
