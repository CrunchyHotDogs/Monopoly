package monopoly;

/**
 * Starts the main menu.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class Monopoly {
    private static MainGame mainGame = null;
    private static MusicPlayer musicPlayer = null;
    
    /**
     * Creates a new instance of the monopoly game.
     * 
     * @param playerCount The number of players playing.
     * @param playerNames An array of the player's names.
     * @param playerImages An array of the player's image locations.
     * @param gameType The board style that is being played.
     * @param startingGold The starting money for the game.
     * @param playerOwnedLabels The color that will display above a property if a player owns it.
     */
    public static void createMonopolyGame(int playerCount, String[] playerNames, String[] playerImages, String gameType, int startingGold, String[] playerOwnedLabels) {
        mainGame = new MainGame(playerCount, playerNames, playerImages, gameType, startingGold, playerOwnedLabels);
        mainGame.setVisible(true);
    }
    
    /**
     * Updates the houses on the monopoly board.
     */
    public static void updateMonopolyHouses() {
        mainGame.updateHousing();
        mainGame.updateMoney();
    }
    
    public static void updatePropertiesOwned() {
        mainGame.updatePropertiesOwned();
        mainGame.updateMoney();
    }
    
    public static boolean pauseMusic(boolean isMusicPlaying) {
        boolean sendBooleanBack;
        
        if (isMusicPlaying == true) {
            sendBooleanBack = false;
            musicPlayer.stopMusic();
        }
        else {
            sendBooleanBack = true;
            musicPlayer.startMusic();
        }
        
        return sendBooleanBack;
    }
    
    public static void changeMusic() {
        musicPlayer.chooseSong("/monopoly/Music/Sevje.wav");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainMenu().setVisible(true);
        musicPlayer = new MusicPlayer();
        musicPlayer.startMusic();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (musicPlayer != null) {
                    musicPlayer.stopMusic();
                }
            }
        });
    }
}
