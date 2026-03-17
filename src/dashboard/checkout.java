/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author aldre
 */
public class checkout extends javax.swing.JFrame {

    private int userId; // ✅ add this field at the top

    // ✅ constructor must ACCEPT userId as parameter
    public checkout(int userId) {
        initComponents();
        this.userId = userId; // ✅ now this works
        tbl2.setModel(dashboard.CartManager.sharedModel);
        jRadioButton1.setSelected(true);
        jTextField4.setEditable(false);
        calculateTotal();
    }
   private void calculateTotal() {
    double total = 0;
    for (int i = 0; i < tbl2.getRowCount(); i++) {
        try {
            // Remove '₱' or spaces if they exist in your price column
            String priceStr = tbl2.getValueAt(i, 2).toString().replace("₱", "").trim();
            int qty = Integer.parseInt(tbl2.getValueAt(i, 1).toString());
            total += Double.parseDouble(priceStr) * qty;
        } catch (Exception e) {
            System.out.println("Error calculating row: " + e.getMessage());
        }
    }
    jTextField4.setText("Total: ₱" + String.format("%.2f", total));
}
   private void processCashOrder() {
    String fullName = jTextField1.getText();
    String address = jTextField3.getText();
    String contact = jTextField2.getText();

    if (fullName.isEmpty() || address.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please provide Name and Shipping Address.");
        return;
    }

    try (java.sql.Connection conn = config.config.connectDB()) {
        // 1. Insert into tbl_orders
        String sqlOrder = "INSERT INTO tbl_orders (u_id, o_total) VALUES (?, ?)";
        java.sql.PreparedStatement pstOrder = conn.prepareStatement(sqlOrder, java.sql.Statement.RETURN_GENERATED_KEYS);
        
        pstOrder.setInt(1, userId); // Temporary: assumes user ID 1
        String totalClean = jTextField4.getText().replace("Total: ₱", "").trim();
        pstOrder.setDouble(2, Double.parseDouble(totalClean));
        pstOrder.executeUpdate();

        // Get the Order ID
        java.sql.ResultSet rs = pstOrder.getGeneratedKeys();
        if (rs.next()) {
            int orderId = rs.getInt(1);

            // 2. Loop through table and save to tbl_order_items
            String sqlItems = "INSERT INTO tbl_order_items (o_id, b_name, oi_qty, oi_price) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement pstItems = conn.prepareStatement(sqlItems);

            for (int i = 0; i < tbl2.getRowCount(); i++) {
                pstItems.setInt(1, orderId);
                pstItems.setString(2, tbl2.getValueAt(i, 0).toString());
                pstItems.setInt(3, Integer.parseInt(tbl2.getValueAt(i, 1).toString()));
                String itemPrice = tbl2.getValueAt(i, 2).toString().replace("₱", "").trim();
                pstItems.setDouble(4, Double.parseDouble(itemPrice));
                pstItems.addBatch();
            }
            pstItems.executeBatch();

            JOptionPane.showMessageDialog(this, "Order Confirmed! Please prepare Cash on Delivery.");
            
            // Clear cart and close
            dashboard.CartManager.sharedModel.setRowCount(0);
            this.dispose();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error processing order: " + e.getMessage());
    }
}
   
  private void saveOrderToDatabase() {
    String sqlOrder = "INSERT INTO tbl_orders (u_id, o_total) VALUES (?, ?)";
    String sqlItems = "INSERT INTO tbl_order_items (o_id, b_name, oi_qty, oi_price) VALUES (?, ?, ?, ?)";

    try (Connection conn = config.config.connectDB();
         PreparedStatement pstOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {

        pstOrder.setInt(1, userId); // ✅ use userId field, NOT hardcoded 1
        double total = Double.parseDouble(jTextField4.getText().replace("Total: ₱", "").trim());
        pstOrder.setDouble(2, total);
        pstOrder.executeUpdate();

        try (ResultSet rs = pstOrder.getGeneratedKeys()) {
            if (rs.next()) {
                int orderId = rs.getInt(1);
                try (PreparedStatement pstItems = conn.prepareStatement(sqlItems)) {
                    for (int i = 0; i < tbl2.getRowCount(); i++) {
                        pstItems.setInt(1, orderId);
                        pstItems.setString(2, tbl2.getValueAt(i, 0).toString());
                        pstItems.setInt(3, Integer.parseInt(tbl2.getValueAt(i, 1).toString()));
                        pstItems.setDouble(4, Double.parseDouble(
                            tbl2.getValueAt(i, 2).toString().replace("₱", "").trim()));
                        pstItems.addBatch();
                    }
                    pstItems.executeBatch();
                }
            }
        }
        dashboard.CartManager.sharedModel.setRowCount(0);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Lock Error: " + e.getMessage());
    }
}
  private void showCashPopup() {
    String totalStr = jTextField4.getText().replace("Total: ₱", "").trim();
    double totalToPay = Double.parseDouble(totalStr);

    javax.swing.JDialog cashDialog = new javax.swing.JDialog(this, "Cash Payment", true);
    cashDialog.setSize(300, 250);
    cashDialog.setLocationRelativeTo(this);
    cashDialog.setLayout(null);

    JLabel lblTotal = new JLabel("Total: ₱" + String.format("%.2f", totalToPay));
    lblTotal.setBounds(30, 20, 200, 30);
    lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 16));
    cashDialog.add(lblTotal);

    JLabel lblAmount = new JLabel("Enter Cash Amount:");
    lblAmount.setBounds(30, 60, 200, 30);
    cashDialog.add(lblAmount);

    javax.swing.JTextField cashInput = new javax.swing.JTextField();
    cashInput.setBounds(30, 90, 220, 40);
    cashInput.setFont(new java.awt.Font("Segoe UI", 1, 18));
    cashDialog.add(cashInput);

    javax.swing.JButton btnPay = new javax.swing.JButton("Pay Now");
    btnPay.setBounds(30, 150, 220, 40);
    btnPay.setBackground(new java.awt.Color(0, 153, 51));
    btnPay.setForeground(java.awt.Color.WHITE);

    btnPay.addActionListener(e -> {
    try {
        double cashPaid = Double.parseDouble(cashInput.getText().trim());
        if (cashPaid < totalToPay) {
            JOptionPane.showMessageDialog(cashDialog, "Insufficient cash amount!");
        } else {
            saveOrderToDatabase();
            cashDialog.dispose();
            
            // ✅ Show receipt
            new reciept(
                jTextField1.getText().trim(),
                jTextField3.getText().trim(),
                jTextField2.getText().trim(),
                "Cash",
                dashboard.CartManager.sharedModel,  // ⚠️ pass BEFORE setRowCount(0)
                totalToPay,
                cashPaid
            ).setVisible(true);
            
            this.dispose();
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(cashDialog, "Please enter a valid number!");
    }
});

    cashDialog.add(btnPay);
    cashDialog.setVisible(true);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jRadioButton4 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jLabel1.setFont(new java.awt.Font("Yu Gothic Medium", 2, 18)); // NOI18N
        jLabel1.setText("CHECK OUT DETAILS");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tbl2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book Name", "Quantity", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Shipping Address");

        jLabel3.setText("Full Name");

        jLabel4.setText("Contact Number");

        jLabel5.setText("Payment Method");

        jRadioButton1.setText("Cashh");
        jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton1MouseClicked(evt);
            }
        });
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Credit Card");
        jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton2MouseClicked(evt);
            }
        });

        jRadioButton3.setText("Gcash");
        jRadioButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButton3MouseClicked(evt);
            }
        });

        jButton1.setText("Place Order");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRadioButton4.setText("Coins");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(2, 2, 2)))
                .addGap(34, 34, 34))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton3))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(38, 38, 38)
                .addComponent(jLabel5)
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addGap(20, 20, 20)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton1MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jRadioButton1MouseClicked

    private void tbl2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl2MouseClicked
        
    }//GEN-LAST:event_tbl2MouseClicked

    private void jRadioButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2MouseClicked

    private void jRadioButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3MouseClicked

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
     
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String fullName = jTextField1.getText().trim();
    String address = jTextField3.getText().trim();
    String contact = jTextField2.getText().trim();

    if (fullName.isEmpty() || address.isEmpty() || contact.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields!");
        return;
    }
    if (tbl2.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Cart is empty!");
        return;
    }

    if (jRadioButton1.isSelected()) {
        showCashPopup();
    } else if (jRadioButton2.isSelected()) {
        saveOrderToDatabase();
        JOptionPane.showMessageDialog(this, "Order placed via Credit Card!");
        this.dispose();
    } else if (jRadioButton3.isSelected()) {
        saveOrderToDatabase();
        JOptionPane.showMessageDialog(this, "Order placed via GCash!");
        this.dispose();
    }
    }//GEN-LAST:event_jButton1ActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tbl2;
    // End of variables declaration//GEN-END:variables
}
