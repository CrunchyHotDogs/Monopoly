package monopoly;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author c0621990 - Kyle Crossman
 * @date December 3 2014
 */
public class HomeBuyer extends javax.swing.JFrame {
    ArrayList<Player> PlayerGroup;
    Property[] BoardProperties;
    JButton[][] PropertyGroups;
    int[][] PropertyNumberGroups;
    int CurrentPlayer = 0, CurrentLoc = 0;
    
    /**
     * Creates new form HomeBuyer
     */
    public HomeBuyer(ArrayList<Player> PG, Property[] BP) {
        initComponents();
        
        this.PlayerGroup = PG;
        this.BoardProperties = BP;
        
        //Creates the array of property buttons. Sorted into the group colors.
        this.PropertyGroups = new JButton[][]{
            {Property1, Property2},
            {Property3, Property4, Property5},
            {Property6, Property7, Property8},
            {Property9, Property10, Property11},
            {Property12, Property13, Property14},
            {Property15, Property16, Property17},
            {Property18, Property19, Property20},
            {Property21, Property22}};
        
        //The location on the board of each property. Sorted into the group colors.
        this.PropertyNumberGroups = new int[][]{
            {1, 3},
            {6, 8, 9},
            {11, 13, 14},
            {16, 18, 19},
            {21, 23, 24},
            {26, 27, 29},
            {31, 32, 34},
            {37, 39}};
        
        //Sets the text of all the buttons to represent their proper property name.
        for (int i = 0; i < PropertyGroups.length; i++) {
            for (int x = 0; x < PropertyGroups[i].length; x++) {
                PropertyGroups[i][x].setText("<html>" + BoardProperties[PropertyNumberGroups[i][x]].GetPropertyName() + "</html>");
            }
        }
        
        //Refresh the player buttons.
        refreshButtons();
        resetPlayerButtons();
    }

    /**
     * When the user clicks on a player, this will enable all the properties
     * they can buy houses for.
     * 
     * @param PlayerNumber Which player is being looked at.
     */
    private void showPropertiesForHouse(int PlayerNumber) {
        disableButtons();
        
        CurrentPlayer = PlayerNumber;
        int[] OwnedProperties = PlayerGroup.get(PlayerNumber).GetLocationsOwned();
        boolean FLAG = true;
        
        //Loops through each group of properties, based on their color.
        for (int i = 0; i < PropertyNumberGroups.length; i++) {
            for (int x = 0; x < PropertyNumberGroups[i].length; x++) {
                //Checks to see if the player owns all of the properties in that group, if they don't set the flag to false.
                if (OwnedProperties[PropertyNumberGroups[i][x]] != 1) {
                    FLAG = false;
                }
            }
            
            //If the player owns all the properties in that group.
            if (FLAG == true) {
                //Loop through the group and enable the buttons.
                for (int x = 0; x < PropertyGroups[i].length; x++) {
                    //Checks to see if there are less than four houses on the property.
                    if (BoardProperties[PropertyNumberGroups[i][x]].GetHouses() != 4) {
                        PropertyGroups[i][x].setEnabled(true);
                    }
                }
            }
            
            //Reset the flag.
            FLAG = true;
        }
    }
    
    private void resetPlayerButtons() {
        PlayerOneButton.setBorder(new LineBorder(Color.GRAY, 2));
        PlayerTwoButton.setBorder(new LineBorder(Color.GRAY, 2));
        PlayerThreeButton.setBorder(new LineBorder(Color.GRAY, 2));
        PlayerFourButton.setBorder(new LineBorder(Color.GRAY, 2));
    }
    
    /**
     * Enables the ability to confirm a purchase of a house.
     * 
     * @param Location The location that is being looked at to buy a house.
     */
    private void confirmEnable(int Location) {
        InfoLabel.setText("<html>Buy a house at " + BoardProperties[Location].GetPropertyName() + " for $" + BoardProperties[Location].GetBuildingCost() + "</html>");
        CurrentLoc = Location;
        ConfirmButton.setEnabled(true);
    }
    
    /**
     * Purchases a house for the property that was selected.
     */
    private void buyPropertyHouse() {
        PlayerGroup.get(CurrentPlayer).SubtractMoney(BoardProperties[CurrentLoc].buyHouse());
        ConfirmButton.setEnabled(false);
        showPropertiesForHouse(CurrentPlayer);
        InfoLabel.setText("<html>Thank you for purchasing a house!</html>");
        Monopoly.updateMonopolyHouses();
    }
    
