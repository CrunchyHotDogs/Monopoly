package monopoly;

/**
 * Starts the main menu.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Monopoly {
    private static MainGame mg = null;
    private static MusicPlayer mp = null;
    
    /**
     * Creates a new instance of the monopoly game.
     * 
     * @param PlayerCount The number of players playing.
     * @param PlayerNames An array of the player's names.
     * @param PlayerImages An array of the player's image locations.
     * @param GameType The board style that is being played.
     * @param StartingGold The starting money for the game.
     */
    public static void createMonopolyGame(int PlayerCount, String[] PlayerNames, String[] PlayerImages, String GameType, int StartingGold, String[] PlayerOwnedLabels) {
        mg = new MainGame(PlayerCount, PlayerNames, PlayerImages, GameType, StartingGold, PlayerOwnedLabels);
        mg.setVisible(true);
    }
    
    /**
     * Updates the houses on the monopoly board.
     */
    public static void updateMonopolyHouses() {
        mg.updateHousing();
        mg.updateMoney();
    }
    
    public static void updatePropertiesOwned() {
        mg.updatePropertiesOwned();
        mg.updateMoney();
    }
    
    public static boolean PauseMusic(boolean IsMusicPlaying) {
        boolean SendBooleanBack;
        
        if (IsMusicPlaying == true) {
            SendBooleanBack = false;
            mp.StopMusic();
        }
        else {
            SendBooleanBack = true;
            mp.StartMusic();
        }
        
        return SendBooleanBack;
    }
    
    public static void ChangeMusic() {
        mp.ChooseSong("/monopoly/Music/Sevje.wav");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainMenu().setVisible(true);
        mp = new MusicPlayer();
        mp.StartMusic();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (mp != null) {
                    mp.StopMusic();
                }
            }
        });
    }
}
