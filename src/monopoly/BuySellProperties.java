package monopoly;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author Kyle
 */
public class BuySellProperties extends javax.swing.JFrame {
    ArrayList<Player> playerGroup;
    Property[] boardProperties;
    JButton[][] propertyGroups;
    int[][] propertyNumberGroups;
    int playerSelling = 0, playerBuying = 0, currentLoc = 0;
    
    
    /**
     * Creates new form BuySellProperties
     */
    public BuySellProperties(ArrayList<Player> sentPlayerGroup, Property[] sentBuildingProperties) {
        initComponents();
        
        this.playerGroup = sentPlayerGroup;
        this.boardProperties = sentBuildingProperties;
        
        //Creates the array of property buttons. Sorted into the group colors.
        this.propertyGroups = new JButton[][]{
            {Property1, Property2},
            {Property3, Property4, Property5},
            {Property6, Property7, Property8},
            {Property9, Property10, Property11},
            {Property12, Property13, Property14},
            {Property15, Property16, Property17},
            {Property18, Property19, Property20},
            {Property21, Property22},
            {Railroad1, Railroad2, Railroad3, Railroad4},
            {Utility1, Utility2}};
        
        //The location on the board of each property. Sorted into the group colors.
        this.propertyNumberGroups = new int[][]{
            {1, 3},
            {6, 8, 9},
            {11, 13, 14},
            {16, 18, 19},
            {21, 23, 24},
            {26, 27, 29},
            {31, 32, 34},
            {37, 39},
            {5, 15, 25, 35},
            {12, 28}};
        
        //Sets the text of all the buttons to represent their proper property name.
        for (int i = 0; i < propertyGroups.length; i++) {
            for (int x = 0; x < propertyGroups[i].length; x++) {
                propertyGroups[i][x].setText("<html>" + boardProperties[propertyNumberGroups[i][x]].GetPropertyName() + "</html>");
            }
        }
        
        //Refresh the player buttons.
        refreshButtons();
        resetBuyButtons();
        resetSellButtons();
    }

    private void showPropertiesOwned(int playerNumber) {
        disableButtons();
        
        playerSelling = playerNumber;
        int[] ownedProperties = playerGroup.get(playerNumber).GetLocationsOwned();
        boolean FLAG = true;
        
        //Loops through each group of properties, based on their color.
        for (int i = 0; i < propertyNumberGroups.length; i++) {
            for (int x = 0; x < propertyNumberGroups[i].length; x++) {
                if (ownedProperties[propertyNumberGroups[i][x]] == 1) {
                    propertyGroups[i][x].setEnabled(true);
                }
            }
        }
    }
    
    public void refreshButtons() {
        JButton[][] arrayOfButtons = {  {Player1Sell, Player2Sell, Player3Sell, Player4Sell},
                                        {Player1Buy, Player2Buy, Player3Buy, Player4Buy}};

        //Loops through the players and hide buttons that aren't needed.
        for (int x = 0; x < arrayOfButtons.length; x++) {
            for (int i = 3; i >= playerGroup.size(); i--) {
                arrayOfButtons[x][i].setVisible(false);
            }
            //Sets the names of the visible buttons.
            for (int i = 0; i < playerGroup.size(); i++) {
                arrayOfButtons[x][i].setText(playerGroup.get(i).GetPlayerName());
            }
        }
        //Disable all of the property buttons.
        disableButtons();
    }
    
    /**
     * Disable all of the property buttons.
     */
    private void disableButtons() {
        JButton[] arrayOfButtons = {Player1Buy, Player2Buy, Player3Buy, Player4Buy};
        
        for (int i = 0; i < propertyGroups.length; i++) {
            for (int x = 0; x < propertyGroups[i].length; x++) {
                propertyGroups[i][x].setEnabled(false);
            }
        }
        
        for (int i = 0; i < arrayOfButtons.length; i++) {
            arrayOfButtons[i].setEnabled(false);
        }
        
        ConfirmButton.setEnabled(false);
    }
    
    private void selectPropertyToSell(int location) {
        JButton[] arrayOfButtons = {Player1Buy, Player2Buy, Player3Buy, Player4Buy};

        currentLoc = location;
        
        for (int i = 0; i < arrayOfButtons.length; i++) {
            arrayOfButtons[i].setEnabled(true);
        }
    }
    
