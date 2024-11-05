/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							 
 *
 *The program reads the file - 3nums.txt, sums up the integers in the file and displays the
 *result. All integers are supported. Integers less than 0 are also supported.
 *
 *********************************************************************************/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ThreeNumbers 
{
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * The method is the entry point of the program
	 * The method calls the methods to read file, 
	 * validate inputs and then print the results
	 * of the sum
	 * @param args
	 */
	public static void main(String args[])
	{
		ThreeNumbers tn = new ThreeNumbers();
		String lineInFile = tn.readFile();
		if(lineInFile != null && lineInFile.length() > 0 )
		{
			System.out.println("Reading numbers from the file \"3nums.txt\"... done.");
			ArrayList<Integer> inputList = tn.validateInputs(lineInFile);
			int sum = tn.performAddition(inputList);

			if(inputList != null && inputList.size() == 3)
			{
				System.out.println();
				System.out.println(inputList.get(0) + " + " + inputList.get(1) + " + " + inputList.get(2) + " = " + sum);
			}
		}
		else
		{
			System.out.println("ERROR: Inputs not found in the 3nums.txt file. Please Check!");
		}
	}

	/**
	 * The method reads the file 3nums.txt and returns the contents of the file
	 * @return contents of 3nums.txt
	 */
	private String readFile()
	{
		String lineInFile = "";
		try
		{
			InputStream input = this.getClass().getResourceAsStream("3nums.txt");
			InputStreamReader isr = new InputStreamReader(input, ENCODING);
			BufferedReader reader = new BufferedReader(isr);

			lineInFile  = reader.readLine();
		}
		catch(IOException ex)
		{
			if(ex instanceof FileNotFoundException)
			{
				System.out.println("ERROR: 3num.txt is not found in the location of the java file. Please check!");
			}
			else
			{
				ex.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: 3num.txt is not found in the location of the java file. Please check!");
		}
		return lineInFile;
	}

	/**
	 * Validates if the file contents are integers. If the file contents are integers then 
	 * they are returned in a list
	 * @param lineInFIle file content to be validated
	 * @return Arraylist containing the integers in the 3nums.txt file
	 */
	private ArrayList<Integer> validateInputs(String lineInFIle)
	{
		ArrayList<Integer> inputList = new ArrayList<>();
		if(lineInFIle != null)
		{
			String[] enteredInputInArray = lineInFIle.split(" ");

			if(enteredInputInArray != null )
			{
				if(enteredInputInArray.length == 3)
				{
					for(String str:enteredInputInArray)
					{
						if(str != null)
						{
							try
							{
								inputList.add(Integer.parseInt(str));
							}
							catch(Exception ex)
							{
								System.out.println("ERROR: Non integers found in the input. Please enter only integers as an input.");
							}
						}
						else
						{
							System.out.println("ERROR: Issue in the input. Please Check!");
						}
					}

				}
				else
				{
					System.out.println("ERROR: Input in the file is of incorrect format. Please Check! Format to be used : 'Integer1 Integer2 Integer3'");
				}
			}
			else
			{
				System.out.println("ERROR: Input file is either empty or is of the incorrect format.");
			}
		}
		else
		{
			System.out.println("ERROR:The 3num.txt file is emplty. Please check!");
		}
		return inputList;
	}

	/**
	 * Adds the integers in the arraylist and returns the sum
	 * @param inputList List of Integers to be added
	 * @return sum of the integers in the list
	 */
	private int performAddition(ArrayList<Integer> inputList)
	{
		int sum = 0;
		if(inputList != null && inputList.size() == 3)
		{
			sum = inputList.get(0) + inputList.get(1) + inputList.get(2);
		}
		return sum;
	}
}
