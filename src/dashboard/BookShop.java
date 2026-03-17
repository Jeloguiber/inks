package dashboard;

import config.config;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class BookShop extends javax.swing.JFrame {

    public BookShop() {
        initComponents();
        displayProducts();
        tbl2.setModel(dashboard.CartManager.sharedModel);
        total.setText("₱ 0.00");
        jButton1.setText("⬅ Back to Profile");
        jButton1.setBackground(new java.awt.Color(255, 102, 102));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        jButton1.addActionListener(e -> this.dispose());
    }

    public void displayProducts() {
        javax.swing.table.DefaultTableModel model
                = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT b_name, b_author, b_date, b_price, b_stock FROM tbl_books ORDER BY b_name";
            try (Connection conn = config.connectDB();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("b_name"),
                        rs.getString("b_author"),
                        rs.getString("b_date"),
                        "₱" + rs.getString("b_price"),
                        rs.getString("b_stock")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    public int currentUserId = 1;

    private void handleCheckout() {
        DefaultTableModel cartModel = (DefaultTableModel) tbl2.getModel();

        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        double grandTotal = Double.parseDouble(total.getText().replace("₱", "").trim());

        try (Connection conn = config.connectDB()) {
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO tbl_orders (u_id, o_total) VALUES (?, ?)";
            PreparedStatement pstOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            pstOrder.setInt(1, currentUserId);
            pstOrder.setDouble(2, grandTotal);
            pstOrder.executeUpdate();

            ResultSet rs = pstOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            String itemSql = "INSERT INTO tbl_order_items (o_id, b_name, oi_qty, oi_price) VALUES (?, ?, ?, ?)";
            String updateStockSql = "UPDATE tbl_books SET b_stock = b_stock - ? WHERE b_name = ?";

            PreparedStatement pstItem = conn.prepareStatement(itemSql);
            PreparedStatement pstStock = conn.prepareStatement(updateStockSql);

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                String name = cartModel.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(cartModel.getValueAt(i, 1).toString());
                double price = Double.parseDouble(cartModel.getValueAt(i, 2).toString().replace("₱", "").trim());

                pstItem.setInt(1, orderId);
                pstItem.setString(2, name);
                pstItem.setInt(3, qty);
                pstItem.setDouble(4, price);
                pstItem.addBatch();

                pstStock.setInt(1, qty);
                pstStock.setString(2, name);
                pstStock.addBatch();
            }

            pstItem.executeBatch();
            pstStock.executeBatch();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Checkout Successful! Order ID: " + orderId);

            cartModel.setRowCount(0);
            updateTotal();
            displayProducts();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Checkout Failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        cartPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl2 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        total = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBackground(new java.awt.Color(255, 204, 204));
        jTable1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book Name", "Author", "Published Date", "Price", "Stockll"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Your cart", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        cartPanel.setBackground(new java.awt.Color(255, 204, 204));

        tbl2.setBackground(new java.awt.Color(204, 255, 0));
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
            .addGroup(cartPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cartPanelLayout.setVerticalGroup(
            cartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cartPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        );

        jLabel2.setText("Total");

        jButton1.setText("Back to Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(cartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jLabel1.setText("Welcome to Our Book Store");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (evt.getClickCount() == 1) { // SINGLE CLICK
            int row = jTable1.getSelectedRow();

            if (row >= 0) {
                String bookName = jTable1.getValueAt(row, 0).toString();
                showBookPopup(bookName);
            }
        }


    }//GEN-LAST:event_jTable1MouseClicked

    private void tbl2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl2MouseClicked
        if (evt.getClickCount() == 2) {
            int row = tbl2.getSelectedRow();

            if (row >= 0) {
                // Stop any active editing just in case
                if (tbl2.isEditing()) {
                    tbl2.getCellEditor().stopCellEditing();
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Remove '" + tbl2.getValueAt(row, 0) + "' from cart?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Remove from the shared model so it updates the Profile too
                    dashboard.CartManager.sharedModel.removeRow(row);

                    // Refresh the total label
                    updateTotal();
                }
            }
        }

    }//GEN-LAST:event_tbl2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void showBookPopup(String bookName) {
        try (Connection conn = config.connectDB();
                PreparedStatement pst = conn.prepareStatement(
                        "SELECT * FROM tbl_books WHERE b_name = ?")) {

            pst.setString(1, bookName);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String author = rs.getString("b_author");
                    String date = rs.getString("b_date");
                    String price = rs.getString("b_price");
                    String stock = rs.getString("b_stock");
                    String imagePath = rs.getString("b_image");

                    showBookDialog(bookName, author, date, price, stock, imagePath);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showBookDialog(String bookName, String author, String date,
            String price, String stock, String imagePath) {
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, true);
        dialog.setTitle("Book Information");
        dialog.setSize(520, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new java.awt.Color(250, 245, 255));
        panel.setBounds(0, 0, 520, 450);

                    JLabel imageLabel = new JLabel();
            imageLabel.setBounds(20, 40, 200, 260);
            try {
                // ✅ Build full path from project root
                String fullImagePath = System.getProperty("user.dir") + "/" + imagePath;
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(fullImagePath);
                java.awt.Image img = icon.getImage().getScaledInstance(200, 260, java.awt.Image.SCALE_SMOOTH);
                imageLabel.setIcon(new javax.swing.ImageIcon(img));
            } catch (Exception e) {
                imageLabel.setText("No Image");
            }
        panel.add(imageLabel);

        JLabel title = new JLabel(bookName);
        title.setBounds(240, 40, 250, 40);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        panel.add(title);

        JLabel authorLbl = new JLabel("Author: " + author);
        authorLbl.setBounds(240, 90, 250, 30);
        authorLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        panel.add(authorLbl);

        JLabel dateLbl = new JLabel("Published: " + date);
        dateLbl.setBounds(240, 130, 250, 30);
        dateLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        panel.add(dateLbl);

        JLabel priceLbl = new JLabel("Price: ₱" + price);
        priceLbl.setBounds(240, 170, 250, 30);
        priceLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panel.add(priceLbl);

        JLabel stockLbl = new JLabel("Stock: " + stock);
        stockLbl.setBounds(240, 210, 250, 30);
        stockLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        panel.add(stockLbl);

        JLabel qtyLbl = new JLabel("Quantity:");
        qtyLbl.setBounds(240, 250, 100, 30);
        qtyLbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        panel.add(qtyLbl);

        javax.swing.JSpinner qtySpinner = new javax.swing.JSpinner(
                new javax.swing.SpinnerNumberModel(1, 1, Integer.parseInt(stock), 1));
        qtySpinner.setBounds(320, 250, 60, 30);
        panel.add(qtySpinner);

        javax.swing.JButton addBtn = new javax.swing.JButton("Add To Cart");
        addBtn.setBounds(240, 310, 200, 45);
        addBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        addBtn.setBackground(new java.awt.Color(255, 102, 102));
        addBtn.setForeground(java.awt.Color.WHITE);
        addBtn.addActionListener(e -> {
            int quantity = (Integer) qtySpinner.getValue();
            dashboard.CartManager.sharedModel.addRow(new Object[]{
                bookName, quantity, "₱" + price
            });
            updateTotal();
            JOptionPane.showMessageDialog(dialog, "Book added to cart!");
            dialog.dispose();
        });
        panel.add(addBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateTotal() {
        double grandTotal = 0;
        for (int i = 0; i < tbl2.getRowCount(); i++) {
            try {
                String priceStr = tbl2.getValueAt(i, 2).toString();
                String cleanPrice = priceStr.replace("₱", "").trim();
                double price = Double.parseDouble(cleanPrice);
                int qty = Integer.parseInt(tbl2.getValueAt(i, 1).toString());
                grandTotal += (price * qty);
            } catch (Exception e) {
            }
        }
        total.setText("₱ " + String.format("%.2f", grandTotal));
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BookShop().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cartPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tbl2;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
