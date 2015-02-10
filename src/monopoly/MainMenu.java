/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly;

import java.io.File;
import java.util.Arrays;
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
    String[] PossibleImages = new String[4];
    String[] ArrayOfExcisting;
    File Folder;
    File[] ListOfFiles;
    JTextField ListOfNames[] = new JTextField[4];
    JButton iconButtons[] = new JButton[4];    
    
    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        int Counter = 0;
        
        Folder = new File(playerIconPath);
        ListOfFiles = Folder.listFiles();
        
        for (int i = 0; i < ListOfFiles.length; i++) {
            if (ListOfFiles[i].isFile()) {
                Counter += 1;
            }
        }
        
        ArrayOfExcisting = new String[Counter];
        
        for (int i = 0; i < ListOfFiles.length; i++) {
            if (ListOfFiles[i].isFile()) {
                ArrayOfExcisting[i] = ListOfFiles[i].getName();
                if (ArrayOfExcisting[i].indexOf(".") > 0) {
                    ArrayOfExcisting[i] = ArrayOfExcisting[i].substring(0, ArrayOfExcisting[i].lastIndexOf("."));
                }
            }
        }
        
        PossibleImages[0] = playerIconPath + "Player 1.jpg";
        PossibleImages[1] = playerIconPath + "Player 2.jpg";
        PossibleImages[2] = playerIconPath + "Player 3.jpg";
        PossibleImages[3] = playerIconPath + "Player 4.jpg";
    
        initComponents();
        
        ListOfNames[0] = NameOneText;
        ListOfNames[1] = NameTwoText;
        ListOfNames[2] = NameThreeText;
        ListOfNames[3] = NameFourText;
        
        iconButtons[0] = SelectIcon1Button;
        iconButtons[1] = SelectIcon2Button;
        iconButtons[2] = SelectIcon3Button;
        iconButtons[3] = SelectIcon4Button;
    }

    private void setUpGame() {
        this.setVisible(false);
        
        String[] PossibleOwnerImages = new String[4];
        PossibleOwnerImages[0] = playerOwnerImagePath + "Player1Owned.png";
        PossibleOwnerImages[1] = playerOwnerImagePath + "Player2Owned.png";
        PossibleOwnerImages[2] = playerOwnerImagePath + "Player3Owned.png";
        PossibleOwnerImages[3] = playerOwnerImagePath + "Player4Owned.png";
        //Creates a variable and give it a default of the Original Board. Gives the game a default value of $1500 for money.
        String GameType = "Original";
        int StartingGold = 1500;
        
        int NumOfPlayers = 0;
        
        //Tries to get the game type the user wants to play.
        if (GameTypeList.getSelectedValue() != null) {
            GameType = GameTypeList.getSelectedValue().toString();
        }
        
        //Tries to get the money the user wants to play with.
        if (!StartGoldText.equals("")) {
            try {
                StartingGold = Integer.parseInt(StartGoldText.getText());
            }
            catch (NumberFormatException e) {
                StartingGold = 1000;
            }
        }
        
        //Finds how many players the user has entered.
        for (int i = 0; i < ListOfNames.length; i++) {
            if (!ListOfNames[i].getText().equals("")) {
                NumOfPlayers += 1;
            }
        }
        
        //Checks to see if there is at least two players entered.
        if (NumOfPlayers > 1) {
            String[] PlayerNames = new String[NumOfPlayers];
            String[] PlayerImages = new String[NumOfPlayers];
            String[] PlayerOwnedImages = new String[NumOfPlayers];
            int Counter = 0;
        
            //Adds the player's info into an array.
            for (int i = 0; i < ListOfNames.length; i++) {
                if (!ListOfNames[i].getText().equals("")) {
                    PlayerNames[Counter] = ListOfNames[i].getText();
                    PlayerImages[Counter] = PossibleImages[i];
                    PlayerOwnedImages[Counter] = PossibleOwnerImages[Counter];
                    Counter += 1;
                }
            }
        
            //Create a new monopoly game.
            Monopoly.createMonopolyGame(NumOfPlayers, PlayerNames, PlayerImages, GameType, StartingGold, PlayerOwnedImages);
        }
        //If the user didn't enter enough players.
        else {
            String[] PlayerNames = new String[2];
            String[] PlayerImages = new String[2];
            String[] PlayerOwnerImages = new String[2];
            
            PlayerNames[0] = "Kyle";
            PlayerNames[1] = "Len";
        
            PlayerImages[0] = PossibleImages[0];
            PlayerImages[1] = PossibleImages[1];
            
            PlayerOwnerImages[0] = PossibleOwnerImages[0];
            PlayerOwnerImages[1] = PossibleOwnerImages[1];
            
            Monopoly.createMonopolyGame(2, PlayerNames, PlayerImages, GameType, StartingGold, PlayerOwnerImages);
        }
    }
    
    private void setIcon(int PlayerToSet) {
        String popUpTitle = "";
        JList iconList = new JList(ArrayOfExcisting);
        iconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        if (!ListOfNames[PlayerToSet].getText().equals("")) {
            popUpTitle = "Pick An Icon For " + ListOfNames[PlayerToSet].getText();
        }
        else {
            popUpTitle = "Choose An Icon";
        }
        
        JOptionPane.showMessageDialog(null, iconList, popUpTitle, JOptionPane.PLAIN_MESSAGE);
        
        if (iconList.getSelectedValue() != null) {
            PossibleImages[PlayerToSet] = playerIconPath + iconList.getSelectedValue().toString() + ".jpg";
            iconButtons[PlayerToSet].setText(iconList.getSelectedValue().toString());
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(StartGoldText, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartGoldText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(StartGameButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ChangeMusic)))
                .addContainerGap(16, Short.MAX_VALUE))
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
        Monopoly.ChangeMusic();
    }//GEN-LAST:event_ChangeMusicActionPerformed

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
    // End of variables declaration//GEN-END:variables
}
