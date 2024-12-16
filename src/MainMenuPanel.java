import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(CardLayout cardLayout, Container parentContainer) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title Panel
        JLabel titleLabel = new JLabel("MATCH CARDS GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(200, 0, 50, 0));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        loginButton.setFont(new Font("Arial", Font.PLAIN, 25));
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 25));

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.setFocusable(false);
        signUpButton.setFocusable(false);

        // Add buttons with spacing
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space between buttons
        buttonPanel.add(signUpButton);

        // Add title and buttons to the main panel
        add(titleLabel);
        add(buttonPanel);

        // Add action listeners
        loginButton.addActionListener(e -> cardLayout.show(parentContainer, "LogIn"));
        signUpButton.addActionListener(e -> cardLayout.show(parentContainer, "SignUp"));
    }
}
