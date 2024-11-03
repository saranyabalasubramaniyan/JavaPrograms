/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 * This class has the functionality to display the weighted sum of the entered digits.
 * Eg: wsd(1971) = 1 * 1 + 9 * 2 + 7 * 3 + 1 * 4 = 44 
 * 
 * Features used: Recursive method calling
 * User entry scanning
 * Usage of Collection- Arraylist 
 *********************************************************************************/
import java.util.ArrayList;
import java.util.Scanner;

public class WeightedSumOfNumbers 
{
	private static ArrayList<Integer> digitList = new ArrayList<>();
	
	/**
	 * Main method that is called when the java program is launched
	 * This method displays the program information and reads the number
	 * entered by the user
	 * The digits of the entered number are then put in an arraylist in 
	 * the reverse order
	 * The calculation of weighted sum is done by multiplying the
	 * weight with each of the digits stored in the arraylist and then
	 * summing them up
	 * @param args Generic String argument
	 */
	public static void main(String args[])
	{
		Scanner keyboard = new Scanner(System.in);

		System.out.println("The program will print the weighted sum of the digits of the number entered.");
		System.out.println();
		System.out.println("Please enter the number : ");
		int enteredNumber = keyboard.nextInt();
		
		separateTheDigits(enteredNumber);
		
		int wsdl = 0;
		for(int inx=digitList.size(); inx >0 ; inx--)
		{
			wsdl = wsdl + (digitList.get(digitList.size()-inx) * inx);
		}
		System.out.println(wsdl);
	}
	
	/**
	 * This method takes each of the digits in the entered number and 
	 * updates the arraylist
	 * The entered number is divided by 10, the reminder is added to the
	 * arraylist.
	 * The quotient is then used to recursively call this method itself until 
	 * the value of the quotient is less than 10
	 * @param enteredNumber Intereger value for which a division by 10 is done to find quotient and reminder
	 */
	private static void separateTheDigits(int enteredNumber)
	{
		if(enteredNumber > 9)
		{
			int divisor = 10;
			int reminder = enteredNumber % divisor;
			int quotient = enteredNumber / 10;
			digitList.add(reminder);
			separateTheDigits(quotient);
		}
		else
		{
			digitList.add(enteredNumber);
		}
	}
}
