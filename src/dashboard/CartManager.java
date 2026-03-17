package dashboard;

import javax.swing.table.DefaultTableModel;

public class CartManager {
    public static javax.swing.table.DefaultTableModel sharedModel = 
        new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Book Name", "Quantity", "Price"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ← THIS is the fix
            }
        };
}

