/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import config.config;
public class ManageProd extends javax.swing.JFrame {

    /**
     * Creates new form ManageProd
     */
    public ManageProd() {
        initComponents();
        displayData();
         loadStats();
        
    }
    public void loadStats() {
    try (Connection conn = config.connectDB()) {

        // ✅ Total Products (count of distinct books)
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM tbl_books");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                totalProd.setText("  " + rs.getInt("total") + " Books");
                totalProd.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18));
            }
        }

        // ✅ Total Stock (sum of all stock)
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT SUM(b_stock) AS total FROM tbl_books");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                totalStock.setText("  " + rs.getInt("total") + " pcs");
                totalStock.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18));
            }
        }

        // ✅ Total Sales (sum of all order totals)
        try (PreparedStatement pst = conn.prepareStatement(
                "SELECT SUM(o_total) AS total FROM tbl_orders");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                double sales = rs.getDouble("total");
                totalSale.setText("  ₱" + String.format("%.2f", sales));
                totalSale.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18));
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Stats Error: " + e.getMessage());
    }
}
    public void displayData() {
    String[] columnNames = {"ID", "Book Name", "Author", "Published Date", "Price", "Stock"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    // ✅ try-with-resources — was leaking before
    try (Connection conn = config.connectDB();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM tbl_books")) {

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("b_id"),
                rs.getString("b_name"),
                rs.getString("b_author"),
                rs.getString("b_date"),
                rs.getDouble("b_price"),
                rs.getInt("b_stock")
            });
        }
        jTable1.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
   private void showAddStockPopup(String bookId, String bookName, int currentStock) {
    javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Update Stock", true);
    dialog.setSize(400, 280);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(null);
    dialog.getContentPane().setBackground(new java.awt.Color(255, 204, 204));

    javax.swing.JLabel lblBook = new javax.swing.JLabel("Book: " + bookName);
    lblBook.setBounds(20, 15, 360, 25);
    lblBook.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 13));
    dialog.add(lblBook);

    javax.swing.JLabel lblCurrent = new javax.swing.JLabel("Current Stock: " + currentStock + " pcs");
    lblCurrent.setBounds(20, 50, 360, 25);
    lblCurrent.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    dialog.add(lblCurrent);

    javax.swing.JLabel lblAdd = new javax.swing.JLabel("Add Stock:");
    lblAdd.setBounds(20, 90, 100, 25);
    lblAdd.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
    dialog.add(lblAdd);

    javax.swing.JSpinner spinner = new javax.swing.JSpinner(
        new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
    spinner.setBounds(130, 90, 100, 30);
    dialog.add(spinner);

    javax.swing.JLabel lblNew = new javax.swing.JLabel("New Total: " + (currentStock + 1) + " pcs");
    lblNew.setBounds(20, 135, 360, 25);
    lblNew.setFont(new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12));
    lblNew.setForeground(new java.awt.Color(0, 102, 0));
    dialog.add(lblNew);

    spinner.addChangeListener(e -> {
        int addQty = (Integer) spinner.getValue();
        lblNew.setText("New Total: " + (currentStock + addQty) + " pcs");
    });

    javax.swing.JButton btnConfirm = new javax.swing.JButton("Add Stock");
    btnConfirm.setBounds(20, 210, 160, 40);
    btnConfirm.setBackground(new java.awt.Color(0, 153, 51));
    btnConfirm.setForeground(java.awt.Color.WHITE);
    btnConfirm.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 13));
    dialog.add(btnConfirm);

    javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");
    btnCancel.setBounds(200, 210, 160, 40);
    btnCancel.addActionListener(e -> dialog.dispose());
    dialog.add(btnCancel);

    btnConfirm.addActionListener(e -> {
        int addQty = (Integer) spinner.getValue();
        String sql = "UPDATE tbl_books SET b_stock = b_stock + ? WHERE b_id = ?";

        try (Connection conn = config.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, addQty);
            pst.setString(2, bookId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(dialog,
                "Stock updated! New total: " + (currentStock + addQty) + " pcs");

            dialog.dispose();
            displayData();  // ✅ refresh table
            loadStats();    // ✅ refresh stats panel

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    });

    dialog.setVisible(true);
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        totalProd = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        totalStock = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        totalSale = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Manage Book", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 204, 255));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Total Product"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalProd, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalProd, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Stock"));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalStock, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Total Sale"));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalSale, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(totalSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Book Name", "Author", "Published Date", "Price", "Stockll"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 49, Short.MAX_VALUE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        BookShop bookShop = new BookShop(); 
        addProduct addProd = new addProduct(this, bookShop);
        addProd.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
            int row = jTable1.getSelectedRow();
    if (row == -1) return;

    // ✅ Safe parsing - handles both "5" and "5.0"
    String stockStr = jTable1.getValueAt(row, 5).toString();
    int currentStock = 0;
    try {
        currentStock = Integer.parseInt(stockStr.split("\\.")[0]); // handles "5.0" → "5"
    } catch (Exception e) {
        currentStock = 0;
    }

    if (currentStock == 0 || evt.getClickCount() == 2) {
        String bookId = jTable1.getValueAt(row, 0).toString();
        String bookName = jTable1.getValueAt(row, 1).toString();
        showAddStockPopup(bookId, bookName, currentStock);
    }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
          int row = jTable1.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to update!");
        return;
    }

    String id     = jTable1.getValueAt(row, 0).toString();
    String name   = jTable1.getValueAt(row, 1).toString();
    String author = jTable1.getValueAt(row, 2).toString();
    String date   = jTable1.getValueAt(row, 3).toString();
    String price  = jTable1.getValueAt(row, 4).toString();
    // ✅ Clean stock value before passing
    String stock  = jTable1.getValueAt(row, 5).toString().split("\\.")[0];

    updateProduct up = new updateProduct(this);
    up.setData(id, name, price, stock, author, date);
    up.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        int row = jTable1.getSelectedRow();
    
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a book from the table to delete.");
        return;
    }

    // 2. Get the ID of the selected book (assuming ID is in Column 0)
    String id = jTable1.getValueAt(row, 0).toString();
    String bookName = jTable1.getValueAt(row, 1).toString();

    // 3. Confirm deletion with the user
    int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + bookName + "'?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // 4. SQL Delete Query
            String sql = "DELETE FROM tbl_books WHERE b_id = ?";
            
            try (Connection conn = config.connectDB();
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                
                pst.setString(1, id);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Book Deleted Successfully!");
                
                // 5. Refresh the table to show updated list
                displayData(); 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }
    
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel totalProd;
    private javax.swing.JLabel totalSale;
    private javax.swing.JLabel totalStock;
    // End of variables declaration//GEN-END:variables

}
