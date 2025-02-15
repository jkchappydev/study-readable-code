package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.List;

public class GameBoard {

    private final Cell[][] board;
    private final int landMineCount;
    private GameStatus gameStatus;

    // GameBoard 입장에서는 GameLevel을 전달받았는데, 인터페이스여서
    // runtime 시점 에서는 어떤 구현체가 들어오는지는 모르지만, 추상화된 스펙(rowSize, colSize, landMineCount)은 알고있기 때문에 실행 가능
    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getColSize();
        int colSize = gameLevel.getRowSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
        initializeGameStatus();
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();

        checkIfGameIsOver();
    }

    private void checkIfGameIsOver() {
        if (isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    public void openOneCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    public void openSurroundedCells(CellPosition cellPosition) {
//        if (cellPosition.isRowIndexMoreThanOrEqual(getRowSize())
//                || cellPosition.isColIndexMoreThanOrEqual(getColSize())) { // 판을 벗어났는지
//            return;
//        }

        if (isOpenedCell(cellPosition)) { // 이미 열렸는지 (이미 열었으면 넘어가라 라는 뜻도 됨)
            return;
        }

        if (isLandMineCellAt(cellPosition)) { // 지뢰 셀이면
            return;
        }

        openAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) { // 지뢰 카운트를 가지고 있는 칸이면
            return;
        }

//        for(RelativePosition relativePosition : RelativePosition.SURROUNDED_POSITIONS) {
//            if(cellPosition.canCalculatePositionBy(relativePosition)) {
//                CellPosition nextCellPosition = cellPosition.calculatePositionBy(relativePosition);
//                openSurroundedCells(nextCellPosition);
//            }
//        }

        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
        surroundedPositions.forEach(this::openSurroundedCells);
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.hasLandMineCount();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    public boolean isLandMineCellAt(CellPosition cellPosition) {
        // return LAND_MINES[selectedRowIndex][selectedColIndex];
        Cell cell = findCell(cellPosition); // 이 셀이 지뢰 셀 이야?
        return cell.isLandMine(); // 이 셀이 지뢰셀 이야?
    }

    public boolean isAllCellChecked() {
        // board로부터 cells일급 컬렉션을 만들고, 모든 셀이 체크되었는지를 묻는 두가지 단계로 진행됨
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    public void initializeGame() {
        initializeGameStatus();
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        initializeNumberCells(numberPositionCandidates);
    }

    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(candidatePosition);
            if(count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    // LSP 위반 예시
    // 만약에 하위 구현체들 중에, updateNearbyLandMineCount를 사용하는 부분이 있다면,
    // 해당 코드를 이렇게 놔둘 경우에 Cell의 종류(지뢰 셀, 숫자 셀, 빈 셀)가 어떤 건지에 따라서 프로그램이 막 터지는 경우가 있음.
    // updateNearbyLandMineCount는 숫자 셀에서만 사용함, 지뢰 셀, 빈 셀에서는 예외를 던짐.
    // 부모 클래스에서 원하는 동작을 특정 자식 클래스(지뢰 셀, 빈 셀)에서는 하지 않고 있음.(예외 처리가 되어있기 때문에)
    // 따라서, 숫자 셀인지 검증하는 타입 체크 로직이 추가적으로 필요함 (하지만, 상속 구조에서는 타입 체크 로직이 없어야 하는것이 정상임)
    // 따라서, 해당 현상은 LSP를 위반하였기에 발생하는 현상임.
//    public void temp(Cell2 cell) {
//        if(cell instanceof NumberCell) {
//            cell.updateNearbyLandMineCount(0);
//        }
//    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

//        int count = 0;
//        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCellAt(row - 1, col - 1)) {
//            count++;
//        }
//        if (row - 1 >= 0 && isLandMineCellAt(row - 1, col)) {
//            count++;
//        }
//        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCellAt(row - 1, col + 1)) {
//            count++;
//        }
//        if (col - 1 >= 0 && isLandMineCellAt(row, col - 1)) {
//            count++;
//        }
//        if (col + 1 < colSize && isLandMineCellAt(row, col + 1)) {
//            count++;
//        }
//        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCellAt(row + 1, col - 1)) {
//            count++;
//        }
//        if (row + 1 < rowSize && isLandMineCellAt(row + 1, col)) {
//            count++;
//        }
//        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCellAt(row + 1, col + 1)) {
//            count++;
//        }
//        return count;

        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
                .filter(this::isLandMineCellAt)
                .count();

        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThan(rowSize))
                .filter(position -> position.isColIndexLessThan(colSize))
                .toList();
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCellAt(cellPosition)) {
            openOneCellAt(cellPosition);
            changeGameStatusToLose();
            return;
        }

        openSurroundedCells(cellPosition);
        checkIfGameIsOver();
    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }

}
