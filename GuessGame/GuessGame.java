/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 * This class has the functionality for the guess game where the java program 
 * allows the user to guess the random generated number between 0 and 1000000
 * by hinting the user if the number is too small or large in comparison with 
 * the number to be guessed.
 * When the number is guessed correctly, the program also enlists the number
 * of attempts taken to guess the number correctly.
 * 
 * Features used: Recursive method calling
 * User entry scanning
 * Usage of Collection- HashMap 
 *********************************************************************************/
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GuessGame 
{
	private static HashMap<Integer, Integer> enteredNumberMap = new HashMap<>();
	
	/**
	 * Entry method, Prints the user interactive information
	 * The main guess activity is done in guesserMethod which is called
	 * from this main method
	 * A Random number is generated in this method for the user to guess
	 * 
	 * @param args generic string arguments for the main method
	 */
	public static void main(String args[])
	{
		Scanner keyboard = new Scanner(System.in);
		
		Random rand = new Random();
		int upperBound = 1000001;
		/**
		 * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), 
		 * drawn from this random number generator's sequence.
		 * So in our case, the upper bound 1000001 is excluded and the random numbers are generated between 0 and 1000000
		 */
		int numberToGuess = rand.nextInt(upperBound);
		
		System.out.println("Welcome to the guess game!");
		System.out.println();
		System.out.println("You will have to guess the number I thought of! It is something between 0 and 1000000");
		
		guesserMethod(keyboard, numberToGuess);
	}
	
	/**
	 * The method reads the user entry and compares with the random generated number
	 * that has to be guessed.
	 * If the guessed number is either smaller or larger than the number to guess, 
	 * the same is printed to the user and again the application waits for the 
	 * user to print another number.
	 * IF the number matches, then the guess game ends
	 * @param keyboard Scanner instance to read the user entry in the keyboard
	 * @param numberToGuess The random generated number that has to be guessed by the user
	 */
	private static void guesserMethod(Scanner keyboard, int numberToGuess)
	{
		int guessedNumber = keyboard.nextInt();
		enteredNumberMap.put(guessedNumber, 0);
		if(guessedNumber == numberToGuess)
		{
			System.out.println("YOU GUESSED IT in " + enteredNumberMap.size() + " steps!");
			return;
		}
		else if(guessedNumber < numberToGuess)
		{
			System.out.println( guessedNumber + " too small");
			guesserMethod(keyboard, numberToGuess);
		}
		else if(guessedNumber > numberToGuess)
		{
			System.out.println( guessedNumber + " too large");
			guesserMethod(keyboard, numberToGuess);
		}
	}
	
}
