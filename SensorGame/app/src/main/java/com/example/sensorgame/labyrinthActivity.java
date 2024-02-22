package com.example.sensorgame;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class labyrinthActivity extends AppCompatActivity {

    private MapView view;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đặt Activity ở chế độ toàn màn hình và ẩn thanh tiêu đề
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Khởi tạo MapView và thiết lập làm nội dung chính của Activity
        view = new MapView(this);
        setContentView(view);

        // Lấy SensorManager từ hệ thống
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Đăng ký người lắng nghe cảm biến gia tốc kế
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.registerListener(view, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        // Bắt đầu trò chơi
        view.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Hủy đăng ký người lắng nghe cảm biến khi Activity không hoạt động
        sensorManager.unregisterListener(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (view.getState() == MapView.GAME_OVER) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                view.startGame(); // Bắt đầu lại trò chơi hoặc thực hiện hành động khác
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

}
