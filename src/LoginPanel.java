import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoginPanel extends JPanel {
    public LoginPanel(App app, CardLayout cardLayout, Container parentContainer, ArrayList<User> registeredUsers) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Log In", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel for input fields using GridBagLayout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Labels and input fields
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        JTextField usernameField = new JTextField(15); // Adjusted width
        usernameField.setFont(new Font("Arial", Font.PLAIN, 25));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        JPasswordField passwordField = new JPasswordField(15); // Adjusted width
        passwordField.setFont(new Font("Arial", Font.PLAIN, 25));

        JButton loginButton = new JButton("Log In");
        JButton backButton = new JButton("Back");

        loginButton.setFont(new Font("Arial", Font.PLAIN, 25));
        backButton.setFont(new Font("Arial", Font.PLAIN, 25));

        // Adjust spacing and alignment with GridBagConstraints
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components stretch horizontally

        // Add username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(usernameField, gbc);

        // Add password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(passwordField, gbc);

        // Add Sign Up and Back buttons below the input fields
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2; // Span both columns
        JPanel buttonPanel = new JPanel(); // Button panel for layout
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        inputPanel.add(buttonPanel, gbc);

        // Add input panel to the center
        add(inputPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username or Password cannot be empty!", "Login Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (authenticate(username, password, registeredUsers)) {
                JOptionPane.showMessageDialog(this, "Login Successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                for (int i = 0; i < registeredUsers.size(); i++) {
                    if (username.equals(registeredUsers.get(i).getName()) ) {
                        app.setCurrentUser(registeredUsers.get(i));
                    }
                }

                cardLayout.show(parentContainer, "Game");
                GamePanel gamePanel = (GamePanel) parentContainer.getComponent(3);
                gamePanel.updateUserLabel(app.currentUser);
                gamePanel.updateBestScoreLabel(app.currentUser);
                gamePanel.updateErrorLabel();
                gamePanel.showCards();
                gamePanel.startHideCardtimer();
            } else {
                usernameField.setText("");
                passwordField.setText("");
                JOptionPane.showMessageDialog(this, "Incorrect Details.", "Login Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(parentContainer, "MainMenu"));
    }

    private boolean authenticate(String username, String password, ArrayList<User> registeredUsers) {
        for (User user : registeredUsers) {
            if (user.getName().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }
}
