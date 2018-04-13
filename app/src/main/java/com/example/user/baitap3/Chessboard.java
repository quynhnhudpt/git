package com.example.user.baitap3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/28/2018.
 */

public class Chessboard {
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private int[][] board;
    private int player;
    private Context context;// dùng để liên kết Dialog.
    private int bitmapWidth;
    private int bitmapHeight;
    private int colQty;
    private int rowQty;
    private List<Line> lineList;
    private int winner = -1;
    private Move tmove;
    private int numWin=5;
    private ABNegamaxing abNegamaxing;

    private Bitmap bmTick;
    private Bitmap bmCross;
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public int getColQty() {
        return colQty;
    }

    public void setColQty(int colQty) {
        this.colQty = colQty;
    }

    public int getRowQty() {
        return rowQty;
    }

    public void setRowQty(int rowQty) {
        this.rowQty = rowQty;
    }


    public Chessboard(Context context, int bitmapWidth, int bitmapHeight, int colQty, int rowQty) {
        this.context = context;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.colQty = colQty;
        this.rowQty = rowQty;

    }
    public void init(){
        lineList = new ArrayList<>();
        bitmap = Bitmap.createBitmap(bitmapWidth,bitmapHeight, Bitmap.Config.ARGB_8888)  ;
        canvas = new Canvas(bitmap);
        paint = new Paint();
        int strockeWidth = 2;
        paint.setStrokeWidth(strockeWidth);

        board = new int[rowQty][colQty];
        player =0;
        int cellWidth = bitmapWidth/colQty;
        int cellHeight = bitmapHeight/rowQty;
        for (int i=0; i<=rowQty; i++) {

          for (int j = 0; i <= colQty; j++) {

              board[i][j] = -1;// giá trị -1 tức chưa có nước đánh.
          }
      }

        for (int i=0; i<= colQty; i++){
            lineList.add(new Line(i*cellWidth,0,i*cellWidth,bitmapHeight));

        }
        for (int i=0; i<=rowQty; i++){
            lineList.add(new Line(0,i*cellHeight, bitmapWidth,i*cellHeight));

        }



    }

    public Bitmap drawBoard(){

        Line line;

        for (int i=0; i<lineList.size(); i++){
            line= lineList.get(i);
            canvas.drawLine(line.getStartx(), line.getStarty(),line.getStopx(),line.getStopy(),paint);
        }
        bmCross = BitmapFactory.decodeResource(context.getResources(),R.drawable.tick);
        bmTick = BitmapFactory.decodeResource(context.getResources(),R.drawable.x);

        return this.bitmap;

    }
    public void makeMove( Move move){
        tmove= move;
        board[move.getRowIndex()][move.getColIndex()]=player;
        player = (player+1)%2;
    }
    public int evaluate() {
        if(winner == -1){
            return 0;
        }else if(winner == player){
            return 1;
        }else {
            return -1;
        }
    }


