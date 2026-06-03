package DCIT23FINALPROJ;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*; //shows unused but may cause error on some devices
import java.util.HashMap;

public class CAFPOS extends JFrame {
    // ============ VARIABLE DECLARATION ================
    JComboBox<String> categoryBox, itemBox, optionBox;
    JTextField qtyField, cashField;
    JLabel totalLabel, changeLabel, totalText, cashText, changeText, menuLabel, me;
    boolean darkMode = false;
    JScrollPane scroll;
    JButton themeBtn, addBtn, removeBtn, resetBtn, payBtn;
    DefaultTableModel model;
    JTable table;
    String item, option, key, category; 
    int qty, choice ,resultRP;
    double price, subtotal, cash, change;

    HashMap<String, Double> priceMap = new HashMap<>();

    double total = 0;

    public CAFPOS() {
        setTitle("Cafe POS System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        me = new JLabel("Made by synzawa");
        me.setBounds(785, 540, 100, 20);
        me.setBackground(Color.WHITE);
        me.setForeground(Color.WHITE);
        add(me);
        // ================= PRICE LIST =================
        setupPrices();

        menuLabel = new JLabel("Menu Section");
        menuLabel.setBounds(20, 10, 200, 25);
        add(menuLabel);

        categoryBox = new JComboBox<>(new String[]{"Drinks", "Snacks"});
        categoryBox.setBounds(20, 40, 150, 25);
        add(categoryBox);

        itemBox = new JComboBox<>();
        itemBox.setBounds(20, 70, 150, 25);
        add(itemBox);

        optionBox = new JComboBox<>();
        optionBox.setBounds(20, 100, 150, 25);
        add(optionBox);

        qtyField = new JTextField("1");
        qtyField.setBounds(20, 130, 150, 25);
        add(qtyField);

        addBtn = new JButton("Add Order");
        addBtn.setBounds(20, 160, 150, 25);
        add(addBtn);

        removeBtn = new JButton("Remove Selected");
        removeBtn.setBounds(20, 190, 150, 25);
        add(removeBtn);

        resetBtn = new JButton("Reset");
        resetBtn.setBounds(20, 220, 150, 25);
        add(resetBtn);
               
        // ================= THEME BUTTON =================
        themeBtn = new JButton("Dark Mode");
        themeBtn.setBounds(20, 260, 150, 25);
        add(themeBtn);

        // ================= TABLE =================
        model = new DefaultTableModel(new String[]{"Item", "Details", "Qty", "Price", "Subtotal"}, 0);
        table = new JTable(model);

        scroll = new JScrollPane(table);
        scroll.setBounds(200, 40, 650, 300);
        add(scroll);

        // ================= TOTAL =================
        totalText = new JLabel("Total:");
        totalText.setBounds(200, 360, 100, 25);
        add(totalText);

        totalLabel = new JLabel("0.00");
        totalLabel.setBounds(250, 360, 100, 25);
        add(totalLabel);

        // ================= PAYMENT =================
        cashText = new JLabel("Cash:");
        cashText.setBounds(200, 400, 100, 25);
        add(cashText);

        cashField = new JTextField();
        cashField.setBounds(250, 400, 150, 25);
        add(cashField);

        payBtn = new JButton("Compute Change");
        payBtn.setBounds(420, 400, 160, 25);
        add(payBtn);

        changeText = new JLabel("Change:");
        changeText.setBounds(200, 440, 100, 25);
        add(changeText);

        changeLabel = new JLabel("0.00");
        changeLabel.setBounds(270, 440, 150, 25);
        add(changeLabel);

        // ================= ACTIONS =================
        updateItems();

        categoryBox.addActionListener(e -> updateItems());

        itemBox.addActionListener(e -> updateOptions());

        addBtn.addActionListener(e -> addOrder());

        removeBtn.addActionListener(e -> removeOrder());

        resetBtn.addActionListener(e -> resetAll());

        payBtn.addActionListener(e -> computeChange());
        
        themeBtn.addActionListener(e -> themeChange());
        
        setVisible(true); //needs to be here para mawala yung 'disappearing effect'
    }

    // ================= PRICE SETUP =================
    void setupPrices() {

        // DRINKS
        priceMap.put("Latte-Small", 120.0);
        priceMap.put("Latte-Medium", 140.0);
        priceMap.put("Latte-Large", 160.0);

        priceMap.put("Cookies and Cream-Small", 130.0);
        priceMap.put("Cookies and Cream-Medium", 150.0);
        priceMap.put("Cookies and Cream-Large", 170.0);

        priceMap.put("Caramel Macchiato-Small", 140.0);
        priceMap.put("Caramel Macchiato-Medium", 160.0);
        priceMap.put("Caramel Macchiato-Large", 180.0);

        priceMap.put("Matcha-Small", 135.0);
        priceMap.put("Matcha-Medium", 155.0);
        priceMap.put("Matcha-Large", 175.0);

        // SNACKS - FRIES
        priceMap.put("Fries-Cheese", 80.0);
        priceMap.put("Fries-Sour and Cream", 75.0);

        // SNACKS - SANDWICH
        priceMap.put("Sandwich-Chicken Sandwich", 120.0);
        priceMap.put("Sandwich-Ham Sandwich", 110.0);
        priceMap.put("Sandwich-Egg Sandwich", 100.0);

        // SNACKS - CAKE
        priceMap.put("Cake Slice-Chocolate Cake", 130.0);
        priceMap.put("Cake Slice-Vanilla Cake", 120.0);
        priceMap.put("Cake Slice-Black Forrest Cake", 150.0);
    }

    // ================= UPDATE MENU =================
    void updateItems() {
        itemBox.removeAllItems();
        String category = (String) categoryBox.getSelectedItem();

        if (category.equals("Drinks")) {
            itemBox.addItem("Latte");
            itemBox.addItem("Cookies and Cream");
            itemBox.addItem("Caramel Macchiato");
            itemBox.addItem("Matcha");
        } else {
            itemBox.addItem("Fries");
            itemBox.addItem("Sandwich");
            itemBox.addItem("Cake Slice");
        }

        updateOptions();
    }

    void updateOptions() {
        optionBox.removeAllItems();
        item = (String) itemBox.getSelectedItem();
        category = (String) categoryBox.getSelectedItem();

        if (category.equals("Drinks")) {
            optionBox.addItem("Small");
            optionBox.addItem("Medium");
            optionBox.addItem("Large");
        } else {
            if (item !=null && item.equals("Fries")) {               //syntax "item!=null &&" is needed to avoid a data error. 
                optionBox.addItem("Cheese");
                optionBox.addItem("Sour and Cream");
            } else if (item !=null && item.equals("Sandwich")) {
                optionBox.addItem("Chicken Sandwich");
                optionBox.addItem("Ham Sandwich");
                optionBox.addItem("Egg Sandwich");
            } else {
                optionBox.addItem("Chocolate Cake");
                optionBox.addItem("Vanilla Cake");
                optionBox.addItem("Black Forrest Cake");
            }
        }
    }

    // ================= ADD ORDER =================
    void addOrder() {

        item = (String) itemBox.getSelectedItem();
        option = (String) optionBox.getSelectedItem();
        qty = Integer.parseInt(qtyField.getText());

        key = item + "-" + option;
        price = priceMap.get(key);
        subtotal = price * qty;

        model.addRow(new Object[]{item, option, qty, price, subtotal});

        total += subtotal;
        updateTotal();
    }

    // ================= REMOVE ORDER =================
    void removeOrder() {
        int row = table.getSelectedRow();
        if (row != -1) {
            double sub = (double) model.getValueAt(row, 4);
            total -= sub;
            model.removeRow(row);
            updateTotal();
        }
    }

    // ================= RESET =================
    void resetAll() {
        model.setRowCount(0);
        total = 0;
        updateTotal();
        cashField.setText("");
        changeLabel.setText("0.00");
    }
    
    // ================= CHANGE =================
    void computeChange() {

    try {
        cash = Double.parseDouble(cashField.getText());

        if (cash < total) {
            JOptionPane.showMessageDialog(
                    this,
                    "Insufficient Cash!");
            return;
        }

        change = cash - total;

        changeLabel.setText(String.format("%.2f", change));

        String[] options = {"Print Receipt", "New Transaction", "Close"};

        choice = JOptionPane.showOptionDialog(
                this,
                "Payment Successful!\n\nChange: ₱"
                        + String.format("%.2f", change),
                "Transaction Complete",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            printReceipt();
        }
        
        else if (choice == 1) {
            resetAll();
        }

    }
    catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Please enter a valid cash amount.");
    }
}
    
