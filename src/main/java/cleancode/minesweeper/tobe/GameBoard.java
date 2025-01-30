package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    private static final int LAND_MINE_COUNT = 10;

    private final Cell[][] board;

    // public method 위쪽에 선언
    public GameBoard(int rowSize, int colSize) {
        board = new Cell[rowSize][colSize];
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.flag();
    }

    public void open(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.open();
    }

    public void openSurroundedCells(int row, int col) {
//        if (!BOARD2[row][col].equalsSign(CLOSED_CELL_SIGN)) { // 이미 열렸는지
//            return;
//        }
        // 부정 연산자 제거
//        if (BOARD[row][col].doesNotEqualSign(CLOSED_CELL_SIGN)) { // 이미 열렸는지
//            return;
//        }
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) { // 판을 벗어났는지
            return;
        }
//        if (BOARD[row][col].doesNotClosed()) { // 이미 열렸는지 (이미 열었으면 넘어가라 라는 뜻도 됨)
//            return;
//        }
        if (isOpenedCell(row, col)) { // 이미 열렸는지 (이미 열었으면 넘어가라 라는 뜻도 됨)
            return;
        }
        if (isLandMineCell(row, col)) { // 지뢰 셀이면
            return;
        }
//        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) { // 지뢰 카운트를 가지고 있는 칸이면
//            // BOARD[row][col] = Cell.of(String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]));
//            BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]);
//            return;
//        }
        open(row, col);

        if (doesCellHaveLandMineCount(row, col)) { // 지뢰 카운트를 가지고 있는 칸이면
            // BOARD[row][col] = Cell.of(String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]));
//            BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        }

        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        // return LAND_MINES[selectedRowIndex][selectedColIndex];
        Cell cell = findCell(selectedRowIndex, selectedColIndex); // 이 셀이 지뢰 셀 이야?
        return cell.isLandMine(); // 이 셀이 지뢰셀 이야?
    }

    // 컴파일 에러를 최소화하면서 리팩토링 할 때는 리팩토리 할 대상의 메소드를 일단 복제한다.
    // 그러고 나서, 해당 메소드를 사용하는 곳을 임시로 아래 메소드명으로 바꾼다.
    // 그러고 나서, 기존 메서드를 삭제하고 아래 메서드를 기존 메서드 명으로 바꾼다.
    public boolean isAllCellChecked() {
        // 1.
//        boolean isAllOpened = true;
//        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
//            for (int col = 0; col < BOARD_COL_SIZE; col++) {
//                // BOARD[row][col]를 순회하고 싶은 것이 목적
//                if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
//                    isAllOpened = false;
//                }
//            }
//        }
//        return isAllOpened;

        // 2. 중첩 배열을 stream 으로 처리
//        Stream<String[]> stringArrayStream = Arrays.stream(BOARD);
//        Stream<Stream<String>> stringStream = stringArrayStream
//                .map(stringArray -> {
//                    Stream<String> stringStream2 = Arrays.stream(stringArray);
//                    return stringStream2;
//                });

        // 3. 평탄화 (이중 배열을 그냥 배열로 : flatMap)
//        Stream<String[]> stringArrayStream = Arrays.stream(BOARD);
//        Stream<String> stringStream = stringArrayStream
//                .flatMap(stringArray -> {
//                    Stream<String> stringStream2 = Arrays.stream(stringArray);
//                    return stringStream2;
//                });
//        return stringStream // Stream<String>
//                .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN)); // 닫힌 셀(CLOSED_CELL_SIGN)이 하나도 없으면(noneMatch()), 셀이 다 열린 것

        // 4. 최종
        return Arrays.stream(board) // 각각의 String[]에 대해 stream 을 걸고,
                .flatMap(Arrays::stream) // 평탄화를 시키면 전체 String 에 대한 stream 으로 1차원적으로 표현 가능
                // .noneMatch(cell -> cell.getSign().equals(CLOSED_CELL_SIGN)); <- 객체에 물어보는 방식으로 변경해야 함
                // .noneMatch(cell -> CLOSED_CELL_SIGN.equals(cell));
                .allMatch(Cell::isChecked);
    }

    public void initializeGame() {
        int rowSize = board.length;
        int colSize = board[0].length;

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create(); // 이 부분은 셀을 처음 생성하는 부분이기 때문에 findCell(row, col) 사용 불가.
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) { // 지뢰 갯수를 의미
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            Cell landMineCell = findCell(landMineCol, landMineRow);
            landMineCell.turnOnLandMine();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (isLandMineCell(row, col)) {
                    // NEARBY_LAND_MINE_COUNTS[row][col] = 0; 셀의 기본 속성을 0으로 했기 때문에 사실 아무것도 안해도 됨.
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                Cell cell = findCell(row, col);
                cell.updateNearbyLandMineCount(count);
            }
        }
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public String getSign(int rowIndex, int colIndex) { // 밖에서 어떤 건지 알 수 있게 row -> rowIndex, col -> colIndex 로 메서드 시그니처 변경
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    private Cell findCell(int row, int col) {
        return board[row][col];
    }

    private int countNearbyLandMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

}
