/**
 * @file HangmanGUI.java
 * @brief This file contains the HangmanGUI class.
 * @author Sergei Rogov U231N0051
 * @date 16.01.2024
 */
package hangman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.FontMetrics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @class HangmanGUI
 * @brief The GUI of the Hangman class.
 */
public class HangmanGUI implements ActionListener, KeyListener {
	
    private final JFrame frame; // GUI component
    private final JButton newGameButton; // GUI component
    private final DrawingPanel drawingPanel; // Panel for display game info
    private final HangmanModel model; // Instance of game model

    /**
	 * @method HangmanGUI
     * @brief Constructor of HangmanGUI class.
     */
    public HangmanGUI() {
        frame = new JFrame("Hangman");
        newGameButton = new JButton("New");
        model = new HangmanModel();
        drawingPanel = new DrawingPanel(model);

        initializeGUI();
    }

    /**
	 * @method initializeGUI
     * @brief Initializes Graphical User Interface components.
     */
    private void initializeGUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setVisible(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(this);
        frame.requestFocus();
        
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

    /**
     * @method actionPerformed
     * @brief Handles action events (button clicks).
     * @param e The ActionEvent representing the button click or action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            model.startNewGame();
            drawingPanel.repaint();
            frame.requestFocus();
        }
    }

    /**
     * @method keyTyped
     * @brief Handles key-typed events.
     * @details This method is called when a key is typed.
     * @param e The KeyEvent representing the key-typed event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char typedChar = e.getKeyChar();
        // only if lower case letter is typed
        if (Character.isLowerCase(typedChar)) {
            model.processGuess(typedChar); // process pressed letter
            drawingPanel.repaint(); // update drawing panel
        }
    }

    /**
     * @method keyPressed
     * @brief Handles key-typed events.
     * @details This method is called when a key is pressed.
     * @param e The KeyEvent representing the key-typed event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }
    
    /**
     * @method keyReleased
     * @brief Handles key-typed events.
     * @details This method is called when a key is released.
     * @param e The KeyEvent representing the key-typed event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * @class DrawingPanel
     * @brief Custom JPanel class for rendering the Hangman game visuals.
     * @details It includes methods to display the hangman image, current word, game status, and the alphabet.
     */
    private class DrawingPanel extends JPanel {
    	
    	// The serialVersionUID provides version control for serialized objects
        private static final long serialVersionUID = 1L;
        
        // game model instance
        private final HangmanModel model;

        /**
    	 * @method DrawingPanel
         * @brief Constructor of DrawingPanel class.
         * @param model Hangman game model
         */
        public DrawingPanel(HangmanModel model) {
            this.model = model;
            setPreferredSize(new Dimension(450, 320));
        }
        
        /**
         * @method paintComponent
         * @brief Overrides the paintComponent method to customize the rendering of the panel.
         * @details Clears the panel, sets the background color, and then displays the hangman image,
         * current word, game status, and alphabet using the provided Graphics object.
         * @param g The Graphics object used for painting components.
         */
        @Override
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
            
        	// set white background color
            setBackground(Color.WHITE);
         
            displayHangmanImage(g); // hangman image    
            displayCurrentWord(g, model.getCurrentWord()); // secret word 
            displayGameStatus(g); // status of a game
            displayAlphabet(g); // alphabet
        }
        
        /**
         * @method displayHangmanImage
         * @brief Puts hangman image on a panel.
         * @param g The Graphics object used for painting components.
         */
        private void displayHangmanImage(Graphics g) {
        	
        	// Load the .gif image
        	ImageIcon hangmanImageIcon = new ImageIcon("images_hangman/hangman" + model.getGuessesLeft() + ".gif");
            Image hangmanImage = hangmanImageIcon.getImage();
            
            // Calculate the position to center horizontally
            int x = (getWidth() - hangmanImageIcon.getIconWidth()) / 2;
            int y = 10; // 10 pixels from the top
            
            g.drawImage(hangmanImage, x, y, this);
        }
        
        /**
         * @method displayCurrentWord
         * @brief Displays current word.
         * @param g The Graphics object used for painting components.
         * @param currentWord Current representation of a secret word (???a??w??).
         */
        private void displayCurrentWord(Graphics g, String currentWord) {
            // Set font and color for the current word display
            Font font = new Font(null, Font.BOLD, 18);
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
        
        /**
         * @method displayGameStatus
         * @brief Shows if you lost, won or how many guesses left.
         * @param g The Graphics object used for painting components.
         */
        private void displayGameStatus(Graphics g) {
            // Set font and color for the game status display
            Font font = new Font(null, Font.BOLD, 18);
            g.setFont(font);
            g.setColor(Color.BLACK);

            // Get FontMetrics to calculate string width
            FontMetrics fontMetrics = g.getFontMetrics();

            // Calculate position to center horizontally
            int x = (getWidth() - fontMetrics.stringWidth(model.getCurrentGameStatus())) / 2;
            int y = getHeight() - 40; // 40 pixels from the bottom

            // Draw the current game status
            g.drawString(model.getCurrentGameStatus(), x, y);
        }
        
        /**
         * @method displayAlphabet
         * @brief Shows 26 English letters in order, grey - not guessed, blue - guessed.
         * @param g The Graphics object used for painting components.
         */
        private void displayAlphabet(Graphics g) {
            // Set font for the alphabet display
            Font font = new Font(null, Font.BOLD, 18);
            g.setFont(font);

            int letterWidth = 14; // assume this letter width for all letters
            
            // Calculate overall width of the alphabet string
            int overallWidth = 26 * letterWidth;

            // Calculate position to center horizontally
            int x = (getWidth() - overallWidth) / 2;

            // Iterate through the alphabet and draw each letter
            for (char letter = 'a'; letter <= 'z'; letter++) {
                // Check if the letter has been guessed or not
                boolean guessed = model.isLetterGuessed(letter); 

                // Set color based on whether the letter has been guessed or not
                if (guessed) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }

                // Draw the letter
                String letterString = String.valueOf(letter);
                g.drawString(letterString, x, getHeight() - 20); // 20 pixels from the bottom

                // Move position 
                x += letterWidth;
            }
        }
    }
}
