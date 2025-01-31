package cleancode.minesweeper.tobe.cell;

public abstract class Cell {
    // Cell에 대한 공통부분(열림, 닫힘)은 부모클래스,
    // 지뢰 셀, 숫자 셀, 빈 셀은 각각 자식클래스에서 구체화 하기 위해 추상메서드로 변경
    // 세 자식 클래스에서 공통으로 사용되는 상수 또는 변수는 protected로 변경
    protected static final String FLAG_SIGN = "⚑"; // closed 기반에 flag가 꽂힌거임
    // private static final String LAND_MINE_SIGN = "☼"; // 지뢰 셀에서만 사용하는 상수, 지뢰 셀로 옮김
    protected static final String UNCHECKED_SIGN = "□";
    // private static final String EMPTY_SIGN = "■"; // 빈 셀에서만 사용하는 상수, 빈 셀로 옮김

    // private int nearbyLandMinesCount; // 숫자 셀에서만 사용하는 변수, 숫자 셀로 옮김
    // private boolean isLandMine; // 지뢰 셀에서만 사용하는 변수, 지뢰 셀로 옮김
    protected boolean isFlagged;
    protected boolean isOpened;

    // 특정 셀(지뢰 셀)에서만 유효한 기능 -> 추상 메서드로 변경
    // 자식 클래스에서 구현해서 사용하겠다는 의미
    // 특정 자식 클래스에서만 유효하게 동작하는 메서드 -> LSP 위반이므로 GameBoard에서 다른 방식으로 처리해줬음. (GameBoard의 temp()에 설명)
    // public abstract void turnOnLandMine();

    // public abstract void updateNearbyLandMineCount(int count);

    public abstract boolean isLandMine();

    public abstract boolean hasLandMineCount();

    public abstract String getSign();

    // 모든 셀에 공통되는 사항은 해당 클래스에 구현해도 무방
    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isOpened() {
        return isOpened;
    }

}
