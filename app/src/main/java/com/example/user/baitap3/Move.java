package com.example.user.baitap3;

/**
 * Created by User on 4/14/2018.
 */
// đại diện cho 1 nước đi của người chơi hay máy.
public class Move {
    private int rowIndex;
    private int colIndex;
    public Move (){

    }

    public Move(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
}
