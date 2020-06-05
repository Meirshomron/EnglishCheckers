package BoardGames.EnglishCheckers;

import java.util.Scanner;

public class EnglishCheckers 
{
	// Global constants.
	public static final int RED = 1;
	public static final int BLUE = -1;
	public static final int EMPTY = 0;

	public static final int BOARD_SIZE = 8;
	public static final int MARK = 3;
	public static EnglishCheckersGUI grid;

	public static Scanner getPlayerFullMoveScanner = null;
	public static Scanner getStrategyScanner = null;
	public static Scanner gameTypeScanner = null;

	public static final int RANDOM = 1;
	public static final int DEFENSIVE = 2;
	public static final int SIDES = 3;
	public static final int CUSTOM = 4;

	public static final int InteractiveMode = 1;
	public static final int twoPlayerMode = 2;
	
	public static void main(String[] args) 
	{
		// Create the graphical grid.
		grid = new EnglishCheckersGUI(BOARD_SIZE);
		
		// Select game type, PvE or PvP.
		int gameType = getGameTypeChoice();
		
		if (gameType == InteractiveMode)
		{
			interactivePlay();
		}
		else
		{
			twoPlayers();
		}

		if (gameTypeScanner != null)
		{
			gameTypeScanner.close();
		}
		if (getPlayerFullMoveScanner != null)
		{
			getPlayerFullMoveScanner.close();
		}
		if (getStrategyScanner != null)
		{
			getStrategyScanner.close();
		}
	}

	public static int[][] createBoard()
	{ 
		int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
		
		// Place the discs of every player in their correct spot on the board.
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
			{
				if (row < 3)
				{
					// The row or the coloumn are in an odd location.
					if ((row % 2) + (col % 2) == 1)
						board[row][col] = 0;
					// Both the row and the column are in odd locations or both aren't.
					else
						board[row][col] = 1;
				}
				else if (row > (BOARD_SIZE - 4))
				{
					// The row or the coloumn are in an odd location.
					if ((row % 2) + (col % 2) == 1)
						board[row][col] = 0;
					// Both the row and the column are in odd locations or both aren't.
					else
						board[row][col] = -1;
				}
				else
					board[row][col] = 0;	
			}
		return board;
	}

	public static int countPlayerDiscs(int[][] board, int player) 
	{
		int counter = 0;
		
		// Count how many discs a player has on the board.
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				if (board[row][col] == player || board[row][col] == 2 * player) 
					counter++; 
		
		return counter;
	}

