/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 * This class has the solution for Assignment 2. The class launches a user interface where 
 * the user can enter a number and get the prime factors of the entered number
 *********************************************************************************/
import java.awt.Dimension;

import javax.swing.JFrame;

public class Assignment2 
{
	/**
	 * Displays my information and launches the user interface
	 * @param args
	 */
	public static void main(String args[])
	{
		System.out.println("*************** Assignment 2 - START *******************");
		
		System.out.println("Name: Saranya Balasubramaniyan");
		System.out.println("Student ID: 999901316");
		System.out.println("Course Name: Advanced Programming Concepts");
		System.out.println("Section Number : 032");
		createDialog();
	}
	
	/**
	 * The method creates a user interface dialog where the user
	 * can enter the prime number
	 */
	private static void createDialog()
	{
		//Setting the Dialog configuration details
        JFrame frame = new JFrame("Assignment 2: Print Prime Factors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400,300));
       
        //Create and set up the content pane.
        ClientInterface newContentPane = new ClientInterface(frame);
        //Content panes are set to be opaque
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
