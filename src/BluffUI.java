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
    private String valToCheck = "K";
    private Map<JButton, Card> cardMap = new HashMap<>();
    private ArrayList<Card> selectedCards = new ArrayList<>();

    // TEMP CODE REMOVE LATER
    private ArrayList<Card> p1 = new ArrayList<>();
    private ArrayList<Card> p2 = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();
    private int turn = 0;
    private ArrayList<Card> recentCards = new ArrayList<>();


    public BluffUI() {
        initialiseComponents();
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

    // Initialises misc ie Window title, window size.
    private void initialiseComponents() {
        setTitle("Bluff");
        // when click X then close window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 700);
        // Let elements appear in the center
        setLocationRelativeTo(null);
        // resizable window is false
        setResizable(false);
        // To allow for each menu option (2P, 3P, 4P) to appear on the frame
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        // Adds cardpanel to the frame component
        add(cardPanel);
        cardPanel.setLayout(cardLayout);
    }

    private void setupStartPanel() {
        // Setting image background for start menu page
        startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // use BLUFF.png as splash screen
                    Image bg = new ImageIcon(getClass().getResource("./img/BLUFF.png")).getImage();
                    g.drawImage(bg, 0, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // use Box Layout for start panel
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));

        // Add vertical glue to push buttons to the center
        startPanel.add(Box.createVerticalGlue());
        JButton play2PButton = new JButton("Play with 2P");
        // put in my func to style buttons
        customiseButton(play2PButton);
        // When 2p button is pressed, show 2 player page
        play2PButton.addActionListener(e -> cardLayout.show(cardPanel, "2P"));
        // center the 2p button
        play2PButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add the 2p button to the start menu panel
        startPanel.add(play2PButton);

        // creates a spacing between 2p button and 3p button
        startPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // create 3p button
        JButton play3PButton = new JButton("Play with 3P");
        // customise 3p button
        customiseButton(play3PButton);
        // When 3p button pressed, swap to 3p screen
        play3PButton.addActionListener(e -> cardLayout.show(cardPanel, "3P"));
        // centralise 3p button
        play3PButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add it to start panel
        startPanel.add(play3PButton);
        // centralise 3p button
        startPanel.add(Box.createVerticalGlue());
        // finally, add everything on start panel to the card under the name "start"
        cardPanel.add(startPanel, "start");
    }

    // 2P screen 
    private void setupGame2PPanel() {
        // create a new panel for the 2p screen
        game2PPanel = new JPanel(new BorderLayout());
        // customise to have the background colour
        customiseGamePanels(game2PPanel);

        // add image of card deck in middle of screen (might change later on :o)
        ImageIcon deckIcon = new ImageIcon("./cards/deck.png");
        JLabel deckLabel = new JLabel(deckIcon);
        game2PPanel.add(deckLabel, BorderLayout.CENTER);

        // Current player = current player now (their cards displayed at the btm)
        // flow layout is to centralise elements 
        JPanel currentPlayerPanel = new JPanel(new FlowLayout());

        // add a label for P1
        JLabel currentPlayerLabel = new JLabel("P1's Cards", SwingConstants.CENTER);
        currentPlayerPanel.add(currentPlayerLabel, BorderLayout.NORTH);

        // add Bluff button
        JButton bluffButton = new JButton("Call Bluff!");
        bluffButton.addActionListener(bluffListener);

        // add playHand button (TO ADD : restrict click when no cards are selected )
        JButton playHandButton = new JButton("Play Selected Cards!");

        // put playhand button and bluff button next to each other
        currentPlayerPanel.add(playHandButton, BorderLayout.NORTH); 
        currentPlayerPanel.add(bluffButton, BorderLayout.NORTH);

        // Player's cards (shown at bottom of the screen beside playhand and bluff button)
        JPanel playerCardsPanel = new JPanel(new GridLayout(0, 18, 0, 3)); 
        ArrayList<Card> currentPlayerCards = getCurrentPlayerCards(getCurrentPlayer()); 
        
        // repaint means to display each card as a JButton
        for (Card card : currentPlayerCards) {
            repaintCardPanel(card, playerCardsPanel);
        }
        // Add Player's cardsd panel to the currentPlayerPanel
        currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

        // add the CurrentPlayerPanel to the bottom of the 2P panel
        game2PPanel.add(currentPlayerPanel, BorderLayout.SOUTH);

        // Opponent's Panel:
        JPanel opponentPanel = new JPanel(new BorderLayout());
        JLabel opponentLabel = new JLabel("P2's Cards", SwingConstants.CENTER);
        // show opponent at the top of the screen
        opponentPanel.add(opponentLabel, BorderLayout.NORTH);

        // display opponent's cards
        JPanel opponentCardPanel = new JPanel(new GridLayout(0, 18, 0, 3)); 
        // get the num of their cards
        ArrayList<Card> nextOpponentCards = getCurrentPlayerCards("P2");
        
        // display them as face down
        for (int i = 0; i < nextOpponentCards.size(); i++) {
            String imagePath = "./cards/blank.png";
            ImageIcon icon = new ImageIcon(imagePath);
            JLabel cardLabel = new JLabel(icon);
            cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
            opponentCardPanel.add(cardLabel);
        }
        // add opponent's cards to the center of the opponent panel (rectangle at the top)
        opponentPanel.add(opponentCardPanel, BorderLayout.CENTER);

        // display it at the top of the 2p page.
        game2PPanel.add(opponentPanel, BorderLayout.NORTH);

        // Play hand button
        playHandButton.addActionListener(e -> {
            // when clicked, clear the recent cards (cards that were just played)
            recentCards.clear();
            // for every card that the player selects and plays
            for (Card card : selectedCards) {
                // add it to the recently played cards
                recentCards.add(card);
                // remove it from p1's card hand
                p1.remove(card);
                
            }
            // clear the selected cards to reset it
            selectedCards.clear();

            // remove all the cards from current player panel in order to redraw them
            currentPlayerPanel.remove(playerCardsPanel);
            playerCardsPanel.removeAll();
            advanceTurn();

            // redrawing cards into cardspanel
            ArrayList<Card> nextPlayerCards = getCurrentPlayerCards("P1");
            
            for (Card card : nextPlayerCards) {
                repaintCardPanel(card, playerCardsPanel);
            }

            // add back the playcardspanel to the center of it
            currentPlayerPanel.add(playerCardsPanel, BorderLayout.CENTER);

            // repaint current player panel
            currentPlayerPanel.revalidate();
            currentPlayerPanel.repaint();
            
            // Repaint the opponent's cards panel
            opponentCardPanel.removeAll();
            ArrayList<Card> opponentCards = getCurrentPlayerCards("P2");
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

    // IGNORE FOR NOW... i trying on 2p first.
    private void setupGame3PPanel() {
        game3PPanel = new JPanel();
        customiseGamePanels(game3PPanel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "start"));
        game3PPanel.add(backButton, BorderLayout.NORTH);
        cardPanel.add(game3PPanel, "3P");
    }


    private void customiseButton(JButton button) {
        // give button tahoma font and bold and 20 font size
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        // give it padding
        Insets insets = new Insets(10, 20, 10, 20);
        button.setMargin(insets);
    }

    private void customiseGamePanels(JPanel gamePanel) {
        // give game panel green colour bg
        gamePanel.setBackground(new Color(46, 163, 108));
    }

    private void repaintCardPanel(Card card, JPanel playerCardsPanel) {
        // get the image path of the card to display in jbutton
        String imagePath = card.getImgPath();
        ImageIcon icon = new ImageIcon(imagePath);
        JButton cardLabel = new JButton(icon);
        // add the event listener to card (for selection)
        cardLabel.addActionListener(cardListener); 
        // make sure that the jbutton is displayed at 53x70 px
        cardLabel.setPreferredSize(new Dimension(cardIconWidth, cardIconHeight));
        // add to the map to make sure each Jbutton is mapped to a card
        cardMap.put(cardLabel, card);
        // add to player cards panel
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
        // REMOVE LATER, to check if can display opponent's cards
        p2.remove(1);
        turn++; 
    }

    // BLUFF button logic
    private final ActionListener bluffListener = new ActionListener() {
        @Override
        // when button is clicked
        public void actionPerformed(ActionEvent e) {
            // if previous person is NOt bluffing, called wrongly
            if (!checkRecentCards()){
                // Label to be updated, now it's dummy text just to show that it works. TO DO: clear the text
                JLabel wrongJLabel = new JLabel("Wrong!");
                game2PPanel.add(wrongJLabel, BorderLayout.CENTER);
                game2PPanel.revalidate();
                game2PPanel.repaint();
            } 
            // else if previous person WAS bluffing, called correctly
            else {
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
        // when a card is pressed
        public void actionPerformed(ActionEvent e) {
            // find out which JButton was pressed
            JButton clickedButton = (JButton) e.getSource(); 
            // get the card which maps to this JButton
            Card card = cardMap.get(clickedButton);
            // if card is ALREADY selected when user clicks on it (CURRENTLY HAS RED BORDER)
            if (card.getSelected()) {
                // remove it from selection list
                selectedCards.remove(card);
                // remove red border
                clickedButton.setBorder(null);
            }
            // if card is NOT selected when user clicks on it (CURRENTLY HAS NO BORDER)
            else {
                // add card to selected cards list
                selectedCards.add(card);
                // create a red border to indicate this card getting selected
                clickedButton.setBorder(BorderFactory.createLineBorder(Color.RED)); 
            }
            // flips the card's selected boolean [ie selected = false -after click-> selected = true]
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

