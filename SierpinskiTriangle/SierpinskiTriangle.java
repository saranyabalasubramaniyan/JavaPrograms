/*************************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 *
 * The Sierpinski triangle is a self-similar fractal. It consists of an equilateral triangle,
 * with smaller equilateral triangles recursively removed from its remaining area.
 * It can be created by starting with one large, equilateral triangle, and then repeatedly
 * cutting smaller triangles out of its center.
 * 
 * The program creates a JFrame and creates a Sierpinski Triangle in it. The Dimensions of 
 * the frame is set to 1024 x 768. Equilateral triangles are then created to fill the space
 * of the largest triangle created.
 **************************************************************************************/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SierpinskiTriangle 
{
	/**
	 * Entry point for the program. The method creates the frame 
	 * that is launched and JPanel is embedded in the JFrame to be
	 * launched when the program is launched
	 * @param args empty parameters, not used
	 */
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Generating Sierpinski Triangle...");
		frame.add(new SierpinskiPanel());
		frame.pack();
		frame.setVisible(true);
	}
}
	@SuppressWarnings("serial")
	class SierpinskiPanel extends JPanel
	{
		int x1 = 512;
		int x2 = 146;
		int x3 = 876;
		int y1 = 109;
		int y2 = 654;
		int y3 = 654;
		
		int x = 512;
		int y = 382;

		/**
		 * Constructor for the SierpinskiPanel class
		 * Sets the auto scrolling option to true in case
		 * the resolution of the system in which the 
		 * program is executed is different.
		 */
		public SierpinskiPanel()
		{
			this.setAutoscrolls(true);
		}
		/**
		 * The Preferred size of the JPanel is set in this method
		 * @return Dimension object to be set for the JPanel
		 */
		@Override
		public Dimension getPreferredSize()
		{
			/**Setting the canvas size to 1024 x 768*/
			return new Dimension(1024, 768);
		}

		/**
		 * The method overrides the default paintComponent of the JPanel
		 * Sierpinski Triangle is then created with the drawline method 
		 * Algorithm: drawline on the JPanel graphics is called with the x and y values
		 * Then the new current point x and y are set by calculating x = x - dx/2 and y = y - dy/2
		 * This calculation and drawline is then run in a loop for 50000 times
		 * The string 'Sierpinski Triangle' is then printed in the points 462, 484
		 * @param g Graphics object used to paint the JPanel
		 */
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			/** Iterates 50000 times*/
			int counter = 50000;
			while(counter > 0)
			{
				g.drawLine(x, y,  x , y);
			
				int dx = 0;
				int dy = 0;
				/** Generating Random Number*/
				int randNumber = generateRand();
				
				/** Finding the distance*/
				if(randNumber == 1)
				{
					dx = x - x1;
					dy = y - y1;
				}
				else if(randNumber == 2)
				{
					dx = x - x2;
					dy = y - y2;
				}
				else if(randNumber == 3)
				{
					dx = x - x3;
					dy = y - y3;
				}
				/** Recalculating current points*/
				x = x - (dx/2);
				y = y - (dy/2);
				counter--;
			}
			Graphics2D g2D = (Graphics2D)g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
	        g2D.drawString("Sierpinski Triangle", 462, 484);
		}

		/**
		 * The method generates one of the numbers 1, 2 or 3 in random
		 * Random class is used to generate the integer with upper bound 4
		 * @return 1, 2 or 3 in random
		 */
		private int generateRand()
		{
			Random rand = new Random();
			int upperBound = 4;

			int randomNumber = rand.nextInt(upperBound);

			return randomNumber;
		}
	}
