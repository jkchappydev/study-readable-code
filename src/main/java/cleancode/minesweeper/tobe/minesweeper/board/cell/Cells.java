package cleancode.minesweeper.tobe.minesweeper.board.cell;

import java.util.Arrays;
import java.util.List;

public class Cells {

    private final List<Cell> cells;


    public Cells(List<Cell> cells) {
        this.cells = cells;
    }

    public static Cells of(List<Cell> cells) {
        return new Cells(cells);
    }

    public static Cells from(Cell[][] cells) {
        // cells 이줌 배열이 들어왔을 때, 아래의 로직으로 인해 cells가 포장된 일급 컬렉션이 만들어 짐.
        List<Cell> cellList = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .toList();

        return of(cellList);
    }

    public boolean isAllChecked() {
        return cells.stream().allMatch(Cell::isChecked);
    }

}
