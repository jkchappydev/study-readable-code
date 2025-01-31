package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell {

    private final int nearbyLandMinesCount;

    public NumberCell(int nearbyLandMinesCount) {
        this.nearbyLandMinesCount = nearbyLandMinesCount;
    }

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return true;
    }

    @Override
    public String getSign() {
        if (isOpened) {
            return String.valueOf(nearbyLandMinesCount);
        }
        if (isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }

}
