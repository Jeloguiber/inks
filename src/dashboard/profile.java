package dashboard;

import config.config;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class profile extends javax.swing.JFrame {
    private int userId;
   public profile(String firstName, String lastName, String userEmail, DefaultTableModel cartData, int userId) {
    initComponents();
    this.userId = userId; // ✅ store it
    tbl2.setModel(CartManager.sharedModel);
    name.setText("" + firstName);
    lastname.setText("" + lastName);
    email.setText("" + userEmail);
    updateTotal();
     loadProfileIcon();
}
private void loadProfileIcon() {
    try {
        // ✅ Option 1 - if you have a profile image in your /images folder
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
            getClass().getResource("/images/profile.png")); // change filename to match yours
        java.awt.Image img = icon.getImage()
            .getScaledInstance(149, 144, java.awt.Image.SCALE_SMOOTH);
        iconProfilelbl.setIcon(new javax.swing.ImageIcon(img));
    } catch (Exception e) {
        // ✅ Option 2 - fallback: draw a default avatar using text
        iconProfilelbl.setText("👤");
        iconProfilelbl.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 80));
        iconProfilelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconProfilelbl.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        iconProfilelbl.setBackground(new java.awt.Color(204, 204, 255));
        iconProfilelbl.setOpaque(true);
    }
}
    private void updateTotal() {
        double grandTotal = 0;
        DefaultTableModel model = (DefaultTableModel) tbl2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                String priceStr = model.getValueAt(i, 2).toString().replace("₱", "").trim();
                double price = Double.parseDouble(priceStr);
                int qty = Integer.parseInt(model.getValueAt(i, 1).toString());
                grandTotal += (price * qty);
            } catch (Exception e) {
                // Ignore parsing errors for empty rows
            }
        }
        jTextField1.setText("₱ " + String.format("%.2f", grandTotal));
    }

    private void showBookPopup(String bookName) {
        // ✅ Fetch data first, close connection, THEN show dialog
        String author = "", date = "", price = "", imagePath = "";

        try (Connection conn = config.connectDB();
                PreparedStatement pst = conn.prepareStatement(
                        "SELECT * FROM tbl_books WHERE b_name = ?")) {

            pst.setString(1, bookName);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return;
                }
                author = rs.getString("b_author");
                date = rs.getString("b_date");
                price = rs.getString("b_price");
                imagePath = rs.getString("b_image");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return;
        }

        // ✅ Connection is fully closed before dialog opens
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, true);
        dialog.setTitle("Item Details");
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBounds(0, 0, 500, 400);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(20, 40, 180, 240);
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imagePath);
            java.awt.Image img = icon.getImage().getScaledInstance(180, 240, java.awt.Image.SCALE_SMOOTH);
            imageLabel.setIcon(new javax.swing.ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("No Image");
        }
        panel.add(imageLabel);

        JLabel title = new JLabel("<html>" + bookName + "</html>");
        title.setBounds(220, 40, 240, 50);
        title.setFont(new java.awt.Font("Segoe UI", 1, 18));
        panel.add(title);

        JLabel auth = new JLabel("Author: " + author);
        auth.setBounds(220, 100, 240, 30);
        auth.setFont(new java.awt.Font("Segoe UI", 0, 14));
        panel.add(auth);

        JLabel pub = new JLabel("Published: " + date);
        pub.setBounds(220, 140, 240, 30);
        panel.add(pub);

        JLabel prc = new JLabel("Price: ₱" + price);
        prc.setBounds(220, 180, 240, 30);
        prc.setFont(new java.awt.Font("Segoe UI", 1, 16));
        prc.setForeground(new java.awt.Color(0, 153, 51));
        panel.add(prc);

        javax.swing.JButton closeBtn = new javax.swing.JButton("Close View");
        closeBtn.setBounds(220, 240, 200, 40);
        closeBtn.addActionListener(e -> dialog.dispose());
        panel.add(closeBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public void setScaledImage(String imgPath, javax.swing.JLabel label) {
        try {
            javax.swing.ImageIcon imgIcon = new javax.swing.ImageIcon(getClass().getResource(imgPath));
            java.awt.Image img = imgIcon.getImage();
            java.awt.Image imgScale = img.getScaledInstance(label.getWidth(), label.getHeight(), java.awt.Image.SCALE_SMOOTH);
            label.setIcon(new javax.swing.ImageIcon(imgScale));
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }
    private void showOrderHistory() {
    String sql = "SELECT o.o_id, o.o_date, o.o_total " +
                 "FROM tbl_orders o WHERE o.u_id = ? ORDER BY o.o_date DESC";
    
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        new String[]{"Order ID", "Date", "Total"}, 0);

    try (java.sql.Connection conn = config.connectDB();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, userId);
        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("o_id"),
                    rs.getString("o_date"),
                    "₱" + rs.getString("o_total")
                });
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        return;
    }

    javax.swing.JTable orderTable = new javax.swing.JTable(model);
    javax.swing.JOptionPane.showMessageDialog(this,
        new javax.swing.JScrollPane(orderTable),
        "My Order History", javax.swing.JOptionPane.PLAIN_MESSAGE);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        iconProfilelbl = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        lastname = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        total = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        total1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cartPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setText("SHOP");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 130, 50));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 3, 36)); // NOI18N
        jLabel3.setText("COSTUMER DASHBOARD");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 520, 60));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 90));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        name.setBackground(new java.awt.Color(102, 255, 255));
        name.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        lastname.setBackground(new java.awt.Color(102, 255, 255));
        lastname.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        email.setBackground(new java.awt.Color(102, 255, 255));
        email.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iconProfilelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(iconProfilelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 190, 510));

        total.setBackground(new java.awt.Color(255, 204, 204));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(total1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(total1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        );

        jButton4.setText("Check Out");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        jLabel2.setText("Total");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(463, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(494, 494, 494)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(511, 511, 511)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1101, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cartPanel.setBackground(new java.awt.Color(255, 204, 204));

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

        javax.swing.GroupLayout cartPanelLayout = new javax.swing.GroupLayout(cartPanel);
        cartPanel.setLayout(cartPanelLayout);
        cartPanelLayout.setHorizontalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        cartPanelLayout.setVerticalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout totalLayout = new javax.swing.GroupLayout(total);
        total.setLayout(totalLayout);
        totalLayout.setHorizontalGroup(
            totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(cartPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        totalLayout.setVerticalGroup(
            totalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalLayout.createSequentialGroup()
                .addComponent(cartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 790, 410));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add to cart.png"))); // NOI18N
        jButton1.setText("CHECK OUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 540, 160, 47));

        jTextField1.setEditable(false);
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 540, 150, 50));

        jLabel1.setText("TOTAL");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 560, -1, -1));

        jButton2.setText("Show Order");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 540, 180, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1022, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbl2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl2MouseClicked
        int row = tbl2.getSelectedRow();
        if (row == -1) {
            return;
        }

        String bookName = tbl2.getValueAt(row, 0).toString();

        // 1 CLICK: Show info
        if (evt.getClickCount() == 1) {
            showBookPopup(bookName);
        } // 2 CLICKS: Delete from cart
        else if (evt.getClickCount() == 2) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove '" + bookName + "' from your cart?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                CartManager.sharedModel.removeRow(row);
                updateTotal();
            }
        }
    }//GEN-LAST:event_tbl2MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
     new BookShop().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if (tbl2.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Your cart is empty!");
        return;
    }
    checkout checkoutWindow = new checkout(userId); 
    checkoutWindow.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        showOrderHistory();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cartPanel;
    private javax.swing.JLabel email;
    private javax.swing.JLabel iconProfilelbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lastname;
    private javax.swing.JLabel name;
    private javax.swing.JTable tbl2;
    private javax.swing.JPanel total;
    private javax.swing.JLabel total1;
    // End of variables declaration//GEN-END:variables
}