    // ================= TOTAL UPDATE =================
    void updateTotal() {
        totalLabel.setText(String.format("%.2f", total));
    }

    // =============== RECEIPT ===================
    void printReceipt() {

        cash = Double.parseDouble(cashField.getText());
        change = cash - total;

        String receipt = "";
        receipt += "===== CAFE RECEIPT =====\n\n";

        for (int i = 0; i < model.getRowCount(); i++) {

            receipt += model.getValueAt(i, 0) + " - "
                + model.getValueAt(i, 1) + "\n";

            receipt += "Qty: " + model.getValueAt(i, 2)
                + "    ₱" + model.getValueAt(i, 4);

            receipt += "\n\n";
        }

        receipt += "-------------------------\n";
        receipt += "TOTAL  : ₱" + String.format("%.2f", total) + "\n";
        receipt += "CASH   : ₱" + String.format("%.2f", cash) + "\n";
        receipt += "CHANGE : ₱" + String.format("%.2f", change) + "\n";
        receipt += "-------------------------\n";
        receipt += "Thank you for your purchase!";

        JTextArea area = new JTextArea(receipt);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        resultRP = JOptionPane.showConfirmDialog(
            this,
            new JScrollPane(area),
            "Receipt",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE
            );
            if (resultRP == JOptionPane.OK_OPTION) {
            resetAll();
        }
    }
    
