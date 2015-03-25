package monopoly;

import database.DatabaseResults;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * A main menu for the game Monopoly. Allows the user to customize the game.
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class MainMenu extends javax.swing.JFrame {
    final String playerIconPath = "Player Images\\";
    final String playerOwnerImagePath = "/monopoly/Images/";
    
    //The images that are available for use.
    String[] possibleImages = new String[4];
    String[] arrayOfExcisting;
    String[] arrayOfBoards;
    int boardId = -1;
    File folder;
    File[] listOfFiles;
    JTextField listOfNames[] = new JTextField[4];
    JButton iconButtons[] = new JButton[4];    
    
    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        int Counter = 0;
        
        folder = new File(playerIconPath);
        listOfFiles = folder.listFiles();
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Counter += 1;
            }
        }
        
        arrayOfExcisting = new String[Counter];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                arrayOfExcisting[i] = listOfFiles[i].getName();
                if (arrayOfExcisting[i].indexOf(".") > 0) {
                    arrayOfExcisting[i] = arrayOfExcisting[i].substring(0, arrayOfExcisting[i].lastIndexOf("."));
                }
            }
        }
        
        possibleImages[0] = playerIconPath + "Player 1.jpg";
        possibleImages[1] = playerIconPath + "Player 2.jpg";
        possibleImages[2] = playerIconPath + "Player 3.jpg";
        possibleImages[3] = playerIconPath + "Player 4.jpg";
    
        initComponents();
        
        listOfNames[0] = NameOneText;
        listOfNames[1] = NameTwoText;
        listOfNames[2] = NameThreeText;
        listOfNames[3] = NameFourText;
        
        iconButtons[0] = SelectIcon1Button;
        iconButtons[1] = SelectIcon2Button;
        iconButtons[2] = SelectIcon3Button;
        iconButtons[3] = SelectIcon4Button;
    }

    private void setUpGame() {
        this.setVisible(false);
        
        String[] possibleOwnerImages = new String[4];
        possibleOwnerImages[0] = playerOwnerImagePath + "Player1Owned.png";
        possibleOwnerImages[1] = playerOwnerImagePath + "Player2Owned.png";
        possibleOwnerImages[2] = playerOwnerImagePath + "Player3Owned.png";
        possibleOwnerImages[3] = playerOwnerImagePath + "Player4Owned.png";
        //Creates a variable and give it a default of the Original Board. Gives the game a default value of $1500 for money.
        String gameType = "Original";
        int startingGold = 1500;
        
        int numOfPlayers = 0;
        
        //Tries to get the game type the user wants to play.
        if (GameTypeList.getSelectedValue() != null) {
            gameType = GameTypeList.getSelectedValue().toString();
        }
        
        //Tries to get the money the user wants to play with.
        if (!StartGoldText.equals("")) {
            try {
                startingGold = Integer.parseInt(StartGoldText.getText());
            }
            catch (NumberFormatException e) {
                startingGold = 1000;
            }
        }
        
        //Finds how many players the user has entered.
        for (int i = 0; i < listOfNames.length; i++) {
            if (!listOfNames[i].getText().equals("")) {
                numOfPlayers += 1;
            }
        }
        
        //Checks to see if there is at least two players entered.
        if (numOfPlayers > 1) {
            String[] playerNames = new String[numOfPlayers];
            String[] playerImages = new String[numOfPlayers];
            String[] playerOwnedImages = new String[numOfPlayers];
            int counter = 0;
        
            //Adds the player's info into an array.
            for (int i = 0; i < listOfNames.length; i++) {
                if (!listOfNames[i].getText().equals("")) {
                    playerNames[counter] = listOfNames[i].getText();
                    playerImages[counter] = possibleImages[i];
                    playerOwnedImages[counter] = possibleOwnerImages[counter];
                    counter += 1;
                }
            }
        
            //Create a new monopoly game.
            Monopoly.createMonopolyGame(numOfPlayers, playerNames, playerImages, gameType, startingGold, playerOwnedImages, boardId);
        }
        //If the user didn't enter enough players.
        else {
            String[] playerNames = new String[2];
            String[] playerImages = new String[2];
            String[] playerOwnerImages = new String[2];
            
            playerNames[0] = "Kyle";
            playerNames[1] = "Len";
        
            playerImages[0] = possibleImages[0];
            playerImages[1] = possibleImages[1];
            
            playerOwnerImages[0] = possibleOwnerImages[0];
            playerOwnerImages[1] = possibleOwnerImages[1];
            
            Monopoly.createMonopolyGame(2, playerNames, playerImages, gameType, startingGold, playerOwnerImages, boardId);
        }
    }
    
    private void setBoard() {
        DefaultListModel<BoardInfo> model = new DefaultListModel();
        
        try (Connection conn = credentials.Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM board;");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BoardInfo boardInfo = new BoardInfo(rs.getString("board_name"), rs.getInt("board_id"));
                model.addElement(boardInfo);
            }
        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        String popUpTitle = "Choose a Board";
        JList mapList = new JList();
        mapList.setModel(model);
        mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JOptionPane.showMessageDialog(null, mapList, popUpTitle, JOptionPane.PLAIN_MESSAGE);
        if (mapList.getSelectedValue() != null) {
            BoardInfo bi = (BoardInfo) mapList.getSelectedValue();
            boardId = bi.getId();
        }
    }
    
    private void setIcon(int playerToSet) {
        String popUpTitle = "";
        JList iconList = new JList(arrayOfExcisting);
        iconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        if (!listOfNames[playerToSet].getText().equals("")) {
            popUpTitle = "Pick An Icon For " + listOfNames[playerToSet].getText();
        }
        else {
            popUpTitle = "Choose An Icon";
        }
        
        JOptionPane.showMessageDialog(null, iconList, popUpTitle, JOptionPane.PLAIN_MESSAGE);
        
        if (iconList.getSelectedValue() != null) {
            possibleImages[playerToSet] = playerIconPath + iconList.getSelectedValue().toString() + ".jpg";
            iconButtons[playerToSet].setText(iconList.getSelectedValue().toString());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TitleAuthorLabel = new javax.swing.JLabel();
        StartGameButton = new javax.swing.JButton();
        NameOneLabel = new javax.swing.JLabel();
        NameTwoLabel = new javax.swing.JLabel();
        NameThreeLabel = new javax.swing.JLabel();
        NameFourLabel = new javax.swing.JLabel();
        NameOneText = new javax.swing.JTextField();
        NameTwoText = new javax.swing.JTextField();
        NameThreeText = new javax.swing.JTextField();
        NameFourText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        GameTypeList = new javax.swing.JList();
        GameboardTypeLabel = new javax.swing.JLabel();
        StartGoldLabel = new javax.swing.JLabel();
        StartGoldText = new javax.swing.JTextField();
        SelectIcon1Button = new javax.swing.JButton();
        SelectIcon2Button = new javax.swing.JButton();
        SelectIcon3Button = new javax.swing.JButton();
        SelectIcon4Button = new javax.swing.JButton();
        ChangeMusic = new javax.swing.JButton();
        selectMapButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        TitleAuthorLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopoly/Images/Logo.jpg"))); // NOI18N

        StartGameButton.setText("Start Game");
        StartGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartGameButtonActionPerformed(evt);
            }
        });

        NameOneLabel.setText("Player 1 Name: ");

        NameTwoLabel.setText("Player 2 Name: ");

        NameThreeLabel.setText("Player 3 Name: ");

        NameFourLabel.setText("Player 4 Name: ");

        GameTypeList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Original", "WoW", "Zelda", "Cat", "Harry Potter" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(GameTypeList);

        GameboardTypeLabel.setText("Gameboard: ");

        StartGoldLabel.setText("Starting Gold:");

        StartGoldText.setText("1500");

        SelectIcon1Button.setText("Select Icon");
        SelectIcon1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectIcon1ButtonActionPerformed(evt);
            }
        });

        SelectIcon2Button.setText("Select Icon");
        SelectIcon2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectIcon2ButtonActionPerformed(evt);
            }
        });

        SelectIcon3Button.setText("Select Icon");
        SelectIcon3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectIcon3ButtonActionPerformed(evt);
            }
        });

        SelectIcon4Button.setText("Select Icon");
        SelectIcon4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectIcon4ButtonActionPerformed(evt);
            }
        });

        ChangeMusic.setText("Change Music");
        ChangeMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeMusicActionPerformed(evt);
            }
        });

        selectMapButton.setText("Select Map");
        selectMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMapButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(TitleAuthorLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(420, 420, 420)
                        .addComponent(GameboardTypeLabel)
                        .addGap(35, 35, 35)
                        .addComponent(StartGoldLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameFourLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(NameOneLabel)
                                    .addComponent(NameTwoLabel)
                                    .addComponent(NameThreeLabel))))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameOneText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NameTwoText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NameThreeText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NameFourText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SelectIcon1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SelectIcon2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SelectIcon3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SelectIcon4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(100, 100, 100)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(StartGoldText, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(selectMapButton))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(StartGameButton)
                            .addComponent(ChangeMusic))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TitleAuthorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GameboardTypeLabel)
                    .addComponent(StartGoldLabel))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(NameOneLabel)
                        .addGap(13, 13, 13)
                        .addComponent(NameTwoLabel)
                        .addGap(10, 10, 10)
                        .addComponent(NameThreeLabel)
                        .addGap(14, 14, 14)
                        .addComponent(NameFourLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(NameOneText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(NameTwoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(NameThreeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(NameFourText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(SelectIcon1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SelectIcon2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SelectIcon3Button, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SelectIcon4Button, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(StartGoldText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(StartGameButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ChangeMusic))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectMapButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Starts the game of monopoly. Gets all of the values that the user has filled in on the frame, or 
     * if they have not filled in the slots set default options.
     * @param evt The button press.
     */
    private void StartGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartGameButtonActionPerformed
        setUpGame();
    }//GEN-LAST:event_StartGameButtonActionPerformed

    private void SelectIcon1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectIcon1ButtonActionPerformed
        setIcon(0);
    }//GEN-LAST:event_SelectIcon1ButtonActionPerformed

    private void SelectIcon2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectIcon2ButtonActionPerformed
        setIcon(1);
    }//GEN-LAST:event_SelectIcon2ButtonActionPerformed

    private void SelectIcon3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectIcon3ButtonActionPerformed
        setIcon(2);
    }//GEN-LAST:event_SelectIcon3ButtonActionPerformed

    private void SelectIcon4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectIcon4ButtonActionPerformed
        setIcon(3);
    }//GEN-LAST:event_SelectIcon4ButtonActionPerformed

    private void ChangeMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeMusicActionPerformed
        Monopoly.changeMusic();
    }//GEN-LAST:event_ChangeMusicActionPerformed

    private void selectMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectMapButtonActionPerformed
        setBoard();
    }//GEN-LAST:event_selectMapButtonActionPerformed

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
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ChangeMusic;
    private javax.swing.JList GameTypeList;
    private javax.swing.JLabel GameboardTypeLabel;
    private javax.swing.JLabel NameFourLabel;
    private javax.swing.JTextField NameFourText;
    private javax.swing.JLabel NameOneLabel;
    private javax.swing.JTextField NameOneText;
    private javax.swing.JLabel NameThreeLabel;
    private javax.swing.JTextField NameThreeText;
    private javax.swing.JLabel NameTwoLabel;
    private javax.swing.JTextField NameTwoText;
    private javax.swing.JButton SelectIcon1Button;
    private javax.swing.JButton SelectIcon2Button;
    private javax.swing.JButton SelectIcon3Button;
    private javax.swing.JButton SelectIcon4Button;
    private javax.swing.JButton StartGameButton;
    private javax.swing.JLabel StartGoldLabel;
    private javax.swing.JTextField StartGoldText;
    private javax.swing.JLabel TitleAuthorLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton selectMapButton;
    // End of variables declaration//GEN-END:variables
}
