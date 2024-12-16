import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class App {
    public User currentUser = null;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Meme Match Cards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new CardLayout());

        ArrayList<User> registeredUsers = new ArrayList<>();
        App app = new App(); // create App object to set currentUser
        app.retrieveRegisteredUsers(registeredUsers);

        // All Panels
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        MainMenuPanel mainMenuPanel = new MainMenuPanel(cardLayout, frame.getContentPane());
        LoginPanel loginPanel = new LoginPanel(app, cardLayout, frame.getContentPane(), registeredUsers);
        SignUpPanel signUpPanel = new SignUpPanel(app, cardLayout, frame.getContentPane(), registeredUsers);
        GamePanel gamePanel = new GamePanel(app, cardLayout, frame.getContentPane(), registeredUsers);
        LeaderboardsPanel leaderboardsPanel = new LeaderboardsPanel(app, cardLayout, frame.getContentPane(),
                registeredUsers);

        // Add panels to the CardLayout
        frame.add(mainMenuPanel, "MainMenu");
        frame.add(loginPanel, "LogIn");
        frame.add(signUpPanel, "SignUp");
        frame.add(gamePanel, "Game");
        frame.add(leaderboardsPanel, "Leaderboards");

        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                app.saveRegisteredUsers(registeredUsers);
            }
        });
    }

    public void saveRegisteredUsers(ArrayList<User> registeredUsers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("registeredUsers.csv"))) {
            // Write header (optional)
            writer.write("name,password,bestScore");
            writer.newLine();

            // Write user data
            for (User user : registeredUsers) {
                String line = user.getName() + "," + user.getPassword() + "," + user.getBestScore();
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error saving registered users: " + e.getMessage());
        }
    }

    public void retrieveRegisteredUsers(ArrayList<User> registeredUsers) {
        registeredUsers.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("registeredUsers.csv"))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header line if present
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Split line by commas
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String password = parts[1];
                    int bestScore = Integer.parseInt(parts[2]);

                    // Add user to the list
                    User user = new User(name, password);
                    user.setBestScore(bestScore); // Set the best score
                    registeredUsers.add(user);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("No registered users file found. Starting with an empty list.");
        } catch (IOException e) {
            System.err.println("Error loading registered users: " + e.getMessage());
        }
    }
}
