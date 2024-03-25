import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BluffUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel startPanel;
    private JPanel game2PPanel;
    private JPanel game3PPanel;
    private int cardIconHeight = 70;
    private int cardIconWidth = 53;
    private ArrayList<Card> p1 = new ArrayList<>();
    private ArrayList<Card> p2 = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();
    private int turn = 0;
    private ArrayList<Card> recentCards = new ArrayList<>();
    private String valToCheck = "K";
    private Map<JButton, Card> cardMap = new HashMap<>();
    private ArrayList<Card> selectedCards = new ArrayList<>();

    public BluffUI() {
        initializeComponents();
        setupStartPanel();
        initialiseGame();
        setVisible(true);
    }

    private void initialiseGame() {
        initialisePlayers();
        initialiseCards();
        initialiseRecentCards();
        setupGame2PPanel();
        setupGame3PPanel();
    }

    // TEMP REMOVELATER
    private void initialisePlayers() {
        players.add("P1");
        players.add("P2");
    }

    // TEMP REMOVELATER
    private void initialiseCards() {
        p1.add(new Card("C", "A"));
        p1.add(new Card("S", "2"));
        p1.add(new Card("D", "3"));
        p1.add(new Card("H", "4"));
        p1.add(new Card("C", "5"));
        
        p2.add(new Card("S", "2"));
        p2.add(new Card("C", "Q"));
        p2.add(new Card("D", "Q"));
        p2.add(new Card("C", "Q"));
        p2.add(new Card("S", "2"));
    }

    // TEMP REMOVELATER 
    private void initialiseRecentCards() {
        recentCards.add(new Card("C", "K"));
        recentCards.add(new Card("C", "Q"));
        recentCards.add(new Card("C", "K"));
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
        cardPanel.setLayout(cardLayout);
    }

    private void setupStartPanel() {
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
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        // Add vertical glue to push buttons to the center
        startPanel.add(Box.createVerticalGlue());
        JButton play2PButton = new JButton("Play with 2P");
        customiseButton(play2PButton);
        play2PButton.addActionListener(e -> cardLayout.show(cardPanel, "2P"));
        play2PButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.add(play2PButton);

        startPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton play3PButton = new JButton("Play with 3P");
        customiseButton(play3PButton);
        play3PButton.addActionListener(e -> cardLayout.show(cardPanel, "3P"));
        play3PButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.add(play3PButton);

        startPanel.add(Box.createVerticalGlue());

        cardPanel.add(startPanel, "start");
    }

    private void setupGame2PPanel() {
        game2PPanel = new JPanel(new BorderLayout());
        customiseGamePanels(game2PPanel);

        ImageIcon deckIcon = new ImageIcon("./cards/deck.png");
        JLabel deckLabel = new JLabel(deckIcon);
        game2PPanel.add(deckLabel, BorderLayout.CENTER);

        JPanel currentPlayerPanel = new JPanel(new FlowLayout());
        JLabel currentPlayerLabel = new JLabel("P1's Cards", SwingConstants.CENTER);
        currentPlayerPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        JButton bluffButton = new JButton("Call Bluff!");
        bluffButton.addActionListener(bluffListener);

        JButton playHandButton = new JButton("Play Selected Cards!");
        currentPlayerPanel.add(playHandButton, BorderLayout.NORTH); 
        currentPlayerPanel.add(bluffButton, BorderLayout.NORTH);

        JPanel playerCardsPanel = new JPanel(new GridLayout(0, 18, 0, 3)); 
        ArrayList<Card> currentPlayerCards = getCurrentPlayerCards(getCurrentPlayer()); 
        
        for (Card card : currentPlayerCards) {
            repaintCardPanel(card, playerCardsPanel);
        }
        currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        game2PPanel.add(currentPlayerPanel, BorderLayout.SOUTH);

        JPanel opponentPanel = new JPanel(new BorderLayout());
        JLabel opponentLabel = new JLabel("P2's Cards", SwingConstants.CENTER);
        opponentPanel.add(opponentLabel, BorderLayout.NORTH);

        JPanel opponentCardPanel = new JPanel(new GridLayout(0, 18, 0, 3)); 
        ArrayList<Card> nextOpponentCards = getCurrentPlayerCards(getNextPlayer());
        
        for (int i = 0; i < nextOpponentCards.size(); i++) {
            String imagePath = "./cards/blank.png";
            ImageIcon icon = new ImageIcon(imagePath);
            JLabel cardLabel = new JLabel(icon);
            cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
            opponentCardPanel.add(cardLabel);
        }
        opponentPanel.add(opponentCardPanel, BorderLayout.CENTER);

        game2PPanel.add(opponentPanel, BorderLayout.NORTH);

        playHandButton.addActionListener(e -> {
            recentCards.clear();
            for (Card card : selectedCards) {
                recentCards.add(card);
                p1.remove(card);
                currentPlayerCards.remove(card);
            }
            selectedCards.clear();

            currentPlayerPanel.remove(playerCardsPanel);
            playerCardsPanel.removeAll();
            advanceTurn();
            ArrayList<Card> nextPlayerCards = getCurrentPlayerCards(getCurrentPlayer());
            
            for (Card card : nextPlayerCards) {
                repaintCardPanel(card, playerCardsPanel);
            }

            currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

            currentPlayerPanel.revalidate();
            currentPlayerPanel.repaint();
            
            // Repaint the opponent's cards panel
            opponentCardPanel.removeAll();
            ArrayList<Card> opponentCards = getCurrentPlayerCards(getNextPlayer());
            for (Card card : opponentCards) {
                String imagePath = "./cards/blank.png";
                ImageIcon icon = new ImageIcon(imagePath);
                JLabel cardLabel = new JLabel(icon);
                cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
                opponentCardPanel.add(cardLabel);
            }
            opponentPanel.revalidate();
            opponentPanel.repaint();
        });
        cardPanel.add(game2PPanel, "2P");

    }

    private void setupGame3PPanel() {
        game3PPanel = new JPanel();
        customiseGamePanels(game3PPanel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "start"));
        game3PPanel.add(backButton, BorderLayout.NORTH);
        cardPanel.add(game3PPanel, "3P");
    }


    private void customiseButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        Insets insets = new Insets(10, 20, 10, 20);
        button.setMargin(insets);
    }

    private void customiseGamePanels(JPanel gamePanel) {
        gamePanel.setBackground(new Color(46, 163, 108));
    }

    private void repaintCardPanel(Card card, JPanel playerCardsPanel) {
        String imagePath = card.getImgPath();
        ImageIcon icon = new ImageIcon(imagePath);
        JButton cardLabel = new JButton(icon);
        cardLabel.addActionListener(cardListener); 
        cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
        cardMap.put(cardLabel, card);
        playerCardsPanel.add(cardLabel);
    }

    // TEMP REMOVELATER 
    private String getCurrentPlayer() {
        return players.get(turn % players.size()); 
    }

    private String getNextPlayer() {
        return players.get((turn+1) % players.size()); 
    }
    

    // TEMP REMOVELATER 
    private void advanceTurn() {
        turn++; 
    }

    // BLUFF button logic
    private final ActionListener bluffListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!checkRecentCards()){
                JLabel wrongJLabel = new JLabel("Wrong!");
                game2PPanel.add(wrongJLabel, BorderLayout.CENTER);
                game2PPanel.revalidate();
                game2PPanel.repaint();
            } else {
                JLabel rightLabel = new JLabel("Right!");
                game2PPanel.add(rightLabel, BorderLayout.CENTER);
                game2PPanel.revalidate();
                game2PPanel.repaint();
            }
            advanceTurn();
        }
    };

    // TEMP REMOVE LATER 
    private boolean checkRecentCards() {
        for (Card card : recentCards){
            if (!card.value.equals(valToCheck)){
                return true;
            }
        }
        return false;
    }

    // TEMP REMOVELATER 
    private ArrayList<Card> getCurrentPlayerCards(String player) {
        if (player.equals("P1")){
            return p1;
        }
        if (player.equals("P2")){
            return p2;
        }
        return null;
    }

    // CURRENT PLAYER'S CARDS
    private final ActionListener cardListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource(); 
            Card card = cardMap.get(clickedButton);
            if (card.getSelected()) {
                selectedCards.remove(card);
                clickedButton.setBorder(null);
            } else {
                selectedCards.add(card);
                clickedButton.setBorder(BorderFactory.createLineBorder(Color.RED)); 
            }
            card.selectOrDeselect();
        }
    };

    // TEMP REMOVELATER 
    public class Card {
        private final String suite;
        private final String value;
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

        public String getValue() {
            return this.value;
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
    
}

