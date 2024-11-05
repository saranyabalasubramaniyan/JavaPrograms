/*************************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							 
 *
 * This Program launches the Tic Tac board board and allows the user to play the game with the 
 * computer. 
 * 1.The game is played on a grid that's 3 X 3 
 * 2. The first player gets the mark X and the mark O is assigned to the next. Player and computer take turns putting their marks in empty squares.
 * 3.The first player to get 3 consecutive marks in a row is the winner.
 * 4. When all 9 squares are full, the game is over.
 **************************************************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TicTacToe
{
	public static final Color DARK_BLUE = new Color(0,0,153);
	/**
	 * Entry method of the Program
	 * @param args
	 */
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("TIC TAC TOE");
		frame.add(new TicTacToePanel(frame));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}
}
class TicTacToePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ArrayList<Integer>> winningCombinations = new ArrayList<>();
	public boolean isComputerFirstPlayer = false;
	public ArrayList<Integer> computerPlay = new ArrayList<>();
	public ArrayList<Integer> playerPlay = new ArrayList<>();
	public HashMap<Integer, JButton> statesAndButtonsMap = new HashMap<>();
	public String nuggetImg = "resources/circle.png";
	public String crossImg = "resources/close.png";
	
	public static final Color DARK_BLUE = new Color(0,0,153);

	JButton helpButton = new JButton("Help");
	JButton playagainButton = new JButton("Play Again");
	
	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JButton button3 = new JButton();

	JButton button4 = new JButton();
	JButton button5 = new JButton();
	JButton button6 = new JButton();

	JButton button7 = new JButton();
	JButton button8 = new JButton();
	JButton button9 = new JButton();
	
	JLabel messageLabel = new JLabel();

	JRadioButton yesRadioButton = new JRadioButton();

	/**
	 * Constructor Method
	 * */
	public TicTacToePanel(Frame frame)
	{
		loadStandardValues();
		createUIComponents(frame);
	}

	/**
	 * The method creates the UI components and creates the Tic Tac Toe Board
	 * The help button and play again button are also managed in this method
	 * @param frame Frame on which the UI components are launched
	 */
	public void createUIComponents(Frame frame)
	{
		JPanel operationsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		operationsPanel.setBackground(Color.WHITE);
		buttonsPanel.setBackground(Color.WHITE);

		GridLayout operationsGridLayout = new GridLayout(4, 1);
		operationsPanel.setLayout(operationsGridLayout);

		JLabel welcomeLabel = new JLabel();
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomeLabel.setText("Welcome to the game of Tic-Tac-Toe!");
		welcomeLabel.setForeground(DARK_BLUE);
		operationsPanel.add(welcomeLabel);

		JPanel questionPanel = new JPanel();
		GridLayout qPanelGridLayoout = new GridLayout(2,1);
		questionPanel.setBackground(Color.WHITE);
		questionPanel.setLayout(qPanelGridLayoout);

		operationsPanel.add(questionPanel);
		
		
		messageLabel.setForeground(DARK_BLUE);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		operationsPanel.add(messageLabel);
		
		JPanel oprButtonsPanel = new JPanel();
		oprButtonsPanel.setBackground(Color.WHITE);
		oprButtonsPanel.add(helpButton);
		oprButtonsPanel.add(playagainButton);
		
		helpButton.setBackground(Color.WHITE);
		helpButton.setForeground(DARK_BLUE);
		helpButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String displayString = "<html>Created by : Saranya Balasubramaniyan <br/> Implemented on : Halloween, 2021"
						+ " <br/><br/> Game Rules: <br/>1. The game is played on a grid that's 3 X 3"
						+ "<br/> 2. The first player gets the mark X and the mark O is assigned to the next. Player and computer"
						+ " take turns putting their marks in empty squares. <br/> 3.The first player to get 3 consecutive "
						+ " marks in a row is the winner. <br/> 4. When all 9 squares are full, the game is over.</html>";
				JDialog helpDialog = new JDialog(frame, "Help Dialog");
				JLabel helpLabel = new JLabel(displayString);
				helpLabel.setBackground(Color.WHITE);
				helpLabel.setForeground(DARK_BLUE);
				helpDialog.setBackground(Color.WHITE);
				helpDialog.add(helpLabel);
				helpDialog.setSize(300, 300);
				helpDialog.setTitle("TIC TAC TOE-Help");
				helpDialog.setLocationRelativeTo(null);
				helpDialog.setVisible(true);
			}
		});
		
		playagainButton.setBackground(Color.WHITE);
		playagainButton.setForeground(DARK_BLUE);
		
		playagainButton.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				enableOrdisableAllStates(true);
				resetButtonState();
				computerPlay = new ArrayList<>();
				playerPlay = new ArrayList<>();
				isComputerFirstPlayer = false;
				messageLabel.setText("");
				yesRadioButton.setEnabled(true);
				yesRadioButton.setSelected(false);
			}
		});
		
		operationsPanel.add(oprButtonsPanel);

		JLabel startFirstLabel = new JLabel();
		startFirstLabel.setHorizontalAlignment(JLabel.CENTER);
		startFirstLabel.setText("Would you like the computer to start? ");
		startFirstLabel.setForeground(DARK_BLUE);
		questionPanel.add(startFirstLabel);

		JPanel yesPanel = new JPanel();
		yesPanel.setBackground(Color.WHITE);
		FlowLayout yesPanelLayout = new FlowLayout();
		yesPanel.setLayout(yesPanelLayout);

		JLabel yesLabel = new JLabel();
		yesLabel.setForeground(DARK_BLUE);
		yesLabel.setText("Yes");
		yesPanel.add(yesLabel);


		yesPanel.add(yesRadioButton);
		yesRadioButton.setBackground(Color.WHITE);
		yesRadioButton.setForeground(DARK_BLUE);
		yesRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{	
				isComputerFirstPlayer = true;
				yesRadioButton.setEnabled(false);
				computersTurn();
			}
		});

		questionPanel.add(yesPanel);

		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		GridLayout panelGridLayout = new GridLayout(2,1);
		this.setLayout(panelGridLayout);

		this.add(operationsPanel);
		this.add(buttonsPanel);

		GridLayout gridLayout = new GridLayout(3,3);
		buttonsPanel.setLayout(gridLayout);

		buttonsPanel.add(button1);
		buttonsPanel.add(button2);
		buttonsPanel.add(button3);
		buttonsPanel.add(button4);
		buttonsPanel.add(button5);
		buttonsPanel.add(button6);
		buttonsPanel.add(button7);
		buttonsPanel.add(button8);
		buttonsPanel.add(button9);

		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		button7.addActionListener(this);
		button8.addActionListener(this);
		button9.addActionListener(this);

		button1.setBackground(Color.WHITE);
		button2.setBackground(Color.WHITE);
		button3.setBackground(Color.WHITE);
		button4.setBackground(Color.WHITE);
		button5.setBackground(Color.WHITE);
		button6.setBackground(Color.WHITE);
		button7.setBackground(Color.WHITE);
		button8.setBackground(Color.WHITE);
		button9.setBackground(Color.WHITE);
	}

	/**
	 * The method loads the standard values for states and the assgins buttons
	 * for the purpose of co ordinating the tic tac toe board with the
	 * backend logic
	 */
	public void loadStandardValues()
	{
		winningCombinations.add(createWinningCombinationList(1, 2, 3));
		winningCombinations.add(createWinningCombinationList(4, 5, 6));
		winningCombinations.add(createWinningCombinationList(7, 8, 9));

		winningCombinations.add(createWinningCombinationList(1, 4, 7));
		winningCombinations.add(createWinningCombinationList(2, 5, 8));
		winningCombinations.add(createWinningCombinationList(3, 6, 9));

		winningCombinations.add(createWinningCombinationList(1, 5, 9));
		winningCombinations.add(createWinningCombinationList(3, 5, 7));	

		statesAndButtonsMap.put(1, button1);
		statesAndButtonsMap.put(2, button2);
		statesAndButtonsMap.put(3, button3);
		statesAndButtonsMap.put(4, button4);
		statesAndButtonsMap.put(5, button5);
		statesAndButtonsMap.put(6, button6);
		statesAndButtonsMap.put(7, button7);
		statesAndButtonsMap.put(8, button8);
		statesAndButtonsMap.put(9, button9);

	}

	/**
	 * The method creates a Arraylist with the numbers passed as input
	 * @param i1 integer to be added to the List
	 * @param i2 integer to be added to the List
	 * @param i3 integer to be added to the List
	 * @return newly created ArrayList
	 */
	public ArrayList<Integer> createWinningCombinationList(int i1, int i2, int i3)
	{
		ArrayList<Integer> combi = new ArrayList<>();
		combi.add(i1);
		combi.add(i2);
		combi.add(i3);
		return combi;
	}

	/**
	 * The method overrides the preferred size of the panel 
	 * to set a dimension of 300 x 500
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(300, 500);
	}

	/**
	 * When the player has completed their turn, this method is evoked for the computer 
	 * to decide on the next move. The method verifies if the players move have already completed the
	 * game to a draw or a win. Then if the game is still to be continued, the logic verifies if there is 
	 * state available such that the computer can win the game. If not, then the logic verifies if there is
	 * a chance such that the player can win in the next move, if not, then the logic verifies if there are 
	 * states that can be filled in so that a winning chance can be increased. If there are no such states, 
	 * then the computer checks for empty slots to mark. If any of these options have more than one state 
	 * that has equal chances of the same option, then the computer randomly chooses the positions to mark.
	 * At the end, a check is run to verify if there is a chance of victory or draw.
	 */
	public void computersTurn()
	{
		int playChoice = 0;
		if(computerPlay.size() == 0 && playerPlay.size() == 0)
		{
			playChoice = generateRandomPlay();	
		}
		else if(playerPlay.size() > 0)
		{
			ArrayList<Integer> emptyStates = getEmptyNeighbours();
			if(emptyStates.size() == 0)
			{
				messageLabel.setForeground(Color.BLACK);
				messageLabel.setText("Game over! Game drawn!!");
			}
			/** winning move choice*/
			if(computerPlay.size() >= 2)
			{
				for(ArrayList<Integer> combiList:winningCombinations)
				{
					ArrayList<Integer> copyList = null;
					int counter = 0;
					for(int state:computerPlay)
					{
						if(combiList.contains(state))
						{
							if(counter == 0)
							{
								copyList = new ArrayList<>(combiList);
							}
							copyList.remove(copyList.indexOf(state));
							counter++;
						}
					}
					if(counter == 2)
					{	
						if(copyList.size() == 1)
						{
							if(computerPlay.contains(copyList.get(0)) || playerPlay.contains(copyList.get(0)))
							{
								playChoice = 0;
							}
							else
							{

								playChoice = copyList.get(0);
								break;
							}
						}
					}
					else
					{
						playChoice = 0;
					}
				}
			}
			/** not letting the player win*/
			if(playChoice == 0 && playerPlay.size() >= 2)
			{
				for(ArrayList<Integer> combiList:winningCombinations)
				{
					ArrayList<Integer> copyList = null;
					int counter = 0;
					for(int state:playerPlay)
					{
						if(combiList.contains(state))
						{
							if(counter == 0)
							{
								copyList = new ArrayList<>(combiList);
							}
							copyList.remove(copyList.indexOf(state));
							counter++;
						}
					}
					if(counter == 2)
					{	
						if(copyList.size() == 1)
						{
							if(computerPlay.contains(copyList.get(0)) || playerPlay.contains(copyList.get(0)))
							{
								playChoice = 0;
							}
							else
							{

								playChoice = copyList.get(0);
								break;
							}
						}
					}
					else
					{
						playChoice = 0;
					}
				}
			}

			if(playChoice == 0)
			{
				ArrayList<Integer> feasibleStates = getFeasibleNeighbours();
				if(feasibleStates.size() > 0)
				{
					int rand = generateRand(feasibleStates.size());
					playChoice = feasibleStates.get(rand);
				}
				else
				{	
					if(emptyStates.size() == 1)
					{
						playChoice = emptyStates.get(0);
					}
					else if(emptyStates.size() > 1)
					{
						int rand = generateRand(emptyStates.size());
						playChoice = emptyStates.get(rand);
					}
				}
			}	
		}
		if(playChoice != 0)
		{	
			JButton buttonPlayed = statesAndButtonsMap.get(playChoice);
			buttonPlayed.setIcon(new ImageIcon(getImageForComputer()));
			buttonPlayed.setEnabled(false);
			computerPlay.add(playChoice);

			boolean victoryStatus = checkVictoryStatus(computerPlay);
			if(victoryStatus)
			{
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("You lost! Better luck next time!");
				enableOrdisableAllStates(false);
				return;
			}
		}
		ArrayList<Integer> emptyStates = getEmptyNeighbours();
		if(emptyStates.size() == 0)
		{
			messageLabel.setForeground(Color.BLACK);
			messageLabel.setText("Game over! Game drawn!!");
		}
	}

	/**
	 * The method enables or disables the game board
	 * @param enable true or false denoting enabling or disabling
	 */
	public void enableOrdisableAllStates(boolean enable)
	{
		button1.setEnabled(enable);
		button2.setEnabled(enable);
		button3.setEnabled(enable);
		button4.setEnabled(enable);
		button5.setEnabled(enable);
		button6.setEnabled(enable);
		button7.setEnabled(enable);
		button8.setEnabled(enable);
		button9.setEnabled(enable);
	}
	
	/**
	 * Resets the tic tac toe board properties to the initial state
	 */
	public void resetButtonState()
	{
		button1.setIcon(null);
		button2.setIcon(null);
		button3.setIcon(null);
		button4.setIcon(null);
		button5.setIcon(null);
		button6.setIcon(null);
		button7.setIcon(null);
		button8.setIcon(null);
		button9.setIcon(null);
	}
	
	/**
	 * When the computer starts the game, the board is not marked at any positions
	 * So, the computer randomly generates a number to start with. This is to ensure 
	 * that the game doesnt start in the same way always and the game is unpredictable
	 * @return Integer that returns the state in which the game is to be started
	 */
	public int generateRandomPlay()
	{
		Random rand = new Random();
		int upperBound = 9;

		int randomNumber = rand.nextInt(upperBound)+1;
		if(!(statesAndButtonsMap.containsKey(randomNumber)))
		{
			randomNumber = generateRandomPlay();
		}

		return randomNumber;
	}

	/**
	 * The method returns the image is used by the computer for the game
	 * @return Image object either containing nugget or cross
	 */
	public Image getImageForComputer()
	{
		try
		{
			if(isComputerFirstPlayer)
			{
				return ImageIO.read(getClass().getResource(crossImg));
			}
			else
			{
				return ImageIO.read(getClass().getResource(nuggetImg));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured. Please check the image location and handling");
			return null;
		}
	}
	
	/**
	 * The method returns the image is used by the player for the game
	 * @return Image object either containing nugget or cross
	 */
	public Image getImageForPlayer()
	{
		try
		{
			if(isComputerFirstPlayer)
			{
				return ImageIO.read(getClass().getResource(nuggetImg));
			}
			else
			{
				return ImageIO.read(getClass().getResource(crossImg));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception occured. Please check the image location and handling");
			return null;
		}
	}

	/**
	 * This method is called when any of the positions in the Tic Tac Toe board is clicked
	 * @param e instance of Action event that contains information on the click action
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		JButton buttonClicked = (JButton)e.getSource();

		if(computerPlay.size() == 0 && playerPlay.size() == 0)
		{
			yesRadioButton.setEnabled(false);
		}

		buttonClicked.setIcon(new ImageIcon(getImageForPlayer()));
		buttonClicked.setEnabled(false);

		int stateSelected = getStateOfButton(buttonClicked);
		playerPlay.add(stateSelected);

		boolean isvictory = checkVictoryStatus(playerPlay);

		if(isvictory)
		{
			messageLabel.setForeground(Color.GREEN);
			messageLabel.setText("Congratulations! You win!!");
			enableOrdisableAllStates(false);
		}
		else
		{
			computersTurn();
		}

	}

	/**
	 * Verifies if any of the combinations in the played states makes up for a winning combination
	 * @param playedStates States that are played by the user or the computer for whom winning is verified
	 * @return true if winning combination is present
	 */
	public boolean checkVictoryStatus(ArrayList<Integer> playedStates)
	{
		boolean victoryStatus = false;
		if(playedStates.size() > 2)
		{
			for(ArrayList<Integer> combiList:winningCombinations)
			{
				int counter = 0;
				for(int state:playedStates)
				{
					if(combiList.contains(state))
					{
						counter++;
					}
				}
				if(counter == 3)
				{
					victoryStatus = true;
					break;
				}
			}
		}

		return victoryStatus;
	}

	/**
	 * The method returns the state of the button passed as the input argument. State is the position of the button
	 * @param button Button for which the position has to be returned
	 * @return Integer denoting the position of the button
	 */
	public Integer getStateOfButton(JButton button)
	{
		Integer buttonState = 0;

		Set<Integer> keySet = statesAndButtonsMap.keySet();
		Iterator<Integer> keyIterator = keySet.iterator();

		while(keyIterator.hasNext())
		{
			int state = keyIterator.next();
			JButton buttonIns = statesAndButtonsMap.get(state);

			if(buttonIns.equals(button))
			{
				buttonState = state;
				break;
			}
		}
		return buttonState;
	}

	/**
	 * The method returns the list of unmarked states that are increases the chance of the computer to win the game
	 * @return List of Integers denoting the states that are unmarked and are more feasible for computer to win
	 */
	public ArrayList<Integer> getFeasibleNeighbours()
	{
		ArrayList<Integer> feasibleNeighbours = new ArrayList<>();

		ArrayList<Integer> emptyNeighbours = getEmptyNeighbours();

		for(ArrayList<Integer> combiList:winningCombinations)
		{
			for(int playedState:computerPlay)
			{
				if(combiList.contains(playedState))
				{
					ArrayList<Integer> copyList = new ArrayList<>(combiList);
					copyList.remove(copyList.indexOf(playedState));

					/**The use case where the 2 elements in a winning combi is with the computer is already handled. Hence not checking again */
					if(!(playerPlay.contains(copyList.get(0)) || playerPlay.contains(copyList.get(1))))
					{
						if(emptyNeighbours.contains(copyList.get(0)) && emptyNeighbours.contains(copyList.get(1)))
						{
							feasibleNeighbours.add(copyList.get(0));
							feasibleNeighbours.add(copyList.get(1));
						}
					}
				}
			}
		}
		return feasibleNeighbours;
	}

	/**
	 * The method returns the slots that are unmarked by the player or the computer at any point of time
	 * @return List of integers denoting the states what are not yet marked
	 */
	public ArrayList<Integer> getEmptyNeighbours()
	{
		ArrayList<Integer> emptyNeighbour = new ArrayList<>(statesAndButtonsMap.keySet());

		if(emptyNeighbour.size() > 0)
		{
			if(computerPlay != null && computerPlay.size() > 0)
			{
				for(int playedState:computerPlay)
				{
					if(emptyNeighbour.contains(playedState))
					{
						emptyNeighbour.remove(emptyNeighbour.indexOf(playedState));
					}
				}
			}
			if(playerPlay != null && playerPlay.size() > 0)
			{
				for(int playedState:playerPlay)
				{
					if(emptyNeighbour.contains(playedState))
					{
						emptyNeighbour.remove(emptyNeighbour.indexOf(playedState));
					}
				}
			}
		}
		return emptyNeighbour;
	}

	/**
	 * The method generates a random number between 0 and the upper bound provided
	 * @param upperBound maximum number below which a random number has to be generated
	 * @return generated random number
	 */
	private int generateRand(int upperBound)
	{
		Random rand = new Random();

		int randomNumber = rand.nextInt(upperBound);

		return randomNumber;
	}
}
