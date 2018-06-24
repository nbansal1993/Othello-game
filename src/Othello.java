
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Othello {

	private static int board[][] = new int[8][8]; // empty is 0

	public static void main(String[] args) {
		othelloGame();
	}

	public static void othelloGame() {

		StringBuffer sb = new StringBuffer();
		initialiseBoard();
		printBoard(sb);

		Scanner scanner = new Scanner(System.in);
		int emptyCells = 60; // counter for empty blocks
		boolean isValidMove = false;
		String indices = "";
		int x = 0;
		int y = 0;
		int tempBoard[][] = new int[8][8];
		List<String> humanMoves = new ArrayList<>();
		List<String> compMoves = new ArrayList<>();

		while (boardNotFull(emptyCells)) {

			OthelloAgentUtils.copyBoard(board, tempBoard);
			humanMoves = OthelloAgentUtils.moveGenerator(tempBoard, 1);
			if (humanMoves.size() == 0) {
				System.out.println("No Possible moves for User");
			} else {
				
				// Take user input
				System.out.println("Please enter a co-ordinate");
				indices = scanner.next();
				try {
					x = Integer.parseInt(indices.split(",")[0]);
					y = Integer.parseInt(indices.split(",")[1]);
					
					// update board
					isValidMove = updateBoard(x, y, 1, tempBoard);
				} catch (Exception e) {
					System.out.println("The input should be of form x,y where x and y varies from 0 to 7");
					continue;
				}
				if (!isValidMove) {
					System.out.println("Invalid Move. Plase enter again a valid move");
					continue;
				} else {
					OthelloAgentUtils.copyBoard(tempBoard, board);
					printBoard(sb);
					emptyCells--;
				}
			}

			// Computer's move
			OthelloAgentUtils.copyBoard(board, tempBoard);
			compMoves = OthelloAgentUtils.moveGenerator(tempBoard, 2);
			if (compMoves.size() == 0) {
				if (humanMoves.size() == 0) {
					System.out.println("No more possible moves for both the players");
					break;
				}
				System.out.println("No Possible moves for Agent");
			} else {
				indices = OthelloAgentUtils.nextMove(tempBoard, compMoves);
				x = Integer.parseInt(indices.split(",")[0]);
				y = Integer.parseInt(indices.split(",")[1]);
				// update board
				isValidMove = updateBoard(x, y, 2, tempBoard);
				OthelloAgentUtils.copyBoard(tempBoard, board);
				System.out.println("Computer's move: " + indices);
				printBoard(sb);
				emptyCells--;
			}
		}
		scanner.close();
		checkWinner();
	}

	private static boolean boardNotFull(int emptyCells) {
		return emptyCells > 0;
	}

	private static void checkWinner() {

		int humanScore = 0;
		int compScore = 0;

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == 1)
					humanScore++;
				if (board[row][col] == 2)
					compScore++;
			}
		}
		if (compScore > humanScore )
			System.out.println("The winner is agent with score of " + compScore);
		else if (humanScore < compScore)
			System.out.println("The winner is user with score of " + humanScore);
		else
			System.out.println("Its a draw with both players having a score of " + compScore);

	}

	private static void printBoard(StringBuffer sb) {
		
		// Note: Square Board, so don't need to worry about row and col
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if(board[col][row] == 0)
					sb.append(". ");
				else if(board[col][row]==1)
					sb.append("w ");
				else
					sb.append("b ");
			}
			sb.append("\n");
		}
		sb.append("\n");
		System.out.println(sb.toString());
		sb.setLength(0);
	}

	private static void initialiseBoard() {
		board[3][3] = 1; // 1 is white
		board[3][4] = 2; // 2 is black
		board[4][3] = 2;
		board[4][4] = 1;
		System.out.println("Hi. Welcome to the Othello game. Your color is black (b). \n" +
				"To make a move enter a co-ordinate in form x,y where \n" +
				"x varies from 0 to 7 (left to right) \n" +
				"y varies from 0 to 7 (top to bottom)");
	}

	/*
	 * This function takes a temporary board, updates the move chosen by a
	 * player and then verifies if its valid move.
	 */

	public static boolean updateBoard(int row, int col, int value, int tempBoard[][]) {

		boolean isValidMove = false;
		
		int rowCounter = 0;
		int colCounter = 0;
		
		//If cell not empty, return false
		if(tempBoard[row][col]!=0)
			return isValidMove;
		
		//Update the cell with user move
		tempBoard[row][col] = value;

		boolean hasValue = false;

		//Check in same column above the given cell if update is required
		int tempRow = row - 1;
		int tempCol = col;
		while (tempRow >= 0) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempRow = tempRow - 1;
		}
		
		//update the cells in upwards direction as necessary
		if (hasValue && tempRow!=row-1) {
			for (rowCounter = tempRow; rowCounter < row; rowCounter++) {
				tempBoard[rowCounter][tempCol] = value;
				isValidMove = true;
			}
		}

		//Check in same column below the given cell if update is required
		tempRow = row + 1;
		tempCol = col;
		hasValue = false; // resetting

		while (tempRow < board.length) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempRow = tempRow + 1;
		}
		
		//update the cells in downwards direction as necessary
		if (hasValue && tempRow!=row+1) {
			for (rowCounter = row + 1; rowCounter < tempRow; rowCounter++) {
				tempBoard[rowCounter][tempCol] = value;
				isValidMove = true;
			}
		}

		//Check in same row left to the given cell if update is required
		tempRow = row;
		tempCol = col - 1;
		hasValue = false;

		while (tempCol >= 0) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol - 1;
		}
		//update the cells in left direction as necessary
		if (hasValue && tempCol!=col-1) {
			for (colCounter = tempCol; colCounter < col; colCounter++) {
				tempBoard[tempRow][colCounter] = value;
				isValidMove = true;
			}
		}

		//Check in same row right to the given cell if update is required
		tempRow = row;
		tempCol = col + 1;
		hasValue = false;

		while (tempCol < board[0].length) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol + 1;
		}
		
		//update the cells in right direction as necessary
		if (hasValue && tempCol!=col+1) {
			for (colCounter = col + 1; colCounter < tempCol; colCounter++) {
				tempBoard[tempRow][colCounter] = value;
				isValidMove = true;
			}
		}
		
		//Check in lower right diagonal to the given cell if update is required
		tempRow = row + 1;
		tempCol = col + 1;
		hasValue = false;

		while (tempRow < board.length && tempCol < board[0].length) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol + 1;
			tempRow = tempRow + 1;
		}
		
		//update the cells in lower right diagonal as necessary
		rowCounter = row + 1;
		colCounter = col + 1;
		if (hasValue && (tempRow!=row+1 && tempCol!=col+1)) {
			while (rowCounter < tempRow && colCounter < tempCol) {
				tempBoard[rowCounter][colCounter] = value;
				isValidMove = true;
				rowCounter++;
				colCounter++;
			}
		}

		//Check in lower left diagonal to the given cell if update is required
		tempRow = row + 1;
		tempCol = col - 1;
		hasValue = false;

		while (tempRow < board.length && tempCol >= 0) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol - 1;
			tempRow = tempRow + 1;
		}
		
		//update the cells in lower left diagonal as necessary
		rowCounter = row + 1;
		colCounter = col - 1;
		if (hasValue && (tempRow!=row+1 && tempCol!=col-1)) {
			while (rowCounter < tempRow && colCounter > tempCol) {
				tempBoard[rowCounter][colCounter] = value;
				isValidMove = true;
				rowCounter++;
				colCounter--;
			}
		}
		
		//Check in upper left diagonal to the given cell if update is required
		tempRow = row - 1;
		tempCol = col - 1;
		hasValue = false;

		while (tempRow >= 0 && tempCol >= 0) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol - 1;
			tempRow = tempRow - 1;
		}
		
		//update the cells in upper left diagonal as necessary
		rowCounter = row - 1;
		colCounter = col - 1;
		if (hasValue && (tempRow!=row-1 && tempCol!=col-1)) {
			while (rowCounter > tempRow && colCounter > tempCol) {
				tempBoard[rowCounter][colCounter] = value;
				isValidMove = true;
				rowCounter--;
				colCounter--;
			}
		}

		//Check in upper right diagonal to the given cell if update is required
		tempRow = row - 1;
		tempCol = col + 1;
		hasValue = false;

		while (tempRow >= 0 && tempCol < board[0].length) {
			if (tempBoard[tempRow][tempCol] == 0)
				break;
			else if (tempBoard[tempRow][tempCol] == value) {
				hasValue = true;
				break;
			}
			tempCol = tempCol + 1;
			tempRow = tempRow - 1;
		}
		
		//update the cells in upper right diagonal as necessary
		rowCounter = row - 1;
		colCounter = col + 1;
		if (hasValue && (tempRow!=row-1 && tempCol!=col+1)) {
			while (rowCounter > tempRow && colCounter < tempCol) {
				tempBoard[rowCounter][colCounter] = value;
				isValidMove = true;
				rowCounter--;
				colCounter++;
			}
		}
		return isValidMove;
	}
}
