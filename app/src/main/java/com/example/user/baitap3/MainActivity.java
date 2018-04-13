package com.example.user.baitap3;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private Chessboard chessboard;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.img);
        chessboard = new Chessboard(MainActivity.this, 300, 300, 8, 8);
        chessboard.init();
        bitmap = chessboard.drawBoard();
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //return chessboard.onTouch(view,motionEvent);
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    return chessboard.onTouch(view, motionEvent);
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    return chessboard.onTouchBot(view, motionEvent);
                }
                return true;
            }
        });


    }
}