
import java.util.Scanner;


public class EnglishCheckers {

	// Global constants
	public static final int RED   = 1;
	public static final int BLUE  = -1;
	public static final int EMPTY = 0;

	public static final int SIZE  = 8;

	// You can ignore these constants
	public static final int MARK  = 3;
	public static EnglishCheckersGUI grid;

	public static Scanner getPlayerFullMoveScanner = null;
	public static Scanner getStrategyScanner = null;

	public static final int RANDOM			= 1;
	public static final int DEFENSIVE		= 2;
	public static final int SIDES				= 3;
	public static final int CUSTOM			= 4;


	public static void main(String[] args) {

		// ******** Don't delete ********* 
		// CREATE THE GRAPHICAL GRID
		grid = new EnglishCheckersGUI(SIZE);
		// ******************************* 

		
		//printMatrix(example);
		//showBoard(createBoard());
		//interactivePlay();
		//twoPlayers();


		/* ******** Don't delete ********* */    
		if (getPlayerFullMoveScanner != null){
			getPlayerFullMoveScanner.close();
		}
		if (getStrategyScanner != null){
			getStrategyScanner.close();
		}
		/* ******************************* */
		
		}


	public static int[][] createBoard(){ 
		int[][] board = null;
		board=new int[8][8];
		for(int i=0;i<8;i=i+1)
		{
		   for(int j=0;j<8;j=j+1)
		   {
			   //go over the whole board and place the discs of every player in their correct spot.
		     if(i%2==0&& i<3)
		     {
			    if (j%2==0)
			       board[i][j]=1;
			    else
                   board[i][j]=0;
		      }
		    if(i%2!=0&& i<3)
		    {
      			if (j%2==0)
		           board[i][j]=0;
		        else
		           board[i][j]=1;
			}		 
		    if(i>2&&i<5)
		       board[i][j]=0;
		    if(i%2==0&& i>4)
		    {
			    if (j%2==0)
			       board[i][j]=-1;
			    else
                   board[i][j]=0;
		    }
		    if(i%2!=0&& i>4)
		    {
      			if (j%2==0)
		            board[i][j]=0;
		        else
		            board[i][j]=-1;
		    }
		
		  }
		}
		return board;
	}


	public static int[][] playerDiscs(int[][] board, int player) {
		int[][] positions = new int[0][2];
		int counter=0;
		// count how many discs a player has on the board.
		for(int i=0;i<8;i=i+1)
			for(int j=0;j<8;j=j+1)
				if(board[i][j]==player||board[i][j]==2*player) 
					counter=counter+1; 
		//has no discs.
		if(counter==0)
			return positions; 
		positions=new int[counter][2];
		int index=0;
		for(int i=0;i<8;i=i+1){
			for(int j=0;j<8;j=j+1){
				if(board[i][j]==player||board[i][j]==2*player)
				{ 
					//place the disc coordinations on the array.
					positions[index][0]=i;  
					positions[index][1]=j;
					index=index+1;
				}
			}
		}
		return positions;
	}


	public static boolean isBasicMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		if(( fromRow<0||fromRow>7)||( fromCol<0||fromCol>7)||( toRow<0||toRow>7)||( toCol<0||toCol>7)) //checks if the coordinates are legal 
			return ans;
		if(board[fromRow][fromCol]!=player&&board[fromRow][fromCol]!=player*2) //if the player's disc is on the source coordinates
			return ans;
		if(board[toRow][toCol]!=0) // checks if the destination is empty.
			return ans;
		if(	board[fromRow][fromCol]==1||board[fromRow][fromCol]==-1)//in case the disc of the player is 1 or -1, checks if it is a legal move for it. 
		{ 
			if(fromRow+player!=toRow) //only if the move is forwards 
				return ans;		
			if(fromCol+1!=toCol&&fromCol-1!=toCol) //only if the move is in in a slant
				return ans;
		}
		else
			if(fromRow+1!=toRow&&fromRow-1!=toRow||fromCol+1!=toCol&&fromCol-1!=toCol) //in case the disc of the player is 2 or -2, checks if it is a legal move for it. 
				return ans; //the move is not forwards or backwards in a single slant
		ans=true;
		
