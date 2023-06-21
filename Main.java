import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("usage: Main row col mine_num");
            return ;
        }

        int inputRow = Integer.parseInt(args[0]);
        int inputCol = Integer.parseInt(args[1]);
        int inputMineNum = Integer.parseInt(args[2]);

        Grid grid = new Grid(inputRow, inputCol);
        grid.scatterMines(inputMineNum);
        grid.numbering();

        System.out.println(grid.toStringOfSkeleton());
        System.out.println();
        System.out.println(grid.toString());

        Scanner s = new Scanner(System.in);

        while (!grid.isOver()) {
            String c = s.nextLine();
            String[] split = c.split(" ");

            int row, col;
            switch (split[0]) {
                case "f":
                    row = Integer.parseInt(split[1]);
                    col = Integer.parseInt(split[2]);
                    grid.flagging(row, col);
                    break;
                default:
                    row = Integer.parseInt(split[0]);
                    col = Integer.parseInt(split[1]);
                    grid.dig(row, col);
                    break;
            }
            System.out.println(grid.toString());
        }

        System.out.println(grid.toString());
        System.out.println("over.");
        s.close();
    }
}
