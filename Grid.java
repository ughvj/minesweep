import java.util.Random;
import java.util.ArrayList;

public class Grid {
    private int row;
    private int col;
    private int mines;
    private Cell[][] body;
    private boolean[][] visitedTemporarily;
    private boolean over;

    public Grid(int row, int col) {
        this.row = row;
        this.col = col;

        this.body = new Cell[row][col];
        this.visitedTemporarily = new boolean[row][col];

        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                this.body[i][j] = new Cell();
                this.visitedTemporarily[i][j] = false;
            }
        }

        this.over = false;
    }

    public boolean scatterMines(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > this.row * this.col) {
            return false;
        }

        this.mines = amount;
    
        Random r = new Random();
        int scattered = 0;

        while (scattered < amount) {
            int rrow = r.nextInt(this.row);
            int rcol = r.nextInt(this.col);

            Cell candidate = this.body[rrow][rcol];
            
            if (!candidate.isMine()) {
                candidate.fillMine();
                scattered++;
            }
        }

        return true;
    }

    public boolean numbering() {
        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                if (this.body[i][j].isMine()) {
                    continue;
                }
                int mines = 0;
                for (Coordinate c : walkAround(i, j)) {
                    if (this.body[c.row][c.col].isMine()) {
                        mines++;
                    }
                }
                this.body[i][j].setNumber(mines);
            }
        }

        return true;
    }

    public boolean flagging(int row, int col) {
        if (this.body[row][col].isDug()) {
            return false;
        }
        this.body[row][col].flagging();
        return true;
    }

    public boolean dig(int row, int col) {
        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                this.visitedTemporarily[i][j] = false;
            }
        }

        if (this.body[row][col].isDug()) {
            return this.digDug(row, col);
        } else {
            return this.digRecursively(row, col);
        }
    }

    public boolean digDug(int row, int col) {
        this.visitedTemporarily[row][col] = true;
    
        int flagged = 0;
        for (Coordinate c : walkAround(row, col)) {
            if (this.body[c.row][c.col].isFlagged()) {
                flagged++;
            }
        }

        if (this.body[row][col].getNumber() == flagged) {
            for (Coordinate c : walkAround(row, col)) {
                digRecursively(c.row, c.col);
            }
        }

        return true;
    }

    private boolean digRecursively(int row, int col) {
        if (!exist(row, col)) {
            return false;
        }

        if (this.visitedTemporarily[row][col]) {
            return false;
        }
        this.visitedTemporarily[row][col] = true;

        Cell target = this.body[row][col];

        if (target.isFlagged()) {
            return false;
        }

        target.dug();
    
        if (target.isMine()) {
            this.over = true;
            return false;
        }

        if (target.getNumber() == 0) {
            for (Coordinate c : walkAround(row, col)) {
                digRecursively(c.row, c.col);
            }
        }

        int left = 0;
        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                if (!this.body[i][j].isDug()) {
                    left++;
                }
            }
        }

        if (left == this.mines) {
            this.over = true;
        }

        return true;
    }

    private ArrayList<Coordinate> walkAround(int row, int col) {
        final int[] aroundPointsRow = { 0, -1, -1, -1, 0, 1, 1,  1};
        final int[] aroundPointsCol = {-1, -1,  0,  1, 1, 1, 0, -1};
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        for (int ar=0; ar<8; ar++) {
            int candidateRow = row + aroundPointsRow[ar];
            int candidateCol = col + aroundPointsCol[ar];
            if (exist(candidateRow, candidateCol)) {
                Coordinate c = new Coordinate();
                c.row = candidateRow;
                c.col = candidateCol;
                coordinates.add(c);
            }
        }
        return coordinates;
    }

    private boolean exist(int row, int col) {
        if (row < 0) {
            return false;
        }
        if (col < 0) {
            return false;
        }

        if (row > this.row - 1) {
            return false;
        }
        if (col > this.col - 1) {
            return false;
        }

        return true;
    }

    public boolean isOver() {
        return this.over;
    }

    public String toString() {
        String serialized = "";
        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                if (this.body[i][j].isFlagged()) {
                    serialized += "F";
                } else if (this.body[i][j].isDug()) {
                    if (this.body[i][j].isMine()) {
                        serialized += "*";
                    } else {
                        serialized += String.valueOf(this.body[i][j].getNumber());
                    }
                } else {
                    serialized += "-";
                }
            }
            serialized += "\n";
        }
        return serialized;
    }

    public String toStringOfSkeleton() {
        String serialized = "";
        for (int i=0; i<this.row; i++) {
            for (int j=0; j<this.col; j++) {
                if (this.body[i][j].isMine()) {
                    serialized += "*";
                } else {
                    serialized += String.valueOf(this.body[i][j].getNumber());
                }
            }
            serialized += "\n";
        }
        return serialized;
    }
}