	// Check if this basic move, a stepping forward 1 tile move is valid.
	public static boolean isBasicMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol)
	{
		boolean ans = true;
		
		// Checks if the coordinates are legal.
		if (( fromRow < 0|| fromRow >= BOARD_SIZE)||( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0 || toRow >= BOARD_SIZE)||( toCol < 0|| toCol >= BOARD_SIZE)) 
			ans = false;
		
		// Check if the player's disc is on the source coordinates.
		else if (board[fromRow][fromCol] != player && board[fromRow][fromCol] != player * 2)
			ans = false;
		
		// Check if the destination is empty.
		else if (board[toRow][toCol] != 0) 
			ans = false;
		
		// In case the disc of the player is 1 or -1, Check if it is a legal move for it.
		else if (board[fromRow][fromCol] == RED || board[fromRow][fromCol] == BLUE) 
		{ 
			// Only if the move is forwards.
			if (fromRow + player != toRow) 
				ans = false;	
			
			 // Only if the move is in a slant.
			if (fromCol + 1 != toCol && fromCol - 1 != toCol)
				ans = false;
		}
		else
			// In case the disc of the player is 2 or -2, checks if it is a legal move for it. 
			if ((fromRow + 1 != toRow && fromRow - 1 != toRow) || (fromCol + 1 != toCol && fromCol - 1 != toCol))
				ans = false;
				
		return ans;
	}

	public static boolean isBasicJumpValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) 
	{
		boolean ans = true;
		
		// Check if the coordinates are legal.
		if (( fromRow < 0 || fromRow >= BOARD_SIZE)||( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0||toRow >= BOARD_SIZE)||( toCol < 0 || toCol >= BOARD_SIZE))
			ans = false;	
		
		// Check if the player's disc is on the source coordinates.
		else if ((board[fromRow][fromCol] != player) && (board[fromRow][fromCol] != (2 * player)))
			ans = false;
		
		// Check if the tile between the source and the destination contains the opposite player's disc.
		else if (board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] != (player * (-1)) && (board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] != (player * (-2))))
			ans = false;
		
		// Check if the destination is empty.
		else if (board[toRow][toCol] != 0)
			ans = false;
		
		// In case the disc of the player is 1 or -1, Check if it is a legal jump for it.
		else if (board[fromRow][fromCol] == RED || board[fromRow][fromCol] == BLUE)
		{
			// Only if the jump is forwards and in a slant.
			if (((fromRow + (2 * player) != toRow) || (fromCol + 2 != toCol && fromCol - 2 != toCol))) 
				ans = false;
		}		
		else
		{
			// In case the disc of the player is 2 or -2, checks if it is a legal jump for it. 
			if ((fromRow + 2 != toRow && fromRow - 2 != toRow) || (fromCol + 2 != toCol && fromCol - 2 != toCol))
				ans = false;
		}
		
		return ans;
	}
	
	public static int[][] getAllBasicMoves(int[][] board, int player) 
	{
		int[][] moves = null;
		int totalMoves = 0;
		int moveIndex = 0;				

		// Count the amount of possible simple moves for a player.
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
			{ 
				if (isBasicMoveValid(board, player, row, col, row + 1, col - 1))
					totalMoves++;
				if (isBasicMoveValid(board, player, row, col, row + 1 ,col + 1))
					totalMoves++;
				if (isBasicMoveValid(board, player, row, col, row - 1, col - 1))
					totalMoves++;
				if (isBasicMoveValid(board, player, row, col, row - 1, col + 1))	
					totalMoves++;
			}			
						
		moves = new int [totalMoves][4];	
		// If their are possible simple moves.
		if (totalMoves != 0)
		{	
			// Add the coordinates of the possible simple move to the array.
			for (int row = 0; row < BOARD_SIZE && moveIndex < totalMoves; row++)
				for (int col = 0; col < BOARD_SIZE && moveIndex < totalMoves; col++)
				{ 
					if (isBasicMoveValid(board, player, row, col, row + 1, col - 1))
					{
						moves[moveIndex][0] = row;
						moves[moveIndex][1] = col;
						moves[moveIndex][2] = row + 1;
						moves[moveIndex][3] = col - 1;
						moveIndex++;
					}
					if (isBasicMoveValid(board, player, row, col, row + 1, col + 1))
					{
				        moves[moveIndex][0] = row;
						moves[moveIndex][1] = col;
						moves[moveIndex][2] = row + 1;
						moves[moveIndex][3] = col + 1;
						moveIndex++;
					}
					if (isBasicMoveValid(board, player, row, col, row - 1, col - 1))
					{
						moves[moveIndex][0] = row;
						moves[moveIndex][1] = col;
						moves[moveIndex][2] = row - 1;
						moves[moveIndex][3] = col - 1;
						moveIndex++;
					}
					if (isBasicMoveValid(board, player, row, col, row - 1, col + 1))
					{
						moves[moveIndex][0] = row;
						moves[moveIndex][1] = col;
						moves[moveIndex][2] = row - 1;
						moves[moveIndex][3] = col + 1;
						moveIndex++;
					}
				}	
		}		
		return moves;
	}

	public static int [][] getRestrictedBasicJumps(int[][] board, int player, int row, int col)
	{
		int[][] moves = null;
		int totalMoves = 0;
		int moveIndex = 0;

		// Check how many discs a certain disc of mine can jump over in a single jump.
		if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
			totalMoves++;
		if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))	
			totalMoves++;
		if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))	
			totalMoves++;
		if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
			totalMoves++;
		
		moves = new int[totalMoves][4];
		if (totalMoves != 0)
		{
			// If my disc can jump over others then place all his jumping possibilities in 'moves'.
			if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
			{
				moves[moveIndex][0] = row;
				moves[moveIndex][1] = col;
				moves[moveIndex][2] = row - 2;
				moves[moveIndex][3] = col - 2;
				moveIndex++;
			}
			if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))
			{
				moves[moveIndex][0] = row;
				moves[moveIndex][1] = col;
				moves[moveIndex][2] = row - 2;
				moves[moveIndex][3] = col + 2;
				moveIndex++;
			}
			if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))
			{
				moves[moveIndex][0] = row;
				moves[moveIndex][1] = col;
				moves[moveIndex][2] = row + 2;
				moves[moveIndex][3] = col - 2;
				moveIndex++;
			}
			if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
			{
				moves[moveIndex][0] = row;
				moves[moveIndex][1] = col;
				moves[moveIndex][2] = row + 2;
				moves[moveIndex][3] = col + 2;
				moveIndex++;
			}
		}
		
		return moves;
	}

	public static int[][] getAllBasicJumps(int[][] board, int player) 
	{
		int[][] moves = null;
		int[][] restrictedMoves = null;
		int totalMoves = 0;
		int moveIndex = 0;				

		// Counting the amount of possible jumps for a player.
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
			{ 
				if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))
					totalMoves++;
				if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
					totalMoves++;
				if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
					totalMoves++;
				if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))	
					totalMoves++;
			}			
						
		moves = new int[totalMoves][4];
		
		// If their are possible jumps.
		if(totalMoves != 0)
		{
			// Add the coordinates of the possible jumps to the array.
			for (int row = 0; row < BOARD_SIZE && moveIndex < totalMoves; row++)
				for (int col = 0; col < BOARD_SIZE && moveIndex < totalMoves; col++)
				{ 
					// Get the array of possible jumps for this tile, assuming it has a player's disc and valid jumps.
					restrictedMoves = getRestrictedBasicJumps(board, player, row, col);
					if (restrictedMoves.length >= 0)
					{
						for (int currentMove = 0; currentMove < restrictedMoves.length; currentMove++)
						{
							for (int idx = 0; idx < 4; idx++)
							{
								moves[moveIndex][idx] = restrictedMoves[currentMove][idx];
							}
							moveIndex++;
						}
					}
				}
		}		
		return moves;
	}

	public static boolean canJump(int[][] board, int player) 
	{
		boolean ans = false;
		int [][]allJumpsArr = getAllBasicJumps(board, player); 
		
		// Check if the array with the player's possible jumps is empty.
		if(allJumpsArr.length != 0)
			ans = true;
		
		return ans;
	}

	// Check if this move is a valid basic move or jump.
	public static boolean isMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol)
	{
		boolean ans = true;
		
		// Check if the coordinates are legal.
		if (( fromRow < 0 || fromRow >= BOARD_SIZE) || ( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0 || toRow >= BOARD_SIZE) || ( toCol < 0 || toCol >= BOARD_SIZE))
			ans = false;
		
		else if ((board[fromRow][fromCol] != player) && (board[fromRow][fromCol] != 2 * player))
			ans = false;
		
		else if (board[toRow][toCol] != 0)
			ans = false;
		
		// Check if it's an illegal jump and an illegal move.
		else if (!isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol) && !isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol))
			ans = false;
		
		// Check if it's a legal move but the player can jump.
		else if ((isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)) && (canJump(board, player)))
			ans = false;
				
		return ans;
	}

	public static boolean hasValidMoves(int[][] board, int player) 
	{
		boolean ans = false;
		
		 // All possible basic simple moves.
		int [][] basicMoves = getAllBasicMoves(board, player);
		
		// All possible simple jumps.
		int [][]basicJumps = getAllBasicJumps(board, player); 
		
		if(basicMoves.length != 0 || basicJumps.length != 0)
			ans = true;
		
		return ans;		
	}

	public static int[][] playMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) 
	{
		 // Check if the desired basic move is valid by game rules.
		if ((isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)))
		{
			// Becoming a queen so the move has special consequences.
			if ((player == RED && toRow == (BOARD_SIZE - 1)) || (player == BLUE && toRow == 0)) 
			{
				board[toRow][toCol] = 2 * player;
			}			
			else
			{
				board[toRow][toCol] = board[fromRow][fromCol]; 
			}
			board[fromRow][fromCol] = 0;
		}
		else
		{
			// Check if the desired basic jump is valid by game rules.
			if ((isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol)))
			{	
				// Becoming a queen so the move has special consequences.
				if ((player == RED && toRow == (BOARD_SIZE - 1)) || (player == BLUE && toRow == 0)) 
				{
					board[toRow][toCol] = 2 * player;
				}			
				else
				{
					board[toRow][toCol] = board[fromRow][fromCol]; 
				}
				
				board[fromRow][fromCol] = 0;
				board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] = 0;
			}
		}
		return board;
	}

	public static boolean gameOver(int[][] board, int player) 
	{
		boolean ans = false;
		
		// Check if he has moves to make.
		if (!hasValidMoves(board, player)) 
			ans = true;

		return ans;
	}

	public static int findTheLeader(int[][] board)
	{
		int ans = 0;
		
		// Sum of discs for player 1.
		int sumScorePlayer1 = 0; 
		
		// Sum of discs for player -1.
		int sumScorePlayer2 = 0;
		
		// Check all the discs on the board.
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
			{ 
				if (board[row][col] == RED)
					sumScorePlayer1++;
				if (board[row][col] == 2 * RED)
					sumScorePlayer1+= 2;
				if (board[row][col] == BLUE)
					sumScorePlayer2++;
				if (board[row][col] == 2 * BLUE)
					sumScorePlayer2+= 2;
			}
		
		// Both players have the same amount of discs.
		if(sumScorePlayer1 == sumScorePlayer2) 
			ans = 0;
		else if(sumScorePlayer1 > sumScorePlayer2) 
			ans = RED;
		else
			ans = BLUE;
		
		return ans;
	}

	// Check if a disc that is moved will be possibly eaten by opponent.
	private static boolean discEaten(int [][] board, int player, int fromRow, int fromCol, int toRow, int toCol) 
	{
		boolean ans = false;
		
		// Copy the original board so we don't change it while checking this move.
		int [][]boardCopy = new int[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++)
			for (int col = 0; col < BOARD_SIZE; col++)
				boardCopy[row][col] = board[row][col];
				
		// Check all potential jumps of opponent over the disc that was moved.
		if (isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol))
		{	
			int[][]tempBoard = playMove(boardCopy, player, fromRow, fromCol, toRow, toCol);
			if (isBasicJumpValid(tempBoard, player * (-1), toRow - 1, toCol - 1, toRow + 1, toCol + 1))
				ans = true;
			if (isBasicJumpValid(tempBoard, player * (-1), toRow - 1 , toCol + 1, toRow + 1, toCol - 1))
				ans = true;
			if (isBasicJumpValid(tempBoard, player * (-1), toRow + 1, toCol - 1, toRow - 1, toCol + 1))
				ans = true;
			if (isBasicJumpValid(tempBoard, player * (-1), toRow + 1, toCol + 1, toRow - 1, toCol - 1))
				ans = true;
		}
		return ans;
	}
	
	/* 
	 * ---------------------------------------------------------- *
	 * ------------------- Computer strategies ------------------ *
	 * ---------------------------------------------------------- *
	*/
	public static int[][] randomPlayer(int[][] board, int player)
	{
		// Check if the player has no moves then return the board unchanged.
		if (!hasValidMoves(board, player)) 
			return board;
		
	    int[][]jumps = getAllBasicJumps(board, player);
	    
	    // Check if the player has no jumps but can make a simple move.
		if(jumps.length == 0) 
		{
			// Make a random legal basic move.
			int [][]basicMoves = getAllBasicMoves(board, player);
			int randomMove = (int)(Math.random() * basicMoves.length);
			board = playMove(board, player, basicMoves[randomMove][0], basicMoves[randomMove][1], basicMoves[randomMove][2], basicMoves[randomMove][3]); 
		}
		else
		{
			// Make a first random legal jump.
			int randomJump = (int)(Math.random() * jumps.length);
			board = playMove(board, player, jumps[randomJump][0], jumps[randomJump][1], jumps[randomJump][2], jumps[randomJump][3]);
			int[][]restrictedJumps = getRestrictedBasicJumps(board, player, jumps[randomJump][2], jumps[randomJump][3]);
			
			// While the disc of the player that jumped can jump over more discs.
			while(restrictedJumps.length != 0) 
			{	
				// Make a random legal jump.
				randomJump = (int)(Math.random() * restrictedJumps.length);
				board = playMove(board, player, restrictedJumps[randomJump][0], restrictedJumps[randomJump][1], restrictedJumps[randomJump][2], restrictedJumps[randomJump][3]);
				restrictedJumps = getRestrictedBasicJumps(board, player, restrictedJumps[randomJump][2], restrictedJumps[randomJump][3]); 
			}	
		}
		return board;
	}

	public static int[][] defensivePlayer(int[][] board, int player)
	{
		// Check if the player has no moves then return the board unchanged.
		if(!hasValidMoves(board, player))
			return board;
		
		// Check if player can jump, make a random jump.
		if(canJump(board, player)) 
		{
			board = randomPlayer(board, player); 
		}
		else
		{
			int[][] basicMoves = getAllBasicMoves(board, player);
			int notBeEatenCounter = 0;
			int beEatenCounter = 0;
			int totalBeEaten = 0;
			int randomMove;
			
			// Check every possible basic move if he will be eaten or not.
			for (int moveIdx = 0; moveIdx < basicMoves.length; moveIdx++) 
			{
				if (discEaten(board, player, basicMoves[moveIdx][0], basicMoves[moveIdx][1], basicMoves[moveIdx][2], basicMoves[moveIdx][3]))
					totalBeEaten++;
			}
			
			int[][] notBeEatenMoves = new int [basicMoves.length - totalBeEaten][4];
			int[][] beEatenMoves = new int[totalBeEaten][4];
			for (int moveIdx = 0; moveIdx < basicMoves.length; moveIdx++)
			{
				if (discEaten(board, player, basicMoves[moveIdx][0], basicMoves[moveIdx][1], basicMoves[moveIdx][2], basicMoves[moveIdx][3]))
				{
					beEatenMoves[beEatenCounter][0] = basicMoves[moveIdx][0];
					beEatenMoves[beEatenCounter][1] = basicMoves[moveIdx][1];
					beEatenMoves[beEatenCounter][2] = basicMoves[moveIdx][2];
					beEatenMoves[beEatenCounter][3] = basicMoves[moveIdx][3];
					beEatenCounter++;
				}
				else
				{
					notBeEatenMoves[notBeEatenCounter][0] = basicMoves[moveIdx][0];
					notBeEatenMoves[notBeEatenCounter][1] = basicMoves[moveIdx][1];
					notBeEatenMoves[notBeEatenCounter][2] = basicMoves[moveIdx][2];
					notBeEatenMoves[notBeEatenCounter][3] = basicMoves[moveIdx][3];	
					notBeEatenCounter++;
				}
			}	
			// Their are moves the player can make and he wont be eaten as a result of the move.
			if(notBeEatenCounter != 0) 
			{
				// Choose a random a basic move.
				randomMove = (int)(Math.random() * notBeEatenMoves.length);
				board = playMove(board, player, notBeEatenMoves[randomMove][0], notBeEatenMoves[randomMove][1], notBeEatenMoves[randomMove][2], notBeEatenMoves[randomMove][3]);
			}
			else 
			{
				randomMove = (int)(Math.random() * beEatenMoves.length);
				board = playMove(board, player, beEatenMoves[randomMove][0], beEatenMoves[randomMove][1], beEatenMoves[randomMove][2], beEatenMoves[randomMove][3]);
			}
		}
				
		return board;
	}		

	public static int[][] sidesPlayer(int[][] board, int player) 
	{
		// Check if the player has no moves then return the board unchanged.
		if(!hasValidMoves(board,player))
			return board;
		
		if(canJump(board, player))
		{ 
			// Check if player can jump then make a random jump.
			board = randomPlayer(board, player); 
		}
		else
		{
			int minColDistanceFromSide = BOARD_SIZE / 2;
			int lastMinColDistanceFromSideIndex = 0;
			int[][] basicMoves = getAllBasicMoves(board, player);	
			int[] basicMovesDistanceFromSides = new int[basicMoves.length]; 
			int colDistanceFromLeft = 0;
			int colDistanceFromRight = 0;
			int minCounter = 1;
			
			for (int moveIdx = 0; moveIdx < basicMoves.length; moveIdx++)
			{	
				colDistanceFromLeft = basicMoves[moveIdx][3];
				colDistanceFromRight = (BOARD_SIZE - 1) - basicMoves[moveIdx][3];	
				
				// Save the minimum distance of a move from the sides.
				basicMovesDistanceFromSides[moveIdx] = Math.min(colDistanceFromLeft, colDistanceFromRight); 
				
				if (basicMovesDistanceFromSides[moveIdx] < minColDistanceFromSide)
				{
					// Save the line where the minimum distance for a moved disc is.
					minColDistanceFromSide = basicMovesDistanceFromSides[moveIdx];
					lastMinColDistanceFromSideIndex = moveIdx; 
					minCounter = 1;
				}
				
				// Count how many discs have the same minimum distance.
				else if (basicMovesDistanceFromSides[moveIdx] == minColDistanceFromSide)
				{
					minCounter++;
				}
			}
			
			// Case of 1 move moves a disc closer to the sides more then any of the other valid moves. 
			if (minCounter == 1)
			{ 
				board = playMove(board, player, basicMoves[lastMinColDistanceFromSideIndex][0], basicMoves[lastMinColDistanceFromSideIndex][1], basicMoves[lastMinColDistanceFromSideIndex][2], basicMoves[lastMinColDistanceFromSideIndex][3]);
			}	
			
			// Choose randomly between moves that will put the player's disc at the same minimum distance from the sides.
			else
			{
				int allMinMovesCounter = 0;
				int randomMoveIndex = lastMinColDistanceFromSideIndex;
				int randomFromAllMinMovesIndex = (int)(Math.random() * minCounter);
				for(int i = 0; i < basicMovesDistanceFromSides.length; i++)
				{
					if (basicMovesDistanceFromSides[i] == basicMovesDistanceFromSides[lastMinColDistanceFromSideIndex])
					{
						if (randomFromAllMinMovesIndex == allMinMovesCounter)
						{
							randomMoveIndex = i;
							break;
						}
						allMinMovesCounter++;
					}
				}
				board = playMove(board, player, basicMoves[randomMoveIndex][0], basicMoves[randomMoveIndex][1], basicMoves[randomMoveIndex][2], basicMoves[randomMoveIndex][3]);
			}
		}
		return board;
	}

	/* 
	 * ---------------------------------------------------------- *
	 * ------- Play an interactive game with the computer ------- *
	 * ---------------------------------------------------------- *
	*/
	public static void interactivePlay()
	{
		int[][] board = createBoard();
		showBoard(board);

		System.out.println("Welcome to the interactive Checkers Game !");

		int strategy = getStrategyChoice();
		grid.enableBoard();
		
		System.out.println("You are the first player (RED discs)\n");

		boolean oppGameOver = false;
		while (!gameOver(board, RED) && !oppGameOver)
		{
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver) 
			{
				EnglishCheckersGUI.sleep(200);
				board = getStrategyFullMove(board, BLUE, strategy);
			}
		}

		int winner = 0;
		if (countPlayerDiscs(board, RED) == 0 | countPlayerDiscs(board, BLUE) == 0)
		{
			winner = findTheLeader(board);
		}

		if (winner == RED) 
		{
			System.out.println("\n\t *************************");
			System.out.println("\t * You are the winner !! *");
			System.out.println("\t *************************");
		}
		else if (winner == BLUE) 
		{
			System.out.println("\n======= You lost :( =======");
		}
		else
		{
			System.out.println("\n======= DRAW =======");
		}
		
		grid.disableBoard();
	}

	/* 
	 * --------------------------------------------------------- *
	 * --------------- A game between two players -------------- *
	 * --------------------------------------------------------- *
	*/
	public static void twoPlayers() 
	{
		int[][] board = createBoard();
		showBoard(board);
		grid.enableBoard();
		
		System.out.println("Welcome to the 2-player Checkers Game !");

		boolean oppGameOver = false;
		while (!gameOver(board, RED) & !oppGameOver)
		{
			System.out.println("\nRED's turn");
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver)
			{
				System.out.println("\nBLUE's turn");
				board = getPlayerFullMove(board, BLUE);
			}
		}

		int winner = 0;
		if (countPlayerDiscs(board, RED) == 0 | countPlayerDiscs(board, BLUE) == 0)
			winner = findTheLeader(board);

		System.out.println("\n\t ************************************");
		if (winner == RED)
			System.out.println("\t * The red player is the winner !!  *");
		else if (winner == BLUE)
			System.out.println("\t * The blue player is the winner !! *");
		else
			System.out.println("\t * DRAW !! *");
		System.out.println("\t ************************************");
		
		grid.disableBoard();
	}

	 // Get a complete (possibly a sequence of jumps) move from a human player.
	public static int[][] getPlayerFullMove(int[][] board, int player) 
	{
		// Get first move/jump.
		int fromRow = -1, fromCol = -1, toRow = -1, toCol = -1;
		boolean jumpingMove = canJump(board, player);
		boolean notValidMove = true;
		getPlayerFullMoveScanner = new Scanner(System.in);
		while (notValidMove) 
		{
			if (player == RED)
			{
				System.out.println("Red, Please play:");
			} else 
			{
				System.out.println("Blue, Please play:");
			}

			fromRow = getPlayerFullMoveScanner.nextInt();
			fromCol = getPlayerFullMoveScanner.nextInt();

			int[][] moves = jumpingMove ? getAllBasicJumps(board, player) : getAllBasicMoves(board, player);
			markPossibleMoves(board, moves, fromRow, fromCol, MARK);
			toRow = getPlayerFullMoveScanner.nextInt();
			toCol = getPlayerFullMoveScanner.nextInt();
			markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

			notValidMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
			if (notValidMove)
				System.out.println("\nThis is an illegal move");
		}

		// Apply move/jump.
		board = playMove(board, player, fromRow, fromCol, toRow, toCol);
		showBoard(board);

		// Get extra jumps.
		if (jumpingMove)
		{
			boolean longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			while (longMove) 
			{
				fromRow = toRow;
				fromCol = toCol;

				int[][] moves = getRestrictedBasicJumps(board, player, fromRow, fromCol);

				boolean notValidExtraMove = true;
				while (notValidExtraMove) 
				{
					markPossibleMoves(board, moves, fromRow, fromCol, MARK);
					System.out.println("Continue jump:");
					toRow = getPlayerFullMoveScanner.nextInt();
					toCol = getPlayerFullMoveScanner.nextInt();
					markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

					notValidExtraMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
					if (notValidExtraMove)
						System.out.println("\nThis is an illegal jump destination :(");
				}

				// Apply extra jump.
				board = playMove(board, player, fromRow, fromCol, toRow, toCol);
				showBoard(board);

				longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			}
		}
		return board;
	}

	// Get a complete (possibly a sequence of jumps) move from a strategy.
	public static int[][] getStrategyFullMove(int[][] board, int player, int strategy)
	{
		if (strategy == RANDOM)
			board = randomPlayer(board, player);
		else if (strategy == DEFENSIVE)
			board = defensivePlayer(board, player);
		else if (strategy == SIDES)
			board = sidesPlayer(board, player);

		showBoard(board);
		return board;
	}

	// Get a strategy choice before the game.
	public static int getStrategyChoice()
	{
		int strategy = -1;
		getStrategyScanner = new Scanner(System.in);
		System.out.println("Choose the strategy of your opponent:" +
				"\n\t(" + RANDOM + ") - Random player" +
				"\n\t(" + DEFENSIVE + ") - Defensive player" +
				"\n\t(" + SIDES + ") - To-the-Sides player");
		while (strategy != RANDOM & strategy != DEFENSIVE & strategy != SIDES) 
		{
			strategy = getStrategyScanner.nextInt();
		}
		return strategy;
	}
	
	// Get a game type choice before the game.
	public static int getGameTypeChoice()
	{
		int gameType = -1;
		gameTypeScanner = new Scanner(System.in);
		System.out.println("Choose game type:" +
				"\n\t(" + InteractiveMode + ") - vs Computer" +
				"\n\t(" + twoPlayerMode + ") - vs player");
		while (gameType != InteractiveMode & gameType != twoPlayerMode) 
		{
			gameType = gameTypeScanner.nextInt();
		}	
		return gameType;
	}

	/* 
	 * --------------------------------------------------------- *
	 * ---------------- General Helper Fucntions --------------- *
	 * --------------------------------------------------------- *
	*/

	// Mark/unmark the possible moves
	public static void markPossibleMoves(int[][] board, int[][] moves, int fromRow, int fromColumn, int value) 
	{
		for (int moveIdx = 0; moveIdx < moves.length; moveIdx++)
			if (moves[moveIdx][0] == fromRow & moves[moveIdx][1] == fromColumn)
				board[moves[moveIdx][2]][moves[moveIdx][3]] = value;

		showBoard(board);
	}

	// Shows the board in a graphic window.
	public static void showBoard(int[][] board)
	{
		grid.showBoard(board);
	}

	// Print the board.
	public static void printBoard(int[][] board)
	{
		for (int row = (board.length - 1); row >= 0; row--)
		{
			for (int col = 0; col < board.length; col++)
			{
				System.out.format("%4d", board[row][col]);
			}
			System.out.println();
		}
	}
}
