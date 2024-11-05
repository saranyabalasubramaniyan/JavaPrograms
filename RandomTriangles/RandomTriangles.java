/*************************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							 
 *
 *The program creates 500 triangles at randomly generated points with random combination 
 *of red, blue and green color points
 **************************************************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RandomTriangles 
{
	/**
	 * Entry method of the program. Creates a frame in which the randomly created triangles are 
	 * generated.
	 * @param args not used in this method context
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Generating Random Triangles...");
		frame.add(new DisplayPanel());
		frame.pack();
		frame.setVisible(true);
	}
}

@SuppressWarnings("serial")
class DisplayPanel extends JPanel
{
	int[] x1Array = new int[500];
	int[] x2Array = new int[500];
	int[] x3Array = new int[500];
	
	int[] y1Array = new int[500];
	int[] y2Array = new int[500];
	int[] y3Array = new int[500];
	
	int[] rArray = new int[500];
	int[] gArray = new int[500];
	int[] bArray = new int[500];
	
	/**
	 * Constructor Method
	 * Generates the Random number points 
	 * for creation of triangles and the color
	 */
	public DisplayPanel()
	{
		for(int inx = 0; inx < 500; inx++)
		{
			x1Array[inx] = generateXRand();
			x2Array[inx] = generateXRand();
			x3Array[inx] = generateXRand();
			
			y1Array[inx] = generateYRand();
			y2Array[inx] = generateYRand();
			y3Array[inx] = generateYRand();
			
			rArray[inx] = generateColorRand();
			gArray[inx] = generateColorRand();
			bArray[inx] = generateColorRand();
		}
	}
	/**
	 * The method overrides the preferred size of the panel 
	 * to set a dimension of 1100 x 600
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(1100, 600);
	}

	/**
	 * The method overrides the paintComponent method 
	 * A loop is executed 500 times to create polygon with 3 points making a triangle
	 * The random numbers generated in the constructor is used for x, y and z points
	 * then color points are taking from the random generated numbers to fill the 
	 * triangle with color.
	 * Opacity of the triangle is reduced so that other triangles are visible from the background
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g.create();

		int counter = 500;
		while(counter > 0)
		{
			counter--;
			Polygon triangle = new Polygon(new int[] { x1Array[counter], x2Array[counter], x3Array[counter] }, new int[] { y1Array[counter], y2Array[counter], y3Array[counter] }, 3);
			Color triangleColor = new Color(rArray[counter], gArray[counter], bArray[counter], 210);
			graphics2d.setColor(triangleColor);
			graphics2d.fill(triangle);		
		}

		graphics2d.dispose();
	}
	
	/**
	 * A random number is generated which would be less than 1100
	 * @return A random integer that is generated in this function
	 */
	private int generateXRand()
	{
		Random rand = new Random();
		int upperBound = 1100;

		int randomNumber = rand.nextInt(upperBound);

		return randomNumber;
	}

	/**
	 * A random number is generated which would be less than 600
	 * @return A random integer that is generated in this function
	 */
	private int generateYRand()
	{
		Random rand = new Random();
		int upperBound = 600;

		int randomNumber = rand.nextInt(upperBound);

		return randomNumber;
	}

	/**
	 * A random number is generated which would be less than 256
	 * @return A random integer that is generated in this function
	 */
	private int generateColorRand()
	{
		Random rand = new Random();
		int upperBound = 256;

		int randomNumber = rand.nextInt(upperBound);

		return randomNumber;
	}
}