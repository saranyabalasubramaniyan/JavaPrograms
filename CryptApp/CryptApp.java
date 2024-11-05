/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							 
 *
 * The application showcases the encryption and decryption of file contents.
 * The user would be able to enter a password. The password is masked when executed in 
 * command prompt. Then the user would be prompted to enter his option to either Save or Load the File
 * 
 * Save - If the file already exists, new file will not be created. If not, a new file will be created.
 * The contents of the file are the encrypted contents of the name entered by the user
 * Encryption - The name entered by the user is processed character by character
 * The character is converted to ascii equivalent decimal number
 * Then the ascii value is converted into binary value.
 * Similarly, the password is taken and processed character by character. Character is converted to 
 * ascii equivalent decimal number. Then ascii value is converted to binary.
 * Te binary value of name and password are then xored.
 * XOR - if both the values are same, return 0, if they are different, return 1
 * Saves the binary result of the XOR in the file as the encrypted value
 * 
 * Load - The operation retrieves the file with the file name entered by the user. If the file does not 
 * exist, it is notified to the user. Then the contents of the file are decrypted using the password
 * entered by the user.
 * Decryption - file contents are taken which are already in binary. The password is converted ,
 * password characters->ascii->binary. XOR is performed between binary values of file contents and password.
 * The resulting value is then processed as follows, xored binary values->decimal equivalent(ascii)-> char for ascii
 * ->string value. This string value is then displayed to the user
 * 
 * The program works with passwords shorter than the name and passwords that are longer than the name.
 * This also governs if the name or the password contains alphanumeric and special characters.
 * Space in the name value is also supported. An entire line can be encrypted with this application.
 * Multiple executions can be done with a single run operation.
 * 
 * Features used: Password Masking
 * File Handling - create, read, write, exceptions around file handling
 * Character - conversion to int, ascii, binary
 * XOR operation - truth table is converted to a method
 * Arrays, ArrayList
 * Method reuse
 *********************************************************************************/
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CryptApp 
{
	/**
	 * Main method is the entry point for the program
	 * The user interactive activities are taken care by the main method
	 * This is recursively called if the user would like to run the program again
	 * @param args no arguments are expected by this program
	 */
	public static void main(String args[])
	{
		System.out.println("This application saves/loads the encrypted data!!");
		String password = getPassword();

		System.out.println("Please choose an option to proceed \n 1. Save file\n 2. Load file");
		Scanner keyboard = new Scanner(System.in);
		int useroption = keyboard.nextInt();

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			if(useroption == 1)
			{	
				System.out.print("Enter your name : ");
				String name = reader.readLine();

				System.out.print("Enter the File name : ");
				String fileName = reader.readLine();

				String encryptedString = encrpt(name, password);

				createAndSaveFile(fileName, encryptedString);
			}
			else if(useroption == 2)
			{
				System.out.print("Enter the File name : ");
				String fileName = reader.readLine();

				String fileContents = readFile(fileName);

				String decryptedString = decrypt(fileContents, password);

				if(decryptedString.length() > 0)
				{
					System.out.println("The file has the following information : " + decryptedString);
				}
			}
			else
			{
				System.out.println("Incorrect entry! Please enter either 1 or 2 to proceed.");
			}

			System.out.println("Would you like to try again ? (Y/N)");
			String yesOrNo = reader.readLine();

			if(yesOrNo.equalsIgnoreCase("Y"))
			{
				main(null);
			}
			else
			{
				System.out.println("Exiting the application");
			}
			reader.close();
		}
		catch(IOException ex)
		{
			System.out.println("ERROR in reading the user inputs. Restarting the application.");
			System.out.println(ex.getMessage());
			main(null);
		}
		keyboard.close();
	}

	/**
	 * Reads the password from the user
	 * If the program is executed in a command prompt, the password will be masked
	 * If executed in an IDE say, eclipse, the password will not be masked.
	 * @return the password entered by the user
	 */
	private static String getPassword()
	{
		System.out.println("<The password is Masked and cannot be viewed while typing in a command prompt>");
		System.out.print("Enter the Password: ");

		String password = "";
		Console console = System.console(); 

		if (console == null)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				password = reader.readLine();
			} 
			catch (IOException e)
			{
				System.err.println("Error getting password" + e.getMessage());
				System.exit(1);
			}
		}	
		else
		{
			password = String.copyValueOf(console.readPassword());
		}
		if(password == null || password.length() == 0)
		{
			System.out.println("Password is empty. Please try again!");
			password = getPassword();
		}
		return password;
	}

	/**
	 * The method converts the string values->characters->ascii->binary
	 * then perform an xor operation and return the encrypted binary 
	 * as a string to be stored in the file
	 * @param name Name entered by the user that has to be encrypted
	 * @param password Password that is used for encryption - entered by the user
	 * @return Encrypted binary value
	 */
	private static String encrpt(String name, String password)
	{
		String encryptedStr = "";

		ArrayList<int[]> nameAsciiList = convertStringToBinary(name.toCharArray());
		ArrayList<int[]> pwAsciiList = convertStringToBinary(password.toCharArray());	

		ArrayList<int[]> xoredList = performXOR(pwAsciiList, nameAsciiList);

		encryptedStr = convertBinaryToString(xoredList);

		return encryptedStr;
	}
	
	/**
	 * The method decrypts the file contents. The password is processed character by character.
	 * Each character is then taken with its ascii value. The decimal value of ascii is then
	 * converted to binary value. The binary from the file contents is then xored with the binary value 
	 * obtained. The xored value is then converted into decimal value. The decimal value is the ascii value 
	 * which is then converted into character. The characters are concated to retrieve the decrypted value 
	 * which is saved in the file.
	 * @param fileContents File contents read from the file - filename is entered by the user
	 * @param password Password that is used for decryption - entered by the user
	 * @return decrypted file content to be displayed to the user
	 */
	private static String decrypt(String fileContents, String password)
	{
		String encryptedStr = "";

		ArrayList<int[]> nameAsciiList = convertbinaryStrToList(fileContents.toCharArray());
		ArrayList<int[]> pwAsciiList = convertStringToBinary(password.toCharArray());	

		ArrayList<int[]> xoredList = performXOR(pwAsciiList, nameAsciiList);

		encryptedStr = decryptBinaryToString(xoredList);

		return encryptedStr;
	}
	
	/**
	 * The method converts the string, splits the character array by 9 characters
	 * takes them and inserts them in array. The array is then added to 
	 * a list which is returned by this method.
	 * @param chArray Array of characters
	 * @return List containing array of integers
	 */
	private static ArrayList<int[]> convertbinaryStrToList(char[] chArray)
	{
		ArrayList<int[]> binaryArrayList = new ArrayList<>();
		if(chArray != null && chArray.length > 0)
		{
			int[] binArray = new int[9];
			int index = 0;
			for(int inx = 0; inx < chArray.length;inx++, index++)
			{
				if(index == 9)
				{
					binaryArrayList.add(binArray);
					index = 0;
					binArray = new int[9];
				}
				
				binArray[index] = Integer.parseInt(Character.toString(chArray[inx]));
			}
			binaryArrayList.add(binArray);
		}
		return binaryArrayList;
	}

	/**
	 * This string sent to this method is processed character by character
	 * For each character, the ascii equivalent is derived
	 * The ascii number is then converted to binary equivalent
	 * This binary value is then added with zeros in the beginning 
	 * to make that as a 9 digit binary number. The 9 digit binary number is then
	 * put in an Interger array which are collected in an arraylist.
	 * The length of the string passed as an input would be same as the size 
	 * of the arraylist returned from the method
	 * @param chArray Input string whose characters are to be processed
	 * @return Arraylist containing the binary equivalent of each element of the string
	 */
	private static ArrayList<int[]> convertStringToBinary(char[] chArray)
	{
		ArrayList<int[]> asciiList = new ArrayList<>();

		if(chArray != null && chArray.length > 0)
		{
			/** taking each character in entered name*/
			for(char ch: chArray)
			{
				/** Converting the character to the ascii value*/
				int asciiCh = (int)ch;
				/** Converting the ascii number to a binary string */
				String binaryStr = Integer.toBinaryString(asciiCh);
				/** converting the string array to character array*/
				char[] binaryChArray = binaryStr.toCharArray();

				/** Taking the binary as 9 bit number because the maximum number of elements in
				 * ASCII is 256 and the binary equivalent of 256 is 100000000. So for any alphanumeric element
				 * entered as the name or password the value of binary equivalent would not exceed 100000000, which
				 * has 9 digits in it. hence appending 0 to the beginning of the digits to allow a clean
				 * XOR operation when performed.
				 */
				int[] binaryArray = new int[9];
				for(int inx=0; inx<binaryArray.length; inx++)
				{
					if(inx < (binaryArray.length - binaryChArray.length))
					{
						binaryArray[inx] = 0;
					}
					else
					{
						int index = inx -binaryArray.length + binaryChArray.length;
						binaryArray[inx] = Integer.parseInt(Character.toString(binaryChArray[index]));
					}
				}
				asciiList.add(binaryArray);
			}
		}
		return asciiList;
	}

	/**
	 * This method takes the binary equivalent of the string to perform the encrypt operation using xor
	 * The bigger of the two lists is chosen, logic is implemented to consider the smaller list from the 
	 * start index again when the all the elements of the smaller list is used up for xor operation.
	 * Each integer array corresponds to the binary equivalent of each character in the string entered by 
	 * the user. each binary digit from both the lists are taken xor is performed. The resulting values 
	 * are added to an array and are in turn added to a list
	 * @param pwList List of integer arrays, each integer array is the binary number equivalent of the ascii
	 *  value of each character of the string - password used for encryption
	 * @param nameList List of integer arrays, each integer array is the binary number equivalent of the ascii 
	 * value of each character of the string - name that would be encryption
	 * @return List of integer arrays resulted by xor operation from the elements of password and name
	 */
	private static ArrayList<int[]> performXOR(ArrayList<int[]> pwList, ArrayList<int[]> nameList)
	{
		ArrayList<int[]> xoredList = new ArrayList<>();
		if(pwList != null && nameList != null && pwList.size() > 0 && nameList.size() > 0)
		{
			for(int inx=0; inx < nameList.size(); inx++)
			{
				int[] nameArray = nameList.get(inx);
				int[] pwArray = null;
				if(inx < pwList.size())
				{
					pwArray = pwList.get(inx);
				}
				else
				{
					int index = inx-pwList.size();
					while(index >= pwList.size())
					{
						index = index - pwList.size();
					}
					pwArray = pwList.get(index);
				}

				int[] resultArray = new int[9];
				if(nameArray != null && pwArray != null && nameArray.length == 9 && pwArray.length == 9)
				{
					for(int jnx = 0; jnx < 9; jnx++)
					{
						resultArray[jnx] = xor(nameArray[jnx], pwArray[jnx]);
					}
					xoredList.add(resultArray);
				}
				else
				{
					System.out.println("ERROR: Issues in the implementation. Please check!");
				}

			}
		}
		return xoredList;
	}

	/**
	 * The method returns the result of an XOR operation
	 * @param a first element of the xor operation - either 0 or 1
	 * @param b second element of the xor operation - either 0 or 1
	 * @return if a and b are same (0 or 1) then 0 is returned, if a and b are different( 1 and 0 / 0 and 1), then 1 is returned
	 */
	private static int xor(int a, int b)
	{
		if(a == b)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

	/**
	 * The method converts the binary list to a string. Arraylist has integer arrays. Integer arrays are of length 9. The 
	 * Integer array contains binary values corresponding to ascii value of the characters. characters are then concatenated
	 * to return the string value.
	 * @param binaryList List of integer arrays. The integer array is of length 9. The contents of the array are binary numbers
	 * @return String value corresponding to the binary->decimal(ascii)->character
	 */
	private static String decryptBinaryToString(ArrayList<int[]> binaryList)
	{
		String stringEquivalent = "";
		if(binaryList != null && binaryList.size() > 0)
		{
			char[] encryptedChArray = new char[binaryList.size()];
			for(int knx= 0 ; knx < binaryList.size(); knx++)
			{
				int[] binArray = binaryList.get(knx);
				if(binArray != null && binArray.length == 9)
				{
					String binaryString = "";
					/** Converting the binary int array to String*/
					for(int inx = 0;inx < 9;inx++)
					{
						binaryString = binaryString + binArray[inx];
					}

					/** Converting binary string to ascii */
					int asciiEq = Integer.parseInt(binaryString, 2);

					/** Converting ASCII to character*/
					char encryptedChar = (char)asciiEq;

					encryptedChArray[knx] = encryptedChar;
				}	
				else
				{
					System.out.println("ERROR : Issue in the Implementation. Please verify!");
				}
			}
			stringEquivalent = new String(encryptedChArray);
		}
		return stringEquivalent;
	}
	
	/**
	 * The method converts the List containing array of integers to a single string with all the 
	 * binary numbers as a continuous string in it.
	 * @param binaryList List containing Array of Integers. Integers are binary numbers either 1 or 0. The integer array is of length 9
	 * @return string containing the binary numbers as a single string
	 */
	private static String convertBinaryToString(ArrayList<int[]> binaryList)
	{
		String stringEquivalent = "";
		if(binaryList != null && binaryList.size() > 0)
		{
			for(int knx= 0 ; knx < binaryList.size(); knx++)
			{
				int[] binArray = binaryList.get(knx);
				if(binArray != null && binArray.length == 9)
				{
					String binaryString = "";
					/** Converting the binary int array to String*/
					for(int inx = 0;inx < 9;inx++)
					{
						binaryString = binaryString + binArray[inx];
					}

					stringEquivalent = stringEquivalent + binaryString;
				}	
				else
				{
					System.out.println("ERROR : Issue in the Implementation. Please verify!");
				}
			}
		}
		return stringEquivalent;
	}

	/**
	 * The method creates a file with the given name and saves the fileContent in the file
	 * The file is created in the current location of the execution
	 * @param fileName Name of the file to be created
	 * @param fileContent Content to be saved in the file
	 */
	private static void createAndSaveFile(String fileName, String fileContent)
	{
		try
		{
			File fileInstance = new File(fileName);
			if (fileInstance.createNewFile()) 
			{
				System.out.println("File created: " + fileInstance.getName());

				try 
				{
					FileWriter myWriter = new FileWriter(fileInstance.getName());
					myWriter.write(fileContent);
					myWriter.close();
				} 
				catch (IOException e) 
				{
					System.out.println("An error occurred.");
					System.out.println(e.getMessage());
				}
			} 
			else
			{
				System.out.println("File already exists. Cannot overwrite an existing file");
			}
		} 
		catch (IOException e)
		{
			System.out.println("An error occurred.");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * The method reads the file and returns the contents of the file
	 * @param fileName File to be read
	 * @return contents of the file
	 */
	private static String readFile(String fileName)
	{
		String fileContents = "";
		
		File fileInstance = new File(fileName);
		try
		{
			Scanner fileReader = new Scanner(fileInstance);
			while (fileReader.hasNextLine())
			{
				fileContents = fileContents + fileReader.nextLine();

			}
			fileReader.close();
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("ERROR: File does not exist");
		}

		return fileContents;
	}
}
