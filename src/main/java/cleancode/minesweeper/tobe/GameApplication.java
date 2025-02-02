package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.*;

public class GameApplication {

    // 세가지로 나눔
    // 1. 프로그램을 시작하는 진입점 역할을 하는 GameApplication
    // 2. 실제 지뢰찾기 도메인을 가지고 지뢰찾기 게임을 실행하는 Minesweeper
    // 3. 입출력 부분은 별개의 책임이 아닐까 라는 질문에서 시작해서 입력과 출력을 담단하는 클래스 ConsoleInputHandler, ConsoleOutputHandler로 나누고,
    // 4. 게임판에서 일아나는 일을 담당하는 GameBoard
    public static void main(String[] args) {
//        GameLevel gameLevel = new VeryBeginner();
//        GameLevel gameLevel = new Beginner();
//        GameLevel gameLevel = new Middle();
        GameLevel gameLevel = new Advanced();

        Minesweeper minesweeper = new Minesweeper(gameLevel);
        minesweeper.initialize();
        minesweeper.run();
    }

}
