package sumukh.is;

import java.util.Random;
import java.util.Scanner;

public class NQueenMain {
	
	public static int numberOfQueens;
	public static int iterationNumber = 0;
	public static int numberOfSteps = 0;
	public static int numberOfRandomRestarts = 0;
	public static int counter = 0;
	
	public static int numberOfStepsMinConflict = 0;
	public static int numberOfRandomRestartsMinConflict = 0;
	
	public static int totalStepsHillClimbing = 0;
	public static int totalStepsMinConflict = 0;
	
	//Main Function
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int choice;
		System.out.println("Enter the value of N: ");
		Scanner scanner = new Scanner(System.in);
		numberOfQueens = scanner.nextInt();
		System.out.println("Enter your choice of the Algorithm: \n1. Random Restart Hill Climbing\t2. Min-Conflicts\t3. Both");
		choice = scanner.nextInt();
		if ((choice <= 0) || (choice > 3)) {
			System.out.println("Wrong Choice. Please restart program.");
			scanner.close();
			return;
		}
		if (numberOfQueens == 2 || numberOfQueens == 3) {
			System.out.println("Atleast 4 Queens are required. Please restart program.");
			scanner.close();
			return;
		}
		scanner.close();
		Node temp = generateRandomState();
		System.out.println("Solution: \n==============================================\n");
		if (choice == 1 || choice == 3) {
			System.out.println("RANDOM RESTART HILL CLIMBING: \n");
			Node start = new Node(temp.getBoard());
			Node next = null;

			//start computation
			while (start.getHeuristicValue() != 0 && ++NQueenMain.counter != 500000) {
				next = start.getSuccessorFromNeighbours();
				if (next != null) {
					++NQueenMain.numberOfSteps;
					start = next;
				}

				//RANDOM RESTART if stuck
				else {
					NQueenMain.iterationNumber = 0;
					NQueenMain.numberOfSteps = 0;
					++NQueenMain.numberOfRandomRestarts;
					start = generateRandomState();
				}
			}

			if (NQueenMain.counter >= 500000) {
				System.out.println("Timed Out!");
			} else {
				start.printState();
				System.out.println();
				System.out.println("Number of random restarts: " + NQueenMain.numberOfRandomRestarts);
				System.out.println("Number of steps: " + NQueenMain.numberOfSteps);
				System.out.println("Total number of tries for Hill Climbing: " + NQueenMain.totalStepsHillClimbing);
			}
		}
		
		System.out.println("\n\n==============================================\n\n");
		//Min Conflicts
		if (choice == 2 || choice == 3) {
			System.out.println("MIN-CONFLICTS: \n");
			int count = 0;
			Node startMinConflicts = new Node(temp.getBoard());
			while (++count <= 500) {
				++NQueenMain.numberOfRandomRestartsMinConflict;
				NQueenMain.numberOfStepsMinConflict = 0;
				startMinConflicts = startMinConflicts.computeMinConflicts(startMinConflicts);
				if (startMinConflicts != null) {
					startMinConflicts.printState();
					System.out.println();
					System.out.println("Number of random restarts: " + NQueenMain.numberOfRandomRestartsMinConflict);
					System.out.println("Number of steps for Min Conflicts: " + NQueenMain.numberOfStepsMinConflict);
					System.out.println("Total number of tries for Min-Conflicts: " + NQueenMain.totalStepsMinConflict);
					break;
				}
				startMinConflicts = generateRandomState();
			}
		}
	}


	// Generates a random state for an N*N board, with N Queens
	public static Node generateRandomState() {
		
		Random random = new Random();
		int[][] array = new int[numberOfQueens][numberOfQueens];
		for (int i = 0; i < numberOfQueens; i++) {
			array[random.nextInt(numberOfQueens)][i] = 1;
		}
		for (int i = 0; i < numberOfQueens; i++) {
			for (int j = 0; j < numberOfQueens; j++) {
				if (array[i][j] != 1) {
					array[i][j] = 0;
				}
			}
		}
		return new Node(array);
	}

}
