import java.util.Random;

public class Matrix {
    private boolean[][] cases;
    private static int DIM = 3;
    int minesNumber = 4;

    public int getDIM() {
        return DIM;
    }

    public void setDim(int dim){
        DIM = dim;
    }

    public void setMines(int mines){
        minesNumber = mines;
    }

    public boolean getCase(int i, int j) {
        return cases[i][j];
    }

    Matrix(){
        cases = new boolean[DIM][DIM];
        fillRandomly();
        //compute

        //display
    }

    Matrix(int nb){
        minesNumber = nb;
        cases = new boolean[DIM][DIM];
    }

    Matrix(int nb, int dim){
        minesNumber = nb;
        this.DIM =dim;
        cases = new boolean[DIM][DIM];
        fillRandomly();
    }

    /**
     * place randomly mines in the matrix
     */
    void fillRandomly(){
        Random generator = new Random();
        int count = 0;
        //pick a random case
        
        for(int i = 0; i < cases.length; i++){  // cases.length
            for(int j = 0; j < cases[0].length; j++){  // cases[0].length
               cases[i][j] = false;
            }
        }

        do{
            int i = generator.nextInt(DIM);
            int j = generator.nextInt(DIM);
            if(!cases[i][j]){
                cases[i][j] = true;
                count++;
            } 
        }while(count < minesNumber);
        
    }

    void changeMatrix(){
        fillRandomly();
        placeNumbersDisplay();
    }

    /**
     * display the matrix
     */
    void display(){
        for(int i = 0; i < DIM; i++){
            for(int j = 0; j < DIM; j++){
                if(cases[i][j]){
                    System.out.print("|X");
                }else{  
                    System.out.print("| ");
                }
            }
            System.out.println("|");
        } 
    }


    /**
     * compute number of mines in the matrix
     */
    int computeMinesNumber(){ 
        //for each case
        //if case is not a mine
        minesNumber = 0;
        for(int i = 0; i < DIM; i++){
            for(int j = 0; j < DIM; j++){
                if(cases[i][j]){
                    minesNumber++;
                }
            }
        }
        return minesNumber;
    }
    
    /**
     * compute number of mines around a case
     */

     int computeMinesAround(int row, int col){
         //for each case
         //if case is not a mine
         //compute number of mines around
         int minesArround = 0;
         if(!cases[row][col]){
            for(int i = row-1; i <= row+1 ; i++){
                for(int j = col-1; j <= col+1; j++){
                    if(i < 0 || i >= DIM || j < 0 || j >= DIM){
                        continue;
                    }
                    if(cases[i][j]){
                        minesArround++;
                    }
                }
            }
            //System.out.println("there are: "+ minesArround + " mines around");
            
        //  }else{
        //      System.out.println("This is a mine");
        //  }
         
        }return minesArround;
    }

void placeNumbersDisplay(){
    // for each case replace the boolean value by the number of mines around
    // if case is a mine do nothing
    // display the matrix
    for(int i = 0; i < cases.length; i++){
            for(int j = 0; j < cases[0].length; j++){
                if(cases[i][j]){
                    System.out.print("| X");
                }else{  
                    System.out.print("| "+computeMinesAround(i, j));
                }
                System.out.print( " ");
            }
            System.out.println("|");
            System.out.println();
        } 
    }
}