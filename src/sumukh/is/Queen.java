package sumukh.is;

public class Queen {
	
	private int row;
	private int col;
	
	//Holds the position of the Queen (row and column value) of a particular state
	public Queen(int argRow, int argCol) {
		this.row = argRow;
		this.col = argCol;
	}
	
	//Returns row
	public int getRow() {
		return row;
	}

	//Returns column
	public int getCol() {
		return col;
	}
	
	//Checks if this Queen can attack the mentioned Queen
	public boolean canAttack(Queen argQueen) {
		if ((row == argQueen.getRow()) || (col == argQueen.getCol())) {
			return true;
		}
		else if (Math.abs(col - argQueen.getCol()) == Math.abs(row - argQueen.getRow())) {
			return true;
		}
		return false;
	}
}