	return ans;
	}


	public static int[][] getAllBasicMoves(int[][] board, int player) {
		int[][] moves = null;
		int counter=0;
		for(int i=0;i<8;i=i+1){
			for(int j=0;j<8;j=j+1)//counting the amount of possible simple moves for a player
			{ 
				if(isBasicMoveValid(board,player,i,j,i+1,j-1))
					counter=counter+1;
				if(isBasicMoveValid(board,player,i,j,i+1,j+1))
					counter=counter+1;
				if(isBasicMoveValid(board,player,i,j,i-1,j-1))
					counter=counter+1;
				if(isBasicMoveValid(board,player,i,j,i-1,j+1))	
					counter=counter+1;
			}			
		}				
		int index=0;				
		moves=new int [counter][4];	
		if(moves.length!=0)// if their are possible simple moves
		{	
			for(int i=0;i<8&&index<counter;i=i+1){
				for(int j=0;j<8&&index<counter;j=j+1){ //adds to the array the coordinates of the possible simple move
						if(isBasicMoveValid(board,player,i,j,i+1,j-1))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i+1;
							moves[index][3]=j-1;
							index=index+1;
						}
						if(isBasicMoveValid(board,player,i,j,i+1,j+1))
						{
					        moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i+1;
							moves[index][3]=j+1;
							index=index+1;
						}
						
						if(isBasicMoveValid(board,player,i,j,i-1,j-1))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i-1;
							moves[index][3]=j-1;
							index=index+1;
						}
						if(isBasicMoveValid(board,player,i,j,i-1,j+1))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i-1;
							moves[index][3]=j+1;
							index=index+1;
						}
							
				}
			}	
		}		
		return moves;
	}


	public static boolean isBasicJumpValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		if(( fromRow<0||fromRow>7)||( fromCol<0||fromCol>7)||( toRow<0||toRow>7)||( toCol<0||toCol>7))//checks if the coordinates are legal
			return ans;	
		if((board[fromRow][fromCol]!=player)&&(board[fromRow][fromCol]!=(2*player)))
			return ans;
		if(board[(toRow+fromRow)/2][(toCol+fromCol)/2]!=(player*(-1))&&(board[(toRow+fromRow)/2][(toCol+fromCol)/2]!=(player*(-2))))
			return ans;
		if(board[toRow][toCol]!=0)
			return ans;
		if(board[fromRow][fromCol]==1||board[fromRow][fromCol]==-1)
		{
			if(((fromRow+(2*player)!=toRow||fromCol+2!=toCol)&&(fromRow+(2*player)!=toRow||fromCol-2!=toCol))) //checks the destination of a -1 or 1 is legal
					return ans;
		}		
		else
		{
			if(fromRow+2!=toRow&&fromRow-2!=toRow||fromCol+2!=toCol&&fromCol-2!=toCol)//checks the destination of a queen is legal
					return ans;
		}
		ans=true;
		return ans;
	}


	public static int [][] getRestrictedBasicJumps(int[][] board, int player, int row, int col) {
		int[][] moves = null;
		int counter=0;
		//checking how many discs a certain disc of mine can jump over in a single jump
		if(isBasicJumpValid(board,player,row,col,row-2,col-2))
			counter=counter+1;
		if(isBasicJumpValid(board,player,row,col,row-2,col+2))	
			counter=counter+1;
		if(isBasicJumpValid(board,player,row,col,row+2,col-2))	
			counter=counter+1;
		if(isBasicJumpValid(board,player,row,col,row+2,col+2))
			counter=counter+1;
		moves= new int [counter][4];
		int index=0;
		if(counter!=0){
		//if my disc can jump over others then place all his jumping possibilities in 'moves'
		if(isBasicJumpValid(board,player,row,col,row-2,col-2))
		{
			moves[index][0]=row;
			moves[index][1]=col;
			moves[index][2]=row-2;
			moves[index][3]=col-2;
			index=index+1;
		}
		if(isBasicJumpValid(board,player,row,col,row-2,col+2))
		{
			moves[index][0]=row;
			moves[index][1]=col;
			moves[index][2]=row-2;
			moves[index][3]=col+2;
			index=index+1;
		}
		if(isBasicJumpValid(board,player,row,col,row+2,col-2))
		{
			moves[index][0]=row;
			moves[index][1]=col;
			moves[index][2]=row+2;
			moves[index][3]=col-2;
			index=index+1;
		}
		if(isBasicJumpValid(board,player,row,col,row+2,col+2))
		{
			moves[index][0]=row;
			moves[index][1]=col;
			moves[index][2]=row+2;
			moves[index][3]=col+2;
			index=index+1;
		}
	}
		
		return moves;
	}


	public static int[][] getAllBasicJumps(int[][] board, int player) {
		int[][] moves = null;
		int counter=0;
		for(int i=0;i<8;i=i+1){
			for(int j=0;j<8;j=j+1){ //counting the amount of possible jumps for a player
					if(isBasicJumpValid(board,player,i,j,i+2,j-2))
						counter=counter+1;
					if(isBasicJumpValid(board,player,i,j,i+2,j+2))
						counter=counter+1;
					if(isBasicJumpValid(board,player,i,j,i-2,j-2))
						counter=counter+1;
					if(isBasicJumpValid(board,player,i,j,i-2,j+2))	
						counter=counter+1;
			}			
		}				
		int index=0;				
		moves=new int [counter][4];	
		if(moves.length!=0){// if their are possible  jumps
			for(int i=0;i<8&&index<counter;i=i+1){
				for(int j=0;j<8&&index<counter;j=j+1){ //adds to the array the coordinates of the possible jumps
						if(isBasicJumpValid(board,player,i,j,i+2,j-2))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i+2;
							moves[index][3]=j-2;
							index=index+1;
						}
						if(isBasicJumpValid(board,player,i,j,i+2,j+2))
						{
					        moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i+2;
							moves[index][3]=j+2;
							index=index+1;
						}
						
						if(isBasicJumpValid(board,player,i,j,i-2,j-2))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i-2;
							moves[index][3]=j-2;
							index=index+1;
						}
						if(isBasicJumpValid(board,player,i,j,i-2,j+2))
						{
							moves[index][0]=i;
							moves[index][1]=j;
							moves[index][2]=i-2;
							moves[index][3]=j+2;
							index=index+1;
						}
							
				}
			}	
		}		
		return moves;
	}


	public static boolean canJump(int[][] board, int player) {
		boolean ans = false;
		int [][]arr=getAllBasicJumps(board,player); 
		if(arr.length!=0)// here we check if the array with the player's possible jumps is empty
			ans=true;
		return ans;
	}


	public static boolean isMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		boolean ans = false;
		if(( fromRow<0||fromRow>7)||( fromCol<0||fromCol>7)||( toRow<0||toRow>7)||( toCol<0||toCol>7))//checks if the coordinates are legal
			return ans;	
		if((board[fromRow][fromCol]!=player)&&(board[fromRow][fromCol]!=2*player))
			return ans;
		if(board[toRow][toCol]!=0)
			return ans;
		if((isBasicMoveValid(board,player,fromRow,fromCol,toRow,toCol)==false)&& (isBasicJumpValid(board,player,fromRow,fromCol,toRow,toCol)==false))	//it's  an illegal jump and an illegal move
			return ans;
		if((isBasicMoveValid(board,player,fromRow,fromCol,toRow,toCol))&& (canJump(board,player)))	//it's a legal move but the player can jump
		    return ans;
		ans=true;
		return ans;
	}


	public static boolean hasValidMoves(int[][] board, int player) {
		boolean ans = false;
		int [][]arr=getAllBasicMoves(board,player); //array of of all possible basic simple moves
		int [][]arr2=getAllBasicJumps(board,player); //array of of all possible simple jumps
		if(arr.length==0&&arr2.length==0) //if we have no jumps and no basic moves to make
			return ans;
		ans=true;
		return ans;		
	}


	public static int[][] playMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
		if((isBasicMoveValid(board,player,fromRow,fromCol,toRow,toCol))) //if the desired basic move is valid by game rules
		{
			if(board[fromRow][fromCol]==1&&toRow==7||board[fromRow][fromCol]==-1&&toRow==0) //becoming a queen so the move has special consequences
			{
					board[fromRow][fromCol]=0;
					board[toRow][toCol]=2*player;
			}			
			else
			{
				board[toRow][toCol]=board[fromRow][fromCol]; //change the board after the move-no new queen was made
				board[fromRow][fromCol]=0;
			}
		}
		else
		{
			if((isBasicJumpValid(board,player,fromRow,fromCol,toRow,toCol)))//if the desired basic move is valid by game rules
			{
				if(board[fromRow][fromCol]==1&&toRow==7||board[fromRow][fromCol]==-1&&toRow==0) //becoming a queen so the move has special consequences
				{
					board[fromRow][fromCol]=0;
					board[toRow][toCol]=2*player;
					board[(toRow+fromRow)/2][(toCol+fromCol)/2]=0;
				}			
				else
				{
					board[toRow][toCol]=board[fromRow][fromCol]; //change the board after the jump-no new queen was made
					board[fromRow][fromCol]=0;
					board[(toRow+fromRow)/2][(toCol+fromCol)/2]=0;
				}
		
			}
		}
	return board;
	}


	public static boolean gameOver(int[][] board, int player) {
	boolean ans = false;
	 if(hasValidMoves(board,player)) //if he has moves to make the game is not over if he has no discs-he has no more moves
		 return ans;
	ans=true;
	return ans;
	}


	public static int findTheLeader(int[][] board) {
	int ans = 0;
	int sum1=0; //sum of discs for player 1
	int sum2=0; //sum of discs for player -1
	for(int i=0;i<8;i=i+1)
		for(int j=0;j<8;j=j+1)//checking every disc on the board to what player it belongs
		{ 
			if(board[i][j]==1)
				sum1=sum1+1;
			if(board[i][j]==2)
				sum1=sum1+2;
			if(board[i][j]==-1)
				sum2=sum2+1;
			if(board[i][j]==-2)
				sum2=sum2+2;
		}
		if(sum1==sum2) //they've got the same amount of discs
			return ans;
		if(sum1>sum2) //player 1 has more disc
			ans=1;
		else
			ans=-1;
		return ans;
	}

	public static int[][] randomPlayer(int[][] board, int player) {
   if(hasValidMoves(board,player)==false) //if the player has no moves then return the board unchanged
	   	return board;
    int[][]jump=getAllBasicJumps(board, player);
	if(jump.length==0) //if the player has no jumps but can make a simple move
	{
		int [][]step=getAllBasicMoves(board,player);
		int n=(int)(Math.random()*step.length);
		board= playMove(board,player,step[n][0],step[n][1],step[n][2],step[n][3]); //make a random legal basic move
		return board;
	}
	else //the player can jump
	{

		int n=(int)(Math.random()*jump.length);
		board= playMove(board,player,jump[n][0],jump[n][1],jump[n][2],jump[n][3]);//make a random legal jump
		int[][]jump2=getRestrictedBasicJumps(board,player,jump[n][2],jump[n][3]);
		while(jump2.length!=0) //if the disc of player that jumped can now jump over more discs
		{	
			n=(int)(Math.random()*jump2.length);
			board= playMove(board,player,jump2[n][0],jump2[n][1],jump2[n][2],jump2[n][3]);    //make a random legal jump
			jump2=getRestrictedBasicJumps(board,player,jump2[n][2],jump2[n][3]); 
		 
			}	
		}
		
	return board;
	}
	
	
	private static boolean discEaten(int [][] board,int player,int fromRow,int fromCol,int toRow,int toCol) //checks if a disc that is moved will be possibly eaten by opponent 
	{
		boolean ans=false;
		int [][]temp2=new int[8][8];
				for(int i=0;i<8;i=i+1)
					for(int j=0;j<8;j=j+1)
						temp2[i][j]=board[i][j];
		if(isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol))
		{	
			int[][]tempBoard=playMove(temp2,player,fromRow,fromCol,toRow,toCol);
			if(isBasicJumpValid(tempBoard,player*(-1),toRow-1,toCol-1,toRow+1,toCol+1)) //checks all potential jumps of opponent over disc that was moved
				ans=true;
			if(isBasicJumpValid(tempBoard,player*(-1),toRow-1,toCol+1,toRow+1,toCol-1))
				ans=true;
			if(isBasicJumpValid(tempBoard,player*(-1),toRow+1,toCol-1,toRow-1,toCol+1))
				ans=true;
			if(isBasicJumpValid(tempBoard,player*(-1),toRow+1,toCol+1,toRow-1,toCol-1))
				ans=true;
		}
	return ans;

	}


	public static int[][] defensivePlayer(int[][] board, int player) {
		if(hasValidMoves(board,player)==false) //player has no moves so board unchanged and returned
			return board;
		else
				if(canJump(board,player)) 
				{
					board=randomPlayer(board,player); //if player can jump then make a random jump
					return board;
				}
				else //player can't jump but do a basic move
				{
					int[][] basicStep=getAllBasicMoves(board,player);
					int iNE=0;
					int iE=0;
					int counter=0;
					for(int i=0;i<basicStep.length;i=i+1) //check every possible basic move if he will be eaten or not
					{
						if(discEaten(board,player,basicStep[i][0],basicStep[i][1],basicStep[i][2],basicStep[i][3]))
							counter=counter+1;
					}
					int[][] notEaten=new int [basicStep.length-counter][4];
					int[][] doEaten=new int[counter][4];
					for(int i=0;i<basicStep.length;i=i+1)
					{
						if(discEaten(board,player,basicStep[i][0],basicStep[i][1],basicStep[i][2],basicStep[i][3]))
						{
							doEaten[iE][0]=basicStep[i][0];
							doEaten[iE][1]=basicStep[i][1];
							doEaten[iE][2]=basicStep[i][2];
							doEaten[iE][3]=basicStep[i][3];
							iE=iE+1;
						}
						else
						{
								notEaten[iNE][0]=basicStep[i][0];
								notEaten[iNE][1]=basicStep[i][1];
								notEaten[iNE][2]=basicStep[i][2];
								notEaten[iNE][3]=basicStep[i][3];	
								iNE=iNE+1;
						}
						
					}	
						if(notEaten.length!=0) //their are moves the player can make and he wont be eaten as a result of the move
						{
							int n=(int)(Math.random()*notEaten.length);
							board=playMove(board,player,notEaten[n][0],notEaten[n][1],notEaten[n][2],notEaten[n][3]); //chooses randomly a basic move 
						}
						else 
						{
							int n=(int)(Math.random()*doEaten.length);
							board=playMove(board,player,doEaten[n][0],doEaten[n][1],doEaten[n][2],doEaten[n][3]);
						}
					}
				
		return board;
		}		

	public static int[][] sidesPlayer(int[][] board, int player) {
		if(hasValidMoves(board,player)==false)//player has no moves so board unchanged and returned
			return board;
		if(canJump(board,player)){ 
			board=randomPlayer(board,player);  //if player can jump then make a random jump
			return board;
		}
		int index=4;
		int n=0;
		int[][] arr=getAllBasicMoves(board,player);	
		int[][] makom=new int[arr.length][2]; 
		int[] minDistance;
		int counter=0;
		
		for(int i=0;i<arr.length;i=i+1)
		{	
			int x=7-arr[i][3];
			int y=arr[i][3];	
			
			makom[i][0]=i;
			makom[i][1]=Math.min(x,y); //saves the minimum distance of a move from the sides
		}
		for(int j=0;j<makom.length;j=j+1)
		{
			if(makom[j][1]<index)
			{
				index=makom[j][1];
				n=j; // saves the line where the minimum distance for a moved disc is
			}
		}
		
		for(int j=0;j<makom.length;j=j+1)
		{		
			if(makom[j][1]==makom[n][1])	
			counter=counter+1; //how many discs have the same minimum distance 
		}
		if(counter==1)
		{ 
			board=playMove(board,player,arr[n][0],arr[n][1],arr[n][2],arr[n][3]);
			return board;
		}	
		else
		{
			minDistance=new int[counter]; 
			index=0;
			for(int i=0;i<makom.length;i=i+1)
				if(makom[i][1]==makom[n][1])
				{
				minDistance[index]=i;
				index=index+1;
				}
			int a=(int)(Math.random()*counter); //chooses randomly between moves that will put the player's disc at the same minimum distance from the sides
			board=playMove(board,player,arr[(minDistance[a])][0],arr[(minDistance[a])][1],arr[(minDistance[a])][2],arr[(minDistance[a])][3]);
		}
		return board;
	}







	
	
	
	
	
	//******************************************************************************//

	/* ---------------------------------------------------------- *
	 * Play an interactive game between the computer and you      *
	 * ---------------------------------------------------------- */
	public static void interactivePlay() {
		int[][] board = createBoard();
		showBoard(board);

		System.out.println("Welcome to the interactive Checkers Game !");

		int strategy = getStrategyChoice();
		System.out.println("You are the first player (RED discs)");

		boolean oppGameOver = false;
		while (!gameOver(board, RED) && !oppGameOver) {
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver) {
				EnglishCheckersGUI.sleep(200);

				board = getStrategyFullMove(board, BLUE, strategy);
			}
		}

		int winner = 0;
		if (playerDiscs(board, RED).length == 0  |  playerDiscs(board, BLUE).length == 0){
			winner = findTheLeader(board);
		}

		if (winner == RED) {
			System.out.println();
			System.out.println("\t *************************");
			System.out.println("\t * You are the winner !! *");
			System.out.println("\t *************************");
		}
		else if (winner == BLUE) {
			System.out.println("\n======= You lost :( =======");
		}
		else
			System.out.println("\n======= DRAW =======");
	}


	/* --------------------------------------------------------- *
	 * A game between two players                                *
	 * --------------------------------------------------------- */
	public static void twoPlayers() {
		int[][] board = createBoard();
		showBoard(board);

		System.out.println("Welcome to the 2-player Checkers Game !");

		boolean oppGameOver = false;
		while (!gameOver(board, RED)  &  !oppGameOver) {
			System.out.println("\nRED's turn");
			board = getPlayerFullMove(board, RED);

			oppGameOver = gameOver(board, BLUE);
			if (!oppGameOver) {
				System.out.println("\nBLUE's turn");
				board = getPlayerFullMove(board, BLUE);
			}
		}

		int winner = 0;
		if (playerDiscs(board, RED).length == 0  |  playerDiscs(board, BLUE).length == 0)
			winner = findTheLeader(board);

		System.out.println();
		System.out.println("\t ************************************");
		if (winner == RED)
			System.out.println("\t * The red player is the winner !!  *");
		else if (winner == BLUE)
			System.out.println("\t * The blue player is the winner !! *");
		else
			System.out.println("\t * DRAW !! *");
		System.out.println("\t ************************************");
	}


	/* --------------------------------------------------------- *
	 * Get a complete (possibly a sequence of jumps) move        *
	 * from a human player.                                      *
	 * --------------------------------------------------------- */
	public static int[][] getPlayerFullMove(int[][] board, int player) {
		// Get first move/jump
		int fromRow = -1, fromCol = -1, toRow = -1, toCol = -1;
		boolean jumpingMove = canJump(board, player);
		boolean badMove   = true;
		getPlayerFullMoveScanner = new Scanner(System.in);//I've modified it
		while (badMove) {
			if (player == 1){
				System.out.println("Red, Please play:");
			} else {
				System.out.println("Blue, Please play:");
			}

			fromRow = getPlayerFullMoveScanner.nextInt();
			fromCol = getPlayerFullMoveScanner.nextInt();

			int[][] moves = jumpingMove ? getAllBasicJumps(board, player) : getAllBasicMoves(board, player);
			markPossibleMoves(board, moves, fromRow, fromCol, MARK);
			toRow   = getPlayerFullMoveScanner.nextInt();
			toCol   = getPlayerFullMoveScanner.nextInt();
			markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

			badMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
			if (badMove)
				System.out.println("\nThis is an illegal move");
		}

		// Apply move/jump
		board = playMove(board, player, fromRow, fromCol, toRow, toCol);
		showBoard(board);

		// Get extra jumps
		if (jumpingMove) {
			boolean longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			while (longMove) {
				fromRow = toRow;
				fromCol = toCol;

				int[][] moves = getRestrictedBasicJumps(board, player, fromRow, fromCol);

				boolean badExtraMove = true;
				while (badExtraMove) {
					markPossibleMoves(board, moves, fromRow, fromCol, MARK);
					System.out.println("Continue jump:");
					toRow = getPlayerFullMoveScanner.nextInt();
					toCol = getPlayerFullMoveScanner.nextInt();
					markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

					badExtraMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol); 
					if (badExtraMove)
						System.out.println("\nThis is an illegal jump destination :(");
				}

				// Apply extra jump
				board = playMove(board, player, fromRow, fromCol, toRow, toCol);
				showBoard(board);

				longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
			}
		}
		return board;
	}


	/* --------------------------------------------------------- *
	 * Get a complete (possibly a sequence of jumps) move        *
	 * from a strategy.                                          *
	 * --------------------------------------------------------- */
	public static int[][] getStrategyFullMove(int[][] board, int player, int strategy) {
		if (strategy == RANDOM)
			board = randomPlayer(board, player);
		else if (strategy == DEFENSIVE)
			board = defensivePlayer(board, player);
		else if (strategy == SIDES)
			board = sidesPlayer(board, player);

		showBoard(board);
		return board;
	}


	/* --------------------------------------------------------- *
	 * Get a strategy choice before the game.                    *
	 * --------------------------------------------------------- */
	public static int getStrategyChoice() {
		int strategy = -1;
		getStrategyScanner = new Scanner(System.in);
		System.out.println("Choose the strategy of your opponent:" +
				"\n\t(" + RANDOM + ") - Random player" +
				"\n\t(" + DEFENSIVE + ") - Defensive player" +
				"\n\t(" + SIDES + ") - To-the-Sides player player");
		while (strategy != RANDOM  &  strategy != DEFENSIVE
				&  strategy != SIDES) {
			strategy=getStrategyScanner.nextInt();
		}
		return strategy;
	}


	/* --------------------------------------- *
	 * Print the possible moves                *
	 * --------------------------------------- */
	public static void printMoves(int[][] possibleMoves) {
		for (int i = 0;  i < 4;  i = i+1) {
			for (int j = 0;  j < possibleMoves.length;  j = j+1)
				System.out.print(" " + possibleMoves[j][i]);
			System.out.println();
		}
	}


	/* --------------------------------------- *
	 * Mark/unmark the possible moves          *
	 * --------------------------------------- */
	public static void markPossibleMoves(int[][] board, int[][] moves, int fromRow, int fromColumn, int value) {
		for (int i = 0;  i < moves.length;  i = i+1)
			if (moves[i][0] == fromRow  &  moves[i][1] == fromColumn)
				board[moves[i][2]][moves[i][3]] = value;

		showBoard(board);
	}


	/* --------------------------------------------------------------------------- *
	 * Shows the board in a graphic window                                         *
	 * you can use it without understanding how it works.                          *                                                     
	 * --------------------------------------------------------------------------- */
	public static void showBoard(int[][] board) {
		grid.showBoard(board);
	}


	/* --------------------------------------------------------------------------- *
	 * Print the board              					                           *
	 * you can use it without understanding how it works.                          *                                                     
	 * --------------------------------------------------------------------------- */
	public static void printMatrix(int[][] matrix){
		for (int i = matrix.length-1; i >= 0; i = i-1){
			for (int j = 0; j < matrix.length; j = j+1){
				System.out.format("%4d", matrix[i][j]);
			}
			System.out.println();
		}
	}

}
