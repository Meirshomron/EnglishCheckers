import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

public class EnglishCheckersGUI extends JFrame implements Runnable {

   private static final long serialVersionUID = 1727543467970128346L;

   private final int         SIZE;
   private final Container      container;
   private final GridLayout   grid;
   private final JButton[][]   buttons;
   private final Color         noColor;
   
   private final InputStream      realSystemIn;
   private final PipedOutputStream systemInOut;

   public EnglishCheckersGUI(int size) {
      super("Checkers Play");
      this.SIZE = size;

      grid     = new GridLayout(SIZE,SIZE);
      container = getContentPane();
      container.setLayout(grid);

      buttons = new JButton[SIZE][SIZE];
      final EnglishCheckersGUI thisRef = this;

      for(int i = 0;  i <= SIZE-1;  ++i)
         for(int j = 0;  j <= SIZE-1;  ++j)
         {
            final int ii = realI(i);
            final int jj = realJ(j);

            buttons[i][j] = new JButton("");
            buttons[i][j].setToolTipText(ii + "," + jj);
            container.add(buttons[i][j]);
            
            buttons[i][j].addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent arg0) {
                  thisRef.pushBack(ii + "\n");
                  thisRef.pushBack(jj + "\n");
               }
            });
         }
      
      noColor = buttons[0][0].getBackground();

      setSize(500,500);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
      // Substitute System.in so that we can write into it
      PipedInputStream systemInIn = new PipedInputStream();
      systemInOut = new PipedOutputStream();
      try {
         systemInOut.connect(systemInIn);
      } catch (IOException e) {
         System.err.println("Can't connect streams: " + e.getMessage());
      }

      realSystemIn = System.in;
      System.setIn(systemInIn);
      
      Thread readerWriter = new Thread(this, "ReaderWriter");
      readerWriter.setPriority(Thread.MAX_PRIORITY);
      readerWriter.start();
   }

   public  void showBoard(int array[][]) {
      for(int i=0; i<=SIZE-1; i++){
         for(int j=0; j<=SIZE-1; j++)
         {
            int boardVal = array[realI(i)][realJ(j)];

            Color color = noColor;
            switch (boardVal) {
            case EnglishCheckers.EMPTY:
               if ((realI(i)+realJ(j)) % 2 == 0)
                  color = color.darker();
               else
                  color = color.brighter();
               break;
            case EnglishCheckers.RED:      color = Color.RED;      break;
            case EnglishCheckers.BLUE:      color = Color.BLUE;      break;
            case EnglishCheckers.RED * 2:   color = Color.PINK;   break;
            case EnglishCheckers.BLUE * 2:   color = Color.CYAN;      break;
            case EnglishCheckers.MARK:      color = Color.YELLOW;   break;
            default:
               System.err.println("Unknown value at position " + realI(i) + "," + realJ(j) + "!");
            }
            
            if (! buttons[i][j].getBackground().equals(color)) {
               buttons[i][j].setBackground(color);
               buttons[i][j].repaint();
            }
         }
      }
   }
   
   private int realI(int i) {
      return SIZE-1-i;
   }
   
   private int realJ(int j) {
      return j;
   }
   
   public static void sleep(long millis) {
      try {
         Thread.sleep(500);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   
   public void pushBack(String s) {
      char[] pushbackC = s.toCharArray();
      byte[] pushbackB = new byte[pushbackC.length];
      for (int i = 0;  i < pushbackC.length;  ++i)
         pushbackB[i] = (byte) pushbackC[i];

      try {
         System.out.print(s);
         System.out.flush();

         systemInOut.write(pushbackB);
         systemInOut.flush();
         Thread.yield();
      } catch (IOException e) {
         System.err.println("Can't push bytes into standard input: " + e.getMessage());
      }
   }

   public void run() {
      byte[] buf = new byte[256];
      try {
         while (realSystemIn.read(buf) >= 0) {
            systemInOut.write(buf);
            systemInOut.flush();
         }
      } catch (IOException e) {
         System.err.println("Error reading from real System.in: " + e.getMessage());
      }
   }
}