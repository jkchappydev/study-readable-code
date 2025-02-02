package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameRunnable;

public class AnotherGame implements GameRunnable {
    // Game 인터페이스를 구현할 때,
    // initialize가 필요없는 게임이라고 가정

//    @Override
//    public void initialize() {
//        // 필요없음
//        // initialize의 메서드 시그니처가 변경되면 해당 기능이 필요없는 AnotherGame도 같이 영향을 받음
//        // ISP 위반
//        // 따라서, Game 인터페이스를 GameInitializable 인터페이스와 GameRunnable 인터페이스로 분리한다.
//    }

    @Override
    public void run() {
        // 게임 진행
    }

}
