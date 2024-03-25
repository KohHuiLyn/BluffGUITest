import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Bluff extends JFrame{
    private JFrame frame;
    private Container container;
    private JPanel startPanel;
    private JPanel gamePanel;
    private JButton quitButton;

    public Bluff() {
        initializeFrame();
        initializePanels();
        setupStartPanel();
        setupGamePanel();
        switchToPanel("start");
        frame.setVisible(true);
    }

    // MAIN
    private void initializeFrame() {
        frame = new JFrame("Bluff");
        container = frame.getContentPane();
        container.setLayout(new CardLayout());
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    private void initializePanels() {
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
        gamePanel = new JPanel();
    }

    private void setupStartPanel() {
        // Use BoxLayout for vertical alignment
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS)); 
        // Add glue to push buttons to center
        startPanel.add(Box.createVerticalGlue()); 
        addStartButton(startPanel, "Play with 2P", e -> switchToPanel("game"));
        // Add vertical margin between buttons
        startPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Adjust as needed
    
        addStartButton(startPanel, "Play with CPU", e -> switchToPanel("game"));
        startPanel.add(Box.createVerticalGlue()); // Add glue to push buttons to center
        container.add(startPanel, "start");
    }
    ArrayList<JButton> cardButtons = new ArrayList<>();
    private void populateButtonList(){

    }
    private void setupGamePanel() {
        // quitButton = new JButton("Quit Game");

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image bg = new ImageIcon(getClass().getResource("./cards/blank.png")).getImage();
                    g.drawImage(bg, 0, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        gamePanel.setBackground(new Color(46, 163, 108));
        
        // Adds a button called quit game, when button is clicked, switch to start
        addBackButton(gamePanel, "Quit Game", e -> switchToPanel("start"));
        container.add(gamePanel, "game");
    }

    private void switchToPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) container.getLayout();
        cardLayout.show(container, panelName);
    }

    private void addStartButton(Container container, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        //==== Customise buttons ====
        // FONT
        button.setFont(new Font("Tahoma", Font.BOLD, 20));

        // 
        

        // MARGIN
        Insets insets = new Insets(10, 20, 10, 20); 
        button.setMargin(insets);   

        // Center horizontally
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // Add action listener 
        button.addActionListener(actionListener);
        container.add(button);
    }

    private void addBackButton(Container container, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        // Set margin around the button for padding
        Insets insets = new Insets(10, 20, 10, 20); // Adjust as needed
        button.setMargin(insets);   
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        button.addActionListener(actionListener);
        container.add(button);
    }

}
