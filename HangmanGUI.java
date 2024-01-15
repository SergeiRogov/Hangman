package hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HangmanGUI implements ActionListener, KeyListener {
    private final JFrame frame;
    private final JButton newGameButton;
    private final DrawingPanel drawingPanel;
    private final HangmanModel model;

    public HangmanGUI() {
        frame = new JFrame("Hangman");
        newGameButton = new JButton("New");
        model = new HangmanModel();
        drawingPanel = new DrawingPanel(model);

        initializeGUI();
    }

    private void initializeGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);

        frame.setVisible(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        frame.setContentPane(contentPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(450, 30));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(newGameButton, BorderLayout.CENTER);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.add(drawingPanel, BorderLayout.CENTER);

        newGameButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            model.startNewGame();
            drawingPanel.repaint();
            frame.requestFocus();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char typedChar = e.getKeyChar();

        if (Character.isLowerCase(typedChar)) {
            model.processGuess(typedChar);
            drawingPanel.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private class DrawingPanel extends JPanel {
    	
        private static final long serialVersionUID = 1L;
        private final HangmanModel model;

        public DrawingPanel(HangmanModel model) {
            this.model = model;
        }
        
        public String getCurrentWord() {
            return model.getCurrentWord();
        }

        public String getCurrentGameStatus() {
            return model.getCurrentGameStatus();
        }
        
        public boolean isLetterGuessed(char letter) {
            return model.isLetterGuessed(letter);
        }
        

        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
            
            setBackground(Color.WHITE);
         
            displayHangmanImage(g);       
            displayCurrentWord(g, getCurrentWord());         
            displayGameStatus(g);      
            displayAlphabet(g);
        }
        
        private void displayHangmanImage(Graphics g) {
        	
        	// Load and draw the .gif image
        	ImageIcon hangmanImageIcon = new ImageIcon("images_hangman/hangman" + model.getGuessesLeft() + ".gif");
            Image hangmanImage = hangmanImageIcon.getImage();
            
            // Calculate the position to center horizontally
            int x = (getWidth() - hangmanImageIcon.getIconWidth()) / 2;
            int y = 10; // 10 pixels from the top
            
            g.drawImage(hangmanImage, x, y, this);
        }
        
        private void displayCurrentWord(Graphics g, String currentWord) {
            // Set font and color for the current word display
            Font font = new Font(null, Font.PLAIN, 16);
            g.setFont(font);
            g.setColor(Color.BLACK);

            // Get FontMetrics to calculate string width
            FontMetrics fontMetrics = g.getFontMetrics();

            // Calculate position to center horizontally
            int x = (getWidth() - fontMetrics.stringWidth(currentWord)) / 2;
            int y = getHeight() - 60; // 60 pixels from the bottom

            // Draw the current word
            g.drawString(currentWord, x, y);
        }
        
        private void displayGameStatus(Graphics g) {
            // Set font and color for the game status display
            Font font = new Font(null, Font.PLAIN, 16);
            g.setFont(font);
            g.setColor(Color.BLACK);

            // Get FontMetrics to calculate string width
            FontMetrics fontMetrics = g.getFontMetrics();

            // Calculate position to center horizontally
            int x = (getWidth() - fontMetrics.stringWidth(getCurrentGameStatus())) / 2;
            int y = getHeight() - 40; // 40 pixels from the bottom

            // Draw the current game status
            g.drawString(getCurrentGameStatus(), x, y);
        }
        
        private void displayAlphabet(Graphics g) {
            // Set font for the alphabet display
            Font font = new Font(null, Font.PLAIN, 16);
            g.setFont(font);

            int letterWidth = 14;
            
            // Calculate overall width of the alphabet string
            int overallWidth = 26 * letterWidth;

            // Calculate position to center horizontally
            int x = (getWidth() - overallWidth) / 2;

            // Get FontMetrics to calculate string width
            FontMetrics fontMetrics = g.getFontMetrics();

            // Iterate through the alphabet and draw each letter
            for (char letter = 'a'; letter <= 'z'; letter++) {
                // Check if the letter has been guessed or not
                boolean guessed = isLetterGuessed(letter); // Replace this with your logic to check if the letter is guessed

                // Set color based on whether the letter has been guessed or not
                if (guessed) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }

                // Draw the letter
                String letterString = String.valueOf(letter);
                g.drawString(letterString, x, getHeight() - 20);

                // Move the position for the next letter
                x += letterWidth;
            }
        }
    }
}
