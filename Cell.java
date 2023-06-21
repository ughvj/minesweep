public class Cell {

    private int number;
    private boolean isMine;
    private boolean isDug;
    private boolean isFlagged;

    public Cell() {
        this.number = 0;
        this.isMine = false;
        this.isDug = false;
        this.isFlagged = false;
    }

    public void fillMine() {
        this.isMine = true;
    }

    public boolean isMine() {
        return this.isMine;
    }

    public void dug() {
        this.isDug = true;
    }

    public boolean isDug() {
        return this.isDug;
    }

    public boolean flagging() {
        if (this.isDug) {
            return false;
        }

        this.isFlagged = !this.isFlagged;
        return true;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public void setNumber(int n) {
        this.number = n;
    }

    public int getNumber() {
        return this.number;
    }
}