    /**
     * Hides and sets the names of the player buttons. Will hide buttons until there are the correct
     * amount for the current amount of players. Also disables all of the property buttons.
     */
    public void refreshButtons() {
        JButton[] arrayOfButtons = {PlayerOneButton, PlayerTwoButton, PlayerThreeButton, PlayerFourButton};

        //Loops through the players and hide buttons that aren't needed.
        for (int i = 3; i >= PlayerGroup.size(); i--) {
            arrayOfButtons[i].setVisible(false);
        }
        //Sets the names of the visible buttons.
        for (int i = 0; i < PlayerGroup.size(); i++) {
            arrayOfButtons[i].setText(PlayerGroup.get(i).GetPlayerName());
        }
        
        //Disable all of the property buttons.
        disableButtons();
    }
    
    /**
     * Disable all of the property buttons.
     */
    private void disableButtons() {
        for (int i = 0; i < PropertyGroups.length; i++) {
            for (int x = 0; x < PropertyGroups[i].length; x++) {
                PropertyGroups[i][x].setEnabled(false);
            }
        }
        
        ConfirmButton.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PlayerOneButton = new javax.swing.JButton();
        PlayerTwoButton = new javax.swing.JButton();
        PlayerThreeButton = new javax.swing.JButton();
        PlayerFourButton = new javax.swing.JButton();
        ConfirmButton = new javax.swing.JButton();
        InfoLabel = new javax.swing.JLabel();
        Property1 = new javax.swing.JButton();
        Property2 = new javax.swing.JButton();
        Property3 = new javax.swing.JButton();
        Property4 = new javax.swing.JButton();
        Property5 = new javax.swing.JButton();
        Property6 = new javax.swing.JButton();
        Property7 = new javax.swing.JButton();
        Property8 = new javax.swing.JButton();
        Property9 = new javax.swing.JButton();
        Property10 = new javax.swing.JButton();
        Property11 = new javax.swing.JButton();
        Property12 = new javax.swing.JButton();
        Property13 = new javax.swing.JButton();
        Property14 = new javax.swing.JButton();
        Property15 = new javax.swing.JButton();
        Property16 = new javax.swing.JButton();
        Property17 = new javax.swing.JButton();
        Property18 = new javax.swing.JButton();
        Property19 = new javax.swing.JButton();
        Property20 = new javax.swing.JButton();
        Property21 = new javax.swing.JButton();
        Property22 = new javax.swing.JButton();

        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(1300, 300));
        jPanel1.setLayout(null);

