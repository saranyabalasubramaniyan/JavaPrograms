/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  Student ID : 999901316
 *							  Course Name : 5103 - Advanced Programming Concepts
 *							  Section Number : 032
 * This class has the solution for Assignment 2. The class launches a user interface where 
 * the user can enter a number and get the prime factors of the entered number
 *********************************************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ClientInterface extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JFrame fFrame = null;
	JTextField txtField = null;
	JTextArea tInText = null;
	JRadioButton rbCautiousWait = null;
	JRadioButton rbWaitDie = null;

	String sServerName;
	int iPortNumber;
	String sFilePath;
	private final String NEWLINE= "\n\n";
	private int enteredNumber = 0;

	public ArrayList<Integer> factorsList = new ArrayList<>();
	public HashMap<Integer, Integer> factorsMap = new HashMap<>();

	public static final Color DARK_BLUE = new Color(0,0,153);

	/**
	 * Constructor that creates the UI components
	 * @param frame
	 */
	public ClientInterface(JFrame frame)
	{
		super(new BorderLayout());

		//Main Panel that contains all other children components in the dialog
		JPanel pMainPanl = new JPanel();
		BoxLayout glMainPanelBoxLayout = new BoxLayout(pMainPanl, BoxLayout.Y_AXIS);
		pMainPanl.setLayout(glMainPanelBoxLayout);

		JPanel pHdrLabelPanel = new JPanel();
		FlowLayout lHdrLabelPanelLayout = new FlowLayout();
		lHdrLabelPanelLayout.setVgap(10);
		pHdrLabelPanel.setLayout(lHdrLabelPanelLayout);
		pHdrLabelPanel.setBackground(Color.white);

		JLabel lHeaderLabel1 = new JLabel("PRIME FACTORS OF THE NUMBER", SwingConstants.CENTER);

		lHeaderLabel1.setForeground(DARK_BLUE);

		pHdrLabelPanel.add(lHeaderLabel1);
		Dimension dim = new Dimension();
		dim.setSize(pHdrLabelPanel.getMaximumSize().width, lHeaderLabel1.getPreferredSize().height);
		pHdrLabelPanel.setMaximumSize(dim);

		this.add(pMainPanl);

		JPanel pContentsPanel = new JPanel();
		GridLayout gContentsPanelLayout = new GridLayout(2,1);
		pContentsPanel.setLayout(gContentsPanelLayout);

		pMainPanl.add(pHdrLabelPanel);
		pMainPanl.add(pContentsPanel);

		JPanel pInnerPanel = new JPanel();
		FlowLayout fFlowLayout = new FlowLayout();
		pInnerPanel.setLayout(fFlowLayout);
		pContentsPanel.add(pInnerPanel);
		pInnerPanel.setBackground(Color.white);

		BoxLayout gLayoutBoxLayout = new BoxLayout(pInnerPanel, BoxLayout.Y_AXIS);
		pInnerPanel.setLayout(gLayoutBoxLayout);

		this.fFrame = frame;

		JPanel pLabelPanel1 = new JPanel();
		BoxLayout blLabelPanel = new BoxLayout(pLabelPanel1, BoxLayout.X_AXIS);
		pLabelPanel1.setLayout(blLabelPanel);
		pLabelPanel1.setBackground(Color.white);
		pLabelPanel1.setAlignmentX(RIGHT_ALIGNMENT);

		JLabel lEnterInfoLabel = new JLabel("Enter a number between 111 and 900. The Prime factors of the number would be displayed by this application.");
		lEnterInfoLabel.setForeground(DARK_BLUE);

		pLabelPanel1.add(lEnterInfoLabel);
		pInnerPanel.add(pLabelPanel1);

		JPanel plabelPanel2 = new JPanel();
		pInnerPanel.add(plabelPanel2);
		plabelPanel2.setBackground(Color.white);

		FlowLayout flowlLayout = new FlowLayout();
		plabelPanel2.setLayout(flowlLayout);

		JLabel lInputFileTxt = new JLabel("Enter the Number : ");
		lInputFileTxt.setForeground(DARK_BLUE);

		txtField = new JTextField();
		txtField.setColumns(5);

		plabelPanel2.add(lInputFileTxt);
		plabelPanel2.add(txtField);


		JPanel pButtonsPanel = new JPanel();
		pButtonsPanel.setBackground(Color.white);
		JButton bExecuteButton = new JButton();
		bExecuteButton.addActionListener(this);

		bExecuteButton.setText("Print Prime Factors");
		bExecuteButton.setForeground(DARK_BLUE);

		JButton bClearButton = new JButton();
		bClearButton.setForeground(DARK_BLUE);
		bClearButton.setText("Clear");
		bClearButton.addActionListener(this);

		pButtonsPanel.add(bExecuteButton);

		pButtonsPanel.add(bClearButton);

		pInnerPanel.add(pButtonsPanel);

		tInText = new JTextArea();
		JScrollPane pInPanel = new JScrollPane(tInText);
		pInPanel.setBackground(Color.white);

		pContentsPanel.add(pInPanel);

		pInPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pInPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Verifies if the entered number is valid or not
	 * It checks if only a number is entered in the field
	 * if the number is greater than 111 and less than 900
	 * @return
	 */
	public boolean validateEnteredNumber()
	{
		boolean isValid = true;

		if(!(txtField.getText().equals("")))
		{
			try
			{
				enteredNumber = Integer.parseInt(txtField.getText());

				if(! (enteredNumber > 111 && enteredNumber < 900))
				{
					updateInTextField("Please enter a valid number between 111 and 900.");
					isValid = false;

				}
			}
			catch(NumberFormatException ex)
			{
				updateInTextField("Please enter a valid number");
				isValid = false;
			}
		}
		else
		{
			updateInTextField("Please enter a valid number between 111 and 900.");
			isValid = false;
		}
		return isValid;
	}

	@Override
	/**
	 * Invoked when any of the buttons Print Prme Factors or Clear is pressed on the screen
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Print Prime Factors"))
		{
			updateInTextField("Inputs provided for this execution are as follows,");
			updateInTextField(" Number Entered: " + txtField.getText());

			if(validateEnteredNumber())
			{
				getAllprimeFactors(enteredNumber);
				printPrimeFactorsForTask1();
				printPrimeFactorsForTask2();
				factorsList.clear();
				factorsMap.clear();
			}
		}
		if(e.getActionCommand().equals("Clear"))
		{
			//When the clear button is pressed, all the text areas are reset to empty
			txtField.setText("");
			tInText.setText("");
		}

	}

	/**
	 * Prints the details on the text area in the user interface
	 * @param printStr
	 */
	private void updateInTextField(String printStr)
	{
		tInText.setText(tInText.getText() + printStr + NEWLINE);
	}


	/**
	 * The method is called recursively to get all the prime factors of the entered number
	 * The factors are collected in a list and a map for the ease of printing them
	 * @param number
	 */
	private void getAllprimeFactors(int number)
	{
		for (int inx = 2; inx <= number; inx++)
		{
			if (number % inx == 0)
			{
				factorsList.add(inx);
				if(factorsMap.containsKey(inx))
				{
					factorsMap.put(inx, factorsMap.get(inx) + 1);
				}
				else
				{
					factorsMap.put(inx, 1);
				}
				int otherFactor = number/inx;
				if(otherFactor != 1)
				{
					getAllprimeFactors(otherFactor);
				}
				break;
			}
		}
	}
	
	/**
	 * The method prints the factors in Factorization form
	 */
	private void printPrimeFactorsForTask1()
	{
		if(factorsList.size() > 0)
		{
			String primeFactStr = "Task 1 : " + enteredNumber + " = " ;
			for(int inx = 0; inx< factorsList.size(); inx++)
			{
				primeFactStr = primeFactStr + factorsList.get(inx);

				if(inx < factorsList.size()-1)
				{
					primeFactStr = primeFactStr + " * ";
				}
			}
			updateInTextField(primeFactStr);
		}		
	}

	/**
	 * The method prints the factors in exponential form
	 */
	private void printPrimeFactorsForTask2()
	{
		if(factorsMap.size() > 0)
		{
			String primeFactStr = "Task 2 : " + enteredNumber + " = " ;
			Iterator<Integer> iter = factorsMap.keySet().iterator();
			int counter = 0;
			while(iter.hasNext())
			{
				counter++;
				int factor = iter.next();

				primeFactStr = primeFactStr + factor + "^" + factorsMap.get(factor);

				if(counter != factorsMap.size())
				{
					primeFactStr= primeFactStr + " * ";
				}
			}
			updateInTextField(primeFactStr);
		}		
	}
}