    private void confirmEnable() {
        String amountOffered = "?250?";
        
        if (!AmountTextField.equals("")) {
            try {
                int tryCatch = Integer.parseInt(AmountTextField.getText());
                amountOffered = "$" + AmountTextField.getText();
            }
            catch (NumberFormatException e) {
                amountOffered = "?250?";
            }
        }
        
        InfoLabel.setText(  "<html>" + playerGroup.get(playerSelling).GetPlayerName() + " is trying to sell " + boardProperties[currentLoc].GetPropertyName() + 
                            " for " + amountOffered + " to " + playerGroup.get(playerBuying).GetPlayerName() +
                            "</html>");
        
        ConfirmButton.setEnabled(true);
    }
    
    private void buySellProperty() {
        int amountOffered = 250;
        
        if (!AmountTextField.equals("")) {
            try {
                amountOffered = Integer.parseInt(AmountTextField.getText());
            }
            catch (NumberFormatException e) {
                amountOffered = 250;
            }
        }
        
        playerGroup.get(playerSelling).SellPropertyFromPlayer(amountOffered, currentLoc);
        playerGroup.get(playerBuying).BuyPropertyFromPlayer(amountOffered, currentLoc);
        Monopoly.updatePropertiesOwned();
        disableButtons();
        InfoLabel.setText("<html>Thank you for purchasing this property!</html>");
    }
    
    private void resetSellButtons() {
        Player1Sell.setBorder(new LineBorder(Color.GRAY, 2));
        Player2Sell.setBorder(new LineBorder(Color.GRAY, 2));
        Player3Sell.setBorder(new LineBorder(Color.GRAY, 2));
        Player4Sell.setBorder(new LineBorder(Color.GRAY, 2));
    }
    
