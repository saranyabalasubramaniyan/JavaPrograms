/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *						
 *The program prints the Pascals Triangle for the given number of Rows.
 * 
 * Features used: 
 * Collections- Arraylist
 * Arrays
 * Recursion
 *********************************************************************************/
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class PascalsTriangle
{
	private static ArrayList<Integer[]> pascalsTriangleList = new ArrayList<>();

	/**
	 * This method is called on the program execution
	 * The method allows the user to enter the number of rows to be printed in the Pascals Triangle.
	 * Then the Pascals triangle constuction is done in the constructThePascalsTriangle method
	 * Once the elements of the Pascals triangle are ready to be printed, this method prints
	 * each element with the necessary tabs and line breaks for the Pascals trianngle
	 * The method is called recursively if the user chooses to reexecute the printing of Pascals Triangle
	 * @param args No arguments are expected for the main method during the program execution
	 */
	public static void main(String args[])
	{
		Scanner keyboard = new Scanner(System.in);

		System.out.println("Welcome to the Pascals triangle - Printer!");
		System.out.println("We are going to construct a Pascals Traingle based on the number of rows given in the input");
		System.out.println("Please enter the number of rows for Pascals Triangle: ");

		int numberOfRows = 0;
		try
		{
			numberOfRows = keyboard.nextInt();
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Uh oh! Incorrect entry. Please enter positive Integers. Better luck next time.");
			System.exit(0);
		}
		
		if(numberOfRows > 0)
		{
			constructThePascalsTriangle(numberOfRows);
			for(int jnx =0;jnx<pascalsTriangleList.size();jnx++)
			{
				Integer[] pascalsRow = pascalsTriangleList.get(jnx);

				if(pascalsRow != null)
				{
					int numberofTabs = (pascalsTriangleList.size() - pascalsRow.length) ;
					while(numberofTabs > 0)
					{
						System.out.print("\t");
						numberofTabs--;
					}
					for(int inx =0;inx<pascalsRow.length;inx++)
					{
						Integer element = pascalsRow[inx];
						System.out.print(element);
						System.out.print("\t\t");
					}
					System.out.println();
				}
			}
		}
		else
		{
			System.out.println("Please enter a positive integer to print the Pascals Triangle.");
		}
		pascalsTriangleList.clear();
		System.out.println("Would you like to try again? Y/N");
		String userEntry = keyboard.next();

		if(userEntry.equalsIgnoreCase("Y"))
		{
			main(new String[] {});
		}
		else if(userEntry.equalsIgnoreCase("N"))
		{
			System.out.println("Exiting the application!!");
		}
		else
		{
			System.out.println("This is definitely not a Yes. So exiting the application.");
		}
		keyboard.close();
	}

	/**
	 * This method loads the arraylist with the elements of the Pascals Triangle
	 * The Arraylist will be a list of Integer Array where each Array represents each row in the Pascals Triangle
	 * While calculating elements of each row, the previous row is retrieved and the elements are accessed to 
	 * calculate the value of each element in the next row.
	 * @param numberOfRows The number of rows in the Pascals Triangle to be printed
	 */
	private static void constructThePascalsTriangle(int numberOfRows)
	{
		for(int inx=0;inx<numberOfRows;inx++)
		{
			Integer[] pascalsRow = new Integer[inx+1];

			Integer[] previousRow = null;
			if(pascalsTriangleList.size() > 0 && (pascalsTriangleList.size() >= inx-1))
			{
				previousRow = pascalsTriangleList.get(inx-1);
				if(!( previousRow.length == inx))
				{
					System.out.println("Error in the implementation or some weird use case is executed! Quitting the execution. Sorry!");
					return;
				}
			}
			for(int jnx=0; jnx < pascalsRow.length; jnx++)
			{
				if(jnx == 0 || jnx == pascalsRow.length-1)
				{
					pascalsRow[jnx] = 1;
				}
				else
				{
					if(previousRow.length >= jnx+1)
					{
						pascalsRow[jnx] = previousRow[jnx-1] + previousRow[jnx];
					}
					else
					{
						System.out.println("Something seriously went wrong. OMG! Quitting the execution!");
						return;
					}
				}
			}
			pascalsTriangleList.add(pascalsRow);
		}
	}
}
