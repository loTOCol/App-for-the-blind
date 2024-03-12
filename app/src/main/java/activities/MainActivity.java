package activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import java.util.Locale;

import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private TextToSpeech textToSpeech;

    private class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // 두 번 클릭 이벤트 처리
            // TTS 중지
            if (textToSpeech != null) {
                textToSpeech.stop();
            }
            // 다음 화면으로 이동
            Intent intent = new Intent(MainActivity.this, ClassifierActivity.class);
            startActivity(intent);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GestureDetector 객체 생성
        gestureDetector = new GestureDetector(this, new DoubleTapGestureListener());

        // TTS 객체 생성
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // 언어 설정
                int result = textToSpeech.setLanguage(Locale.KOREAN);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(MainActivity.this, "지원되지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 음성 출력
                    String message = "안녕하세요. 시각장애인을 위한 간식 도우미입니다. 두번 클릭시 앱을 이용하실 수 있습니다";
                    textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                Toast.makeText(MainActivity.this, "TTS 초기화 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체 해제
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // onTouchEvent() 메서드에서 GestureDetector 이벤트 처리
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
