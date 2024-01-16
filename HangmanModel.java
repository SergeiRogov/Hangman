/**
 * @file HangmanModel.java
 * @brief This file contains the HangmanModel class.
 * @author Sergei Rogov U231N0051
 * @date 16.01.2024
 */
package hangman;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @class HangmanModel
 * @brief The model of Hangman game.
 */
public class HangmanModel {
	
    private static final ArrayList<String> SECRET_WORDS = new ArrayList<>(); // list of words to guess
    private static final Set<Character> guessedLetters = new HashSet<>(); // set to remember already guessed letters
    private static String secret_word; // word to guess
    private static int guesses_left = 6; // how many attempts left
    private static final int TOTAL_NUMBER_OF_GUESSES = 6; // how many attempts allowed in a game
    private static boolean userWon = false; // flag indicating if user has won
    private static boolean userLost = false; // flag indicating if user has lost
    
    /**
	 * @method HangmanModel
     * @brief Constructor of HangmanModel class.
     */
    public HangmanModel() {
        initializeSecretWords();
    }

    /**
	 * @method initializeSecretWords
     * @brief Initializes array of words to guess and randomly picks one of them as a secret word.
     */
    private void initializeSecretWords() {
    	
    	// populate array with words
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
		
		// pick a word
		secret_word = SECRET_WORDS.get(getRandomIndex(SECRET_WORDS.size())); 
    }

    /**
	 * @method startNewGame
     * @brief Resets everything to starting conditions in order to start a new game.
     */
    protected void startNewGame() {
        guessedLetters.clear(); // clear set of guessed words
        guesses_left = TOTAL_NUMBER_OF_GUESSES; // set number of guesses left back to maximum
        userLost = false; // player has not lost yet
        userWon = false;// player has not win yet
        secret_word = SECRET_WORDS.get(getRandomIndex(SECRET_WORDS.size())); // pick a word
    }
    
    /**
	 * @method getCurrentWord
     * @brief Creates current representation of a secret word (??r??o??).
     * @return Current representation of a word.
     */
    protected String getCurrentWord() {
        String currentWord = "";
  
        for (int i = 0; i < secret_word.length(); i++) {
            char currentChar = secret_word.charAt(i);
            
            if (isLetterGuessed(currentChar)) {
            	// if letter is guessed, leave it as it is
            	currentWord += currentChar;
            } else {
            	// if letter is not guessed present it as ?
            	currentWord += "?";
            }
        }
        return currentWord;
    }

    /**
	 * @method getGuessesLeft
     * @brief Getter method for number of guesses left.
     * @return Number of guesses left.
     */
    protected int getGuessesLeft() {
        return guesses_left;
    }

    /**
	 * @method isLetterGuessed
     * @brief Checks if letter was guessed before.
     * @param letter Character to check.
     * @return <code>true</code> if letter was already guessed, <code>false</code> otherwise.
     */
    protected boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(letter);
    }

    /**
	 * @method isWordGuessed
     * @brief Checks if the word is guessed.
     * @return <code>true</code> if word is guessed, <code>false</code> otherwise.
     */
    protected boolean isWordGuessed() {
        for (int i = 0; i < secret_word.length(); i++) {
            char currentChar = secret_word.charAt(i);
            if (!isLetterGuessed(currentChar)) {
                return false; // if unguessed letter appears - word is not guessed 
            }
        }
        return true; // word is guessed 
    }
    
    /**
	 * @method getCurrentGameStatus
     * @brief Constructs game status string.
     * @return String to represent game status.
     */
    protected String getCurrentGameStatus() {
    	String status;
    	
    	if (userWon) {
    		// user won (guessed the secret word)
    		status = "You won with " + guesses_left + " guesses left!";
    	} else if (userLost) {
    		// user lost (ran out of guesses)
    		status = "You lost! (" + secret_word + ")";
    	} else {
    		// in progress - indicate how many guesses left
    		status = guesses_left + " guesses left";
    	}

        return status;
    }

    /**
	 * @method processGuess
     * @brief Processes a keyboard-typed letter.
     * @param letter Character to process.
     */
    protected void processGuess(char letter) {
        boolean gameOver = userLost || userWon; // has game ended ?

        // if the typed letter was not typed before AND game has not ended
        // so, the game will not "punish" you in case you enter a letter more than ones 
        if (!isLetterGuessed(letter) && !gameOver) {
        	
        	// add letter to a set of guessed letters (already typed-in letters)
            guessedLetters.add(letter);
            
            // if letter is not in the secret word - you loose a guess
            if (!secret_word.contains(String.valueOf(letter))) {
                guesses_left--;
            }
        }

        // condition to win
        if (isWordGuessed()) {
            userWon = true;
        }

        // condition to loose
        if (guesses_left == 0) {
            userLost = true;
        }
    }

    /**
	 * @method getRandomIndex
     * @brief Generates random number.
     * @param number Upper bound of range (excluding).
     * @return Random number between 0 and number-1.
     */
    private int getRandomIndex(int number) {
        Random random = new Random();
        return random.nextInt(number);
    }
}
