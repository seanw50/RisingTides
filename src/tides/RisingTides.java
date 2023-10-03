package tides;

import java.util.*;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {

        /* WRITE YOUR CODE BELOW */
        double min = terrain[0][0];
        double max = terrain[0][0];

        double current;
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                 current = terrain[i][j];
                 if (current < min){
                    min = current;
                }
                if (current > max) {
                    max = current;
                }
            }
        }
        double[] extrema = {min, max};
        return extrema;
     // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
        int rows = terrain.length;
        int cols = terrain[0].length;
        boolean[][] resultingArray = new boolean[rows][cols];
        ArrayList<GridLocation> grid = new ArrayList<GridLocation>();
    
        for (int i = 0; i < sources.length; i++) {
            grid.add(sources[i]);
            resultingArray[sources[i].row][sources[i].col] = true;
        }
    
        while (!grid.isEmpty()) {
            GridLocation cur = grid.remove(0);
    
            int row = cur.row;
            int col = cur.col;
    
            if (row - 1 >= 0 && terrain[row - 1][col] <= height && !resultingArray[row - 1][col]) {
                resultingArray[row - 1][col] = true;
                grid.add(new GridLocation(row - 1, col));
            }

            if (row + 1 < rows && terrain[row + 1][col] <= height && !resultingArray[row + 1][col]) {
                resultingArray[row + 1][col] = true;
                grid.add(new GridLocation(row + 1, col));
            }

            if (col - 1 >= 0 && terrain[row][col - 1] <= height && !resultingArray[row][col - 1]) {
                resultingArray[row][col - 1] = true;
                grid.add(new GridLocation(row, col - 1));
            }

            if (col + 1 < cols && terrain[row][col + 1] <= height && !resultingArray[row][col + 1]) {
                resultingArray[row][col + 1] = true;
                grid.add(new GridLocation(row, col + 1));
            }
        }
    
        return resultingArray;
    }
    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */
        // get data from floodedRegionsIn(height)
          // this data contains each cell where the water is aboe the height
          // using this data we would check [cell.x, cell.y] return what is inside that data and see if it is true
        boolean[][] floodedRegions = floodedRegionsIn(height);
        System.out.println(floodedRegions);

        return floodedRegions[cell.row][cell.col];
        // return false; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        
        // check the current height of the water 
        double currentHeight = terrain[cell.row][cell.col];
        return currentHeight-height;
        // // use that height inside the cell 
        // return -1; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        //find which parts are underwater
        boolean[][] floodedRegions = floodedRegionsIn(height);
        int counter = 0;

        
        //go through the grid and check each piece of land using for loosp
        for( int row =0; row< terrain.length; row++){
            for(int col = 0; col < terrain[0].length; col++){
                if(!floodedRegions[row][col]){
                    counter++;
                }
            }

        }
        /* WRITE YOUR CODE BELOW */
        // return -1; // substitute this line. It is provided so that the code compiles.
        return counter;
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        int current = totalVisibleLand(height);
        int future = totalVisibleLand(newHeight);
        return current- future;


        /* WRITE YOUR CODE BELOW */
        // return -1; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        //initialize arrays and variables for union find
        //weighted quick union
        int rows = terrain.length;
        int cols = terrain[0].length;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(rows, cols);
        boolean[][] floodedRegions = floodedRegionsIn(height);
        //iterate through terrain, check each cell if its below or equal to water height
        for(int i = 0; i <  terrain.length; i++){
            for(int j = 0; j < terrain[0].length; j++){
                 //how to check if cell its above water?
                if(!floodedRegions[i][j]){
                //examine neighboring cells in eight directions, up,down,left,right,diagonal
                    int []dirRow= {-1,-1,-1,0,0,1,1,1};
                    int []dirCol ={-1,0,1,-1,1,-1,0,1};
                    for(int f = 0; f < 8; f++){
                        int x = i+dirRow[f];
                        int y = j+dirCol[f];

                        if (x >= 0 && x < rows && y >= 0 && y < cols && !floodedRegions[x][y]) {
                                // union operation for connected cells
                                //if neighboring cell is not on water then perform union operation. connect current cell and the neighboring cell

                            uf.union(new GridLocation(i, j), new GridLocation(x,y));
                        }
                    }
                }
            }
        }
        HashSet<GridLocation> islands = new HashSet<GridLocation>();
        // iterate thru terrains
        for(int i = 0; i < terrain.length; i++){
            for(int j = 0; j < terrain[0].length; j ++){
                if(!floodedRegions[i][j]){
                    islands.add (uf. find (new GridLocation(i,j))) ;

                }
            }
        }
        return islands.size(); 
        //after iterating thru entire terrain count the number of unique roots
        //determine and return count



        /* WRITE YOUR CODE BELOW */
         // substitute this line. It is provided so that the code compiles.
    
    }
}
