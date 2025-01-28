package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    public static final int LAND_MINE_COUNT = 10;

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                showBoard();

                // 이렇게 하면 읽는 사람의 입장에서 gameStatus가 어떤 의미를 가지고 있는지 해석할 필요가 없다.
                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                System.out.println(e.getMessage()); // 개발자가 의도한 예외
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 생겼습니다.");
                // e.printStackTrace(); // 실무에서는 안티 패턴. log 사용
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            // 셀을 갈아끼우는 것이 아닌 셀의 상태를 바꿀거임
            // BOARD[selectedRowIndex][selectedColIndex] = Cell.ofFlag();
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        // 앞의 if의 조건을 기억할 필요가 없음.
        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                // initializeGame의 turnOnLandMine() 부분에서 지뢰셀로 갈아 끼웟기 떼문에 사실 필요가 없음.
                // BOARD[selectedRowIndex][selectedColIndex] = Cell.ofLandMine(); 지뢰셀로 갈아 끼우는 부분
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        // return LAND_MINES[selectedRowIndex][selectedColIndex];
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol); // From ~~ cellInputCol 처럼 자연스럽게 읽힌다.
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    // 만약에, 여러군데에서 사용하는 메서드라면, 해당 메서드를 수정 또는 변경 했을 때 해당 메서드를 사용하는 곳이 모두 영향이 간다.
//    private static boolean isAllCellOpened() {
//        boolean isAllOpened = true;
//        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
//            for (int col = 0; col < BOARD_COL_SIZE; col++) {
//                if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
//                    isAllOpened = false;
//                }
//            }
//        }
//        return isAllOpened;
//    }

    // 컴파일 에러를 최소화하면서 리팩토링 할 때는 리팩토리 할 대상의 메소드를 일단 복제한다.
    // 그러고 나서, 해당 메소드를 사용하는 곳을 임시로 아래 메소드명으로 바꾼다.
    // 그러고 나서, 기존 메서드를 삭제하고 아래 메서드를 기존 메서드 명으로 바꾼다.
    private static boolean isAllCellChecked() {
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
        return Arrays.stream(BOARD) // 각각의 String[]에 대해 stream 을 걸고,
                .flatMap(Arrays::stream) // 평탄화를 시키면 전체 String 에 대한 stream 으로 1차원적으로 표현 가능
                // .noneMatch(cell -> cell.getSign().equals(CLOSED_CELL_SIGN)); <- 객체에 물어보는 방식으로 변경해야 함
                // .noneMatch(cell -> CLOSED_CELL_SIGN.equals(cell));
                .allMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        // Character.getNumericValue(cellInputRow) = 10 (디버거 모드의 More -> Evalute Expression으로 실행 가능)
        if(rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
//                selectedColIndex = 0;
//                break;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                // return -1;
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " "); // getter를 사용하는 이유 : board를 그리는 쪽은 여긴데, cell에다가 board를 그려줘 하는 것은 관심사가 쪼개짐
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) { // 지뢰 갯수를 의미
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLandMine();
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    // NEARBY_LAND_MINE_COUNTS[row][col] = 0; 셀의 기본 속성을 0으로 했기 때문에 사실 아무것도 안해도 됨.
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
//        if (!BOARD2[row][col].equalsSign(CLOSED_CELL_SIGN)) { // 이미 열렸는지
//            return;
//        }
        // 부정 연산자 제거
//        if (BOARD[row][col].doesNotEqualSign(CLOSED_CELL_SIGN)) { // 이미 열렸는지
//            return;
//        }
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) { // 판을 벗어났는지
            return;
        }
//        if (BOARD[row][col].doesNotClosed()) { // 이미 열렸는지 (이미 열었으면 넘어가라 라는 뜻도 됨)
//            return;
//        }
        if (BOARD[row][col].isOpened()) { // 이미 열렸는지 (이미 열었으면 넘어가라 라는 뜻도 됨)
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
        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) { // 지뢰 카운트를 가지고 있는 칸이면
            // BOARD[row][col] = Cell.of(String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]));
//            BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        }

        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
