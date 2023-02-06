

import java.io.*;
import java.util.*;

public class Puzzle {
    // the dimension of the puzzle grid
    public static final int DIM = 9;
    
    // the dimension of the smaller subgrids within the grid
    public static final int SUBGRID_DIM = 3; 
    
    // The current contents of the cells of the puzzle. 
    // values[r][c] gives the value in the cell at row r, column c.
    // The rows and columns are numbered from 0 to DIM-1.
    private int[][] values;
    
    // Indicates whether the value in a given cell is fixed 
    // (i.e., part of the original puzzle).
    // valIsFixed[r][c] is true if the value in the cell 
    // at row r, column c is fixed, and valIsFixed[r][c] is false 
    // if the value in that cell is not fixed.
    private boolean[][] valIsFixed;
    
    // This 3-D array allows us to determine if a given
    // subgrid (i.e., a given SUBGRID_DIM x SUBGRID_DIM region 
    // of the puzzle) already contains a given value.
    // We use 2 indices to identify a given subgrid.
    // For example:
    //
    //    (0,0)   (0,1)   (0,2)
    //
    //    (1,0)   (1,1)   (1,2)
    // 
    //    (2,0)   (2,1)   (2,2)
    // 
    // For example, subgridHasValue[0][2][5] will be true if
    // the subgrid in the upper right-hand corner already has
    // a 5 in it, and false otherwise.
    //
    // If a given cell of the board has indices [r][c], it falls
    // within the subgrid with indices [r/3][c/3] using integer
    // division.
    //
    private boolean[][][] subgridHasValue;
    
    // XXX: add your additional fields here. 
    // In particular, we recommend adding fields to keep track 
    // of whether a given row or column already contains a given value.
    // You should be able to use a similar approach to what we've
    // done for the subgrids, but it will be simpler, because each
    // row and column can be identified by a single integer.
    private boolean[][] row_value;
    private boolean[][] col_value;
    
    /* 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Puzzle() {
        this.values = new int[DIM][DIM];
        this.valIsFixed = new boolean[DIM][DIM];
        this.row_value= new boolean[DIM][DIM + 1];
        this.col_value= new boolean[DIM][DIM + 1];
        
        // Note that the third dimension of the array has a length
        // of DIM + 1, because we want to be able to use the possible
        // values (1 through 9) as indices.
        this.subgridHasValue = new boolean[SUBGRID_DIM][SUBGRID_DIM][DIM + 1];        
        
       
    }
    
    /*
     * This is the key recursive-backtracking method.
     * Returns true if a solution has been found, and false otherwise.
     * 
     
     */
    private boolean solveRB(int n) {
        if (n==81) {
            this.display();
            return true;
        }
        if (this.valIsFixed[n/9][n%9])  {
            return (this.solveRB(n+1));
        }
        
        for (int i = 1; i <= DIM; i++) {
            
            if (this.isSafe(n/9,n%9, i)) {
                this.placeVal(i, n/9,n%9);
                if (this.solveRB(n+1)) {
                    return true;
                }
            this.removeVal(i, n/9,n%9);
            }
        }
        return false; 
    }
    
    /*
     * public "wrapper" method for the private solve() method above.
     * Makes the initial call to that method, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    /*
     * place the specified value in the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.values[row][col] = val;
        this.subgridHasValue[row/SUBGRID_DIM][col/SUBGRID_DIM][val] = true;
        
        
        this.col_value[col][val]=true;
        this.row_value[row][val]=true;
    }
    
    /*
     * remove the specified value from the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.values[row][col] = 0;
        this.subgridHasValue[row/SUBGRID_DIM][col/SUBGRID_DIM][val] = false;
        
       
        this.col_value[col][val]=false;
        this.row_value[row][val]=false;
    }
    
    /*
     * Reads in a puzzle specification from the specified Scanner,
     * and uses it to initialize the state of the puzzle.  The
     * specification should consist of one line for each row, with the
     * values in the row specified as digits separated by spaces.  A
     * value of 0 is used to indicate an empty cell.
     */ 
    public void readFrom(Scanner input) {
        for (int r = 0; r < DIM; r++) {
            for (int c = 0; c < DIM; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
    
    /*
     * Displays the current state of the puzzle.
     */
    public void display() {
        for (int r = 0; r < DIM; r++) {
            printRowSeparator();
            for (int c = 0; c < DIM; c++) {
                System.out.print("|");
                if (this.values[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.values[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        printRowSeparator();
    }
    
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < DIM; i++) {
            System.out.print("----");
        }
        System.out.println("-");
    }

    private boolean isSafe(int r, int c, int i) {
        //keeps track of whether a given row already contains a given value, and whether a given column or subgrid already contains a given value
        return (!(this.col_value[c][i] || this.row_value[r][i] || this.subgridHasValue[r/3][c/3][i]));
                 
    }
}