package BoardGames.EnglishCheckers;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class EnglishCheckersGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final String title = "English Checkers";
	
	private final int SIZE;
	private final Container container;
	private final GridLayout grid;
	private final JButton[][] buttons;
	private final Color noColor;
	private final InputStream realSystemIn;
	private final PipedOutputStream systemInOut;
   
	private boolean boardEnabled = false;
	
	private ImageIcon redDisc;
	private ImageIcon redQueenDisc;
	private ImageIcon blueDisc;
	private ImageIcon blueQueenDisc;

	public EnglishCheckersGUI(int size)
	{
		super(title);
		this.SIZE = size;

		grid = new GridLayout(SIZE, SIZE);
		container = getContentPane();
		container.setLayout(grid);
		buttons = new JButton[SIZE][SIZE];

		redDisc = new ImageIcon("redDisc.png");
		redQueenDisc = new ImageIcon("redQueenDisc.png");
		blueDisc = new ImageIcon("blueDisc.png");
		blueQueenDisc = new ImageIcon("blueQueenDisc.png");

		for (int row = 0; row < SIZE; ++row)
			for (int col = 0; col < SIZE; ++col)
			{
				final int actualRow = actualRow(row);
				final int finalCol = col;
            
				buttons[row][col] = new JButton("");
				buttons[row][col].setToolTipText(actualRow + "," + col);
				container.add(buttons[row][col]);

				buttons[row][col].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (boardEnabled)
						{
							pushBack(actualRow + "\n");
							pushBack(finalCol + "\n");
						}
					}
				});
			}
      
		noColor = buttons[0][0].getBackground();

		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
      
		// Substitute System.in so that we can write into it
		PipedInputStream systemInIn = new PipedInputStream();
		systemInOut = new PipedOutputStream();
		try 
		{
			systemInOut.connect(systemInIn);
		} 
		catch (IOException e) 
		{
			System.err.println("Can't connect streams: " + e.getMessage());
		}

		realSystemIn = System.in;
		System.setIn(systemInIn);
      
		Thread readerWriter = new Thread(this, "ReaderWriter");
		readerWriter.setPriority(Thread.MAX_PRIORITY);
		readerWriter.start();
	}

	public void enableBoard() 
	{
		boardEnabled = true;
	}
	
	public void disableBoard() 
	{
		boardEnabled = false;
	}
	
	public void showBoard(int array[][]) 
	{
		for (int row = 0; row < SIZE; row++)
			for (int col = 0; col < SIZE; col++)
			{
				int boardVal = array[actualRow(row)][col];

				Color color = noColor;
				ImageIcon currentImage = null;
				
				// Calculate the checkered background color.
				if ((actualRow(row) + col) % 2 == 0)
					color = color.darker();
				else
					color = color.brighter();
				
				switch (boardVal) 
				{
					// Mark the possible moves of the currently selected disc yellow.
					case EnglishCheckers.MARK:      
						color = Color.YELLOW;   
						break;
						
					// Set the images of the discs according to the board values.
					case EnglishCheckers.RED:      
						currentImage = redDisc;
						break;
					case EnglishCheckers.BLUE:      
						currentImage = blueDisc;
						break;
					case EnglishCheckers.RED * 2:   
						currentImage = redQueenDisc;
						break;
					case EnglishCheckers.BLUE * 2:   
						currentImage = blueQueenDisc;
						break;
				}
				
				// Set the checkered background color of the board.
				if (!buttons[row][col].getBackground().equals(color)) 
				{
					buttons[row][col].setBackground(color);
				}
				
				// If the currentImage is null then no image is drawn, otherwise draw the image of the disc at this position.
				buttons[row][col].setIcon(currentImage);
				buttons[row][col].repaint();
				
				currentImage = null;
			}
	}
   
	private int actualRow(int row) 
	{
		return SIZE-1-row;
	}
   
	public static void sleep(long millis) 
	{
		try 
		{
			Thread.sleep(500);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
   
	public void pushBack(String s) 
	{
		char[] pushbackC = s.toCharArray();
		byte[] pushbackB = new byte[pushbackC.length];
		for (int i = 0; i < pushbackC.length;  ++i)
			pushbackB[i] = (byte) pushbackC[i];

		try 
		{
			System.out.print(s);
			System.out.flush();

			systemInOut.write(pushbackB);
			systemInOut.flush();
			Thread.yield();
		} 
		catch (IOException e)
		{
			System.err.println("Can't push bytes into standard input: " + e.getMessage());
		}
	}

	public void run() 
	{
		byte[] buf = new byte[256];
		try 
		{
			while (realSystemIn.read(buf) >= 0) 
			{
				systemInOut.write(buf);
				systemInOut.flush();
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Error reading from real System.in: " + e.getMessage());
		}
	}
}