        PlayerOneButton.setText("jButton1");
        PlayerOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerOneButtonActionPerformed(evt);
            }
        });
        jPanel1.add(PlayerOneButton);
        PlayerOneButton.setBounds(10, 20, 100, 25);

        PlayerTwoButton.setText("jButton1");
        PlayerTwoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerTwoButtonActionPerformed(evt);
            }
        });
        jPanel1.add(PlayerTwoButton);
        PlayerTwoButton.setBounds(120, 20, 100, 25);

        PlayerThreeButton.setText("jButton1");
        PlayerThreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerThreeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(PlayerThreeButton);
        PlayerThreeButton.setBounds(230, 20, 100, 25);

        PlayerFourButton.setText("jButton1");
        PlayerFourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayerFourButtonActionPerformed(evt);
            }
        });
        jPanel1.add(PlayerFourButton);
        PlayerFourButton.setBounds(340, 20, 100, 25);

        ConfirmButton.setText("Confirm");
        ConfirmButton.setEnabled(false);
        ConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmButtonActionPerformed(evt);
            }
        });
        jPanel1.add(ConfirmButton);
        ConfirmButton.setBounds(590, 50, 90, 25);

        InfoLabel.setText("House For");
        jPanel1.add(InfoLabel);
        InfoLabel.setBounds(550, 10, 240, 40);

        Property1.setText("Prop 1");
        Property1.setEnabled(false);
        Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property1ActionPerformed(evt);
            }
        });
        jPanel1.add(Property1);
        Property1.setBounds(10, 120, 150, 40);

        Property2.setText("Prop 2");
        Property2.setEnabled(false);
        Property2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property2ActionPerformed(evt);
            }
        });
        jPanel1.add(Property2);
        Property2.setBounds(10, 170, 150, 40);

        Property3.setText("Prop 3");
        Property3.setEnabled(false);
        Property3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property3ActionPerformed(evt);
            }
        });
        jPanel1.add(Property3);
        Property3.setBounds(170, 120, 150, 40);

        Property4.setText("Prop 4");
        Property4.setEnabled(false);
        Property4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property4ActionPerformed(evt);
            }
        });
        jPanel1.add(Property4);
        Property4.setBounds(170, 170, 150, 40);

        Property5.setText("Prop 5");
        Property5.setEnabled(false);
        Property5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property5ActionPerformed(evt);
            }
        });
        jPanel1.add(Property5);
        Property5.setBounds(170, 220, 150, 40);

        Property6.setText("Prop 6");
        Property6.setEnabled(false);
        Property6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property6ActionPerformed(evt);
            }
        });
        jPanel1.add(Property6);
        Property6.setBounds(330, 120, 150, 40);

        Property7.setText("Prop 7");
        Property7.setEnabled(false);
        Property7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property7ActionPerformed(evt);
            }
        });
        jPanel1.add(Property7);
        Property7.setBounds(330, 170, 150, 40);

        Property8.setText("Prop 8");
        Property8.setEnabled(false);
        Property8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property8ActionPerformed(evt);
            }
        });
        jPanel1.add(Property8);
        Property8.setBounds(330, 220, 150, 40);

        Property9.setText("Prop 9");
        Property9.setEnabled(false);
        Property9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property9ActionPerformed(evt);
            }
        });
        jPanel1.add(Property9);
        Property9.setBounds(490, 120, 150, 40);

        Property10.setText("Prop 10");
        Property10.setEnabled(false);
        Property10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property10ActionPerformed(evt);
            }
        });
        jPanel1.add(Property10);
        Property10.setBounds(490, 170, 150, 40);

        Property11.setText("Prop 11");
        Property11.setEnabled(false);
        Property11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property11ActionPerformed(evt);
            }
        });
        jPanel1.add(Property11);
        Property11.setBounds(490, 220, 150, 40);

        Property12.setText("Prop 12");
        Property12.setEnabled(false);
        Property12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property12ActionPerformed(evt);
            }
        });
        jPanel1.add(Property12);
        Property12.setBounds(650, 120, 150, 40);

        Property13.setText("Prop 13");
        Property13.setEnabled(false);
        Property13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property13ActionPerformed(evt);
            }
        });
        jPanel1.add(Property13);
        Property13.setBounds(650, 170, 150, 40);

        Property14.setText("Prop 14");
        Property14.setEnabled(false);
        Property14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property14ActionPerformed(evt);
            }
        });
        jPanel1.add(Property14);
        Property14.setBounds(650, 220, 150, 40);

        Property15.setText("Prop 15");
        Property15.setEnabled(false);
        Property15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property15ActionPerformed(evt);
            }
        });
        jPanel1.add(Property15);
        Property15.setBounds(810, 120, 150, 40);

        Property16.setText("Prop 16");
        Property16.setEnabled(false);
        Property16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property16ActionPerformed(evt);
            }
        });
        jPanel1.add(Property16);
        Property16.setBounds(810, 170, 150, 40);

        Property17.setText("Prop 17");
        Property17.setEnabled(false);
        Property17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property17ActionPerformed(evt);
            }
        });
        jPanel1.add(Property17);
        Property17.setBounds(810, 220, 150, 40);

        Property18.setText("Prop 18");
        Property18.setEnabled(false);
        Property18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property18ActionPerformed(evt);
            }
        });
        jPanel1.add(Property18);
        Property18.setBounds(970, 120, 150, 40);

        Property19.setText("Prop 19");
        Property19.setEnabled(false);
        Property19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property19ActionPerformed(evt);
            }
        });
        jPanel1.add(Property19);
        Property19.setBounds(970, 170, 150, 40);

        Property20.setText("Prop 20");
        Property20.setEnabled(false);
        Property20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property20ActionPerformed(evt);
            }
        });
        jPanel1.add(Property20);
        Property20.setBounds(970, 220, 150, 40);

        Property21.setText("Prop 21");
        Property21.setEnabled(false);
        Property21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property21ActionPerformed(evt);
            }
        });
        jPanel1.add(Property21);
        Property21.setBounds(1130, 120, 150, 40);

        Property22.setText("Prop 22");
        Property22.setEnabled(false);
        Property22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property22ActionPerformed(evt);
            }
        });
        jPanel1.add(Property22);
        Property22.setBounds(1130, 170, 150, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlayerOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerOneButtonActionPerformed
        resetPlayerButtons();
        PlayerOneButton.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesForHouse(0);
    }//GEN-LAST:event_PlayerOneButtonActionPerformed

    private void PlayerTwoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerTwoButtonActionPerformed
        resetPlayerButtons();
        PlayerTwoButton.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesForHouse(1);
    }//GEN-LAST:event_PlayerTwoButtonActionPerformed

    private void PlayerThreeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerThreeButtonActionPerformed
        resetPlayerButtons();
        PlayerThreeButton.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesForHouse(2);
    }//GEN-LAST:event_PlayerThreeButtonActionPerformed

    private void PlayerFourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayerFourButtonActionPerformed
        resetPlayerButtons();
        PlayerFourButton.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesForHouse(3);
    }//GEN-LAST:event_PlayerFourButtonActionPerformed

    private void ConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmButtonActionPerformed
        buyPropertyHouse();
    }//GEN-LAST:event_ConfirmButtonActionPerformed

    private void Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property1ActionPerformed
        confirmEnable(1);
    }//GEN-LAST:event_Property1ActionPerformed

    private void Property2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property2ActionPerformed
        confirmEnable(3);
    }//GEN-LAST:event_Property2ActionPerformed

    private void Property4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property4ActionPerformed
        confirmEnable(8);
    }//GEN-LAST:event_Property4ActionPerformed

    private void Property3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property3ActionPerformed
        confirmEnable(6);
    }//GEN-LAST:event_Property3ActionPerformed

    private void Property5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property5ActionPerformed
        confirmEnable(9);
    }//GEN-LAST:event_Property5ActionPerformed

    private void Property6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property6ActionPerformed
        confirmEnable(11);
    }//GEN-LAST:event_Property6ActionPerformed

    private void Property7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property7ActionPerformed
        confirmEnable(13);
    }//GEN-LAST:event_Property7ActionPerformed

    private void Property8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property8ActionPerformed
        confirmEnable(14);
    }//GEN-LAST:event_Property8ActionPerformed

    private void Property9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property9ActionPerformed
        confirmEnable(16);
    }//GEN-LAST:event_Property9ActionPerformed

    private void Property10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property10ActionPerformed
        confirmEnable(18);
    }//GEN-LAST:event_Property10ActionPerformed

    private void Property11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property11ActionPerformed
        confirmEnable(19);
    }//GEN-LAST:event_Property11ActionPerformed

    private void Property12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property12ActionPerformed
        confirmEnable(21);
    }//GEN-LAST:event_Property12ActionPerformed

    private void Property13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property13ActionPerformed
        confirmEnable(23);
    }//GEN-LAST:event_Property13ActionPerformed

    private void Property14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property14ActionPerformed
        confirmEnable(24);
    }//GEN-LAST:event_Property14ActionPerformed

    private void Property15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property15ActionPerformed
        confirmEnable(26);
    }//GEN-LAST:event_Property15ActionPerformed

    private void Property16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property16ActionPerformed
        confirmEnable(27);
    }//GEN-LAST:event_Property16ActionPerformed

    private void Property17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property17ActionPerformed
        confirmEnable(29);
    }//GEN-LAST:event_Property17ActionPerformed

    private void Property18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property18ActionPerformed
        confirmEnable(31);
    }//GEN-LAST:event_Property18ActionPerformed

    private void Property19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property19ActionPerformed
        confirmEnable(32);
    }//GEN-LAST:event_Property19ActionPerformed

    private void Property20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property20ActionPerformed
        confirmEnable(34);
    }//GEN-LAST:event_Property20ActionPerformed

    private void Property21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property21ActionPerformed
        confirmEnable(37);
    }//GEN-LAST:event_Property21ActionPerformed

    private void Property22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property22ActionPerformed
        confirmEnable(39);
    }//GEN-LAST:event_Property22ActionPerformed

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
            java.util.logging.Logger.getLogger(HomeBuyer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeBuyer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeBuyer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeBuyer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConfirmButton;
    private javax.swing.JLabel InfoLabel;
    private javax.swing.JButton PlayerFourButton;
    private javax.swing.JButton PlayerOneButton;
    private javax.swing.JButton PlayerThreeButton;
    private javax.swing.JButton PlayerTwoButton;
    private javax.swing.JButton Property1;
    private javax.swing.JButton Property10;
    private javax.swing.JButton Property11;
    private javax.swing.JButton Property12;
    private javax.swing.JButton Property13;
    private javax.swing.JButton Property14;
    private javax.swing.JButton Property15;
    private javax.swing.JButton Property16;
    private javax.swing.JButton Property17;
    private javax.swing.JButton Property18;
    private javax.swing.JButton Property19;
    private javax.swing.JButton Property2;
    private javax.swing.JButton Property20;
    private javax.swing.JButton Property21;
    private javax.swing.JButton Property22;
    private javax.swing.JButton Property3;
    private javax.swing.JButton Property4;
    private javax.swing.JButton Property5;
    private javax.swing.JButton Property6;
    private javax.swing.JButton Property7;
    private javax.swing.JButton Property8;
    private javax.swing.JButton Property9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
