import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class LeaderboardsPanel extends JPanel {

    JLabel titleLabel = new JLabel();
    JLabel firstRankLabel = new JLabel();
    JLabel secondRankLabel = new JLabel();
    JLabel thirdRankLabel = new JLabel();
    JLabel currentUserRankLabel = new JLabel();

    JPanel leaderboardPanel = new JPanel();

    public LeaderboardsPanel(App app, CardLayout cardLayout, Container parentContainer,
            ArrayList<User> registeredUsers) {
        setLayout(new BorderLayout());

        // Title
        titleLabel.setText("LEADERBOARDS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        updateLeaderboards(app.currentUser, registeredUsers);

        leaderboardPanel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows (1st, 2nd, 3rd, current user)
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        firstRankLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        secondRankLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        thirdRankLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        currentUserRankLabel.setFont(new Font("Arial", Font.PLAIN, 30));

        leaderboardPanel.add(firstRankLabel);
        leaderboardPanel.add(secondRankLabel);
        leaderboardPanel.add(thirdRankLabel);
        leaderboardPanel.add(currentUserRankLabel);

        add(leaderboardPanel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(e -> cardLayout.show(parentContainer, "Game"));
        add(backButton, BorderLayout.SOUTH);
    }

    public void updateLeaderboards(User currentUser, ArrayList<User> registeredUsers) {
        registeredUsers.sort(Comparator.comparingInt(User::getBestScore));

        if (!registeredUsers.isEmpty()) {
            if (registeredUsers.get(0).getBestScore() >= 100) {
                firstRankLabel.setText("1st: N/A");
            } else {
                firstRankLabel.setText("1st: " + registeredUsers.get(0).getName() + " | "
                        + registeredUsers.get(0).getBestScore() + " tries");
            }

        } else {
            firstRankLabel.setText("1st: N/A");
        }

        if (registeredUsers.size() > 1) {
            if (registeredUsers.get(1).getBestScore() >= 100) {
                secondRankLabel.setText("2nd: N/A");
            } else {
                secondRankLabel.setText("2nd: " + registeredUsers.get(1).getName() + " | "
                        + registeredUsers.get(1).getBestScore() + " tries");
            }

        } else {
            secondRankLabel.setText("2nd: N/A");
        }

        if (registeredUsers.size() > 2) {
            if (registeredUsers.get(2).getBestScore() >= 100) {
                thirdRankLabel.setText("3rd: N/A");
            } else {
                thirdRankLabel.setText("3rd: " + registeredUsers.get(2).getName() + " | "
                        + registeredUsers.get(2).getBestScore() + " tries");
            }
        } else {
            thirdRankLabel.setText("3rd: N/A");
        }

        // Find and display the current user's rank
        int currentUserRank = -1;
        for (int i = 0; i < registeredUsers.size(); i++) {
            if (registeredUsers.get(i).equals(currentUser)) {
                currentUserRank = i + 1;
                break;
            }
        }

        if (currentUserRank != -1) {
            if (currentUser.getBestScore() == 100) {
                currentUserRankLabel.setText("Your Rank: N/A");
            } else {
                currentUserRankLabel
                        .setText("Your Rank: #" + currentUserRank + " | " + currentUser.getBestScore() + " tries");
            }

        } else {
            currentUserRankLabel.setText("Your Rank: N/A");
        }
    }
}
