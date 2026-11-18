package mattresssalesapp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mattress Sales Management System
 * Q2.2 - Customer text box, product combo box, price text box
 * Q2.3 - Process button calculates VAT / Commission / Total and saves to data.txt
 * Q2.4 - Menu bar: File > Exit, Tools > Process | Clear
 * Q2.5 - Uses ProductCalculations (implements IProductCalculations)
 */
public class MattressSalesApp extends JFrame {

    // ---- Components ----
    private JTextField txtCustomer, txtPrice;
    private JComboBox<String> cboProduct;
    private JTextField txtVat, txtCommission, txtTotal;
    private JButton btnProcess, btnClear, btnExit;

    // ---- Constants ----
    private static final DecimalFormat MONEY = new DecimalFormat("R #,##0.00");
    private static final String FILE_NAME = "data.txt";

    private static final String[] PRODUCTS = {
            "-- Select Product --",
            "Cozy Biggts Monarch",
            "Rostonic Barbados",
            "Sealy Phuket"
    };

    public MattressSalesApp() {
        setTitle("Mattress Sales Management System");
        setSize(520, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // ---------- Q2.4 : MENU BAR ----------
        buildMenuBar();

        // ---------- Header ----------
        JLabel header = new JLabel("MATTRESS SALES - PROCESS SALE", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(45, 62, 80));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(520, 55));
        add(header, BorderLayout.NORTH);

        // ---------- Form ----------
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35));
        form.setBackground(new Color(245, 245, 245));

        form.add(makeLabel("Customer Name:"));
        txtCustomer = new JTextField();
        form.add(txtCustomer);

        form.add(makeLabel("Product Name:"));
        cboProduct = new JComboBox<>(PRODUCTS);
        cboProduct.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(cboProduct);

        form.add(makeLabel("Product Price (R):"));
        txtPrice = new JTextField();
        form.add(txtPrice);

        form.add(makeLabel("VAT Amount (15%):"));
        txtVat = new JTextField();
        txtVat.setEditable(false);
        txtVat.setBackground(new Color(230, 230, 230));
        form.add(txtVat);

        form.add(makeLabel("Commission Amount (15%):"));
        txtCommission = new JTextField();
        txtCommission.setEditable(false);
        txtCommission.setBackground(new Color(230, 230, 230));
        form.add(txtCommission);

        form.add(makeLabel("Total Sales:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(220, 240, 220));
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        form.add(txtTotal);

        add(form, BorderLayout.CENTER);

        // ---------- Buttons ----------
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttons.setBackground(new Color(245, 245, 245));

        btnProcess = new JButton("Process");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");

        styleButton(btnProcess, new Color(46, 139, 87));
        styleButton(btnClear, new Color(70, 130, 180));
        styleButton(btnExit, new Color(178, 34, 34));

        buttons.add(btnProcess);
        buttons.add(btnClear);
        buttons.add(btnExit);
        add(buttons, BorderLayout.SOUTH);

        // ---------- Button handlers (share the same methods as the menu) ----------
        btnProcess.addActionListener(e -> processSale());
        btnClear.addActionListener(e -> clearForm());
        btnExit.addActionListener(e -> exitApplication());
    }

    // ---------- Q2.4 : build the menu bar ----------
    private void buildMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // ---- File menu -> Exit ----
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitItem);

        // ---- Tools menu -> Process | Clear ----
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        JMenuItem clearItem = new JMenuItem("Clear");

        processItem.addActionListener(e -> processSale());
        clearItem.addActionListener(e -> clearForm());

        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);
    }

    // ---------- Exit handler (used by both button and menu) ----------
    private void exitApplication() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Exit",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) System.exit(0);
    }

    // ---------- Helper: styled label ----------
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    // ---------- Helper: styled button ----------
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    // ---------- Q2.3 + Q2.5 : Process sale ----------
    private void processSale() {

        // 1. Validate customer
        String customer = txtCustomer.getText().trim();
        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the customer's name.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            txtCustomer.requestFocus();
            return;
        }

        // 2. Validate selected product
        int idx = cboProduct.getSelectedIndex();
        if (idx <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a mattress product from the list.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            cboProduct.requestFocus();
            return;
        }
        String product = cboProduct.getSelectedItem().toString();

        // 3. Validate price
        String priceText = txtPrice.getText().trim();
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the product price.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Product price must be a valid number (e.g. 2500.00).",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        if (price <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Product price must be greater than zero.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        // 4. Q2.5 : Calculations handled by ProductCalculations
        ProductCalculations calc = new ProductCalculations(price);
        double vat = calc.getVat();
        double commission = calc.getCommission();
        double total = calc.getTotal();

        // 5. Display results on the form
        txtVat.setText(MONEY.format(vat));
        txtCommission.setText(MONEY.format(commission));
        txtTotal.setText(MONEY.format(total));

        // 6. Save record to data.txt
        try {
            saveToFile(customer, product, price, vat, commission, total);

            JOptionPane.showMessageDialog(this,
                    "SALE PROCESSED & SAVED SUCCESSFULLY\n\n"
                  + "Customer : " + customer + "\n"
                  + "Product : " + product + "\n"
                  + "Price : " + MONEY.format(price) + "\n"
                  + "VAT (15%) : " + MONEY.format(vat) + "\n"
                  + "Commission (15%): " + MONEY.format(commission) + "\n"
                  + "--------------------------------------\n"
                  + "TOTAL SALES : " + MONEY.format(total) + "\n\n"
                  + "Record appended to: " + new File(FILE_NAME).getAbsolutePath(),
                    "Sales Summary", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "The sale was calculated but could NOT be saved to data.txt.\n\n"
                  + "Error: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Write one record to data.txt ----------
    private void saveToFile(String customer, String product, double price,
                            double vat, double commission, double total)
            throws IOException {

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String record = String.format(
                "Customer: %s | Product: %s | Price: %s | VAT: %s | Commission: %s | Total: %s | Date: %s",
                customer, product,
                MONEY.format(price), MONEY.format(vat),
                MONEY.format(commission), MONEY.format(total),
                timestamp);

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            out.println(record);
        }
    }

    // ---------- Q2.4 : Clear form ----------
    private void clearForm() {
        txtCustomer.setText("");
        cboProduct.setSelectedIndex(0);
        txtPrice.setText("");
        txtVat.setText("");
        txtCommission.setText("");
        txtTotal.setText("");
        txtCustomer.requestFocus();
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MattressSalesApp().setVisible(true));
    }
}