import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {
    private int N;
    private int countOpen;
    private boolean [][] open;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufbs;                  //ufbs is used for back wash problem
    private final int top;                          //virtual top site
    private final int bottom;                 //virtual bottom site

    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        //Corner case:
       if (N <= 0) {
           throw new IllegalArgumentException("N must be greater than 0");
       }

       //Create N-by-N grid
        this.N = N;
        this.open = new boolean[N][N];
        this.countOpen = 0;
        this.top = 0;
        this.bottom = N*N + 1;


       //All sites blocked
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++) {
                open[i][j] = false;
            }
        }

        //Create a uf with size of N*N and top&bottom sites
        uf = new WeightedQuickUnionUF(N*N + 2);

        //Create a uf with size of N*N and top site only
        ufbs = new WeightedQuickUnionUF(N*N + 1);
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        //check if row/col out of bound
        if (row < 0 || row >= N || col < 0 || col >= N)
            throw new IndexOutOfBoundsException("Argument is outside its prescribed range.");

        //Open site if not open already
        if (!isOpen(row, col)) {
            open[row][col] = true;
            countOpen++;

            //Connect both ufs to virtual top site
            if (row == 0){
                uf.union(encode(row,col), top);
                ufbs.union(encode(row,col), top);
            }

            //Connect uf only to virtual bottom site
            //not ufbs (because it only connects with top site)
            if (row == N-1)
                uf.union(encode(row, col), bottom);
        }

        //Connect the site to other corresponding sites which are open
        if (row > 0 && isOpen(row-1,col)) { //check up
            uf.union(encode(row, col), encode(row - 1, col));
            ufbs.union(encode(row, col), encode(row - 1, col));
        }
        if (row < N-1 && isOpen(row+1,col)) { //check down
            uf.union(encode(row, col), encode(row + 1, col));
            ufbs.union(encode(row, col), encode(row + 1, col));
        }
        if (col > 0 && isOpen(row, col-1)) { //check left
            uf.union(encode(row, col), encode(row, col - 1));
            ufbs.union(encode(row, col), encode(row, col - 1));
        }
        if (col < N-1 && isOpen(row, col+1)) {//check right
            uf.union(encode(row, col), encode(row, col + 1));
            ufbs.union(encode(row, col), encode(row, col + 1));
        }
    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        //check if row/col out of bound
        if (row < 0 || row >= N || col < 0 || col >= N)
            throw new IndexOutOfBoundsException("Argument is outside its prescribed range.");

        return open[row][col];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        //check if row/col out of bound
        if (row < 0 || row >= N || col < 0 || col >= N)
            throw new IndexOutOfBoundsException("Argument is outside its prescribed range.");

        if (isOpen(row,col))
            return ufbs.connected(encode(row,col),top);
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
         return countOpen;
    }

    // Does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        //check if row/col out of bound
        if (row < 0 || row >= N || col < 0 || col >= N)
            throw new IndexOutOfBoundsException("Argument is outside its prescribed range.");

        return N * row + col + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
