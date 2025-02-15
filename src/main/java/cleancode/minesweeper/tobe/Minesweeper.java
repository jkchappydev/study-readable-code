package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.io.config.GameConfig;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.user.UserAction;

public class Minesweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard; // Board 입장에서는 GameBoard 로 객체화 및 내부에서 캡슐화 되어있어서, 데이터 구조에 대해서는 모른다.
    // DIP
    // Minesweeper 입장 에서는 얘네들은 너무 저수준 모듈임
    // 사용자는 현재 console로만 이 게임과 소통을 할 수 있음
    // 만약, 이 지뢰찾기가 어떤 웹페이지에서 동작하는 거라면?
    // Minesweeper이라는 지뢰찾기의 도메인 룰(=규칙)은 변하지 않지만, console 이냐 web 이냐가 변경됨.
    // 이 지뢰찾기를 웹 페이지에 올리기 위해서는 현재 consoleInputHandler과 consoleOutputHandler가 직접적으로 박혀 있기 때문에,
    // 해당 두 개의 기능에 변경이 불가피하다. 하지만 사용자와 소통한다는 기능 자체는 변함이 없음.
    // private final ConsoleInputHandler inputHandler = new ConsoleInputHandler();
    // private final ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    // DIP : InputHandler와 OutputHandler을 외부에서 받도록 구현
    // Minesweeper은 InputHandler와 OutputHandler라는 인터페이스만 알고 있음. 인터페이스만 받아서 사용하고, 실제로 어떤게 들어오는지는 신경쓰지 않아도 됨
    // 고수준 모듈(Minesweeper)이 InputHandler, OutputHandler의 추상화에만 의존하게 됨.
    // 기존에는 저수준 모듈인 ConsoleInputHandler, ConsoleOutputHandler 직접 사용
//    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
//        gameBoard = new GameBoard(gameLevel); // gameBoard를 생성할 때, gameLevel을 전달함
//        this.inputHandler = inputHandler;
//        this.outputHandler = outputHandler;
//    }

    public Minesweeper(GameConfig gameConfig) {
        gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments();

        while (gameBoard.isInProgress()) {
            try {
                outputHandler.showBoard(gameBoard);

                CellPosition cellPosition = getCellInputFromUser();
                UserAction userActionInput = getUserActionInputFromUser();
                actOnCell(cellPosition, userActionInput);
            } catch (GameException e) {
                // 개발자가 의도한 예외 상황 (예외 상황에 대한 메세지 출력)
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                // 프로그램에서 처리한 예외 상황 (단순 메세지 출력)
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
                // e.printStackTrace(); // 실무에서는 안티 패턴. log 사용
            }
        }

        outputHandler.showBoard(gameBoard);

        if (gameBoard.isWinStatus()) {
            outputHandler.showGameWinningComment();
        }
        if (gameBoard.isLoseStatus()) {
            outputHandler.showGameLosingComment();
        }
    }

    private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flagAt(cellPosition);
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            gameBoard.openAt(cellPosition);
            return;
        }

        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private boolean doesUserChooseToOpenCell(UserAction userAction) {
        return userAction == UserAction.OPEN;
    }

    private boolean doesUserChooseToPlantFlag(UserAction userAction) {
        return userAction == UserAction.FLAG;
    }

    private UserAction getUserActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserActionFromUser();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectionCell();

        // CellPosition이라는 Value Object가 만들어 질 때부터 유효성 검사하고, 만들어 지는 순간 인덱스의 기능을 함
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if (gameBoard.isInvalidCellPosition(cellPosition)) { // 올바른 cellPosition으로 gameBoard에서 동작할 수 있는가?
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
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
