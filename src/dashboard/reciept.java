package dashboard;

public class reciept extends javax.swing.JFrame {

    public reciept(String fullName, String address, String contact, 
                   String paymentMethod, javax.swing.table.DefaultTableModel cartModel, 
                   double total, double cashPaid) {
        initComponents();
        setLocationRelativeTo(null);
        displayReceipt(fullName, address, contact, paymentMethod, cartModel, total, cashPaid);
    }

   private void displayReceipt(String fullName, String address, String contact,
                            String paymentMethod, javax.swing.table.DefaultTableModel cartModel,
                            double total, double cashPaid) {

    String line = "========================================\n";
    String thinLine = "----------------------------------------\n";
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    java.time.format.DateTimeFormatter fmt = 
        java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    StringBuilder sb = new StringBuilder();

    sb.append(line);
    sb.append("            INKSPIRE BOOK STORE         \n");
    sb.append("         Your Story Starts Here 📖      \n");
    sb.append(line);
    sb.append("  Date   : ").append(now.format(fmt)).append("\n");
    sb.append("  Name   : ").append(fullName).append("\n");
    sb.append("  Address: ").append(address).append("\n");
    sb.append("  Contact: ").append(contact).append("\n");
    sb.append("  Payment: ").append(paymentMethod).append("\n");
    sb.append(thinLine);
    sb.append(String.format("  %-22s %5s %8s\n", "ITEM", "QTY", "PRICE"));
    sb.append(thinLine);

    for (int i = 0; i < cartModel.getRowCount(); i++) {
        String name = cartModel.getValueAt(i, 0).toString();
        String qty  = cartModel.getValueAt(i, 1).toString();
        String price = cartModel.getValueAt(i, 2).toString().replace("₱", "").trim();

        if (name.length() > 22) name = name.substring(0, 19) + "...";

        double itemTotal = Double.parseDouble(price) * Integer.parseInt(qty);
        sb.append(String.format("  %-22s %5s  %7s\n",
            name, qty, "₱" + String.format("%.2f", itemTotal)));
    }

    sb.append(thinLine);
    sb.append(String.format("  %-28s %8s\n", "TOTAL", "₱" + String.format("%.2f", total)));

    if (paymentMethod.equals("Cash") && cashPaid > 0) {
        sb.append(String.format("  %-28s %8s\n", "CASH PAID", 
            "₱" + String.format("%.2f", cashPaid)));
        sb.append(String.format("  %-28s %8s\n", "CHANGE", 
            "₱" + String.format("%.2f", cashPaid - total)));
    }

    sb.append(line);
    sb.append("        Thank you for your purchase!     \n");
    sb.append("          Please come back again! 😊     \n");
    sb.append(line);

    reciept.setText(sb.toString());
    reciept.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
    reciept.setEditable(false);
}

    // Keep your existing initComponents() unchanged below...
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        reciept = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        reciept.setColumns(20);
        reciept.setRows(5);
        jScrollPane1.setViewportView(reciept);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea reciept;
    // End of variables declaration//GEN-END:variables
}
