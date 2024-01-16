/**
 * @file HangmanMain.java
 * @brief This file contains the HangmanMain class.
 * @author Sergei Rogov U231N0051
 * @date 16.01.2024
 */
package hangman;

import javax.swing.SwingUtilities;

/**
 * @class HangmanMain
 * @brief Starts Hangman game application.
 */
public class HangmanMain {
	
	/**
	 * @method main
     * @brief Kick-starts the application.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            HangmanGUI gui = new HangmanGUI();
        });
    }
}
