package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.*;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.io.config.GameConfig;

public class GameApplication {

    // 세가지로 나눔
    // 1. 프로그램을 시작하는 진입점 역할을 하는 GameApplication
    // 2. 실제 지뢰찾기 도메인을 가지고 지뢰찾기 게임을 실행하는 Minesweeper
    // 3. 입출력 부분은 별개의 책임이 아닐까 라는 질문에서 시작해서 입력과 출력을 담단하는 클래스 ConsoleInputHandler, ConsoleOutputHandler로 나누고,
    // 4. 게임판에서 일아나는 일을 담당하는 GameBoard
    public static void main(String[] args) {
//        GameLevel gameLevel = new Advanced();
//        // DIP : inputHandler의 구현체는 ConsoleInputHandler, outputHandler의 구현체는 ConsoleOutputHandler
//        // 실제 외부에서 실행시점에 어떤 구현체를 넣어줄 것인지를 결정
//        InputHandler inputHandler = new ConsoleInputHandler();
//        OutputHandler outputHandler = new ConsoleOutputHandler();

        GameConfig gameConfig = new GameConfig(
                new Advanced(),
                new ConsoleInputHandler(),
                new ConsoleOutputHandler()
        );

        Minesweeper minesweeper = new Minesweeper(gameConfig);
        minesweeper.initialize();
        minesweeper.run();
    }
}
