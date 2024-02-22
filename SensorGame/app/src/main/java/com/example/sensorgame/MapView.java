 package com.example.sensorgame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.example.sensorgame.Ball;
import com.example.sensorgame.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapView extends View implements SensorEventListener, Runnable {


    private static final float FILTER_FACTOR = 0.2f;

    private float mAccelX = 0.0f;
    private float mAccelY = 0.0f;

    private float mVectorX = 0.0f;
    private float mVectorY = 0.0f;


    private static final int WALL = GameMap.WALL_TILE;
    private static final float REBOUND = 0.5f;

    private GameMap map = null;
    private Ball ball = null;
    // 画面サイズ
    private int mWidth;

    private Handler mHandler = null;
    private int mHeight;
    public static final int GAME_RUN = 1;
    public static final int GAME_OVER = 2;
    private int state = 0;

    private int currentStage = 1;


    private long mStartTime = 0;
    private long mtoTalTime = 0;


    Paint fullScr = new Paint();
    Paint message = new Paint();


    private static final int MAX_STAGE = 3;

    public void stopGame() {
        state = GAME_OVER;
        freeHandler();
    }

    @Override
    public void run() {
        gameLoop();
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, 30);
        }
    }

    public void freeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
            // Chỉ set mHandler thành null sau khi đã chắc chắn không còn sử dụng nó
            mHandler = null;
        }
    }


    public int getState() {
        return state;
    }

    public MapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mHandler = new Handler();
        map = new GameMap();
        ball = new Ball();

        int[][] data = loadLabybyrinth(1);
        map.setData(data);
    }

    //         private int[][] makeTestData(){
