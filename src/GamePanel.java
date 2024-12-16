import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardList = {
            "ai",
            "cry",
            "like",
            "please",
            "rizz",
            "sad",
            "shades",
            "shrek",
            "wolf",
            "wonder"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JPanel textPanel = new JPanel(); // To hold user and error labels
    JLabel userLabel = new JLabel();
    JLabel errorLabel = new JLabel();
    JLabel bestScoreLabel = new JLabel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel(); // To hold restart and log-out buttons
    JButton restartButton = new JButton();
    JButton logOutButton = new JButton();
    JButton leaderboardsButton = new JButton();

    int errorCount = 0;
    int correctCount = 0;
    int tryCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    public GamePanel(App app, CardLayout cardLayout, Container parentContainer, ArrayList<User> registeredUsers) {
        setUpCards();
        shuffleCards();

        setLayout(new BorderLayout());

        // Set up the top panel (user label and error label)
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Arrange components vertically
        updateUserLabel(app.currentUser);
        textPanel.add(userLabel);

        updateBestScoreLabel(app.currentUser);
        bestScoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        bestScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(bestScoreLabel);

        errorLabel.setText("Tries: " + Integer.toString(tryCount) + " | Errors: " + Integer.toString(errorCount)
                + " | Correct: " + Integer.toString(correctCount));
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(errorLabel);

        add(textPanel, BorderLayout.NORTH);

        // Set up the game board (center panel)
        boardPanel.setLayout(new GridLayout(rows, columns));
        board = new ArrayList<JButton>();

        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        } else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            tryCount += 1;

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount += 1;
                                hideCardTimer.start();
                            } else {
                                correctCount += 1;
                                if (correctCount >= 10) {
                                    JOptionPane.showMessageDialog(GamePanel.this, "Congratulations! You won!.",
                                            "GAME WON!", JOptionPane.INFORMATION_MESSAGE);

                                    if (tryCount < app.currentUser.getBestScore()) {
                                        for (int i = 0; i < registeredUsers.size(); i++) {
                                            if (app.currentUser.getName().equals(registeredUsers.get(i).getName())) {
                                                registeredUsers.get(i).setBestScore(tryCount);
                                                app.setCurrentUser(registeredUsers.get(i));
                                            }
                                        }
                                        updateBestScoreLabel(app.currentUser);
                                    }

                                    restartButton.doClick();
                                }
                                card1Selected = null;
                                card2Selected = null;
                            }
                            errorLabel.setText("Tries: " + Integer.toString(tryCount) + " | Errors: "
                                    + Integer.toString(errorCount) + " | Correct: " + Integer.toString(correctCount));

                            if (tryCount >= 100) {
                                JOptionPane.showMessageDialog(GamePanel.this, "Maximum tries exceeded! Try again!",
                                        "YOU LOST!", JOptionPane.INFORMATION_MESSAGE);
                                restartButton.doClick();
                            }
                        }
                    }
                }
            });

            board.add(tile);
            boardPanel.add(tile);
        }
        add(boardPanel, BorderLayout.CENTER);

        // Set up the bottom panel (restart and log-out buttons)
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Arrange buttons vertically
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.setText("Restart Game");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                logOutButton.setEnabled(false);
                leaderboardsButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                shuffleCards();

                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                errorCount = 0;
                tryCount = 0;
                correctCount = 0;
                updateBestScoreLabel(app.currentUser);
                updateErrorLabel();
                hideCardTimer.start();
            }
        });
        buttonPanel.add(restartButton);

        logOutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logOutButton.setPreferredSize(new Dimension(boardWidth, 30));
        logOutButton.setFocusable(false);
        logOutButton.setEnabled(false);
        logOutButton.setText("Log Out");
        logOutButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        logOutButton.addActionListener(e -> {
            if (!gameReady) {
                return;
            }
            app.currentUser = null;
            cardLayout.show(parentContainer, "MainMenu");
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
        buttonPanel.add(logOutButton);

        leaderboardsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardsButton.setPreferredSize(new Dimension(boardWidth, 30));
        leaderboardsButton.setFocusable(false);
        leaderboardsButton.setEnabled(false);
        leaderboardsButton.setText("Check Leaderboards");
        leaderboardsButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        leaderboardsButton.addActionListener(e -> {
            if (!gameReady) {
                return;
            }
            LeaderboardsPanel leaderboardsPanel = (LeaderboardsPanel) parentContainer.getComponent(4);
            leaderboardsPanel.updateLeaderboards(app.currentUser, registeredUsers);
            cardLayout.show(parentContainer, "Leaderboards");
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
        buttonPanel.add(leaderboardsButton);

        add(buttonPanel, BorderLayout.SOUTH);

        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);

        showCards();
        startHideCardtimer();
    }

    void setUpCards() {
        cardSet = new ArrayList<>();
        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("./cardPics/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(
                    cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }

        cardSet.addAll(cardSet);

        Image cardBackImg = new ImageIcon(getClass().getResource("./cardPics/chillBackCard.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(
                cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());

            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        } else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
            logOutButton.setEnabled(true);
            leaderboardsButton.setEnabled(true);
            correctCount = 0;
            tryCount = 0;
            errorCount = 0;
        }
    }

    void showCards() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardSet.get(i).cardImageIcon);
        }
    }

    void startHideCardtimer() {
        hideCardTimer.start();
    }

    public void updateUserLabel(User currentUser) {
        if (currentUser == null) {
            userLabel.setText("Please log in or sign up.");
        } else {
            userLabel.setText("Welcome To Match Cards Game, " + currentUser.getName() + "!");
        }
        userLabel.setFont(new Font("Arial", Font.BOLD, 24));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
    }

    public void updateErrorLabel() {
        errorCount = 0;
        errorLabel.setText("Tries: " + Integer.toString(tryCount) + " | Errors: " + Integer.toString(errorCount)
                + " | Correct: " + Integer.toString(correctCount));
    }

    public void updateBestScoreLabel(User currentUser) {
        if (currentUser == null) {
            bestScoreLabel.setText("");
        } else {
            if (currentUser.getBestScore() >= 100)
                bestScoreLabel.setText("");
            else
                bestScoreLabel.setText("Best Score: " + Integer.toString(currentUser.getBestScore()) + " tries");
        }
        bestScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bestScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
    }

}
