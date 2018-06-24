
import java.util.ArrayList;
import java.util.List;

public class OthelloAgentUtils {

	private static int maxDepth = 2;

	public static void copyBoard(int board[][], int temp[][]) {
		for (int row = 0; row < board.length; row++) {
			temp[row] = board[row].clone();
		}
	}

	/*
	 * The following function moveGenerator takes the current state of board and
	 * the value (whose turn is it) and returns a list of all possible valid moves for
	 * that player.
	 */
	public static List<String> moveGenerator(int board[][], int value) {
		List<String> moves = new ArrayList<String>();
		boolean isValidMove = false;
		int tempBoard[][] = new int[8][8];

		copyBoard(board, tempBoard);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == 0) {
					isValidMove = Othello.updateBoard(row, col, value, tempBoard);
				} else
					isValidMove = false;
				if (isValidMove)
					moves.add(row + "," + col);
				copyBoard(board, tempBoard);
			}
		}
		return moves;
	}

	/*
	 * The following function evaluator takes the current state of board and returns a heuristic value for it.
	 * the heuristic value > 0 if computer has more score at that stage than human
	 */
	public static int evaluator(int board[][]) {
		int heuristic = 0;
		int human = 0;
		int comp = 0;

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == 1)
					human++;
				if (board[row][col] == 2)
					comp++;
			}
		}
		heuristic = comp - human;
		return heuristic;

	}

	/*
	 * This function takes the current state and all possible moves for the computer and then uses alphaBeta search
	 * to decide the best possible move for the computer.
	 */
	public static String nextMove(int board[][], List<String> moves) {
		String nextMove = "";
		int tempBoard[][] = new int[8][8];
		int bestScore = Integer.MIN_VALUE;
		int score = Integer.MIN_VALUE;

		for (String move : moves) {
			copyBoard(board, tempBoard);
			Othello.updateBoard(Integer.parseInt(move.split(",")[0]), Integer.parseInt(move.split(",")[1]), 2, tempBoard);
			score = alphaBeta(tempBoard, 1, 2);
			if (score > bestScore) {
				bestScore = score;
				nextMove = move;
			}
		}
		return nextMove;
	}

	public static int alphaBeta(int board[][], int depth, int value) {
		int tempBoard[][] = new int[8][8];
		int bestScore = Integer.MIN_VALUE;
		int score = Integer.MIN_VALUE;
		int newValue = value == 1 ? 2 : 1;
		if (depth > maxDepth)
			return evaluator(board);
		List<String> moves = moveGenerator(board, newValue);
		if (moves.isEmpty())
			return alphaBeta(board, depth + 1, newValue);
		else {
			for (String move : moves) {
				copyBoard(board, tempBoard);
				Othello.updateBoard(Integer.parseInt(move.split(",")[0]),
						Integer.parseInt(move.split(",")[1]), newValue,
						tempBoard);
				score = alphaBeta(tempBoard, depth + 1, newValue);
				if (score > bestScore) {
					bestScore = score;
				}
			}
			return bestScore;
		}

	}
}