//         int[][] data = new int[32][20];
//         for (int i = 0; i < data.length; i++) {
//             for (int j = 0; j < data[i].length; j++) {
//                 if( i == 0 || i == data.length - 1 ||
//                         j == 0 || j == data[i].length - 1){
//                     data[i][j] = 1; // 壁
//                     }else{
//                     data[i][j] = 0; // 通路
//                     }
//                 }
//             }
//
//         data[3][8] = 2;
//         return data;
//         }
    public int[][] loadLabybyrinth(int level) {
        final String fileName = "stage" + level + ".txt";
        final int MAZE_ROWS = GameMap.MAP_ROWS;
        final int MAZE_COLS = GameMap.MAP_COLS;

        int[][] data = new int[MAZE_ROWS][MAZE_COLS];
        InputStream is = null;
        try {
            is = getContext().getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < MAZE_ROWS) {

                String[] rows = line.split(",");
                for (int j = 0; j < rows.length; j++) {
                    data[i][j] = Integer.parseInt(rows[j].trim());
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return data;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        map.draw(canvas);
        ball.draw(canvas);

        if (state == GAME_OVER) {
            // Vẽ màn hình game over và thông điệp
            fullScr.setColor(0xDD000000);
            canvas.drawRect(0f, 0f, (float) mWidth, (float) mHeight, fullScr);
            message.setColor(Color.GREEN);
            message.setAntiAlias(true);
            message.setTextSize(40);
            message.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("やった", mWidth / 2, mHeight / 2, message);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        map.setSize(w, h);
        ball.setmRadius(w / (2 * GameMap.MAP_COLS));
        initGame();
    }

    public void initGame() {
        mtoTalTime = 0;
        ball.setPosition(ball.getRadius() * 6, ball.getRadius() * 6);
        invalidate();
    }

    public void startGame() {
        mHandler.post(this);
        ball.setPosition(ball.getRadius() * 6, ball.getRadius() * 6);
        mStartTime = System.currentTimeMillis() - mStartTime;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Ánh xạ trực tiếp giá trị cảm biến gia tốc đến tốc độ của bóng
            mAccelX = sensorEvent.values[0];
            mAccelY = sensorEvent.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

////    @Override
//    public void run() {
////        gameLoop();
////    }

    private void gameLoop() {

//        // Thêm giới hạn tốc độ để tránh di chuyển quá nhanh
//        final float maxSpeed = 10.0f;
//        mVectorX += mAccelX;
//        mVectorY += mAccelY;


        if (mHandler != null) {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, 30);
        }
        mVectorX = mVectorX - mAccelX;
        mVectorY = mVectorY + mAccelY;
        int nextX = ball.getmX() + (int) mVectorX;
        int nextY = ball.getmY() + (int) mVectorY;
        int radius = ball.getRadius();

        int cellType = map.getCellType(nextX, nextY);


        if (cellType == GameMap.EXIT_TILE) {
            if (currentStage < MAX_STAGE) {
                currentStage++;
                loadLabybyrinth(currentStage);
                initGame();

            } else {
                // Hiển thị thông báo chiến thắng và thoát trò chơi
                showMessageWinning();
                stopGame();
            }
        }




        if ((nextX - radius) < 0) {
            mVectorY *= -0.5f;
        } else if ((nextX + radius) > mWidth) {
            mVectorX *= -0.5f;
        }
        if ((nextY - radius) < 0) {
            mVectorY *= -0.5f;
        } else if ((nextY + radius) > mHeight) {
            mVectorY *= -0.5f;
        }

        if (radius < nextX && nextX < mWidth - radius && radius < nextY && nextY < mHeight - radius) {
            // 壁の当たり判定
            int ul = map.getCellType(nextX - radius, nextY - radius);
            int ur = map.getCellType(nextX + radius, nextY - radius);
            int dl = map.getCellType(nextX - radius, nextY + radius);
            int dr = map.getCellType(nextX + radius, nextY + radius);
            if (ul != WALL && ur != WALL && dl != WALL && dr != WALL) {
            } else if (ul != WALL && ur == WALL && dl != WALL && dr == WALL) {
                mVectorX *= -REBOUND;
            } else if (ul == WALL && ur != WALL && dl == WALL && dr != WALL) {
                mVectorX *= -REBOUND;
            } else if (ul == WALL && ur == WALL && dl != WALL && dr != WALL) {
                mVectorY *= -REBOUND;
            } else if (ul != WALL && ur != WALL && dl == WALL && dr == WALL) {
                mVectorY *= -REBOUND;
            } else if (ul == WALL && ur != WALL && dl != WALL && dr != WALL) {
                if (mVectorX < 0.0f && mVectorY > 0.0f) {
                    mVectorX *= -REBOUND;
                } else if (mVectorX > 0.0f && mVectorY < 0.0f) {
                    mVectorY *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur == WALL && dl != WALL && dr != WALL) {
                if (mVectorX > 0.0f && mVectorY > 0.0f) {
                    mVectorX *= -REBOUND;
                } else if (mVectorX < 0.0f && mVectorY < 0.0f) {
                    mVectorY *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur != WALL && dl == WALL && dr != WALL) {
                if (mVectorX > 0.0f && mVectorY > 0.0f) {
                    mVectorY *= -REBOUND;
                } else if (mVectorX < 0.0f && mVectorY < 0.0f) {
                    mVectorX *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur != WALL && dl != WALL && dr == WALL) {
                if (mVectorX < 0.0f && mVectorY > 0.0f) {
                    mVectorY *= -REBOUND;
                } else if (mVectorX > 0.0f && mVectorY < 0.0f) {
                    mVectorX *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else {
                mVectorX *= -REBOUND;
                mVectorY *= -REBOUND;
            }
        }


        if (map.getCellType(nextX, nextY) == GameMap.EXIT_TILE) {
            state = GAME_OVER;
            freeHandler();
            invalidate(); // Yêu cầu vẽ lại View
        }
//        else if (map.getCellType(nextX,nextY) == GameMap.VOID_TILE) {
//            state = GAME_FALL;
//            stopGame();
//        }

        ball.move((int) mVectorX, (int) mVectorY);
        invalidate();

        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, 30);


    }

//    private void loadStage(int stageNumber) {
//        currentStage = stageNumber;
//        int[][] data = loadLabyrinth(stageNumber);
//        map.setData(data);
//        initGame();
//    }

    private void showMessageWinning() {
        fullScr.setColor(0xDD000000);
        message.setColor(Color.GREEN);
        message.setAntiAlias(true);
        message.setTextSize(40);
        message.setTextAlign(Paint.Align.CENTER);
        // Đoạn text thông báo chiến thắng, bạn có thể thay đổi nội dung tùy ý
        String winMessage = "Chiến thắng! Bạn đã hoàn thành tất cả các stage!";
//       Canvas canvas.drawText();
    }


}