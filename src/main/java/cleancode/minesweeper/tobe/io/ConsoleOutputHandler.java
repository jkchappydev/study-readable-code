package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;
import cleancode.minesweeper.tobe.Cell;

public class ConsoleOutputHandler {

    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public void showBoard(GameBoard board) {
        System.out.println("   a b c d e f g h i j");
        // BOARD_ROW_SIZE, BOARD_COL_SIZE는 BOARD로 부터 가져올 수 있다. 따라서 인자를 BOARD를 받음
        for (int row = 0; row < board.getRowSize(); row++) { // board 안쪽으로 cell이 숨어들어갔기 때문에 board한테 rowSize좀 줄래? 해서 getRowSize()
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < board.getColSize(); col++) {
                System.out.print(board.getSign(row, col) + " "); // getter를 사용하는 이유 : board를 그리는 쪽은 여긴데, cell에다가 board를 그려줘 하는 것은 관심사가 쪼개짐
            } //board.getCell().getSign() <- 무례한 것 (혹시 row, col 인덱스에 있는 cell의 sign좀 줄래? 라고 변경)
            System.out.println();
        }
        System.out.println();
    }

    public void printGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    public void printGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    public void printCommentForSelectionCell() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    public void printCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    public void printExceptionMessage(GameException e) {
        System.out.println(e.getMessage()); // 개발자가 의도한 예외
    }

    public void printSimpleMessage(String message) {
        System.out.println(message);
    }
}
