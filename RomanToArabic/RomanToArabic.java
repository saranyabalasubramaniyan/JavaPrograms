/*************************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 * 
 * Standard Input considered is text file of the format .txt
 * Standard output considered is text file of format .txt
 * 
 * Multiple lines of input are supported
 * Assumption is that all each input line contains only one roman numeral in column one
 * all the roman numerals are entered in upper case and there are no blanks in the line
 * 
 * Basic Roman symbols, I - 1, X -10, C -100, M - 1000
 * Auxillary Roman symbols, V - 5, L - 50, D - 500
 * 
 * How a Roman entry is processed: 
 * a symbol following one of greater or equal values adds to its value (E.g., XII = 12)
 * A symbol preceding one of greater value subtracts its value (E.g., IV = 4; XL = 40)
 * 
 * Validations
 * 1.The numeral is valid only if the following characters are present
 *  - I,V,X,L,C,D,M - Invalid character in input. Valid characters are I,V,X,L,C,D,M." 
 *  RULE: Only the listed characters are valid.
 * 2.If an auxillary symbol is found, the next symbol can be any numeral that is less than
 * that auxillary character - Invalid numeral: can't subtract auxiliary symbol." 
 * RULE: It is not permitted to subtract an "auxiliary" symbol.
 * 3. For any symbol 1, if the next symbol 2 is greater, the next to next symbol 3 cannot be 
 * greater than symbol 2. - Invalid numeral: two consecutive subtractions." RULE: Can't do 
 * two subtractions in a row, thus LIVX is illegal.
 * 4. If a symbol is to be added, any symbols following that symbol must be lesser or equal 
 * to the symbol. - "4 Invalid numeral: additions don't decrease." RULE: Additions must decrease, 
 * as you go from left to right. Thus, each symbol added must have a value equal or less than 
 * the last symbol which was added. Thus, LIIX is wrong, cause we added L, added I, subtracted I, 
 * then try to add X.
 * 5. The elements V, L, D should occur only once - 5 Invalid repetition of V, L or D": 
 * RULE The symbols V, L and D are never repeated
 * 6. Any symbol must occur to a maximum number of 3 times - 6 Too long repetition": 
 * RULE  A symbol is not repeated more than three times
 * 7. I can appear only before V and X, X can only appear before L, M and C. C can appear before D and M.
 * Wrong subtraction" RULE The symbol I can be subtracted from V and X only. The symbol X can be 
 * subtracted from L, M, and C only. The symbol C can be subtracted from D and M.
 * 8. Cannot subtract more than one from a particular symbol" RULE A symbol cannot be subtracted more 
 * than once from a particular symbol of greater value. In other words, we cannot repeat a symbol on
 * the left side of a symbol. Eg, 98 is written as XCVIII and not as IIC.
 * 
 * If the file is not present in the location
 * If the command line arg is not passed
 * If the string passed in command line argument is not a location
 * Location is correct but no file
 * File is present in the location but empty
 * File is present in the location and has only a single enter and no values
 **************************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RomanToArabic 
{
	private static HashMap<String, Integer> basicSymbols = new HashMap<>();
	private static HashMap<String, Integer> auxillarySymbols = new HashMap<>();

	/**
	 * Entry method to the program. The method reads the input file, processes the data and 
	 * writes the output in an output file
	 * @param args path of the input file
	 */
	public static void main(String args[])
	{
		RomanToArabic rta = new RomanToArabic();
		rta.loadMaps();
		System.out.println("********* This Program converts the Roman Numbers to Arabic Numbers *********");

		System.out.println();
		System.out.println("1. Command Line entry (Supports single input conversion from Roman to Arabic Number)");
		System.out.println("2. Input text file (Supports single/multiple input conversion from Roman to Arabic Numbers)");
		System.out.println();
		System.out.println("Please choose the input type for conversion : ");

		try
		{
			Scanner keyboard1 = new Scanner(System.in);
			int userChoice = keyboard1.nextInt();

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			if(userChoice == 1)
			{
				String romanNumeral = "";

				System.out.print("Enter the Roman Numeral to convert : ");
				romanNumeral = reader.readLine();

				if(romanNumeral != null && romanNumeral.length() > 0)
				{
					String displayString = "";
					String validity = rta.performValidations(romanNumeral);
					if(validity.equals(""))
					{
						displayString = rta.converToAracbic(romanNumeral);
						System.out.println("----------- Output Information - Start -------------");
						System.out.println();
						System.out.println("Arabic numeral : " + displayString);
						System.out.println("----------- Output Information - End -------------");
					}
					else
					{
						displayString = validity;
						System.out.println("----------- Output Information - Start -------------");
						System.out.println();
						System.out.println(displayString);
						System.out.println("----------- Output Information - End -------------");
					}

				}
				else
				{
					System.out.println("ERROR: Empty Value passed. Please check!");
				}
			}
			else if(userChoice == 2)
			{
				String inputFilePath = "";

				System.out.print("Please provide the path of the input file [Please enter Absolute Path]: ");
				try
				{
					inputFilePath = reader.readLine();
				}
				catch(IOException ex)
				{
					System.out.println("Error reading the Path entered");
				}

				if(inputFilePath != null && inputFilePath.length() > 0)
				{
					ArrayList<String> inputList = rta.readFile(inputFilePath);

					if(inputList != null && inputList.size() > 0)
					{
						ArrayList<String> outputList = new ArrayList<>();

						for(String romanNumeral: inputList)
						{
							String validity = rta.performValidations(romanNumeral);

							if(validity.equals(""))
							{
								outputList.add(rta.converToAracbic(romanNumeral));
							}
							else
							{
								outputList.add(validity);
							}
						}	
						if(outputList.size() > 0)
						{
							rta.writeToFile(inputFilePath, outputList);
						}
					}
					else
					{
						//System.out.println("ERROR: Empty Value passed. Please check!");
					}
				}
				else
				{
					System.out.println("ERROR: Empty Value passed. Please check!");
				}
			}
			else
			{
				System.out.println("ERROR: Invalid entry!");
			}

			System.out.println("Would you like to execute the program again? Y/N");
			Scanner keyboard = new Scanner(System.in);
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
			reader.close();
			keyboard1.close();
			keyboard.close();
		}
		catch(IOException ex)
		{
			System.out.println("ERROR in reading the user inputs.");
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Invalid input entered. Restarting the program!");
			main(new String[] {});
		}
	}

	/**
	 * This method writes the converted Arabic numerals to the standard text output file
	 * @param fileLocation Location where the output file is placed
	 * @param outputlList List containing the converted arabic numerals or errors that are to be written on the output file
	 */
	private void writeToFile(String fileLocation, ArrayList<String> outputlList)
	{
		if(fileLocation.contains("."))
		{
			System.out.println("----------- Output Information - Start -------------");
			/* Path can contain . only in the extension*/
			String[] fileLocArray = fileLocation.split("\\.");
			String outputFileLoc = fileLocArray[0]+"_output"+"."+fileLocArray[1];

			File outputFile = new File(outputFileLoc);

			try
			{
				boolean isFileCreated = outputFile.createNewFile();
				if(!isFileCreated)
				{
					System.out.println(outputFileLoc + " already exists and will be overwritten.");
					System.out.println();
				}

				FileWriter fileWriter = new FileWriter(outputFile);
				for(String outdata:outputlList)
				{
					fileWriter.write(outdata);
					fileWriter.write("\n");
				}
				fileWriter.close();
				System.out.println("Conversion Completed! Output file :" + outputFileLoc);
			}
			catch(IOException ex)
			{
				System.out.println("ERROR: Error in writing data to the output file.");
			}
			System.out.println("----------- Output Information - End -------------");
		}
	}

	/**
	 * This method converts the roman numeral to arabic numeral
	 * @param romanNumeral Roman Numeral to be converted
	 * @return Arabic numeral after conversion
	 */
	private String converToAracbic(String romanNumeral)
	{
		String arabicNumeral = "";

		if(romanNumeral != null && romanNumeral.length() > 0)
		{
			char[] romanChars = romanNumeral.toCharArray();
			ArrayList<Integer> addList = new ArrayList<>();
			ArrayList<Integer> subList = new ArrayList<>();
			for(int inx = 0; inx < romanChars.length; inx++)
			{
				int romanValue = getArabicValue(romanChars[inx]);
				if(inx+1 < romanChars.length)
				{
					int nextRomanValue = getArabicValue(romanChars[inx + 1]);

					if(romanValue >= nextRomanValue)
					{
						addList.add(romanValue);
					}
					else
					{
						subList.add(romanValue);
					}
				}
				if(inx == romanChars.length-1 )
				{
					addList.add(romanValue);
				}	
			}
			int addValue = 0;
			int subValue = 0;
			if(addList.size() > 0)
			{
				for(Integer add:addList)
				{
					addValue = addValue + add;
				}
			}
			if(subList.size() > 0)
			{
				for(Integer sub:subList)
				{
					subValue = subValue + sub;
				}
			}
			int arabicNumber = addValue - subValue;
			arabicNumeral = Integer.toString(arabicNumber);
		}
		return arabicNumeral;
	}

	/**
	 * The method reads the file passed as an argument and returns the file contents in
	 * an ArrayList
	 * @param filePath Path of the file containing the input
	 * @return List containing the file contents as a String List
	 */
	private ArrayList<String> readFile(String filePath)
	{
		ArrayList<String> inputList = new ArrayList<>();
		try
		{
			if(filePath != null && !(filePath.equals("")))
			{
				BufferedReader bufReader = new BufferedReader(new FileReader(filePath));

				String line = bufReader.readLine();

				while (line != null)
				{
					inputList.add(line);
					line = bufReader.readLine();
				}

				if(inputList.size() == 0)
				{
					System.out.println("ERROR: The Input file is empty.");
				}
				bufReader.close();
			}
			else
			{
				System.out.println("ERROR: Empty Value passed. Please check!");
			}
		}
		catch(IOException ex)
		{
			if(ex instanceof FileNotFoundException)
			{
				System.out.println("ERROR: Input file is not found in the location passed as an input. Please check!");
			}
			else
			{
				ex.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			System.out.println("ERROR: Input file is not found in the location passed as an input. Please check!");
		}
		return inputList;
	}

	/**
	 * The method performs validation on the roman numeral sent as an input to this method
	 * If the roman numeral does not satisfy any of the validation rules, then the error is returned
	 * If the roman numeral adheres to all the validation rules, then an empty string is returned
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String performValidations(String romanNumeral)
	{
		String validity = "";
		if(romanNumeral != null && romanNumeral.length() > 0)
		{
			validity = validCharacterRule(romanNumeral);
			if(validity.equals(""))
			{
				validity = auxillarySubtractionRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = consecutiveSubtractionRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = additionsDontDecreaseRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = VLDRepititionRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = longRepitionRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = wrongSubtractionRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = subtractfromSameSymbolAgainRule(romanNumeral);
			}
			if(validity.equals(""))
			{
				validity = repeatInAddAndSubRule(romanNumeral);
			}
		}
		else
		{
			validity = "ERROR: Blank value";
		}
		return validity;
	}

	/**
	 * Loads the arabic equivalent values for the roman numerals in the map
	 */
	private void loadMaps()
	{
		basicSymbols.put("I", 1);
		basicSymbols.put("X", 10);
		basicSymbols.put("C", 100);
		basicSymbols.put("M", 1000);

		auxillarySymbols.put("V", 5);
		auxillarySymbols.put("L", 50);
		auxillarySymbols.put("D", 500);
	}

	/* To Test : ASICS, MIX, DIM, VIM, MALL*/
	/**
	 * If the input contains characters other than I, X, C, M, V, L, D, then 
	 * an error is returned
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String validCharacterRule(String romanNumeral)
	{
		String validity = "";

		char[] romanChars = romanNumeral.toCharArray();
		for(char romanChar: romanChars)
		{
			if(!(basicSymbols.containsKey(Character.toString(romanChar))) && !(auxillarySymbols.containsKey(Character.toString(romanChar))))
			{
				validity = "1. Invalid character in input. Valid characters are I,V,X,L,C,D,M. RULE: Only the listed characters are valid.";
				break;
			}
		}
		return validity;
	}

	/* To Test : VLD, VX, DVX, DIV, DVI, DVV, MLC*/
	/**
	 * If there is an auxillary symbol, the next symbol cannot be greater than that auxillary
	 * symbol. Meaning, an auxillary symbol cannot be subtracted from the following symbol.
	 * Error is returned in case the next symbol is greater than the auxillary symbol
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String auxillarySubtractionRule(String romanNumeral)
	{
		String validity = "";

		char[] romanChars = romanNumeral.toCharArray();
		for(int inx = 0; inx < romanChars.length; inx++)
		{
			if(auxillarySymbols.containsKey(Character.toString(romanChars[inx])))
			{
				int romanCharValue = getArabicValue(romanChars[inx]);

				if(inx+1 < romanChars.length)
				{
					int nextRomanCharValue = getArabicValue(romanChars[inx + 1]);

					if(romanCharValue < nextRomanCharValue)
					{
						validity = "2. Invalid numeral: can't subtract auxiliary symbol. RULE: It is not permitted to subtract an 'auxiliary' symbol.";
						break;
					}
				}
			}
		}
		return validity;
	}

	/*Test : XIV, XVI, XIVX, VII, LIVX*/
	/**
	 * Two consecutive subrtractions are not allowed. So the method validates by getting the arabic equivalent of a symbol and
	 * checks if the arabic equivalent of the symbol to the right is greater and then checks if the arabic equivalent of the next to
	 * next symbol is greater than the next symbol. Thus ensuring there are no consecutive subtractions
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String consecutiveSubtractionRule(String romanNumeral)
	{
		String validity = "";
		char[] romanChars = romanNumeral.toCharArray();
		for(int inx = 0; inx < romanChars.length; inx++)
		{
			if(inx+1 < romanChars.length && inx+2 < romanChars.length)
			{
				int romanValue = getArabicValue(romanChars[inx]);
				int nextRomanValue = getArabicValue(romanChars[inx + 1]);
				int nextToNextRomanValue = getArabicValue(romanChars[inx + 2]);

				if(nextRomanValue > romanValue && nextToNextRomanValue > nextRomanValue)
				{
					validity = "3. Invalid numeral: two consecutive subtractions.RULE: Can't do two subtractions in a row";
					break;
				}			
			}
		}

		return validity;
	}

	/**
	 * The method returns the equivalent arabic value for the roman character passed as 
	 * an input
	 * @param romanChar Roman Symbol
	 * @return Arabic equivalent
	 */
	private int getArabicValue(char romanChar)
	{
		int romanValue = 0;
		String romanStr = Character.toString(romanChar);
		if(basicSymbols.containsKey(romanStr))
		{
			romanValue = basicSymbols.get(romanStr);
		}
		else if(auxillarySymbols.containsKey(romanStr))
		{
			romanValue = auxillarySymbols.get(romanStr);
		}
		return romanValue;
	}

	/* Test: XII, XXXIII, LXXVII, LVIX, IX, XI, X, V, L, M, XIX, XXI, XXXIX, VIX, LIIX */
	/**
	 * This method verifies if the symbols that correspond to add operation do not decrease
	 * in value from left to right. The validation is dont only on characters that correspond
	 * to add.
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String additionsDontDecreaseRule(String romanNumeral)
	{
		String validity = "";
		char[] romanChars = romanNumeral.toCharArray();
		ArrayList<Integer> addList = new ArrayList<>();
		for(int inx = 0; inx < romanChars.length; inx++)
		{
			int romanValue = getArabicValue(romanChars[inx]);
			if(inx+1 < romanChars.length)
			{
				int nextRomanValue = getArabicValue(romanChars[inx + 1]);

				if(romanValue >= nextRomanValue)
				{
					addList.add(romanValue);
				}
			}
			if(inx == romanChars.length-1 )
			{
				addList.add(romanValue);
			}	
		}
		if(addList.size() > 0)
		{
			for(int inx=0; inx < addList.size(); inx++)
			{
				if(inx+1 < addList.size())
				{
					if(addList.get(inx) < addList.get(inx + 1))
					{	
						validity = "4. Invalid numeral: additions don't decrease. RULE: Additions must decrease, as you go from left to right";
						break;
					}
				}
			}
		}

		return validity;
	}

	/* Test VV, XXIIV, VIVD, LDL, DDLL, VVX, XI, I, V*/
	/**
	 * The method verifies if the symbols V, L or D are repeated
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String VLDRepititionRule(String romanNumeral)
	{
		String validity = "";
		char[] romanChars = romanNumeral.toCharArray();
		ArrayList<Character> romanCharsList = new ArrayList<>();

		for(char romanChar:romanChars)
		{
			romanCharsList.add(romanChar);
		}
		if(isRepetitive(romanCharsList, 'V') || isRepetitive(romanCharsList, 'L') || isRepetitive(romanCharsList, 'D'))
		{
			validity = "5. Invalid repetition of V, L or D. RULE The symbols V, L and D are never repeated";
		}	

		return validity;	
	}

	/**
	 * The method returns a boolean if the specified character is repeated multiple times in the Arraylist 
	 * @param romanCharsList Arraylist to be checked
	 * @param ch Character to be verified for
	 * @return boolean value is true if the character is present multiple times list
	 */
	private boolean isRepetitive(ArrayList<Character> romanCharsList, char ch)
	{
		boolean isRepeated = false;
		if(!(romanCharsList.indexOf(ch) == romanCharsList.lastIndexOf(ch)))
		{
			isRepeated = true;
		}
		return isRepeated;
	}

	/*To Test: XXXV, XXXIX, LXXXI, XXXVI, LXXXIX*/
	/**
	 * The method validates if any of the Roman symbol is repeated for more than 3 times consecutively
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String longRepitionRule(String romanNumeral)
	{
		String validity = "";
		char[] romanChars = romanNumeral.toCharArray();
		ArrayList<Character> romanCharsList = new ArrayList<>();

		for(char romanChar:romanChars)
		{
			romanCharsList.add(romanChar);
		}

		for(int inx=0; inx < romanChars.length; inx++)
		{
			if(inx+3 < romanChars.length)
			{
				if(romanChars[inx] == romanChars[inx+1] &&  romanChars[inx+1] == romanChars[inx+2] && romanChars[inx+2] == romanChars[inx+3])
				{
					validity = "6. Too long repetition. RULE  A symbol is not repeated more than three times consecutively";
					break;
				}
			}
		}
		return validity;
	}

	/**
	 * I, X, C can be last elements or I can appear only before V and X, X can only appear before L, M and C. C can appear before D and M.
	 * Wrong subtraction" RULE The symbol I can be subtracted from V and X only. The symbol X can be 
	 * subtracted from L, M, and C only. The symbol C can be subtracted from D and M.
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String wrongSubtractionRule(String romanNumeral)
	{
		String validity = "";

		ArrayList<Character> iList = new ArrayList<>();
		iList.add('V');
		iList.add('X');
		validity = subRuleImplementor(romanNumeral, 'I', iList);

		if(validity.equals(""))
		{
			ArrayList<Character> xList = new ArrayList<>();
			xList.add('L');
			xList.add('M');
			xList.add('C');
			validity = subRuleImplementor(romanNumeral, 'X', xList);

			if(validity.equals(""))
			{
				ArrayList<Character> cList = new ArrayList<>();
				cList.add('D');
				cList.add('M');
				validity = subRuleImplementor(romanNumeral, 'C', cList);
			}
		}

		return validity;
	}

	/**
	 * The method verifies if the symbol is preceeding the succesors that are accepted.
	 * @param romanNumeral Roman Numeral to be verified
	 * @param ch character to be verified
	 * @param acceptedSuccessors accepted succesors after the character ch
	 * @return Empty string denotes that the specific characters in the roman numeral are followed by the accepted successors in the list
	 */
	private String subRuleImplementor(String romanNumeral, char ch, ArrayList<Character> acceptedSuccessors)
	{
		String validity = "";
		char[] romanChars = romanNumeral.toCharArray();

		for(int inx = 0; inx < romanChars.length; inx++)
		{
			int romanValue = getArabicValue(romanChars[inx]);
			if(romanChars[inx] == ch)
			{
				if(inx+1 < romanChars.length)
				{
					int nextRomanValue = getArabicValue(romanChars[inx + 1]);

					if(romanValue < nextRomanValue)
					{
						if(!(acceptedSuccessors.contains(romanChars[inx + 1])))
						{
							validity = "7. Wrong subtraction. RULE The symbol I can be subtracted from V and X only. The symbol X can be subtracted from L, M, and C only. The symbol C can be subtracted from D and M.";
						}
					}
				}
			}
		}
		return validity;
	}

	/**
	 * The method validates if the same symbol is subtracted twice in the Roman Numeral
	 * @param romanNumeral The roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String subtractfromSameSymbolAgainRule(String romanNumeral)
	{
		String validity = "";

		char[] romanChars = romanNumeral.toCharArray();

		ArrayList<Integer> subList = new ArrayList<>();
		for(int inx = 0; inx < romanChars.length; inx++)
		{
			int romanValue = getArabicValue(romanChars[inx]);
			if(inx+1 < romanChars.length)
			{
				int nextRomanValue = getArabicValue(romanChars[inx + 1]);

				if(romanValue < nextRomanValue)
				{
					if(subList.contains(romanValue))
					{
						validity = "8. Cannot subtract more than one from a particular symbol. RULE A symbol cannot be subtracted more than once from a particular symbol of greater value.";
						break;
					}
					else
					{
						subList.add(romanValue);
					}
				}	
			}
		}

		return validity;
	}
	
	/**
	 * If the same symbol is added and subtracted in a Roman numeral, then that makes no additional value to the Roman numeral
	 * Then that makes that roman numeral invalid
	 * @param romanNumeralT he roman numeral to be validated
	 * @return empty string denotes that all characters are valid, error will be returned
	 * if an invalid character is encountered
	 */
	private String repeatInAddAndSubRule(String romanNumeral)
	{
		String validity = "";
		
		if(romanNumeral != null && romanNumeral.length() > 0)
		{
			char[] romanChars = romanNumeral.toCharArray();
			ArrayList<Integer> addList = new ArrayList<>();
			ArrayList<Integer> subList = new ArrayList<>();
			for(int inx = 0; inx < romanChars.length; inx++)
			{
				int romanValue = getArabicValue(romanChars[inx]);
				if(inx+1 < romanChars.length)
				{
					int nextRomanValue = getArabicValue(romanChars[inx + 1]);

					if(romanValue >= nextRomanValue)
					{
						addList.add(romanValue);
						if(subList.contains(romanValue))
						{
							validity = "9. Symbol cannot repeat with no value. A symbol cannot be added and subtracted to nullify its value.";
							break;
						}
					}
					else
					{
						subList.add(romanValue);
						if(addList.contains(romanValue))
						{
							validity = "9. Symbol cannot repeat with no value. A symbol cannot be added and subtracted to nullify its value.";
							break;
						}
					}
				}
				if(inx == romanChars.length-1 )
				{
					addList.add(romanValue);
					if(subList.contains(romanValue))
					{
						validity = "9. Symbol cannot repeat with no value. A symbol cannot be added and subtracted to nullify its value.";
						break;
					}
				}	
			}
			
		}
		return validity;
	}
}
