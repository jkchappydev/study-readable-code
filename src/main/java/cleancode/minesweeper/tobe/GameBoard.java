package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.*;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.RelativePosition;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class GameBoard {

    private final Cell[][] board;
    private final int landMineCount;

    // GameBoard 입장에서는 GameLevel을 전달받았는데, 인터페이스여서
    // runtime 시점 에서는 어떤 구현체가 들어오는지는 모르지만, 추상화된 스펙(rowSize, colSize, landMineCount)은 알고있기 때문에 실행 가능
    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getColSize();
        int colSize = gameLevel.getRowSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();
    }

    public void openAt(CellPosition cellPosition) {
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

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                // board[row][col] = Cell.create(); // 이 부분은 셀을 처음 생성하는 부분이기 때문에 findCell(row, col) 사용 불가.
                board[row][col] = new EmptyCell();
            }
        }

        for (int i = 0; i < landMineCount; i++) { // 지뢰 갯수를 의미
            int landMineRow = new Random().nextInt(rowSize);
            int landMineCol = new Random().nextInt(colSize);
            // Cell2 landMineCell = findCell(landMineRow, landMineCol);
            // landMineCell.turnOnLandMine();
            // LandMineCell landMineCell = new LandMineCell();
            // landMineCell.turnOnLandMine(); // 지뢰 셀인데 생성 시점에 지뢰 셀 표시를 할 필요가 없음
            // board[landMineRow][landMineCol] = landMineCell;
            board[landMineRow][landMineCol] = new LandMineCell();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                CellPosition cellPosition = CellPosition.of(row, col);

                if (isLandMineCellAt(cellPosition)) {
                    // NEARBY_LAND_MINE_COUNTS[row][col] = 0; 셀의 기본 속성을 0으로 했기 때문에 사실 아무것도 안해도 됨.
                    continue;
                }
                int count = countNearbyLandMines(cellPosition); // 주변 지뢰 셀을 할당한 다음에
                // Cell2 cell = findCell(row, col);
                // cell.updateNearbyLandMineCount(count);
                NumberCell numberCell = new NumberCell(count); // 숫자 셀을 만듦
                // numberCell이 추가됨으로써 기존에는 없던, 지뢰 셀이 없는 경우 0이라는 숫자가 출력됨
                if(count == 0) {
                    continue;
                }
                // 이 로직도 이렇게 처리를 하니깐 지뢰 셀과 빈 셀에서도 뭔가 처리를 해 줘야 하기 때문에 따로 예외처리를 한 것임
                // 그냥 생성자에 담아서 넘기기 NumberCell numberCell = new NumberCell(); -> NumberCell numberCell = new NumberCell(count);
                // numberCell.updateNearbyLandMineCount(count);
                board[row][col] = numberCell;
            }
        }
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

    public String getSign(CellPosition cellPosition) { // 밖에서 어떤 건지 알 수 있게 row -> rowIndex, col -> colIndex 로 메서드 시그니처 변경
        Cell cell = findCell(cellPosition);
        return cell.getSign();
    }

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

}