    private void resetBuyButtons() {
        Player1Buy.setBorder(new LineBorder(Color.GRAY, 2));
        Player2Buy.setBorder(new LineBorder(Color.GRAY, 2));
        Player3Buy.setBorder(new LineBorder(Color.GRAY, 2));
        Player4Buy.setBorder(new LineBorder(Color.GRAY, 2));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BuySellPanel = new javax.swing.JPanel();
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
        PlayerSellLabel = new javax.swing.JLabel();
        Player1Sell = new javax.swing.JButton();
        Player2Sell = new javax.swing.JButton();
        Player3Sell = new javax.swing.JButton();
        Player4Sell = new javax.swing.JButton();
        PlayerBuyLabel = new javax.swing.JLabel();
        Player1Buy = new javax.swing.JButton();
        Player2Buy = new javax.swing.JButton();
        Player3Buy = new javax.swing.JButton();
        Player4Buy = new javax.swing.JButton();
        InfoLabel = new javax.swing.JLabel();
        ConfirmButton = new javax.swing.JButton();
        AmountLabel = new javax.swing.JLabel();
        AmountTextField = new javax.swing.JTextField();
        Railroad1 = new javax.swing.JButton();
        Railroad2 = new javax.swing.JButton();
        Railroad3 = new javax.swing.JButton();
        Railroad4 = new javax.swing.JButton();
        Utility1 = new javax.swing.JButton();
        Utility2 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1300, 500));
        setResizable(false);

        BuySellPanel.setPreferredSize(new java.awt.Dimension(1300, 400));
        BuySellPanel.setLayout(null);

        Property1.setText("Prop 1");
        Property1.setEnabled(false);
        Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property1ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property1);
        Property1.setBounds(12, 218, 150, 40);

        Property2.setText("Prop 2");
        Property2.setEnabled(false);
        Property2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property2ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property2);
        Property2.setBounds(12, 268, 150, 40);

        Property3.setText("Prop 3");
        Property3.setEnabled(false);
        Property3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property3ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property3);
        Property3.setBounds(172, 218, 150, 40);

        Property4.setText("Prop 4");
        Property4.setEnabled(false);
        Property4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property4ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property4);
        Property4.setBounds(172, 268, 150, 40);

        Property5.setText("Prop 5");
        Property5.setEnabled(false);
        Property5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property5ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property5);
        Property5.setBounds(172, 318, 150, 40);

        Property6.setText("Prop 6");
        Property6.setEnabled(false);
        Property6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property6ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property6);
        Property6.setBounds(332, 218, 150, 40);

        Property7.setText("Prop 7");
        Property7.setEnabled(false);
        Property7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property7ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property7);
        Property7.setBounds(332, 268, 150, 40);

        Property8.setText("Prop 8");
        Property8.setEnabled(false);
        Property8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property8ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property8);
        Property8.setBounds(332, 318, 150, 40);

        Property9.setText("Prop 9");
        Property9.setEnabled(false);
        Property9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property9ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property9);
        Property9.setBounds(492, 218, 150, 40);

        Property10.setText("Prop 10");
        Property10.setEnabled(false);
        Property10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property10ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property10);
        Property10.setBounds(492, 268, 150, 40);

        Property11.setText("Prop 11");
        Property11.setEnabled(false);
        Property11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property11ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property11);
        Property11.setBounds(490, 318, 150, 40);

        Property12.setText("Prop 12");
        Property12.setEnabled(false);
        Property12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property12ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property12);
        Property12.setBounds(652, 218, 150, 40);

        Property13.setText("Prop 13");
        Property13.setEnabled(false);
        Property13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property13ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property13);
        Property13.setBounds(652, 268, 150, 40);

        Property14.setText("Prop 14");
        Property14.setEnabled(false);
        Property14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property14ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property14);
        Property14.setBounds(650, 318, 150, 40);

        Property15.setText("Prop 15");
        Property15.setEnabled(false);
        Property15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property15ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property15);
        Property15.setBounds(812, 218, 150, 40);

        Property16.setText("Prop 16");
        Property16.setEnabled(false);
        Property16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property16ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property16);
        Property16.setBounds(812, 268, 150, 40);

        Property17.setText("Prop 17");
        Property17.setEnabled(false);
        Property17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property17ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property17);
        Property17.setBounds(810, 318, 150, 40);

        Property18.setText("Prop 18");
        Property18.setEnabled(false);
        Property18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property18ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property18);
        Property18.setBounds(972, 218, 150, 40);

        Property19.setText("Prop 19");
        Property19.setEnabled(false);
        Property19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property19ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property19);
        Property19.setBounds(972, 268, 150, 40);

        Property20.setText("Prop 20");
        Property20.setEnabled(false);
        Property20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property20ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property20);
        Property20.setBounds(970, 318, 150, 40);

        Property21.setText("Prop 21");
        Property21.setEnabled(false);
        Property21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property21ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property21);
        Property21.setBounds(1132, 218, 150, 40);

        Property22.setText("Prop 22");
        Property22.setEnabled(false);
        Property22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Property22ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Property22);
        Property22.setBounds(1132, 268, 150, 40);

        PlayerSellLabel.setText("Player That Is Selling");
        BuySellPanel.add(PlayerSellLabel);
        PlayerSellLabel.setBounds(12, 13, 121, 16);

        Player1Sell.setText("Player");
        Player1Sell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player1SellActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player1Sell);
        Player1Sell.setBounds(12, 56, 110, 25);

        Player2Sell.setText("Player");
        Player2Sell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player2SellActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player2Sell);
        Player2Sell.setBounds(12, 88, 110, 25);

        Player3Sell.setText("Player");
        Player3Sell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player3SellActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player3Sell);
        Player3Sell.setBounds(12, 120, 110, 25);

        Player4Sell.setText("Player");
        Player4Sell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player4SellActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player4Sell);
        Player4Sell.setBounds(12, 152, 110, 25);

        PlayerBuyLabel.setText("Player That Is Buying");
        BuySellPanel.add(PlayerBuyLabel);
        PlayerBuyLabel.setBounds(344, 13, 120, 16);

        Player1Buy.setText("Player");
        Player1Buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player1BuyActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player1Buy);
        Player1Buy.setBounds(344, 56, 110, 25);

        Player2Buy.setText("Player");
        Player2Buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player2BuyActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player2Buy);
        Player2Buy.setBounds(344, 88, 110, 25);

        Player3Buy.setText("Player");
        Player3Buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player3BuyActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player3Buy);
        Player3Buy.setBounds(344, 120, 110, 25);

        Player4Buy.setText("Player");
        Player4Buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Player4BuyActionPerformed(evt);
            }
        });
        BuySellPanel.add(Player4Buy);
        Player4Buy.setBounds(344, 152, 110, 25);

        InfoLabel.setText("Info");
        BuySellPanel.add(InfoLabel);
        InfoLabel.setBounds(588, 13, 270, 70);

        ConfirmButton.setText("Confirm");
        ConfirmButton.setEnabled(false);
        ConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmButtonActionPerformed(evt);
            }
        });
        BuySellPanel.add(ConfirmButton);
        ConfirmButton.setBounds(590, 80, 110, 25);

        AmountLabel.setText("Amount of Money");
        BuySellPanel.add(AmountLabel);
        AmountLabel.setBounds(158, 78, 100, 16);
        BuySellPanel.add(AmountTextField);
        AmountTextField.setBounds(158, 110, 100, 22);

        Railroad1.setText("Railroad 1");
        Railroad1.setEnabled(false);
        Railroad1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Railroad1ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Railroad1);
        Railroad1.setBounds(170, 380, 150, 36);

        Railroad2.setText("Railroad 2");
        Railroad2.setEnabled(false);
        Railroad2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Railroad2ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Railroad2);
        Railroad2.setBounds(170, 420, 150, 36);

        Railroad3.setText("Railroad 3");
        Railroad3.setEnabled(false);
        Railroad3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Railroad3ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Railroad3);
        Railroad3.setBounds(340, 380, 150, 36);

        Railroad4.setText("Railroad 4");
        Railroad4.setEnabled(false);
        Railroad4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Railroad4ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Railroad4);
        Railroad4.setBounds(340, 420, 148, 36);

        Utility1.setText("Utility 1");
        Utility1.setEnabled(false);
        Utility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Utility1ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Utility1);
        Utility1.setBounds(660, 380, 140, 40);

        Utility2.setText("Utility 2");
        Utility2.setEnabled(false);
        Utility2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Utility2ActionPerformed(evt);
            }
        });
        BuySellPanel.add(Utility2);
        Utility2.setBounds(820, 380, 140, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BuySellPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BuySellPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Railroad4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Railroad4ActionPerformed
        selectPropertyToSell(35);
    }//GEN-LAST:event_Railroad4ActionPerformed

    private void Railroad3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Railroad3ActionPerformed
        selectPropertyToSell(25);
    }//GEN-LAST:event_Railroad3ActionPerformed

    private void Railroad2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Railroad2ActionPerformed
        selectPropertyToSell(15);
    }//GEN-LAST:event_Railroad2ActionPerformed

    private void Railroad1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Railroad1ActionPerformed
        selectPropertyToSell(5);
    }//GEN-LAST:event_Railroad1ActionPerformed

    private void ConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmButtonActionPerformed
        buySellProperty();
    }//GEN-LAST:event_ConfirmButtonActionPerformed

    private void Player4BuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player4BuyActionPerformed
        resetBuyButtons();
        Player4Buy.setBorder(new LineBorder(Color.RED, 2));
        playerBuying = 3;
        confirmEnable();
    }//GEN-LAST:event_Player4BuyActionPerformed

    private void Player3BuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player3BuyActionPerformed
        resetBuyButtons();
        Player3Buy.setBorder(new LineBorder(Color.RED, 2));
        playerBuying = 2;
        confirmEnable();
    }//GEN-LAST:event_Player3BuyActionPerformed

    private void Player2BuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player2BuyActionPerformed
        resetBuyButtons();
        Player2Buy.setBorder(new LineBorder(Color.RED, 2));
        playerBuying = 1;
        confirmEnable();
    }//GEN-LAST:event_Player2BuyActionPerformed

    private void Player1BuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player1BuyActionPerformed
        resetBuyButtons();
        Player1Buy.setBorder(new LineBorder(Color.RED, 2));
        playerBuying = 0;
        confirmEnable();
    }//GEN-LAST:event_Player1BuyActionPerformed

    private void Player4SellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player4SellActionPerformed
        resetSellButtons();
        Player4Sell.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesOwned(3);
    }//GEN-LAST:event_Player4SellActionPerformed

    private void Player3SellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player3SellActionPerformed
        resetSellButtons();
        Player3Sell.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesOwned(2);
    }//GEN-LAST:event_Player3SellActionPerformed

    private void Player2SellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player2SellActionPerformed
        resetSellButtons();
        Player2Sell.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesOwned(1);
    }//GEN-LAST:event_Player2SellActionPerformed

    private void Player1SellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Player1SellActionPerformed
        resetSellButtons();
        Player1Sell.setBorder(new LineBorder(Color.RED, 2));
        showPropertiesOwned(0);
    }//GEN-LAST:event_Player1SellActionPerformed

    private void Property22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property22ActionPerformed
        selectPropertyToSell(39);
    }//GEN-LAST:event_Property22ActionPerformed

    private void Property21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property21ActionPerformed
        selectPropertyToSell(37);
    }//GEN-LAST:event_Property21ActionPerformed

    private void Property20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property20ActionPerformed
        selectPropertyToSell(34);
    }//GEN-LAST:event_Property20ActionPerformed

    private void Property19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property19ActionPerformed
        selectPropertyToSell(32);
    }//GEN-LAST:event_Property19ActionPerformed

    private void Property18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property18ActionPerformed
        selectPropertyToSell(31);
    }//GEN-LAST:event_Property18ActionPerformed

    private void Property17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property17ActionPerformed
        selectPropertyToSell(29);
    }//GEN-LAST:event_Property17ActionPerformed

    private void Property16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property16ActionPerformed
        selectPropertyToSell(27);
    }//GEN-LAST:event_Property16ActionPerformed

    private void Property15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property15ActionPerformed
        selectPropertyToSell(26);
    }//GEN-LAST:event_Property15ActionPerformed

    private void Property14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property14ActionPerformed
        selectPropertyToSell(24);
    }//GEN-LAST:event_Property14ActionPerformed

    private void Property13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property13ActionPerformed
        selectPropertyToSell(23);
    }//GEN-LAST:event_Property13ActionPerformed

    private void Property12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property12ActionPerformed
        selectPropertyToSell(21);
    }//GEN-LAST:event_Property12ActionPerformed

    private void Property11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property11ActionPerformed
        selectPropertyToSell(19);
    }//GEN-LAST:event_Property11ActionPerformed

    private void Property10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property10ActionPerformed
        selectPropertyToSell(18);
    }//GEN-LAST:event_Property10ActionPerformed

    private void Property9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property9ActionPerformed
        selectPropertyToSell(16);
    }//GEN-LAST:event_Property9ActionPerformed

    private void Property8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property8ActionPerformed
        selectPropertyToSell(14);
    }//GEN-LAST:event_Property8ActionPerformed

    private void Property7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property7ActionPerformed
        selectPropertyToSell(13);
    }//GEN-LAST:event_Property7ActionPerformed

    private void Property6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property6ActionPerformed
        selectPropertyToSell(11);
    }//GEN-LAST:event_Property6ActionPerformed

    private void Property5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property5ActionPerformed
        selectPropertyToSell(9);
    }//GEN-LAST:event_Property5ActionPerformed

    private void Property4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property4ActionPerformed
        selectPropertyToSell(8);
    }//GEN-LAST:event_Property4ActionPerformed

    private void Property3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property3ActionPerformed
        selectPropertyToSell(6);
    }//GEN-LAST:event_Property3ActionPerformed

    private void Property2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property2ActionPerformed
        selectPropertyToSell(3);
    }//GEN-LAST:event_Property2ActionPerformed

    private void Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Property1ActionPerformed
        selectPropertyToSell(1);
    }//GEN-LAST:event_Property1ActionPerformed

    private void Utility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Utility1ActionPerformed
        selectPropertyToSell(12);
    }//GEN-LAST:event_Utility1ActionPerformed

    private void Utility2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Utility2ActionPerformed
        selectPropertyToSell(28);
    }//GEN-LAST:event_Utility2ActionPerformed

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
            java.util.logging.Logger.getLogger(BuySellProperties.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuySellProperties.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuySellProperties.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuySellProperties.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AmountLabel;
    private javax.swing.JTextField AmountTextField;
    private javax.swing.JPanel BuySellPanel;
    private javax.swing.JButton ConfirmButton;
    private javax.swing.JLabel InfoLabel;
    private javax.swing.JButton Player1Buy;
    private javax.swing.JButton Player1Sell;
    private javax.swing.JButton Player2Buy;
    private javax.swing.JButton Player2Sell;
    private javax.swing.JButton Player3Buy;
    private javax.swing.JButton Player3Sell;
    private javax.swing.JButton Player4Buy;
    private javax.swing.JButton Player4Sell;
    private javax.swing.JLabel PlayerBuyLabel;
    private javax.swing.JLabel PlayerSellLabel;
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
    private javax.swing.JButton Railroad1;
    private javax.swing.JButton Railroad2;
    private javax.swing.JButton Railroad3;
    private javax.swing.JButton Railroad4;
    private javax.swing.JButton Utility1;
    private javax.swing.JButton Utility2;
    // End of variables declaration//GEN-END:variables
}
