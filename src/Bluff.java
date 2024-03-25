import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Flow;

public class Bluff extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel startPanel;
    private JPanel game2PPanel;
    private JPanel game3PPanel;
    int cardIconHeight = 70;
    int cardIconWidth = 53;

    public Bluff() {
        initializeComponents();
        setupCardLayout();
        setupStartPanel();
        setupGame2PPanel();
        setupGame3PPanel();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Bluff");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);
    }

    private void setupCardLayout() {
        cardPanel.setLayout(cardLayout);
    }

    private void customiseButton(JButton button) {
        // ==== Customise buttons ====
        // FONT
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        // MARGIN
        Insets insets = new Insets(10, 20, 10, 20);
        button.setMargin(insets);

    }

    private void setupStartPanel() {
        // startPanel = new JPanel();
        startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image bg = new ImageIcon(getClass().getResource("./img/BLUFF.png")).getImage();
                    g.drawImage(bg, 0, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // Add vertical glue to push buttons to the center
        startPanel.add(Box.createVerticalGlue());

        // play with 2P button
        JButton play2PButton = new JButton("Play with 2P");
        customiseButton(play2PButton);
        play2PButton.addActionListener(e -> cardLayout.show(cardPanel, "2P"));
        play2PButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        startPanel.add(play2PButton);

        // vertical margin between buttons
        startPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Adjust as needed

        // Add play with 3P button
        JButton play3PButton = new JButton("Play with 3P");
        customiseButton(play3PButton);
        play3PButton.addActionListener(e -> cardLayout.show(cardPanel, "3P"));
        play3PButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        startPanel.add(play3PButton);

        // Add vertical glue to push buttons to the center
        startPanel.add(Box.createVerticalGlue());

        // Add the start panel to the card panel
        cardPanel.add(startPanel, "start");
    }

    private void customiseGamePanels(JPanel gamePanel) {
        gamePanel.setBackground(new Color(46, 163, 108));
    }

    public class Card {
        private String suite;
        private String value;
        private boolean selected;

        Card(String suite, String value) {
            this.suite = suite;
            this.value = value;
            this.selected = false;
        }

        public void selectOrDeselect() {
            this.selected = !this.selected;
        }

        public boolean getSelected() {
            return this.selected;
        }

        @Override
        public String toString() {
            return suite + "" + value;
        }

        public String getImgPath() {
            return "./cards/" + toString() + ".png";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Card other = (Card) obj;
            return suite.equals(other.suite) && value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(suite, value);
        }
    }

    // TEMP CODE, will replace
    ArrayList<Card> p1 = new ArrayList<>();
    ArrayList<Card> p2 = new ArrayList<>();

    private ArrayList<Card> getCurrentPlayerCards(String player) {
        // ArrayList<Card> p1 = new ArrayList<>();
        p1.add(new Card("C", "A"));
        p1.add(new Card("S", "2"));
        p1.add(new Card("D", "3"));
        p1.add(new Card("H", "4"));
        p1.add(new Card("C", "5"));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("C", "Q" ));
        // p1.add(new Card("D", "Q" ));
        // p1.add(new Card("C", "Q" ));
        // p1.add(new Card("S", "2" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("C", "K" ));
        // p1.add(new Card("D", "K" ));

        // ArrayList<Card> p2 = new ArrayList<>();
        p2.add(new Card("S", "2"));
        p2.add(new Card("C", "Q"));
        p2.add(new Card("D", "Q"));
        p2.add(new Card("C", "Q"));
        p2.add(new Card("S", "2"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("C", "K"));
        p2.add(new Card("D", "K"));

        return player.equals("P2") ? p2 : p1;
    }

    ArrayList<Card> selectedCards = new ArrayList<>();
    // Define a Map to store the association between JButton and Card
    Map<JButton, Card> cardMap = new HashMap<>();

    private void setupGame2PPanel() {
        game2PPanel = new JPanel(new BorderLayout());
        customiseGamePanels(game2PPanel);

        // Add deck in the middle
        ImageIcon deckIcon = new ImageIcon("./cards/deck.png");
        JLabel deckLabel = new JLabel(deckIcon);
        game2PPanel.add(deckLabel, BorderLayout.CENTER);

        // ===================================2 PLAYERS=======================================================
        // Player 1 = Current player. to be replaced
        // Create a panel to hold P1's cards and label
        JPanel currentPlayerPanel = new JPanel(new FlowLayout());
        // currentPlayerPanel.setBackground(new Color(68, 72, 82));
        // Add label for P1's cards
        JLabel currentPlayerLabel = new JLabel("P1's Cards", SwingConstants.CENTER);
        currentPlayerPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        // Create a JButton for the "Bluff!" button
        JButton bluffButton = new JButton("Call Bluff!");
        bluffButton.addActionListener(e -> {

        });
        JButton playHandButton = new JButton("Play Selected Cards!");
        currentPlayerPanel.add(playHandButton, BorderLayout.NORTH); // Add the button to the left side
        currentPlayerPanel.add(bluffButton, BorderLayout.NORTH); // Add the button to the left side

        // Create a JPanel for the player's cards
        JPanel playerCardsPanel = new JPanel(new GridLayout(0, 18, 0, 3)); // 5 columns with gaps
        ArrayList<Card> currentPlayerCards = getCurrentPlayerCards("P1"); // Retrieve player's cards

        // reuseable card listener=====
        ActionListener cardListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource(); // Get the clicked button
                // Retrieve the Card object associated with the clicked button
                Card card = cardMap.get(clickedButton);
                if (card.getSelected()) {
                    selectedCards.remove(card);
                    clickedButton.setBorder(null);
                } else {
                    // Add the clicked card to the selectedCards array
                    selectedCards.add(card);
                    clickedButton.setBorder(BorderFactory.createLineBorder(Color.RED)); // For example, change border color to red
                }
                card.selectOrDeselect();
            }
        };
        for (Card card : currentPlayerCards) {
            String imagePath = card.getImgPath();
            ImageIcon icon = new ImageIcon(imagePath);
            JButton cardLabel = new JButton(icon);

            // select or deselect cards to play
            cardLabel.addActionListener(cardListener);
            // Set the size of the label to match the size of the ImageIcon
            cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
            playerCardsPanel.add(cardLabel);
            // Associate the JButton with the Card in the map
            cardMap.put(cardLabel, card);
        }
        currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        playHandButton.addActionListener(e -> {
            // Remove selected cards from the player's hand
            for (Card card : selectedCards) {
                p1.remove(card);
                currentPlayerCards.remove(card);
            }

            // Remove duplicates from the currentPlayerCards list
            Set<Card> uniqueCards = new HashSet<>(currentPlayerCards);
            currentPlayerCards.clear();
            currentPlayerCards.addAll(uniqueCards);

            // Clear the selected cards list
            selectedCards.clear();

            // Print currentPlayerCards after removing selected cards
            System.out.println("After removing selected cards:");
            for (Card card : currentPlayerCards) {
                System.out.println(card);
            }

            // Update the currentPlayerPanel with the updated player's hand
            currentPlayerPanel.remove(playerCardsPanel); // Remove the old playerCardsPanel
            playerCardsPanel.removeAll(); // Clear the old cards from the playerCardsPanel

            // Add the updated cards to the playerCardsPanel
            for (Card card : currentPlayerCards) {
                // Create the JButton with the updated card image
                String imagePath = card.getImgPath();
                ImageIcon icon = new ImageIcon(imagePath);
                JButton cardLabel = new JButton(icon);

                // Add action listener to the card button
                cardLabel.addActionListener(cardListener); // Assuming cardListener is defined elsewhere

                // Set the size of the label to match the size of the ImageIcon
                cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));

                // Add the card button to the playerCardsPanel
                playerCardsPanel.add(cardLabel);
            }

            // Add the updated playerCardsPanel back to the currentPlayerPanel
            currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

            // Repaint the currentPlayerPanel to reflect the changes
            currentPlayerPanel.revalidate();
            currentPlayerPanel.repaint();
        });

        // Add P1's panel to game2PPanel
        game2PPanel.add(currentPlayerPanel, BorderLayout.SOUTH);

        // Create a panel to hold P2's cards and label
        JPanel player2Panel = new JPanel(new BorderLayout());

        // Add label for P2's cards
        JLabel player2Label = new JLabel("P2's Cards", SwingConstants.CENTER);
        player2Panel.add(player2Label, BorderLayout.NORTH);

        // Create a JPanel for the opponent's cards
        JPanel opponentCardPanel = new JPanel(new GridLayout(0, 18, 0, 3)); // 5 columns with gaps
        ArrayList<Card> player2Cards = getCurrentPlayerCards("P2");
        for (int i = 0; i < player2Cards.size(); i++) {
            String imagePath = "./cards/blank.png";
            ImageIcon icon = new ImageIcon(imagePath);
            // Create a JLabel with the icon
            JLabel cardLabel = new JLabel(icon);
            // Set the size of the label to match the size of the ImageIcon
            cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
            opponentCardPanel.add(cardLabel);
        }
        player2Panel.add(opponentCardPanel, BorderLayout.CENTER);

        // Add P2's panel to game2PPanel
        game2PPanel.add(player2Panel, BorderLayout.NORTH);

        cardPanel.add(game2PPanel, "2P");
    }

    private void setupGame3PPanel() {
        game3PPanel = new JPanel();
        customiseGamePanels(game3PPanel);

        // Setup components for "Play with 3P" game mode
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "start"));
        game3PPanel.add(backButton, BorderLayout.NORTH);
        cardPanel.add(game3PPanel, "3P");
    }
}