package cleancode.minesweeper.tobe.minesweeper.board.cell;

public class CellState {

    /**
     * 기존에 상속 구조에서는 부모 클래스에 존재했던 이 두가지의 필드를
     * 자식 클래스에서 직접적으로 알고 있어야 했는데, 인터페이스로 변경함으로써 이제는 몰라도 됨
     * 이렇게 되면 CellState를 마음대로 변경해도 됨 (자식 클래스에서 CellState와의 소통은 메서드들로만 하기 때문에)
     * (isFlagged를 enum으로 관리하던, boolean으로 관리하던, String으로 관리하던)
     */
    private boolean isFlagged;
    private boolean isOpened;

    public CellState(boolean isFlagged, boolean isOpened) {
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // static factory method
    public static CellState initialize() {
        return new CellState(false, false);
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isFlagged() {
        return isFlagged;
    }
}
