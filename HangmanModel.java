package hangman;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HangmanModel {
    private static final ArrayList<String> SECRET_WORDS = new ArrayList<>();
    private final Set<Character> guessedLetters = new HashSet<>();
    private static String secret_word;
    private static int guesses_left = 6;
    private static boolean userWon = false;
    private static boolean userLost = false;
    private static final int TOTAL_NUMBER_OF_GUESSES = 6;

    public HangmanModel() {
        initializeSecretWords();
        startNewGame();
    }

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

    protected void startNewGame() {
        guessedLetters.clear();
        guesses_left = TOTAL_NUMBER_OF_GUESSES;
        userLost = false;
        userWon = false;
        secret_word = SECRET_WORDS.get(getRandomIndex(SECRET_WORDS.size()));
    }
    
    protected String getCurrentWord() {
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

    public String getSecretWord() {
        return secret_word;
    }

    public int getGuessesLeft() {
        return guesses_left;
    }

    public boolean isUserWon() {
        return userWon;
    }

    public boolean isUserLost() {
        return userLost;
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(letter);
    }

    public boolean isWordGuessed() {
        for (int i = 0; i < secret_word.length(); i++) {
            char currentChar = secret_word.charAt(i);
            if (!isLetterGuessed(currentChar)) {
                return false;
            }
        }
        return true;
    }
    
    protected String getCurrentGameStatus() {
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

    public void processGuess(char letter) {
        boolean gameOver = userLost || userWon;

        if (!isLetterGuessed(letter) && !gameOver) {
            guessedLetters.add(letter);

            if (!secret_word.contains(String.valueOf(letter))) {
                guesses_left--;
            }
        }

        if (isWordGuessed()) {
            userWon = true;
        }

        if (guesses_left == 0) {
            userLost = true;
        }
    }

    private int getRandomIndex(int number) {
        Random random = new Random();
        return random.nextInt(number);
    }
}
