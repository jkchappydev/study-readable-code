package cleancode.minesweeper.tobe;

public class BoardIndexConverter {

    private static final char BASE_CHAR_FOR_COL = 'a';

    public int getSelectedRowIndex(String cellInput) {
        // char cellInputRow = cellInput.charAt(1);
        String cellInputRow = cellInput.substring(1); // 'a10'에서 10을 계산하기 위함 (두 자리 수에 대한 대응)
        return convertRowFrom(cellInputRow);
    }

    public int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol); // From ~~ cellInputCol 처럼 자연스럽게 읽힌다.
    }

    private int convertRowFrom(String cellInputRow) { // '10'이 들어옴
        // Character.getNumericValue(cellInputRow) = 10 (디버거 모드의 More -> Evalute Expression으로 실행 가능)
        // int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        int rowIndex = Integer.parseInt(cellInputRow) - 1;

        if (rowIndex < 0) {
            throw new GameException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private int convertColFrom(char cellInputCol) { // 'a'가 들어옴
        // ascii code 연산
        int colIndex = cellInputCol - BASE_CHAR_FOR_COL; // 'a'가 입력되면 0, 'b'가 업력되면 1, ...

        if (colIndex < 0) {
            throw new GameException("잘못된 입력입니다.");
        }

        return colIndex;
    }
}
