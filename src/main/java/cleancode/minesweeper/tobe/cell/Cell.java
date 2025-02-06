package cleancode.minesweeper.tobe.cell;

public interface Cell {

    String FLAG_SIGN = "⚑"; // closed 기반에 flag가 꽂힌거임
    String UNCHECKED_SIGN = "□";

    boolean isLandMine();

    boolean hasLandMineCount();

    String getSign();

    // 모든 셀에 공통되는 사항은 해당 클래스에 구현해도 무방
    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

}
