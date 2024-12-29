package ma.ensa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

public class Displayvid extends View {
    private static final String TAG = "PoseDisplay";

    private Pose currentPose;
    private Paint paint;
    private int videoWidth;
    private int videoHeight;

    public Displayvid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
    }

    public void setVideoDimensions(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
        invalidate();
    }

    public void updatePose(Pose pose) {
        this.currentPose = pose;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentPose != null && videoWidth > 0 && videoHeight > 0) {
            float scaleFactor = Math.min(
                    (float) getWidth() / videoWidth,
                    (float) getHeight() / videoHeight
            );
            float offsetX = (getWidth() - videoWidth * scaleFactor) / 2;
            float offsetY = (getHeight() - videoHeight * scaleFactor) / 2;

            for (PoseLandmark landmark : currentPose.getAllPoseLandmarks()) {
                float x = landmark.getPosition().x * scaleFactor + offsetX;
                float y = landmark.getPosition().y * scaleFactor + offsetY;
                canvas.drawCircle(x, y, 5, paint);
            }
        }
    }

}
