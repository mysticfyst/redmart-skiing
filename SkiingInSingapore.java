import java.util.Scanner;

public class SkiingInSingapore {
	
	public class DataClass{
		int localMinimaValue;
		int ongoingLength;
		
		DataClass(int a, int b){
			localMinimaValue = a;
			ongoingLength = b;
		}
		
		public DataClass copy() {
			return new DataClass(localMinimaValue, ongoingLength);
		}
		
		public void incrementLength() {
			ongoingLength++;
		}
	}
	//size1 = rows in the matrix ; size2 = columns in the matrix 
	int size1, size2;
	//double dimentional array to store the map
	int[][] theGrid;
	//theScore[][] stores the values already computed for future use
	DataClass[][] theScore;
	int maxLength = 0, maxDrop = 0;
	
	SkiingInSingapore(int size1, int size2){
		this.size1 = size1;
		this.size2 = size2;
		theGrid = new int[size1][size2];
		theScore = new DataClass[size1][size2];
	}
	
	public boolean canGoNorth(int i, int j) {
		return i>0 && theGrid[i][j] > theGrid[i-1][j];
	}
	
	public boolean canGoSouth(int i, int j) {
		return i+1<size1 && theGrid[i][j] > theGrid[i+1][j];
	}
	
	public boolean canGoWest(int i, int j) {
		return j>0 && theGrid[i][j] > theGrid[i][j-1];
	}
	
	public boolean canGoEast(int i, int j) {
		return j+1<size2 && theGrid[i][j] > theGrid[i][j+1];
	}
	
	public DataClass returnMax(DataClass[] nsew) {
		DataClass max = nsew[0];
		for(int i = 1; i < 4; i++) {
			if(max==null) {
				max = nsew[i];
			}
			else {
				if(nsew[i]!=null) {
					if(max.ongoingLength < nsew[i].ongoingLength) {
						max = nsew[i];
					}
					else if(max.ongoingLength == nsew[i].ongoingLength && max.localMinimaValue > nsew[i].localMinimaValue) {
						max = nsew[i];
					}
				}
			}
		}
		return max;
	}
	
	
	/*
 	* The function skiAway() uses a dfs like logic to calculate the 'score' for each point in the grid. 
 	* The 'Score' for each point in the grid is the longest one can ski from that point.
 	* Uses memoization to avoid recalculation of the same subproblems. The values are stored in theScore[][].
 	*/
	public DataClass skiAway(int i, int j) {
		//if the result for (i,j) has already been computed before
		if(theScore[i][j]!=null) {
			return theScore[i][j].copy();
		}
		//index of north, south, east, west is 0, 1, 2 and 3 respectively and nsew stands for northSouthEastWest
		DataClass[] nsew = new DataClass[4];
		boolean b = true;
		if(canGoNorth(i,j)) {
			nsew[0] = skiAway(i-1,j);
			nsew[0].incrementLength();
			b = false;
		}
		if(canGoSouth(i,j)) {
			nsew[1] = skiAway(i+1,j);
			nsew[1].incrementLength();
			b = false;
		}
		if(canGoWest(i,j)) {
			nsew[3] = skiAway(i,j-1);	
			nsew[3].incrementLength();
			b = false;
		}
		if(canGoEast(i,j)) {
			nsew[2] = skiAway(i,j+1);
			nsew[2].incrementLength();
			b = false;
		}
		theScore[i][j] = returnMax(nsew);
		if(b) {theScore[i][j] = new DataClass(theGrid[i][j],1);}
		if(theScore[i][j].ongoingLength > maxLength || 
				(theScore[i][j].ongoingLength == maxLength && maxDrop < (theGrid[i][j] - theScore[i][j].localMinimaValue))) {
			maxLength = theScore[i][j].ongoingLength;
			maxDrop = theGrid[i][j] - theScore[i][j].localMinimaValue;
		}
		return theScore[i][j].copy();
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		//input
		SkiingInSingapore ob = new SkiingInSingapore(s.nextInt(),s.nextInt());
		for(int i = 0; i < ob.size1; i++) {
			for(int j = 0; j < ob.size2; j++) {
				ob.theGrid[i][j] = s.nextInt();
			}
		}
		//run skiAway() for every point in the grid
		for(int i = 0; i < ob.size1; i++) {
			for(int j = 0; j < ob.size2; j++) {
				if(ob.theScore[i][j]==null) {
					ob.skiAway(i, j);
				}
			}
		}
		System.out.println("\n"+ob.maxLength+""+ob.maxDrop);
	}
}

