package cleancode.minesweeper.tobe;

public class Cell {

    private static final String FLAG_SIGN = "⚑"; // closed 기반에 flag가 꽂힌거임
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMinesCount;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;

    // Cell이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
    // Cell의 상태 : 깃발 유무, 열렸다/닫혔다, 사용자가 확인함

    // 외부에서는 static factory method 만 사용 가능하게 private 처리
    private Cell(int nearbyLandMinesCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.nearbyLandMinesCount = nearbyLandMinesCount;
        this.isLandMine = isLandMine;
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // static factory method
    public static Cell of(int nearbyLandMinesCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        return new Cell(nearbyLandMinesCount, isLandMine, isFlagged, isOpened);
    }

    public static Cell create() {
        return of( 0, false, false, false);
    }

    public void turnOnLandMine() {
        this.isLandMine = true; // 지뢰 셀이 된다.
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMinesCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isLandMine() {
        return isLandMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return nearbyLandMinesCount != 0;
    }

    public String getSign() {
        if(isOpened) {
            if(isLandMine) {
                return LAND_MINE_SIGN;
            }
            if(hasLandMineCount()) {
                return String.valueOf(nearbyLandMinesCount);
            }
            return EMPTY_SIGN;
        }

        if(isFlagged) {
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }
}
