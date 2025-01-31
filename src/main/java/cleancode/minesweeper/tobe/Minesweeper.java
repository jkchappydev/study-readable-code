package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class Minesweeper {

    private final GameBoard gameBoard; // Board 입장에서는 GameBoard 로 객체화 및 내부에서 캡슐화 되어있어서, 데이터 구조에 대해서는 모른다.
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameLevel gameLevel) {
        gameBoard = new GameBoard(gameLevel); // gameBoard를 생성할 때, gameLevel을 전달함
    }

    public void run() {
        consoleOutputHandler.showGameStartComments();
        gameBoard.initializeGame();

        while (true) {
            try {
                consoleOutputHandler.showBoard(gameBoard);

                // 이렇게 하면 읽는 사람의 입장에서 gameStatus가 어떤 의미를 가지고 있는지 해석할 필요가 없다.
                if (doesUserWinTheGame()) {
                    consoleOutputHandler.printGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    consoleOutputHandler.printGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (GameException e) {
                // 개발자가 의도한 예외 상황 (예외 상황에 대한 메세지 출력)
                consoleOutputHandler.printExceptionMessage(e);
            } catch (Exception e) {
                // 프로그램에서 처리한 예외 상황 (단순 메세지 출력)
                consoleOutputHandler.printSimpleMessage("프로그램에 문제가 생겼습니다.");
                // e.printStackTrace(); // 실무에서는 안티 패턴. log 사용
            }
        }

    }

    private void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());
        int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());

        if (doesUserChooseToPlantFlag(userActionInput)) {
            // 셀을 갈아끼우는 것이 아닌 셀의 상태를 바꿀거임
            // BOARD[selectedRowIndex][selectedColIndex] = Cell.ofFlag();
            // BOARD[selectedRowIndex][selectedColIndex].flag();
            // gameBoard에 요청 (flag()를 해줘. selectedRowIndex 와 selectedColIndex를 가진 애한테)
            gameBoard.flag(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        // 앞의 if의 조건을 기억할 필요가 없음.
        if (doesUserChooseToOpenCell(userActionInput)) {
            // 이제 gameBoard 에 물어봐야 함.
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
                // initializeGame의 turnOnLandMine() 부분에서 지뢰셀로 갈아 끼웟기 떼문에 사실 필요가 없음.
                // BOARD[selectedRowIndex][selectedColIndex] = Cell.ofLandMine(); 지뢰셀로 갈아 끼우는 부분
                // BOARD[selectedRowIndex][selectedColIndex].open();
                // gameBoard에 요청
                gameBoard.open(selectedRowIndex, selectedColIndex);
                changeGameStatusToLose();
                return;
            }

            gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private String getUserActionInputFromUser() {
        consoleOutputHandler.printCommentForUserAction();
        return consoleInputHandler.getUserInput();
    }

    private String getCellInputFromUser() {
        consoleOutputHandler.printCommentForSelectionCell();
        return consoleInputHandler.getUserInput();
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
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
}
