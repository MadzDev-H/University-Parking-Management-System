package smartpark.ui;

import smartpark.manager.*;
import smartpark.model.Transport;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame implements ActionListener {
    private static final Color NAVY = new Color(25, 43, 72);
    private static final Color BLUE = new Color(48, 104, 184);
    private static final Color GREEN = new Color(45, 145, 93);
    private static final Color GRAY = new Color(100, 110, 125);
    private static final Color LIGHT = new Color(242, 245, 249);

    private LoginManager loginManager;
    private ParkingManager parkingManager;
    private BookingManager bookingManager;
    private SessionManager sessionManager;
    private ArrayList<Transport> transports;

    private CardLayout cardLayout;
    private JPanel pages;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public LoginFrame(LoginManager loginManager,
                      ParkingManager parkingManager,
                      BookingManager bookingManager,
                      SessionManager sessionManager,
                      ArrayList<Transport> transports) {
        this.loginManager = loginManager;
        this.parkingManager = parkingManager;
        this.bookingManager = bookingManager;
        this.sessionManager = sessionManager;
        this.transports = transports;

        setTitle("Campus Smart Parking");
        setSize(500, 460);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        pages = new JPanel(cardLayout);
        pages.setBackground(LIGHT);
        pages.add(makeLoginPage(), "login");
        pages.add(makeRegisterPage(), "register");
        setContentPane(pages);
    }

    private JPanel makeLoginPage() {
        JPanel page = makePage();
        page.add(makeTitle("CAMPUS SMART PARKING",
                "Management System"), BorderLayout.NORTH);

        JPanel form = makeForm();
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        addField(form, "Username", usernameField);
        addField(form, "Password", passwordField);

        // Keep the two-row login form centered, so its fields do not stretch.
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(form);
        page.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(makeButton("LOGIN", BLUE, "login"));
        buttons.add(makeButton("CREATE ACCOUNT", GREEN, "showRegister"));
        page.add(buttons, BorderLayout.SOUTH);

        passwordField.setActionCommand("login");
        passwordField.addActionListener(this);
        return page;
    }

    private JPanel makeRegisterPage() {
        JPanel page = makePage();
        page.add(makeTitle("CREATE ACCOUNT",
                "Register a campus user"), BorderLayout.NORTH);

        JPanel form = makeForm();
        nameField = new JTextField();
        contactField = new JTextField();
        newUsernameField = new JTextField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        addField(form, "Full name", nameField);
        addField(form, "Contact number", contactField);
        addField(form, "Username", newUsernameField);
        addField(form, "Password", newPasswordField);
        addField(form, "Confirm password", confirmPasswordField);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(form);
        page.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(makeButton("CREATE", GREEN, "register"));
        buttons.add(makeButton("CLEAR", GRAY, "clearRegister"));
        buttons.add(makeButton("BACK TO LOGIN", NAVY, "showLogin"));
        page.add(buttons, BorderLayout.SOUTH);
        return page;
    }

    private JPanel makePage() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(LIGHT);
        page.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        return page;
    }

    private JPanel makeTitle(String title, String subtitle) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 3, 3));
        panel.setOpaque(false);

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setForeground(NAVY);

        JLabel smallText = new JLabel(subtitle, SwingConstants.CENTER);
        smallText.setForeground(GRAY);

        panel.add(heading);
        panel.add(smallText);
        return panel;
    }

    private JPanel makeForm() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        return form;
    }

    private void addField(JPanel panel, String label, JTextField field) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(NAVY);
        panel.add(fieldLabel);

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(175, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(195, 205, 218)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        panel.add(field);
    }

    private JButton makeButton(String text, Color color, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setFocusPainted(false);
        return button;
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("login")) {
            logIn();
        } else if (command.equals("showRegister")) {
            cardLayout.show(pages, "register");
        } else if (command.equals("register")) {
            register();
        } else if (command.equals("clearRegister")) {
            clearRegisterFields();
        } else if (command.equals("showLogin")) {
            cardLayout.show(pages, "login");
        }
    }

    private void logIn() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("") || password.equals("")) {
            showMessage("Please enter a username and password.");
            return;
        }

        if (!loginManager.login(username, password)) {
            showMessage("Invalid username or password.");
            return;
        }

        passwordField.setText("");
        setVisible(false);

        MainFrame mainFrame = new MainFrame(loginManager, this,
                parkingManager, bookingManager, sessionManager, transports);
        mainFrame.setVisible(true);
    }

    private void register() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String username = newUsernameField.getText().trim();
        String password = new String(newPasswordField.getPassword());
        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (name.equals("") || contact.equals("") || username.equals("")
                || password.equals("") || confirmPassword.equals("")) {
            showMessage("Complete every registration field.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match.");
            return;
        }

        if (!loginManager.register(name, username, password, contact)) {
            showMessage("Account could not be created. Check the username.");
            return;
        }

        clearRegisterFields();
        usernameField.setText(username);
        cardLayout.show(pages, "login");
        showMessage("Account created successfully.");
    }

    private void clearRegisterFields() {
        nameField.setText("");
        contactField.setText("");
        newUsernameField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
