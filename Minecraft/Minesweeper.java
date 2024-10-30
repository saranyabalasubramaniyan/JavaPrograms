/*************************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  Student ID : 999901316
 *							  Course Name : 5103 - Advanced Programming Concepts
 *							  Section Number : 032
 *							  Class Project
 *
 * The program has the implementation of the game : Minesweeper. As a part of this implementation,
 * a Java applet will be launched based on the frame of the User interface. The user will be able to
 * choose the level of the game. There will be 3 buttons to play, end the game and get help. Play 
 * will create the game play with the chosen game level. End button ends the game at any point of time,
 * help button displays information on how to play the game and the meanings of the game icons.
 * A timer is running on the top of the board when the user selects play. A left click will uncover the
 * clicked block, a right click will mark the block as a mine. a left click on a block with mine will 
 * explode the mine and end the game. If all the mines are marked correctly,the player wins and the game ends
 **************************************************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class Minesweeper 
{
	/**
	 * Entry point of the program that launches a user interface
	 * The minesweeper options and boards are created based on this window
	 * @param args the argument is not used
	 */
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Minesweeper");
		frame.setResizable(false);
		frame.add(new MinesweeperPanel(frame));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}
}	

class GameGridLayout extends GridLayout
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor method, reads the number of rows and columns
	 * @param rows number of rows to be created in the layout
	 * @param cols number of columns to be created in the layout
	 */
	public GameGridLayout(int rows, int cols) 
	{
		super(rows, cols);
	}

	/**
	 * The method ensures that the width and height of the column are same
	 * so as to have a square block in the minesweeper game board
	 * @param parent The parent container
	 */
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			Insets insets = parent.getInsets();
			int componentsCount = parent.getComponentCount();
			int nrows = getRows();
			int ncols = getColumns();
			boolean ltr = parent.getComponentOrientation().isLeftToRight();

			if (componentsCount == 0)
			{
				return;
			}
			if (nrows > 0) 
			{
				ncols = (componentsCount + nrows - 1) / nrows;
			} 
			else 
			{
				nrows = (componentsCount + ncols - 1) / ncols;
			}

			int totalGapsWidth = (ncols - 1) * getHgap();
			int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
			int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
			int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;

			int totalGapsHeight = (nrows - 1) * getVgap();
			int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
			int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
			int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;

			int size=Math.min(widthOnComponent, heightOnComponent);
			widthOnComponent=size;
			heightOnComponent=size;
			if (ltr)
			{
				for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols ; c++, x += widthOnComponent + getHgap()) 
				{
					for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + getVgap()) 
					{
						int i = r * ncols + c;
						if (i < componentsCount)
						{
							parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
						}
					}
				}
			} 
			else 
			{
				for (int c = 0, x = (parent.getWidth() - insets.right - widthOnComponent) - extraWidthAvailable; c < ncols ; c++, x -= widthOnComponent + getHgap()) 
				{
					for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + getVgap())
					{
						int i = r * ncols + c;
						if (i < componentsCount) 
						{
							parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
						}
					}
				}
			}
		}
	}
}