    public boolean onTouch(View view, MotionEvent motionEvent) {

        int cellWidth = view.getWidth()/colQty;
        int cellHeight = view.getHeight()/rowQty;
        int colIndex = (int) (motionEvent.getX()/cellWidth);
        int rowIndex = (int) (motionEvent.getY()/cellHeight);
        //Log.i("DOO", colIndex+"-"+rowIndex);
        if (winner==0||winner==1){
            return  true;
        }
        if(board[rowIndex][colIndex] != -1){
            return true;//vị trí đã có người đi.
        }



        board[rowIndex][colIndex] = player;
        onDrawBoard(colIndex,rowIndex,view);
        makeMove(new Move(rowIndex,colIndex));

        view.invalidate();
        if(isGameOver()) {
            if (winner == 1) {
                Toast.makeText(context, "Ban thua roi", Toast.LENGTH_LONG).show();
            } else if (winner == 0) {
                Toast.makeText(context, "Ban thang roi", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Ban hoa", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return true;
    }
    public boolean isGameOver(){
        if(checkWin()){
            return true;
        }
        int count = 0;
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) count++;
            }
        }
        if (count == 0){
            winner = -1;
            return true;//trò chơi kết thúc
        }
        //chưa thắng hoặc còn vị trí để đi=>game chưa kết thúc
        return false;
    }
    private boolean checkWin(){
        if(tmove == null) return false;
        if (checkWinLeftBottom(tmove.getRowIndex(),tmove.getColIndex())
                || checkWinRightBottom(tmove.getRowIndex(),tmove.getColIndex())
                || checkWinHorizontal(tmove.getRowIndex())
                || checkWinVerical(tmove.getColIndex())
                ){
            return true;
        }
        return false;

    }
    private boolean checkWinLeftBottom(int row, int col){
        int rowStart, colStart;
        int i = 0;
        int count = 0;

        if (row > col) {
            rowStart = row - col;
            colStart = 0;
        } else {
            rowStart = 0;
            colStart = col - row;
        }
        while (rowStart + i + 1 < colQty && colStart + i + 1 < rowQty) {
            if (board[rowStart + i][colStart + i] == board[rowStart + i + 1][colStart + i + 1] && board[rowStart + i][colStart + i] != -1) {
                count++;

                if (count == numWin) {
                    winner = board[rowStart + i][colStart + i];
                    return true;
                }
            } else {
                count = 0;
            }
            i++;
        }

        return false;
    }
    private boolean checkWinRightBottom(int row, int col){
        int rowStart, colStart;
        int i = 0;
        int count = 0;

        if (row + col < colQty - 1) {
            colStart = row + col;
            rowStart = 0;
        } else {
            colStart = colQty - 1;
            rowStart = col + row - (colQty - 1);
        }

        while (colStart - i - 1 >= 0 && rowStart + i + 1 < colQty) {
            if (board[rowStart + i][colStart - i] == board[rowStart + i + 1][colStart - i - 1] && board[rowStart + i][colStart - i] != -1) {
                count++;

                if (count == numWin) {
                    winner = board[rowStart + i][colStart - i];
                    return true;
                }
            } else {
                count = 0;
            }

            i++;
        }
        return false;
    }
    public boolean checkWinHorizontal(int row){
        int dem = 0;
        for (int i = 1; i < rowQty; i++) {
            if (board[row][i] != board[row][i-1]) {
                dem = 0;
            }else if(board[row][i] != -1){
                dem++;
            }
            if (dem == numWin) {
                winner = board[row][i];
                return true;
            }
        }

        return false;
    }
    private boolean checkWinVerical(int col){
        int dem = 0;
        for (int i = 1; i < rowQty; i++) {
            if (board[i][col] != board[i-1][col]) {
                dem = 0;
            }else if(board[i][col] != -1){
                dem++;
            }
            if (dem == numWin) {
                winner = board[i][col];
                return true;
            }
        }

        return false;
    }







    private void onDrawBoard(int colIndex, int rowIndex, View view) {
        int cellwidth = bitmapWidth / colQty;
        int cellheight = bitmapHeight / rowQty;
        board[rowIndex][colIndex] = player;//gán nước đi
        if (player == 0) {
            canvas.drawBitmap(
                    bmCross,
                    new Rect(0, 0, bmCross.getWidth(), bmCross.getHeight()),
                    new Rect(colIndex * cellwidth, rowIndex * cellheight, (colIndex + 1) * cellwidth, (rowIndex + 1) * cellheight),
                    paint);
            // player = 1;
        } else {

            canvas.drawBitmap(
                    bmTick,
                    new Rect(0, 0, bmTick.getWidth(), bmTick.getHeight()),
                    new Rect(colIndex * cellwidth, rowIndex * cellheight, (colIndex + 1) * cellwidth, (rowIndex + 1) * cellheight),
                    paint);
            //player =0;
        }
    }
    public List<Move> getMove() {
        //tạo mới 1 danh sách, duyệt qua từng vị trí, nếu -1 còn vị trí đi
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) moves.add(new Move(i, j));//có thể đi dc
            }
        }
        return moves;
    }
    public int[][] getNewBoard(){
        int[][] newBoard = new int[rowQty][colQty];
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
    public int getCurrentDept(){
        int count = 0;
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (board[i][j] == -1) count++;
            }
        }
        return count;
    }
    public boolean onTouchBot(View view, MotionEvent motionEvent) {
        if (winner == 0 || winner == 1) {
            return true;
        }

        int count = getCurrentDept();
        final int currentDetp = rowQty*colQty - count;

        Record record = abNegamaxing.negamax(Chessboard.this,currentDetp,rowQty*colQty,Integer.MIN_VALUE,Integer.MAX_VALUE);//nước đi
        //có nước đi, đặt nước đi
        //tiến trình
        if (isGameOver()) {
            if (winner == 1) {
                Toast.makeText(context, "Ban thua roi", Toast.LENGTH_LONG).show();
            } else if (winner == 0) {
                Toast.makeText(context, "Ban thang roi", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Ban hoa", Toast.LENGTH_LONG).show();
            }
        }else {
            onDrawBoard(record.getMove().getColIndex(),record.getMove().getRowIndex(), view);
            board[record.getMove().getRowIndex()][record.getMove().getColIndex()] = player;
            makeMove(record.getMove());
        }

        view.invalidate();
        return true;
    }

}


