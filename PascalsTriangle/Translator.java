/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							
 *
 *The program showcases a translator where String processing is done on the user input String
 *This class handles translation of English to Fake Latin and vice versa. Detection in language 
 * is not handled as a part of this implementation
 * 
 * Features used: 
 * Character class features - isLetterOrDigit, isUpperCase, toUpperCase, toLowerCase
 * String class features - split, length, charAt, concat using +, 
 *********************************************************************************/
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Translator 
{
	/**
	 * This is the entry method for the class.
	 * This method is called on execution of this class
	 * @param args default arguments to be used for command line arguments
	 */
	public static void main(String args[])
	{
		System.out.println("Welcome to the Translator!");
		translatorMethod();
	}

	/**
	 * The method displays the user messages for providing the options to initiate
	 * a translation process. Allows the user to choose the type of translation.
	 * Requests for the sentence to translate. When the translation is complete,
	 * prints the translated sentences
	 */
	private static void translatorMethod()
	{
		Scanner keyboard = new Scanner(System.in);
		Scanner keyboard2 = new Scanner(System.in).useDelimiter("\n");


		System.out.println("This Translator can translate :");

		System.out.println("1. English to Fake Latin");
		System.out.println("2. Fake Latin to English");
		System.out.println("What would you like to translate? Please enter 1 or 2");


		int userChoice = 0;
		try
		{
			userChoice = keyboard.nextInt();
		}
		catch(InputMismatchException ex)
		{
			System.out.println("Incorrect entry. Please enter either 1 or 2.");
			translatorMethod();
		}

		System.out.print("Please enter the sentence to be translated: ");

		String sentenceToTranslate = "";
		try
		{
			sentenceToTranslate=keyboard2.next();
		}
		catch(NoSuchElementException ex)
		{
			keyboard2.close();
			keyboard.close();
			System.exit(0);
		}
		keyboard2.close();
		keyboard.close();

		if(!(sentenceToTranslate.equals("")))
		{
			if(userChoice == 1)
			{
				System.out.println(" Translated : " + englishToFakeLatin(sentenceToTranslate));
			}
			else if(userChoice == 2)
			{
				System.out.println(" Translated : " + fakeLatintoEnglish(sentenceToTranslate));
			}
			else
			{
				System.out.println("Incorrect Value. No translation.");
			}
		}
	}

	/**
	 * Translates the sentence from English to Fake Latin
	 * Takes each word in the sentence and performs the translation
	 * For each word, first char is moved to the end
	 * the other char in the word are moved to the left
	 * to the end of this modified word, 'ay' is added
	 * Features like Upper case at the beginning of the word are retained 
	 * as entered by the user
	 * Any punctuations at the end of a word are handled.
	 * 
	 * @param sentenceToTranslate Sentence in English to be translated
	 * @return Sentence in Fake Latin which is the result
	 */
	private static String englishToFakeLatin(String sentenceToTranslate)
	{
		String fakeLatinSentence = "";
		String[] wordsinSentence = sentenceToTranslate.split(" ");
		for(String word: wordsinSentence)
		{
			if(word != null && word.length() > 0)
			{
				boolean isLetterOrDigit = Character.isLetterOrDigit(word.charAt(word.length()-1));
				char specialLastChar = word.charAt(word.length() - 1);
				char addAtEnd = '\0';
				char[] newWordArray = null;
				if(isLetterOrDigit)
				{
					newWordArray = new char[word.length()];
				}
				else
				{
					newWordArray = new char[word.length() - 1];
				}

				for(int inx=0; inx < word.length()-1; inx++)
				{
					if(inx == 0 && Character.isUpperCase(word.charAt(0)))
					{
						newWordArray[inx] = Character.toUpperCase(word.charAt(inx+1));
					}
					else if(!isLetterOrDigit && !(Character.isLetterOrDigit(word.charAt(word.length()-2))) && inx == word.length()-2)
					{
						continue;
					}
					else
					{
						if(Character.isLetterOrDigit(word.charAt(inx+1)))
						{
							newWordArray[inx] = word.charAt(inx+1);
						}
						else
						{
							if(inx+1 != word.length()-1)
							{
								addAtEnd = word.charAt(inx+1);
							}
						}
					}
				}

				/**
				 * Places the first char as the last char in the new word
				 */
				if(!isLetterOrDigit)
				{
					/**
					 * fox to oxfay (No punctuation at last)
					 */
					if(word.length() >= 2)
					{
						if(Character.isLetterOrDigit(word.charAt(word.length()-2)))
						{
							newWordArray[word.length()-2] = Character.toLowerCase(word.charAt(0));
						}
						else
						{
							/**
							 * fox. to oxfay. (With punctuation at last)
							 */
							if(word.length() >= 3)
							{
								newWordArray[word.length()-3] = Character.toLowerCase(word.charAt(0));
							}
						}
					}
				}
				else
				{
					if(word.length() >= 1)
					{
						if(Character.isLetterOrDigit(word.charAt(word.length()-1)))
						{
							newWordArray[word.length()-1] = Character.toLowerCase(word.charAt(0));
						}
					}
				}
				String newWord = new String(newWordArray);

				newWord = newWord.trim() + "ay";
				if(!isLetterOrDigit)
				{
					if(addAtEnd == '\0')
					{
						newWord = newWord + specialLastChar;
					}
					else
					{
						newWord = newWord + addAtEnd + specialLastChar;
					}
				}
				fakeLatinSentence = fakeLatinSentence + newWord + " ";

			}
		}
		return fakeLatinSentence;
	}

	/**
	 * Translates sentences from Fake Latin to English.
	 * The translation is made word by word in the sentence.
	 * In each word, it is verified if there is a punctuation or 
	 * if the word makes the last word of the sentence.
	 * Based on this information, the length of the new word is
	 * formulated by reducing the length of suffix 'ay'
	 * Then each char is moved to the right and the char before ay 
	 * is moved to the first position to make the translated word
	 * Care is taken to ensure that the upper case /lower case specifications
	 * are maintained. Also, punctuation are handled at the end of the words.
	 * @param sentenceToTranslate
	 * @return
	 */
	private static String fakeLatintoEnglish(String sentenceToTranslate)
	{
		String englishSentence = "";
		String[] wordsinSentence = sentenceToTranslate.split(" ");
		for(String word: wordsinSentence)
		{
			if(word != null && word.length() > 0)
			{
				boolean isEndWord = false;
				if(word.charAt(word.length()-1)== '\r')
				{
					isEndWord = true;
				}

				boolean isPunctuated = false;
				if(isEndWord)
				{
					isPunctuated = !(Character.isLetterOrDigit(word.charAt(word.length()-2)));
				}
				else
				{
					isPunctuated = !(Character.isLetterOrDigit(word.charAt(word.length()-1)));
				}

				char punctuation ='\0';
				if(isPunctuated)
				{
					if(isEndWord)
					{
						punctuation = word.charAt(word.length()-2);
					}
					else
					{
						punctuation = word.charAt(word.length()-1);
					}
				}

				int newWordLength = word.length() - 2;
				if(isEndWord)
				{
					newWordLength--;
				}
				if(isPunctuated)
				{
					newWordLength--;
				}
				char newWord[] = new char[newWordLength];
				for(int inx=0; inx<newWord.length;inx++)
				{
					if(inx==0)
					{
						if(isEndWord)
						{
							if(isPunctuated)
							{
								if(word.length() >= 5)
								{
									if(Character.isUpperCase(word.charAt(0)))
									{
										newWord[inx] = Character.toUpperCase(word.charAt(word.length() - 5));
									}
									else
									{
										newWord[inx] = word.charAt(word.length() - 5);
									}
								}
								else
								{
									System.out.println("Issue in the word to be translated. Please verify");
									return "";
								}
							}
							else
							{
								if(word.length() >= 4)
								{
									if(Character.isUpperCase(word.charAt(0)))
									{
										newWord[inx] = Character.toUpperCase(word.charAt(word.length() - 4));
									}
									else
									{
										newWord[inx] = word.charAt(word.length() - 4);
									}
								}
								else
								{
									System.out.println("Issue in the word to be translated. Please verify");
									return "";
								}
							}
						}
						else if(isPunctuated)
						{
							if(word.length() >= 4)
							{
								if(Character.isUpperCase(word.charAt(0)))
								{
									newWord[inx] = Character.toUpperCase(word.charAt(word.length() - 4));
								}
								else
								{
									newWord[inx] = word.charAt(word.length() - 4);
								}
							}
							else
							{
								System.out.println("Issue in the word to be translated. Please verify");
								return "";
							}
						}
						else
						{
							if(word.length() >= 3)
							{
								if(Character.isUpperCase(word.charAt(0)))
								{
									newWord[inx] = Character.toUpperCase(word.charAt(word.length() - 3));
								}
								else
								{
									newWord[inx] = word.charAt(word.length() - 3);
								}
							}
							else
							{
								System.out.println("Issue in the word to be translated. Please verify");
								return "";
							}
						}
					}
					else
					{
						if(word.length() >= inx-1)
						{
							newWord[inx] = Character.toLowerCase(word.charAt(inx-1));
						}
						else
						{
							System.out.println("Issue in the word to be translated. Please verify");
							return "";
						}
					}		
				}
				String translatedWord = new String(newWord);
				if(isPunctuated)
				{
					translatedWord = translatedWord + punctuation;
				}
				if(isEndWord)
				{
					translatedWord = translatedWord + '\r';
				}

				englishSentence = englishSentence + translatedWord + " ";

			}
		}

		return englishSentence;
	}

}
