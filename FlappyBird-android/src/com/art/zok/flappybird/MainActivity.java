package com.art.zok.flappybird;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AndroidApplication {	
    private int points = 0;
    private Point pt1 = new Point();
    private Point pt2 = new Point();
    private View view;
    private int width;
    private int height;
    private ViewGroup.LayoutParams layoutParams;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        view = initializeForView(new FlappyBirdGame(), cfg);
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game_layout);
        gameLayout.addView(view);
        layoutParams = view.getLayoutParams();
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pt1.set((int)event.getX(), (int)event.getY());
                points += 1;
                break;
            case MotionEvent.ACTION_UP:
                points -= 1;
                break;
            case MotionEvent.ACTION_POINTER_1_DOWN:
                pt2.set((int)event.getX(), (int)event.getY());
                points += 1;
                break;
            case MotionEvent.ACTION_POINTER_1_UP:
                points -= 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if(points >= 1) {
                    float lenOrg = (float) Math.sqrt(Math.pow((pt1.x - pt2.x), 2) + Math.pow((pt1.y - pt2.y), 2));
//                    float lenCur = (float) Math.sqrt(Math.pow((event.getX() - event.getX(1)), 2) + Math.pow((event.getY() - event.getY(2)), 2));
                    float diff = lenOrg;
                    float x = view.getWidth() + diff;
                    float y = view.getHeight() / view.getWidth() * x;
                    if(x > 0 && x < width && y > 0 && y < height) {
                        layoutParams.width = (int) x;
                        layoutParams.height = (int) y;
                    }
                }
                break;
            default:
                break;
            }
        return super.dispatchTouchEvent(event);
    }
}