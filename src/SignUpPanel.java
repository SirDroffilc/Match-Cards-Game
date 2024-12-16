import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SignUpPanel extends JPanel {
    public SignUpPanel(App app, CardLayout cardLayout, Container parentContainer, ArrayList<User> registeredUsers) {
        setLayout(new BorderLayout());

        // Title at the top
        JLabel titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel for input fields and buttons using GridBagLayout
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

        // Sign Up and Back buttons
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        signUpButton.setFont(new Font("Arial", Font.PLAIN, 25));
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
        buttonPanel.add(signUpButton);
        buttonPanel.add(backButton);
        inputPanel.add(buttonPanel, gbc);

        // Add input panel to the center
        add(inputPanel, BorderLayout.CENTER);

        // Sign Up button action
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username or Password cannot be empty!", "Sign Up Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (isUsernameTaken(username, registeredUsers)) {
                JOptionPane.showMessageDialog(this, "Username already taken", "Sign Up Failed",
                        JOptionPane.INFORMATION_MESSAGE);

            } else if (containsComma(username, password)) {
                JOptionPane.showMessageDialog(this, "Username or Password should not contain any commas.",
                        "Sign Up Failed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                User newUser = new User(username, password);
                app.setCurrentUser(newUser);
                registeredUsers.add(newUser);

                // Show a pop-up dialog indicating sign-up success
                JOptionPane.showMessageDialog(this, "Sign Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields
                usernameField.setText("");
                passwordField.setText("");

                // Switch to the next screen or "Game" screen
                cardLayout.show(parentContainer, "Game");

                GamePanel gamePanel = (GamePanel) parentContainer.getComponent(3);
                gamePanel.updateUserLabel(app.currentUser);
                gamePanel.updateBestScoreLabel(app.currentUser);
                gamePanel.updateErrorLabel();
                gamePanel.showCards();
                gamePanel.startHideCardtimer();
            }
        });

        // Back button action
        backButton.addActionListener(e -> cardLayout.show(parentContainer, "MainMenu"));
    }

    private boolean isUsernameTaken(String username, ArrayList<User> registeredUsers) {
        for (User user : registeredUsers) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsComma(String username, String password) {
        if (username.contains(",") || password.contains(","))
            return true;

        return false;
    }
}
