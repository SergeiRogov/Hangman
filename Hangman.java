/**
 * @file Hangman.java
 * @brief This file contains the Hangman class.
 * @author Sergei Rogov U231N0051
 * @date 15.01.2024
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @class Hangman
 * @brief Represents the main page of the Hangman game application.
 */ 
public class Hangman implements ActionListener, KeyListener {
	
	// Array with questions
	final private static ArrayList<String> SECRET_WORDS = new ArrayList<String>();
	// Array with answers
	final private static ArrayList<String> LETTERS = new ArrayList<String>();
	
	private Set<Character> guessedLetters = new HashSet<>();

	final private static int TOTAL_NUMBER_OF_GUESSES = 6;
	
	private static String secret_word;
	
	private static int guesses_left = 6;
	
	private static boolean userWon = false;
	private static boolean userLost = false;
	
	JButton newGameButton;
	JPanel drawingPanel;
	JFrame frame;

	/**
	 * @method HedgeYourBet
     * @brief Constructs a new HedgeYourBet game.
     * 		  Instantiates all system-related objects and GUI.
     */
	public Hangman(){
		initializeSecretWords();
		initializeGUI();
	}
	
	/**
	 * @method initializeQuestionsAndAnswers
     * @brief Initializes all questions and answers.
     */
	private void initializeSecretWords() {
		
		SECRET_WORDS.add("accretion");
		SECRET_WORDS.add("boisterous");
		SECRET_WORDS.add("dullard");
		SECRET_WORDS.add("feigned");
		SECRET_WORDS.add("haughty");
		SECRET_WORDS.add("insipid");
		SECRET_WORDS.add("noisome");
		SECRET_WORDS.add("obdurate");
		SECRET_WORDS.add("parsimonious");
		SECRET_WORDS.add("sycophant");
		
		secret_word = SECRET_WORDS.get(getRandomIndex(SECRET_WORDS.size()));
	
	}
	
	/**
	 * @method initializeGUI
     * @brief Initializes all necessary GUI elements. GUI setup logic.
     */
	private void initializeGUI() {
		
		// Main frame
	    frame = new JFrame("Hangman");
	    
	    frame.setVisible(true);
	    frame.requestFocusInWindow();
		frame.addKeyListener(this); // Register the Hangman instance as a KeyListener
	    frame.requestFocus(); // Set focus to the frame to capture keyboard input

	    // Submit button
		newGameButton = new JButton("New");

		// JPanel for the entire content
        JPanel contentPanel = new JPanel(new BorderLayout());
        frame.setContentPane(contentPanel);

        // JPanel for drawing (main panel)
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(450, 320));

        // JPanel for holding the "New Game" button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(450, 30));
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(newGameButton, BorderLayout.CENTER);

        // Add the drawingPanel and buttonPanel to the contentPanel
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.add(drawingPanel, BorderLayout.CENTER);
        
        // Add action listener to the submit button
        newGameButton.addActionListener(this);
		
        // Application will exit after user clicks close button
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.setSize(450, 350); // Dimensions of a frame
 		frame.setVisible(true);
	
    }
	
	/**
     * @class DrawingPanel
     * @brief Custom JPanel for drawing.
     */
    private class DrawingPanel extends JPanel {
        // The serialVersionUID provides version control for serialized objects
        private static final long serialVersionUID = 1L;

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
        	ImageIcon hangmanImageIcon = new ImageIcon("images_hangman/hangman" + guesses_left + ".gif");
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
    
    private String getCurrentWord() {
        String currentWord = "";
  
        for (int i = 0; i < secret_word.length(); i++) {
            char currentChar = secret_word.charAt(i);
        
            if (isLetterGuessed(currentChar)) {
            	currentWord += currentChar;
            } else {
            	currentWord += "?";
            }
        }
        
        return currentWord;
    }
    
    private String getCurrentGameStatus() {
    	String status;
    	
    	if (userWon) {
    		status = "You won with " + guesses_left + " guesses left!";
    	} else if (userLost) {
    		status = "You lost! (" + secret_word + ")";
    	} else {
    		status = guesses_left + " guesses left";
    	}

        return status;
    }
    
    private boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(letter);
    }
    
    /**
     * @method getRandomIndex
     * @brief computes random number between 0 (inclusive) and the length of the array (exclusive).
     * @param arrayLength Length of array.
     * @return Number from 0 to arrayLength.
     */
    private static int getRandomIndex(int arrayLength) {
        Random random = new Random();
        return random.nextInt(arrayLength);
    }
    
    private boolean isWordGuessed() {
    	
    	for (int i = 0; i < secret_word.length(); i++) {
            char currentChar = secret_word.charAt(i);
            if (!isLetterGuessed(currentChar)) {
            	return false;
            }
        }
    	return true;
    }
    
    
    private void startNewGame(){
    	guessedLetters.clear();
    	guesses_left = TOTAL_NUMBER_OF_GUESSES;
    	userLost = false;
    	userWon = false;
    	secret_word = SECRET_WORDS.get(getRandomIndex(SECRET_WORDS.size()));
    	drawingPanel.repaint();
    	frame.requestFocus();
    }
    
    
    /**
     * @method actionPerformed
     * @brief Handles action events.
     * @param e The ActionEvent representing the button click or action.
     */
	@Override
	public void actionPerformed(ActionEvent e) {

		// if the New Game button is clicked
		if(e.getSource()==newGameButton) {
			startNewGame();
			
		}
		
	}
	
	/**
     * @method main
     * @brief Kick-starts the application.
     */
	public static void main(String[] args) {
	
		new Hangman();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Get the typed character
        char typedChar = e.getKeyChar();

        // Check if the typed character is a lower case letter
        if (Character.isLowerCase(typedChar)) {
            // Process the guess (you need to implement this method)
            processGuess(typedChar);

            // Repaint the drawing panel
            drawingPanel.repaint();
        }
		
	}
	
	// Implement the processGuess method based on your game logic
    private void processGuess(char letter) {
    	
    	boolean gameOver = userLost || userWon;
    	
    	if (!isLetterGuessed(letter) && !gameOver) {
    		guessedLetters.add(letter);
    		
    		if (!secret_word.contains(String.valueOf(letter)) ) {
            	guesses_left--;
            } 
    	}
    	
    	if (isWordGuessed()) {
    		userWon = true; // game is over
    	}
    	
        if (guesses_left == 0) {
        	userLost = true; // game is over
        }
         
    }

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
