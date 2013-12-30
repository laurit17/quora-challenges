public class DataCenterMap {
	int[][] map;
	int width, height;
	int zeroCount;
	int[] start, end;
	int[] numInRows, numInColumns;
	
	public DataCenterMap(int width, int height) { //creates an empty map. It is NOT ready for use
		zeroCount = width*height;
		this.width = width;
		this.height = height;
		map = new int[width][height];
		start = new int[2];
		end = new int[2];
		numInRows = new int[height];
		numInColumns = new int[width];
	}
	
	public int get(int w, int h) {
		return map[w][h];
	}
	
	public boolean winningPos(int w, int h) {
		return map[w][h] == 3 && zeroCount == 0;
	}
	
	public boolean badPos(int w, int h, boolean firstTurn) {
		
		if (outOfBounds(w, h)) {return true;}
		if (alreadyTaken(w, h)) {return true;}
		if (containsObstacle(w, h)) {return true;}
		if (isStart(w, h, firstTurn)) {return true;}
		if (endAndNotDone(w, h)) {return true;}
		return false;
		
	}
	
	public boolean goodToPlace(int w, int h, boolean firstTurn) {
		if (outOfBounds(w, h)) {return true;}
		if (alreadyTaken(w, h)) {return true;}
		if (containsObstacle(w, h)) {return true;}
		if (isStart(w, h, firstTurn)) {return true;}
		
		return false;
		
	}
	
	public boolean endStillFree() {
		int w = end[0];
		int h = end[1];
		if(badPos(w, h + 1, false) && badPos(w, h - 1, false) && badPos(w + 1, h, false)
				&& badPos(w - 1, h, false)) {
			return false;
		}
		
		return true;
	}
	

	public boolean checkDegree(int w, int h) {
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				if(i == w && j == h) {
					continue;
				}
				if(map[i][j] == 0) {
					if (getDegree(i, j) < 2) {
						return false;
					}
				}
				
			}
		}
		return true;
	}

	
	private boolean dumbCheck(int i, int j) {
		return !outOfBounds(i, j) && map[i][j] == 0;
		
	}
	
	public int getDegree(int i, int j) {
		if(map[i][j] != 0) {
			return 0;
		}
		int degree = 0;
		if(!goodToPlace(i + 1, j, false)) {
			degree++;
		}
		if(!goodToPlace(i - 1, j, false)) {
			degree++;
		}
		if(!goodToPlace(i, j - 1, false)) {
			degree++;
		}
		if(!goodToPlace(i, j + 1, false)) {
			degree++;
		}
		return degree;
		
	}
	
	private boolean alreadyTaken(int w, int h) {
		return map[w][h] == 4;
	}
	
	private boolean outOfBounds(int w, int h) {
		return w < 0 || h < 0 || w >= width || h >= height;
	}
	
	private boolean containsObstacle(int w, int h) {
		return map[w][h] == 1;
	}
	
	private boolean isStart(int w, int h, boolean firstTurn) {
		return map[w][h] == 2 && !firstTurn;
	}
	
	private boolean endAndNotDone(int w, int h) { 
		return map[w][h] == 3 && zeroCount != 0;
	}
	
	public void setSquare(int w, int h, int blot) {
		
		// set start and finish squares
		if(blot == 2) {
			start[0] = w;
			start[1] = h;
		} else if(blot == 3) {
			end[0] = w;
			end[1] = h;
		}
		
		if(map[w][h] == 0 && blot != 0) {
			zeroCount--;
			numInRows[h]++;
			numInColumns[w]++;
		} else if(map[w][h] != 0 && blot == 0) {
			zeroCount++;
			numInRows[h]--;
			numInColumns[w]--;
		}
		
		map[w][h] = blot;
	}
	
	public boolean trapped(int w, int h) {
		for(int i = w + 1; i < end[0]; i++) {
			if(columnFull(i)) {
				//System.out.println("Trapped: column " + i + " is full");
				return true;
			}
		}
		
		for(int j = h + 1; j < end[1]; j++) {
			if(rowFull(j)) {
				//System.out.println("Trapped: Row " + j + " is full");
				return true;
			}
		}
		
		return false;
	}
	
	public int hashCode() {
		int code = 0;
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				code = (5*code + map[i][j]) % 179425579;
			}
		}
		
		return code;
	}
	
	public boolean rowFull(int row) {
		return numInRows[row] == width;
	}
	
	public boolean columnFull(int column) {
		return numInColumns[column] == height;
	}
	
	
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				sb.append(map[i][j]);
				sb.append(' ');
			}
			sb.append('\n');
		}
		
		return sb.toString();
	}
	
	public void printNums() {
		System.out.println("Per row");
		System.out.print("{");
		for(int i = 0; i < numInRows.length; i++) {
			System.out.print(numInRows[i]);
			
			if(i != numInRows.length - 1) {
				System.out.print(", ");
			}
		}
		
		System.out.println("}");
		System.out.println();
		
		System.out.println("Per column");
		System.out.print("{");
		for(int i = 0; i < numInColumns.length; i++) {
			System.out.print(numInColumns[i]);
			
			if(i != numInColumns.length - 1) {
				System.out.print(", ");
			}
		}
		
		System.out.println("}");
		System.out.println();
	}

}
