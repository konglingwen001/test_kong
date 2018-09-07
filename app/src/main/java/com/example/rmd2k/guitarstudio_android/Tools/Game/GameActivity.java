package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.rmd2k.guitarstudio_android.R;

import java.lang.ref.WeakReference;

public class GameActivity extends AppCompatActivity {

    LinearLayout gameViewLayout;
    GameView gameView;
    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //gameViewLayout = findViewById(R.id.gameViewLayout);
        gameView = findViewById(R.id.gameView);
        int height = gameView.getHeight();
        Log.e("AAA", "height:" + height);
        myHandler = new MyHandler(this);

        new MyThread().start();
    }

//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        View view = super.onCreateView(name, context, attrs);
//        gameView = findViewById(R.id.gameView);
//        int height = gameView.getHeight();
//        Log.e("AAA", "height:" + height);
//        return view;
//    }

    class MyThread extends Thread {
        @Override
        public void run() {
            //Message msg = Message.obtain();
            while(true) {
                myHandler.sendEmptyMessage(0);
                try {
                    sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyHandler extends Handler {

        private final WeakReference<GameActivity> mActivity;
        public MyHandler(GameActivity gameActivity) {
            mActivity = new WeakReference<>(gameActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            GameActivity gameActivity = mActivity.get();
            gameActivity.gameView.invalidate();
            //gameActivity.gameViewLayout.invalidate();
        }
    }
}
