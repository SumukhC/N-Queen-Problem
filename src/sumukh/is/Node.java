package sumukh.is;

import java.util.ArrayList;
import java.util.Random;

public class Node {
	
	private int numberOfQueens;
	private int[][] board;
	private ArrayList<Node> neighbours;
	private Queen[] queens;
	private int heuristic;
	private ArrayList<Node> possibleStatesMinConflicts;
	
	//Constructor - Node is a state in the state space
	public Node(int[][] argBoard) {
		this.numberOfQueens = NQueenMain.numberOfQueens;
		this.queens = new Queen[numberOfQueens];
		this.heuristic = 0;
		this.neighbours = new ArrayList<>();
		this.possibleStatesMinConflicts = new ArrayList<>();
		this.board = new int[numberOfQueens][numberOfQueens];
		for (int i = 0; i < numberOfQueens; i++) {
			for (int j = 0; j < numberOfQueens; j++) {
				board[i][j] = argBoard[i][j];
				if (board[i][j] == 1) {
					queens[j] = new Queen(i, j);
				}
			}
		}
		calculateHeuristicValue();
	}
	
	public int[][] getBoard() {
		return board;
	}

	//Calculates the heuristic value for this state
	public void calculateHeuristicValue() {
		for (int i = 0; i < numberOfQueens; i++) {
			for (int j = i + 1; j < numberOfQueens; j++) {
				if (queens[i].canAttack(queens[j])) {
					this.heuristic++;
				}
			}
		}
	}
	
	//Returns the heuristic value
	public int getHeuristicValue() {
		return this.heuristic;
	}
	
	//Picks out the next best state
	public Node getSuccessorFromNeighbours() {
		generateCombinations();
		Node successor = this;
		for (Node node : neighbours) {
			if (node.getHeuristicValue() <= successor.getHeuristicValue()) {
				successor = node;
				if (NQueenMain.iterationNumber == (numberOfQueens - 1)) {
					return null;
				}	
			}
		}
		if (successor != this)
			++NQueenMain.totalStepsHillClimbing;
		NQueenMain.iterationNumber++;
		return successor;
	}
	
	//Given a state, it picks out the prospective states
	public void generateCombinations() {
		int[][] temp = new int[numberOfQueens][numberOfQueens];
		for (int i = 0; i < numberOfQueens; i++) {
			for (int j = 0; j < numberOfQueens; j++) {
				temp[i][j] = board[i][j];
			}
		}
		
		Queen queenInConsideration = queens[NQueenMain.iterationNumber];
		
		for (int i = 0; i < numberOfQueens; i++) {
			if (i != queenInConsideration.getRow()) {
				moveQueen(i, queenInConsideration.getRow(), queenInConsideration.getCol(), temp);
			}
		}
		neighbours.add(this);
	}
	
	//Moves the queen in the particular column to generate new combinations
	private void moveQueen(int i, int row, int col, int[][] temp) {
		int[][] t = new int[numberOfQueens][numberOfQueens];
		
		for (int k = 0; k < numberOfQueens; k++) {
			for (int j = 0; j < numberOfQueens; j++) {
				t[k][j] = temp[k][j];
			}
		}
		t[i][col] = 1;
		t[row][col] = 0;
		neighbours.add(new Node(t));
		
	}
	
	//Prints the state
	public void printState() {
		for (int i = 0; i < numberOfQueens; i++) {
			for (int j = 0; j < numberOfQueens; j++) {
				if (board[i][j] == 1) {
					System.out.print("Q ");
				}
				else {
					System.out.print("* ");
				}
			}
			System.out.println();
		}
	}
	
	//Starting function for the Min-Conflicts algorithm
	public Node computeMinConflicts(Node node) {
		Random random = new Random();
		int steps = 0;
		while ((node.getHeuristicValue() != 0) && (steps < (numberOfQueens * numberOfQueens))) {
			++steps;
			int column = random.nextInt(numberOfQueens);
			Queen queen = node.queens[column];
			node = getNextBestState(node, queen);
//			++NQueenMain.numberOfStepsMinConflict;
//			++NQueenMain.totalStepsMinConflict;
		}
		if ((node.getHeuristicValue() != 0) && steps >=5000) {
			return null;
		} else if (node.getHeuristicValue() == 0) {
			return node;
		} else {
			return null;
		}
		
	}
	
	//Returns the next best move
	private Node getNextBestState(Node node, Queen queen) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfQueens; i++) {
			if (i != queen.getRow()) {
				moveQueenMinConflicts(i, queen.getRow(), queen.getCol(), node.board);
			}
		}
		possibleStatesMinConflicts.add(node);
		return getSuccessorFromNeighboursForMinConflicts(node);

	}
	
	//Moves the Queen to check for possible next steps
	private void moveQueenMinConflicts(int i, int row, int col, int[][] temp) {
		int[][] t = new int[numberOfQueens][numberOfQueens];
		
		for (int k = 0; k < numberOfQueens; k++) {
			for (int j = 0; j < numberOfQueens; j++) {
				t[k][j] = temp[k][j];
			}
		}
		t[i][col] = 1;
		t[row][col] = 0;
		possibleStatesMinConflicts.add(new Node(t));
		
	}
	
	//Picks out the best option from a bunch of options
	public Node getSuccessorFromNeighboursForMinConflicts(Node argNode) {
		Node successor = argNode;
		for (Node node : possibleStatesMinConflicts) {
			if (node.getHeuristicValue() <= successor.getHeuristicValue()) {
				successor = node;		
			}
		}
		if (successor != argNode) {
			++NQueenMain.numberOfStepsMinConflict;
			++NQueenMain.totalStepsMinConflict;
		}
		return successor;
	}
}
