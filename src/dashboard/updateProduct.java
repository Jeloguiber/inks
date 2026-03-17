/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard;

/**
 *
 * @author aldre
 */
public class updateProduct extends javax.swing.JFrame {
   ManageProd mp; // Changed from admin to ManageProd
    String bookId = ""; 
    java.io.File selectedFile;
    
    // Updated Constructor
    public updateProduct(ManageProd mp) {
        initComponents();
        this.mp = mp; // Store the reference to ManageProd
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    // This stays the same
    public void setData(String id, String name, String price, String stock, String author, String date) {
        this.bookId = id;
        txtName3.setText(name);
        txtPrice3.setText(price);
        jTextField1.setText(stock);
        jTextField2.setText(author);
        jTextField3.setText(date);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtName3 = new javax.swing.JTextField();
        txtPrice3 = new javax.swing.JTextField();
        lbl_path3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(255, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "UPDATING PRODUCT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 225, 200))); // NOI18N

        jLabel13.setText("Book Name");

        jLabel15.setText("Image");

        jLabel16.setText("Price");

        txtPrice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrice3ActionPerformed(evt);
            }
        });

        lbl_path3.setText("Choose File");
        lbl_path3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_path3ActionPerformed(evt);
            }
        });

        jButton6.setText("UPDATE PRODUCT");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setText("Stock");

        jLabel2.setText("Author");

        jLabel3.setText("Publish Date");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel1)
                    .addComponent(jLabel16)
                    .addComponent(jLabel2)
                    .addComponent(jLabel13)
                    .addComponent(jLabel3))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(txtName3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtPrice3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(163, 163, 163))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(lbl_path3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(144, 144, 144))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtPrice3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_path3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPrice3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrice3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrice3ActionPerformed

    private void lbl_path3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_path3ActionPerformed
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
    int result = chooser.showOpenDialog(this);
    if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
        selectedFile = chooser.getSelectedFile();
        lbl_path3.setText(selectedFile.getName());
    }
    }//GEN-LAST:event_lbl_path3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String name   = txtName3.getText().trim();
    String price  = txtPrice3.getText().trim();
    String stock  = jTextField1.getText().trim();
    String author = jTextField2.getText().trim();
    String date   = jTextField3.getText().trim();

    if (name.isEmpty() || price.isEmpty() || stock.isEmpty() || author.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    try {
        String savedImagePath = "";

        // ✅ Only copy if a NEW image was selected
        if (selectedFile != null) {
            String destFolder = System.getProperty("user.dir") + "/src/images/books/";
            new java.io.File(destFolder).mkdirs();
            String destPath = destFolder + selectedFile.getName();
            java.nio.file.Files.copy(
                selectedFile.toPath(),
                new java.io.File(destPath).toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            savedImagePath = "src/images/books/" + selectedFile.getName();
        } else {
            // ✅ Keep existing image from DB
            java.sql.Connection connCheck = config.config.connectDB();
            java.sql.PreparedStatement pstCheck = connCheck.prepareStatement(
                "SELECT b_image FROM tbl_books WHERE b_id = ?");
            pstCheck.setString(1, bookId);
            java.sql.ResultSet rs = pstCheck.executeQuery();
            if (rs.next()) {
                savedImagePath = rs.getString("b_image");
            }
            rs.close();
            pstCheck.close();
            connCheck.close();
        }

        // ✅ Update the book
        java.sql.Connection conn = config.config.connectDB();
        java.sql.PreparedStatement pst = conn.prepareStatement(
            "UPDATE tbl_books SET b_name=?, b_author=?, b_date=?, " +
            "b_stock=?, b_price=?, b_image=? WHERE b_id=?");

        pst.setString(1, name);
        pst.setString(2, author);
        pst.setString(3, date);
        pst.setString(4, stock);
        pst.setString(5, price);
        pst.setString(6, savedImagePath);
        pst.setString(7, bookId);
        pst.executeUpdate();

        pst.close();
        conn.close();

        javax.swing.JOptionPane.showMessageDialog(this, "Book Successfully Updated!");

        if (mp != null) {
            mp.displayData();
            mp.loadStats();
        }

        this.dispose();

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton lbl_path3;
    private javax.swing.JTextField txtName3;
    private javax.swing.JTextField txtPrice3;
    // End of variables declaration//GEN-END:variables
}
