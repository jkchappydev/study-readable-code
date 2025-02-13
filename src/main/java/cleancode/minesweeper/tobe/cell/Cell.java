package cleancode.minesweeper.tobe.cell;

public interface Cell {

    boolean isLandMine();

    boolean hasLandMineCount();

    CellSnapshot getSnapshot();
    // 모든 셀에 공통되는 사항은 해당 클래스에 구현해도 무방
    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

}