    // =============== THEME CHANGE ===================
    void themeChange() {
         if (!darkMode) {
                
                // DARK MODE
                getContentPane().setBackground(Color.DARK_GRAY);
                
                table.setForeground(Color.WHITE);
                table.setBackground(new Color(45, 45, 45));
                table.setGridColor(Color.GRAY); 
                
                table.setSelectionBackground(new Color(70, 70, 70));
                table.setSelectionForeground(Color.WHITE);
                
                table.getTableHeader().setForeground(Color.WHITE);
                table.getTableHeader().setBackground(new Color(60, 60, 60));
                
                scroll.getViewport().setBackground(new Color(45, 45, 45));
                
                menuLabel.setForeground(Color.WHITE);
                totalText.setForeground(Color.WHITE);
                cashText.setForeground(Color.WHITE);
                changeText.setForeground(Color.WHITE);
                                
                totalLabel.setForeground(Color.WHITE);
                changeLabel.setForeground(Color.WHITE);
                
                categoryBox.setBackground(Color.GRAY);
                itemBox.setBackground(Color.GRAY);
                optionBox.setBackground(Color.GRAY);
                categoryBox.setForeground(Color.WHITE);
                itemBox.setForeground(Color.WHITE);
                optionBox.setForeground(Color.WHITE);
                
                qtyField.setBackground(Color.GRAY);
                cashField.setBackground(Color.GRAY);
                qtyField.setForeground(Color.WHITE);
                cashField.setForeground(Color.WHITE);
                
                addBtn.setBackground(Color.GRAY);
                removeBtn.setBackground(Color.GRAY);
                resetBtn.setBackground(Color.GRAY);
                payBtn.setBackground(Color.GRAY);
                themeBtn.setBackground(Color.GRAY);
                
                addBtn.setForeground(Color.WHITE);
                removeBtn.setForeground(Color.WHITE);
                resetBtn.setForeground(Color.WHITE);
                payBtn.setForeground(Color.WHITE);
                themeBtn.setForeground(Color.WHITE);
                
                me.setBackground(Color.WHITE);
                me.setForeground(Color.WHITE);
                
                themeBtn.setText("Light Mode");
                darkMode = true;
                
            } else {
                
                // LIGHT MODE
                getContentPane().setBackground(Color.WHITE);
                
                table.setBackground(Color.WHITE);
                table.setForeground(Color.BLACK);
                
                totalLabel.setForeground(Color.BLACK);
                changeLabel.setForeground(Color.BLACK);
                
                categoryBox.setBackground(Color.WHITE);
                itemBox.setBackground(Color.WHITE);
                optionBox.setBackground(Color.WHITE);
                
                qtyField.setBackground(Color.WHITE);
                cashField.setBackground(Color.WHITE);
                
                menuLabel.setForeground(Color.BLACK);
                totalText.setForeground(Color.BLACK);
                cashText.setForeground(Color.BLACK);
                changeText.setForeground(Color.BLACK);

                categoryBox.setForeground(Color.BLACK);
                itemBox.setForeground(Color.BLACK);
                optionBox.setForeground(Color.BLACK);

                qtyField.setForeground(Color.BLACK);
                cashField.setForeground(Color.BLACK);
                
                addBtn.setBackground(null);
                removeBtn.setBackground(null);
                resetBtn.setBackground(null);
                payBtn.setBackground(null);
                themeBtn.setBackground(null);
                table.getTableHeader().setBackground(null);
                
                addBtn.setForeground(Color.BLACK);
                removeBtn.setForeground(Color.BLACK);
                resetBtn.setForeground(Color.BLACK);
                payBtn.setForeground(Color.BLACK);
                themeBtn.setForeground(Color.BLACK);
                
                table.getTableHeader().setForeground(Color.BLACK);
                
                scroll.getViewport().setBackground(Color.WHITE);
                
                me.setBackground(Color.BLACK);
                me.setForeground(Color.BLACK);
                
                themeBtn.setText("Dark Mode");
                darkMode = false;
            }        
    }
    public static void main(String[] args) {
        new CAFPOS();
    }
}