class MinesweeperPanel extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	public static final Color DARK_BLUE = new Color(0,0,153);
	public static final Color RED = new Color(220,20,60);
	public static final Color GREEN = new Color(50,205,50);
	public static final String BEGINNER = "Beginner";
	public static final String INTERMEDIATE = "Intermediate";
	public static final String ADVANCED = "Advanced";
	public static final String MINES = "Mines";
	public static final String ROWCOUNT = "RowCount";
	public static final String COLUMNCOUNT = "ColumnCount";
	public static final String COMMA = ",";

	HashMap<String, HashMap<String, Integer>> levelData = new HashMap<>();
	HashMap<JButton, String> componentPositionMap = new HashMap<>();
	HashMap<String, Integer> positionDetailsMap = new HashMap<>();
	ArrayList<String> mineLocations = new ArrayList<>();
	ArrayList<String> markedAsMines = new ArrayList<>();
	ArrayList<String> alreadyHintedPositions = new ArrayList<>();
	int rowLength = 0;
	int columnLength = 0;
	int mineCount = 0;
	boolean isRightClick = false;

	JRadioButton beginnerRB = new JRadioButton("Beginner", true);
	JRadioButton intermediateRB = new JRadioButton("Advanced");
	JRadioButton advancedRB = new JRadioButton("Expert");
	JButton playButton = new JButton("Play!");
	JButton endButton = new JButton("End Game");
	JButton helpButton = new JButton("Help");

	JPanel timerPanel = new JPanel();
	JLabel timerLabel = new JLabel();
	JPanel boardPanel = new JPanel();
	JLabel gameResultLabel = new JLabel();

	ActionListener actListner = new ActionListener() {

		/**
		 * This method is called when the timer is running
		 * When the timer reaches 00:00, then it indicates
		 * that the timer runs out and that the game play stops 
		 * and the program finishes the game bursting out
		 * all the mines
		 * @param action event that corresponds to the timer change
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			String currentTime = timerLabel.getText();
			if(currentTime.equals("00:00"))
			{
				timer.stop();

				finishGame("Time is up! Better luck next time!", RED);
			}
			else
			{
				String[] timeArr = currentTime.split(":");
				int minutes = Integer.parseInt(timeArr[0]);
				int seconds = Integer.parseInt(timeArr[1]);

				if(seconds > 0)
				{
					seconds--;
				}
				else
				{
					seconds = 59;
					minutes--;
				}

				String minStr = "0" + Integer.toString(minutes);
				String secStr = Integer.toString(seconds);

				if(secStr.length() == 1)
				{
					secStr = "0" + Integer.toString(seconds);
				}

				timerLabel.setText(minStr + ":" + secStr);
			}  		 
		}
	};

	Timer timer = new Timer(1000, actListner);


	/**
	 * Constructor - creates the user interface for the minesweeper game
	 * @param frame instance of the base container on which the panel is built
	 */
	public MinesweeperPanel(JFrame frame) 
	{
		loadGameData();
		createUIComponents(frame);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	/**
	 * The method updates the levelData map with the game configurations
	 * for number of mines and board size
	 */
	public void loadGameData()
	{
		levelData.put(BEGINNER, createInternalMap(10,7,9));
		levelData.put(INTERMEDIATE, createInternalMap(35, 13, 18));
		levelData.put(ADVANCED, createInternalMap(91, 22, 25));
	}

	/**
	 * The method creates a map with the number of mines, row number and column number
	 * @param mineCount Number of mines
	 * @param rowCount number of rows
	 * @param columnCount number of columns
	 * @return a map containing the mines, row count and column count inforamtion
	 */
	public HashMap< String, Integer> createInternalMap(int mineCount, int rowCount, int columnCount)
	{
		HashMap<String, Integer> intrlMap = new HashMap<>();
		intrlMap.put(MINES, mineCount);
		intrlMap.put(ROWCOUNT, rowCount);
		intrlMap.put(COLUMNCOUNT, columnCount);
		return intrlMap;
	}

	/**
	 * The method overrides the preferred size of the panel 
	 * to set a dimension of 500 x 670
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(500, 670);
	}

	/**
	 * The method creates the user interface for the game. All the game related setting and 
	 * controls are placed in the operations panel on the top. The game panel contains the game board 
	 * and the timer. The operation panel contains the Radio button group to select the game level,
	 * a play button, help button and end button. The game result is also displayed in a label.
	 * @param frame Frame based on which the help dialog will be launched
	 */
	private void createUIComponents(JFrame frame)
	{
		BoxLayout mainGridLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(mainGridLayout);
		this.setBackground(Color.white);
		JPanel operationsPanel = new JPanel();
		operationsPanel.setBackground(Color.white);

		BoxLayout oprPanelLayout = new BoxLayout(operationsPanel,BoxLayout.Y_AXIS);
		operationsPanel.setLayout(oprPanelLayout);
		JPanel gamePanel = new JPanel();
		BoxLayout gamePanelLayout = new BoxLayout(gamePanel, BoxLayout.Y_AXIS);
		gamePanel.setLayout(gamePanelLayout);
		gamePanel.setBackground(Color.white);

		JLabel welcomeLabel = new JLabel();
		welcomeLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		welcomeLabel.setText("Welcome to the game of Minesweeper!");
		welcomeLabel.setForeground(DARK_BLUE);
		welcomeLabel.setBackground(Color.white);
		operationsPanel.add(welcomeLabel);

		beginnerRB.setForeground(DARK_BLUE);
		beginnerRB.setBackground(Color.white);
		intermediateRB.setForeground(DARK_BLUE);
		intermediateRB.setBackground(Color.white);
		advancedRB.setForeground(DARK_BLUE);
		advancedRB.setBackground(Color.white);

		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(beginnerRB);
		bgroup.add(intermediateRB);
		bgroup.add(advancedRB);

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		radioPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		radioPanel.add(beginnerRB);
		radioPanel.add(intermediateRB);
		radioPanel.add(advancedRB);

		radioPanel.setBackground(Color.WHITE);
		radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Game Level"));

		operationsPanel.add(radioPanel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setMaximumSize(new Dimension(500, 40));
		buttonsPanel.setBackground(Color.white);

		playButton.addActionListener(new ActionListener()
		{
			/**
			 * When the play button is clicked, the game board is created based on
			 * the level chosen by the user. The timer is initiated and the mines
			 * are placed in the respective location
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				reset();

				String targetTime = "";
				ImageIcon icon = null;
				if(beginnerRB.isSelected())
				{
					mineCount = 10;
					targetTime = "01:00";
					rowLength = levelData.get(BEGINNER).get(ROWCOUNT);
					columnLength = levelData.get(BEGINNER).get(COLUMNCOUNT);
					icon = new ImageIcon(getClass().getClassLoader().getResource("resources/blank60x60.png"));
				}
				else if(intermediateRB.isSelected())
				{
					mineCount = 35;
					targetTime = "03:00";
					rowLength = levelData.get(INTERMEDIATE).get(ROWCOUNT);
					columnLength = levelData.get(INTERMEDIATE).get(COLUMNCOUNT);
					icon = new ImageIcon(getClass().getClassLoader().getResource("resources/blank32x32.png"));
				}
				else if(advancedRB.isSelected())
				{
					mineCount = 91;
					targetTime = "10:00";
					rowLength = levelData.get(ADVANCED).get(ROWCOUNT);
					columnLength = levelData.get(ADVANCED).get(COLUMNCOUNT);
					icon = new ImageIcon(getClass().getClassLoader().getResource("resources/blank24x24.png"));
				}
				else
				{
					System.out.println("Error in the execution");
					return;
				}
				timerPanel.add(timerLabel);
				createBoard(boardPanel,rowLength ,columnLength, icon );
				placeMines();
				timerLabel.setText(targetTime);
				enableGameControls(false);
				timer.start();
			}
		});
		endButton.addActionListener(new ActionListener() 
		{	
			/**
			 * When the finish button is clicked, the game is ended
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				finishGame("Game over!!!", RED);
			}
		});

		helpButton.addActionListener(new ActionListener() {

			/**
			 * When the help button is clicked, a dialog pops up with information on how to play the game
			 * and the game semantics. The information on the different images in the game are also explained
			 */
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String displayString = "<html>Created by : Saranya Balasubramaniyan <br/> Implemented on : Nov 14, 2021"
						+ " <br/><br/> The Game :<br/>Well, the game is all about finding the mines! <br/>* The Beginner board has 10 mines,"
						+ " the Advanced board has 35 mines and the Expert board has 91 mines. <br/>* Beginner level has a time limit of 1 minute, "
						+ "3 minutes for Advanced and 10 minutes for expert level. <br/>* If all the mines are marked correctly with the time limit"
						+ "the game ends and you win the game.<br/>* If the game ends before the time runs out, the game ends and you lose the game.<br/>"
						+ "* If by any chance you hit a mine, the game ends and you lose the game! <br/> <br/>"
						+ " How to play the game: <br/> * Left click uncovers the location, if there are any mines nearby, the number of mines "
						+ "nearby will be displayed in the block.<br/> * A second click on the uncovered location will uncover the adjacent nearby non mine"
						+ "location.<br/> * Right click on a location will flag the location as a mine.<br/> * A second right click on location will "
						+ "remove the marking as mine. <br/>* A left click on the mine will burst the mine and end the game! </html>";
				JDialog helpDialog = new JDialog(frame, "Help Dialog");
				helpDialog.setBackground(Color.white);
				helpDialog.setForeground(Color.white);
				JLabel helpLabel = new JLabel(displayString);
				helpLabel.setBackground(Color.white);
				helpDialog.setLayout(new GridLayout(1, 2));

				ImageIcon iconHelp = new ImageIcon(getClass().getClassLoader().getResource("resources/iconhelp.PNG"));
				helpLabel.setBackground(Color.WHITE);
				helpLabel.setForeground(DARK_BLUE);
				helpDialog.setBackground(Color.WHITE);
				helpDialog.add(helpLabel);
				JLabel imageLabel = new JLabel(iconHelp);
				imageLabel.setBackground(Color.white);
				helpDialog.add(imageLabel);
				helpDialog.setSize(1300, 600);
				helpDialog.setTitle("Minesweeper-Help");
				helpDialog.setLocationRelativeTo(null);
				helpDialog.setVisible(true);
			}
		});
		buttonsPanel.add(playButton);
		buttonsPanel.add(endButton);
		buttonsPanel.add(helpButton);

		endButton.setEnabled(false);
		operationsPanel.add(buttonsPanel);
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBackground(Color.white);
		resultsPanel.add(gameResultLabel);

		timerLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		timerPanel.setBackground(Color.white);

		gamePanel.add(timerPanel);

		boardPanel.setSize(new Dimension(400,400));

		boardPanel.setBackground(Color.white);
		gamePanel.add(boardPanel);
		operationsPanel.add(resultsPanel);

		gameResultLabel.setBackground(Color.white);

		this.add(operationsPanel);
		this.add(gamePanel);
	}

	/**
	 * This mehod places mines the game board based on the 
	 * game level chosen by the player. The mine locations 
	 * are then stored in a list to be referred during the 
	 * game play
	 */
	public void placeMines()
	{
		while(mineLocations.size() < mineCount)
		{
			int rowPosition = generateRand(rowLength);
			int colPosition = generateRand(columnLength);
			String locationString = rowPosition +COMMA+colPosition;
			if(!(mineLocations.contains(locationString)))
			{
				mineLocations.add(locationString);
				positionDetailsMap.put(locationString, -1);			
			}
		}	

		for(String mineSpot:mineLocations)
		{
			String[] posArray = mineSpot.split(COMMA);
			int rowNumber = Integer.parseInt(posArray[0]);
			int colNumber = Integer.parseInt(posArray[1]);

			ArrayList<String> mineAdjacents = new ArrayList<>();
			String posString = (rowNumber-1)+COMMA+(colNumber-1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}
			posString = (rowNumber-1)+COMMA+(colNumber);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}
			posString = (rowNumber-1)+COMMA+(colNumber+1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}

			posString = (rowNumber)+COMMA+(colNumber-1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}
			posString = (rowNumber)+COMMA+(colNumber+1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}

			posString = (rowNumber+1)+COMMA+(colNumber-1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}
			posString = (rowNumber+1)+COMMA+(colNumber);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}
			posString = (rowNumber+1)+COMMA+(colNumber+1);
			if(componentPositionMap.containsValue(posString))
			{
				mineAdjacents.add(posString);
			}

			if(mineAdjacents.size() > 0)
			{
				for(String adjSpot:mineAdjacents)
				{
					if(positionDetailsMap.containsKey(adjSpot) && !(positionDetailsMap.get(adjSpot).equals(-1)))
					{
						positionDetailsMap.put(adjSpot, positionDetailsMap.get(adjSpot)+1);
					}
					else
					{
						if(!(positionDetailsMap.containsKey(adjSpot)))
						{
							positionDetailsMap.put(adjSpot, 1);
						}
					}
				}
			}
		}
	}

	/**
	 * This method creates the actual components that are displayed as a part of the game play
	 * The created board is then added to the game panel in the user interface
	 * @param boardPanel panel to which the created components are to be added
	 * @param numOfRows number of rows to be created in the game
	 * @param numOfColumns number of columns to be created in the game
	 * @param icon icon to be used in the game board
	 */
	public void createBoard(JPanel boardPanel, int numOfRows, int numOfColumns, ImageIcon icon)
	{
		GameGridLayout boardLayout = new GameGridLayout(numOfRows,numOfColumns);
		boardPanel.setLayout(boardLayout);
		int ct = 0;
		int totButtons = numOfRows * numOfColumns;
		int rowNum = 0;
		int colNum = 0;
		while(ct < totButtons)
		{
			JButton butn = new JButton();
			butn.setIcon(icon);
			butn.addMouseListener(this);
			JPanel butnPanel = new JPanel();
			GridLayout butnPanelLayout = new GridLayout(1,1);
			butnPanel.setLayout(butnPanelLayout);
			butnPanel.add(butn);
			boardPanel.add(butn);

			componentPositionMap.put(butn, rowNum+ COMMA +colNum);
			colNum++;
			if(colNum == numOfColumns)
			{
				colNum = 0;
				rowNum++;
			}
			ct++;
		}
	}

	ArrayList<JButton> unmarkedMineLocations = new ArrayList<>();
	int mineTime = 1;
	ActionListener timerListner = new ActionListener() {

		/**
		 * This action handler corresponds to the timer that is used for 
		 * mine blast animation
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(mineTime > 0)
			{
				mineTime--;
			}
			else
			{
				minetimer.stop();
				if(unmarkedMineLocations.size() > 0)
				{
					for(JButton unFoundMine:unmarkedMineLocations)
					{
						unFoundMine.setEnabled(false);
						unFoundMine.setBackground(RED);
					}
				}
				enableGameControls(true);

				Set<JButton> keyButtons = componentPositionMap.keySet();
				Iterator<JButton> iter = keyButtons.iterator();

				while(iter.hasNext())
				{
					JButton button = iter.next();
					button.setEnabled(false);
					if(markedAsMines.contains(componentPositionMap.get(button)))
					{
						if(mineLocations.contains(componentPositionMap.get(button)))
						{
							if(beginnerRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine60x60.png")));
							}
							else if(intermediateRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine32x32.png")));
							}
							else if(advancedRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine24x24.png")));
							}
							button.setBackground(GREEN);
						}
						else
						{
							if(beginnerRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/NotAMine60x60.png")));
							}
							else if(intermediateRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/NotAMine32x32.png")));
							}
							else if(advancedRB.isSelected())
							{
								button.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/NotAMine24x24.png")));
							}
						}
					}
				}	
				//playAudio("sounds/gameOver.wav");
			}
		}
	};
	Timer minetimer = new Timer(500, timerListner);
	
	/**
	 * The method is invoked when the user clicks on the game board locations
	 * @param e mouse event that contains the information on the click of the mouse
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(!isRightClick)
		{
			playAudio("sounds/click.wav");
			JButton clickedButton = (JButton) e.getComponent();
			String position = componentPositionMap.get(clickedButton);
			if(mineLocations.contains(position))
			{

				finishGame("Oops!! You hit a mine! Better luck next time!", RED);
			}
			else
			{
				if(positionDetailsMap.containsKey(position))
				{
					int data = positionDetailsMap.get(position);
					if(alreadyHintedPositions.contains(position))
					{
						uncoverNonMineNeighbours(position);
					}
					else
					{
						alreadyHintedPositions.add(position);
						addMineHint(data,clickedButton);
					}
				}
				else
				{
					clickedButton.setEnabled(false);
					clickedButton.setIcon(null);

				}
			}	
		}
		else
		{
			isRightClick = false;
		}
	}

	/**
	 * This method is called when the mouse is pressed. However, this method
	 * is able to distinguish if a right click or a left click is performed
	 * If a right click is performed, the location is marked or unmarked as mine
	 * @param e mouse event that contains the information on the click of the mouse
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
		{
			isRightClick = true;
			markOrUnMarkAsMine(e.getComponent());
		}	
	}

	/**
	 * This method is called when the mouse is pressed. However, this method
	 * is able to distinguish if a right click or a left click is performed
	 * If a right click is performed, the location is marked or unmarked as mine
	 * @param e mouse event that contains the information on the click of the mouse
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
		{
			isRightClick = true;
			markOrUnMarkAsMine(e.getComponent());
		}	
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{

	}

	/**
	 * If the button passed as the input argument is already marked as mine,
	 * then the mine is removed. If the location is empty, then the location 
	 * is marked as mine
	 * @param comp Button on which the mark or unmark as mine is performed
	 */
	public void markOrUnMarkAsMine(Component comp)
	{
		JButton clickedButton =(JButton) comp;
		if(markedAsMines.contains(componentPositionMap.get(clickedButton)))
		{
			markedAsMines.remove(componentPositionMap.get(clickedButton));
			addBlankIcon(clickedButton);
			playAudio("sounds/unflag.wav");
		}
		else
		{
			markedAsMines.add(componentPositionMap.get(clickedButton));
			addMarkedAsMineIcon(clickedButton);
			playAudio("sounds/flag.wav");
		}

		boolean isVictory = checkVictoryStatus();
		if(isVictory)
		{
			finishGame("Congratulations! You win!!", GREEN);
		}
	}

	/**
	 * The method verfies if the user has marked all the mines correctly
	 * if yes then true is returned, else false is returned
	 * @return true if the stauts is win , false otherwise
	 */
	public boolean checkVictoryStatus()
	{
		boolean isVictory = false;
		if(markedAsMines.size() != mineCount)
		{
			return isVictory;
		}
		if(markedAsMines.containsAll(mineLocations))
		{
			isVictory = true;
		}
		return isVictory;
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

	/**
	 * The method adds marked as mine icon to the button passed as input. The method sets both icon in the enabled and
	 * disabled state 
	 * @param clickedButton button on which the icon has to be implemented
	 */
	private void addMarkedAsMineIcon(JButton clickedButton)
	{
		if(beginnerRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked60x60.jpg")));
			clickedButton.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked60x60.jpg")));
		}
		else if(intermediateRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked32x32.jpg")));
			clickedButton.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked32x32.jpg")));
		}
		else if(advancedRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked24x24.jpg")));
			clickedButton.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mineMarked24x24.jpg")));
		}
	}

	/**
	 * The method adds blank icon to the button passed as input
	 * @param clickedButton button on which the icon has to be implemented
	 */
	private void addBlankIcon(JButton clickedButton)
	{
		if(beginnerRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/blank60x60.png")));
		}
		else if(intermediateRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/blank32x32.png")));
		}
		else if(advancedRB.isSelected())
		{
			clickedButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/blank24x24.png")));
		}
	}

	/**
	 * The method adds icons with numbers to the locations adjacent to the block containing the mine. The number represents
	 * the number of mines suprrounding the location
	 * @param data the number of mines surrounding the location
	 * @param clickedButton button on which the icon has to be implemented
	 */
	private void addMineHint(int data, JButton clickedButton)
	{
		String resourcePath = "resources/" + data + "_";

		if(beginnerRB.isSelected())
		{
			resourcePath = resourcePath + "60x60.png";	
		}
		else if(intermediateRB.isSelected())
		{
			resourcePath = resourcePath + "32x32.png";
		}
		else if(advancedRB.isSelected())
		{
			resourcePath = resourcePath + "24x24.png";
		}

		ImageIcon icon = null;
		try
		{
			icon = new ImageIcon(getClass().getClassLoader().getResource(resourcePath));
		}
		catch(Exception ex)
		{
			System.out.println(resourcePath);
			ex.printStackTrace();
		}
		clickedButton.setIcon(icon);
		clickedButton.setDisabledIcon(icon);
	}

	/**
	 * The method enables or disables the game controls
	 * @param enable true to enable the game controls, false to disable
	 */
	private void enableGameControls(boolean enable)
	{
		beginnerRB.setEnabled(enable);
		advancedRB.setEnabled(enable);
		intermediateRB.setEnabled(enable);
		playButton.setEnabled(enable);
		endButton.setEnabled(!enable);
	}

	/**
	 * The method finishes the game if the player clicks on the end button or if the player ran out of time
	 * or if the player hit a mine. The method displays the message to the user accordingly.
	 * Then the unmarked mine locations burst to display with a audio to indicate the mine locations.
	 * The ones that are correctly marked are displayed in green.
	 * @param textToDisplay Message to the user
	 * @param displayColor color of the the text to be displayed
	 */
	private void finishGame(String textToDisplay, Color displayColor)
	{
		timer.stop();
		playButton.setEnabled(true);
		gameResultLabel.setText(textToDisplay);
		gameResultLabel.setForeground(displayColor);
		gameResultLabel.repaint();
		for(String mineSpot:mineLocations)
		{
			if(!(markedAsMines.contains(mineSpot)))
			{

				Set<JButton> buttonsSet = componentPositionMap.keySet();
				Iterator<JButton> buttonsIter = buttonsSet.iterator();
				while(buttonsIter.hasNext())
				{
					JButton buttonInstance = buttonsIter.next();
					if(componentPositionMap.get(buttonInstance).equals(mineSpot))
					{
						if(!(unmarkedMineLocations.contains(buttonInstance)))
						{
							unmarkedMineLocations.add(buttonInstance);
							break;
						}
					}
				}
			}
		}
		if(unmarkedMineLocations.size() > 0)
		{
			playAudio("sounds/bomb.wav");
			for(JButton unFoundMine:unmarkedMineLocations)
			{
				if(beginnerRB.isSelected())
				{
					unFoundMine.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/boom60x60.gif")));
					unFoundMine.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine60x60.png")));
				}
				else if(intermediateRB.isSelected())
				{
					unFoundMine.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/boom32x32.gif")));
					unFoundMine.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine32x32.png")));
				}
				else if(advancedRB.isSelected())
				{
					unFoundMine.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/boom24x24.gif")));
					unFoundMine.setDisabledIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/mine24x24.png")));
				}			
			}

		}
		minetimer.start();
		/**/

	}

	/**
	 * The method resets the game controls and the game storage
	 */
	private void reset()
	{
		boardPanel.removeAll();
		componentPositionMap.clear();
		positionDetailsMap.clear();
		mineLocations.clear();
		markedAsMines.clear();
		gameResultLabel.setText("");
		alreadyHintedPositions.clear();
	}

	/**
	 * The method plays the sound when invoked. The location of the audio file has to be passed as the input
	 * @param audioLocation Location of the audio file
	 */
	private void playAudio(String audioLocation)
	{
		try
		{
			URL audiourl = getClass().getClassLoader().getResource(audioLocation);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audiourl);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();			
		} 
		catch (LineUnavailableException e)
		{
			System.out.println("Sound format not supported");
		}
		catch (UnsupportedAudioFileException e) 
		{
			System.out.println("Sound format not supported");
		} 
		catch (IOException e)
		{
			System.out.println("Sound file not found");
		}
	}

	/**
	 * The method uncovers the neighbouring position of the input passed as input
	 * @param position The position whose adjacent cells are to be uncovered
	 */
	private void uncoverNonMineNeighbours(String position)
	{
		String[] posArray = position.split(COMMA);
		int rowNumber = Integer.parseInt(posArray[0]);
		int colNumber = Integer.parseInt(posArray[1]);

		uncoverPosition((rowNumber-1)+COMMA+(colNumber-1));
		uncoverPosition((rowNumber-1)+COMMA+(colNumber));
		uncoverPosition((rowNumber-1)+COMMA+(colNumber+1));
		uncoverPosition((rowNumber)+COMMA+(colNumber-1));
		uncoverPosition((rowNumber)+COMMA+(colNumber+1));
		uncoverPosition((rowNumber+1)+COMMA+(colNumber-1));
		uncoverPosition((rowNumber+1)+COMMA+(colNumber));
		uncoverPosition((rowNumber+1)+COMMA+(colNumber+1));

	}

	/**
	 * The method uncovers and displays the information on the cells
	 * @param position position of the cell whose adjacent cells are to be uncovered
	 */
	private void uncoverPosition(String position)
	{
		if(componentPositionMap.containsValue(position))
		{
			if(!(mineLocations.contains(position)) && !(markedAsMines.contains(position)))
			{
				if(!(alreadyHintedPositions.contains(position)))
				{
					JButton button = null;
					Set<JButton> keyButtons = componentPositionMap.keySet();
					Iterator<JButton> iter = keyButtons.iterator();

					while(iter.hasNext())
					{
						button = iter.next();
						if(componentPositionMap.get(button).equals(position))
						{
							break;
						}
					} 
					if(positionDetailsMap.containsKey(position))
					{
						int data = positionDetailsMap.get(position);
						alreadyHintedPositions.add(position);
						addMineHint(data,button);
					}
					else
					{
						button.setEnabled(false);
						button.setIcon(null);
					}
				}
			}
		}
	}
}


