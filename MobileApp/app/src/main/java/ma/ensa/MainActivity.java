package ma.ensa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ma.ensa.models.Exercise;
import ma.ensa.utils.ExerciseDataHolder;

public class MainActivity extends AppCompatActivity {

    private double totalSimilarity = 0;
    private int similarityCount = 0;
    private Exercise exercise;

    private ArrayList<Double> cosineValues = new ArrayList<>();

    // Video Analysis Components
    VideoView videoView;
    Displayvid displayVid;

    private int repeatCount = 3;
    private int currentRepeat = 0;

    // Camera Components
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    Display displayCam;
    int PERMISSION_REQUESTS = 1;

    // Shared Components
    Paint mPaint = new Paint();
    PoseDetectorOptions options = new PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build();
    PoseDetector poseDetector = PoseDetection.getClient(options);

    // Video Analysis Lists
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ArrayList<Bitmap> bitmap4DisplayArrayList = new ArrayList<>();
    ArrayList<Pose> poseArrayList = new ArrayList<>();
    ArrayList<List<PoseLandmark>> videoLandmarksList = new ArrayList<>();

    // Camera Analysis Lists
    ArrayList<Bitmap> cameraBitmapList = new ArrayList<>();
    ArrayList<Bitmap> cameraDisplayBitmapList = new ArrayList<>();
    ArrayList<Pose> cameraPoseList = new ArrayList<>();
    ArrayList<List<PoseLandmark>> cameraLandmarksList = new ArrayList<>();

    boolean isRunning = false;
    boolean isCameraRunning = false;

    private int videoWidth;
    private int videoHeight;
    private Bitmap bitmap4Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Video Components
        videoView = findViewById(R.id.videoView);
        displayVid = findViewById(R.id.displayOverlay);
        exercise = ExerciseDataHolder.getInstance().getExercise();

        // Setup Paint
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);

        // Configure video player with base64 video
        setupVideoPlayer();

        // Initialize Camera Components
        previewView = findViewById(R.id.previewView);
        displayCam = findViewById(R.id.displaycam);
        setupCameraPreview();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // Setup Camera
        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("MainActivity", "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void setupVideoPlayer() {
        videoView.setZOrderOnTop(false);
        videoView.setZOrderMediaOverlay(false);

        if (exercise.getBase64Video() != null) {
            try {
                // Create temporary file for decoded video
                File videoFile = File.createTempFile("video", "mp4", getCacheDir());

                // Decode base64 string to bytes
                byte[] decodedVideo = Base64.decode(exercise.getBase64Video(), Base64.DEFAULT);

                // Write bytes to temporary file
                FileOutputStream fos = new FileOutputStream(videoFile);
                fos.write(decodedVideo);
                fos.close();

                // Configure VideoView with temporary file
                videoView.setVideoPath(videoFile.getPath());
                videoView.setOnCompletionListener(mp -> {
                    if (currentRepeat < repeatCount - 1) {
                        currentRepeat++;
                        videoView.start();
                    } else {
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("cosineValues", cosineValues);
                        startActivity(intent);
                    }
                });

                // Start frame analysis
                analyzeVideoFrames(Uri.fromFile(videoFile));

                // Start playback
                videoView.start();

            } catch (IOException e) {
                Toast.makeText(this, "Erreur lors de la lecture de la vidéo", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Erreur de lecture vidéo", e);
            }
        } else {
            Toast.makeText(this, "Aucune vidéo disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCameraPreview() {
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        previewView.setScaleType(PreviewView.ScaleType.FIT_CENTER);
    }

    private void analyzeVideoFrames(Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoUri);

        Bitmap firstFrame = retriever.getFrameAtTime(0);
        if (firstFrame != null) {
            videoWidth = firstFrame.getWidth();
            videoHeight = firstFrame.getHeight();
            Log.d("MainActivity", "Video dimensions: " + videoWidth + "x" + videoHeight);
            runOnUiThread(() -> displayVid.setVideoDimensions(videoWidth, videoHeight));
        }

        new Thread(() -> {
            long frameTime = 0;
            long frameInterval = 203333;
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(durationStr) * 1000;

            while (frameTime < duration) {
                Bitmap bitmap = retriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST);
                if (bitmap != null) {
                    bitmapArrayList.add(rotateBitmap(bitmap, 0));

                    if (poseArrayList.size() == 0 && bitmapArrayList.size() >= 1 && !isRunning) {
                        isRunning = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!bitmapArrayList.isEmpty()) {
                                    Bitmap currentBitmap = bitmapArrayList.get(0);
                                    poseDetector.process(InputImage.fromBitmap(currentBitmap, 0))
                                            .addOnSuccessListener(pose -> {
                                                poseArrayList.add(pose);
                                                videoLandmarksList.add(pose.getAllPoseLandmarks());

                                                Log.d("MainActivity", "Video Keypoints:");
                                                for (PoseLandmark landmark : pose.getAllPoseLandmarks()) {
                                                    Log.d("MainActivity", String.format("Video Landmark %d: x=%.2f, y=%.2f",
                                                            landmark.getLandmarkType(), landmark.getPosition().x, landmark.getPosition().y));
                                                }

                                                if (!cameraPoseList.isEmpty() && !cameraLandmarksList.isEmpty()) {
                                                    double similarity = MovementComparison.cosineSimilarity(videoLandmarksList, cameraLandmarksList);
                                                    cosineValues.add(similarity);

                                                    double dtwDistance = MovementComparison.dtw(videoLandmarksList, cameraLandmarksList);
                                                }

                                                Bitmap mutableBitmap = currentBitmap.copy(Bitmap.Config.ARGB_8888, true);
                                                Canvas canvas = new Canvas(mutableBitmap);

                                                for (PoseLandmark poseLandmark : pose.getAllPoseLandmarks()) {
                                                    canvas.drawCircle(
                                                            poseLandmark.getPosition().x,
                                                            poseLandmark.getPosition().y,
                                                            5,
                                                            mPaint
                                                    );
                                                }

                                                bitmap4DisplayArrayList.clear();
                                                bitmap4DisplayArrayList.add(mutableBitmap);

                                                if (displayVid != null) {
                                                    displayVid.updatePose(pose);
                                                    displayVid.invalidate();
                                                }

                                                bitmapArrayList.clear();
                                                poseArrayList.clear();
                                                videoLandmarksList.clear();
                                                isRunning = false;
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("MainActivity", "Pose detection failed: " + e.getMessage());
                                                isRunning = false;
                                            });
                                }
                            }
                        });
                    }
                }
                frameTime += frameInterval;
            }

            try {
                retriever.release();
            } catch (IOException e) {
                Log.e("MainActivity", "Error releasing MediaMetadataRetriever: " + e.getMessage());
            }
        }).start();
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ActivityCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @OptIn(markerClass = ExperimentalGetImage.class)
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                ByteBuffer byteBuffer = imageProxy.getImage().getPlanes()[0].getBuffer();
                byteBuffer.rewind();
                Bitmap bitmap = Bitmap.createBitmap(imageProxy.getWidth(), imageProxy.getHeight(), Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(byteBuffer);

                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                matrix.postScale(-1, 1);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, imageProxy.getWidth(), imageProxy.getHeight(), matrix, false);

                cameraBitmapList.add(rotatedBitmap);

                if (cameraPoseList.size() >= 1) {
                    Canvas canvas = new Canvas(cameraBitmapList.get(0));

                    for (PoseLandmark poseLandmark : cameraPoseList.get(0).getAllPoseLandmarks()) {
                        canvas.drawCircle(poseLandmark.getPosition().x, poseLandmark.getPosition().y, 5, mPaint);
                    }

                    cameraDisplayBitmapList.clear();
                    cameraDisplayBitmapList.add(cameraBitmapList.get(0));
                    bitmap4Save = cameraBitmapList.get(cameraBitmapList.size() - 1);
                    cameraBitmapList.clear();
                    cameraBitmapList.add(bitmap4Save);
                    cameraPoseList.clear();
                    isCameraRunning = false;
                }

                if (cameraPoseList.size() == 0 && cameraBitmapList.size() >= 1 && !isCameraRunning) {
                    runCameraML();
                    isCameraRunning = true;
                }

                if (cameraDisplayBitmapList.size() >= 1) {
                    displayCam.getBitmap(cameraDisplayBitmapList.get(0));
                }

                imageProxy.close();
            }
        });

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }

    private void runCameraML() {
        poseDetector.process(InputImage.fromBitmap(cameraBitmapList.get(0), 0))
                .addOnSuccessListener(pose -> {
                    cameraPoseList.add(pose);
                    cameraLandmarksList.add(pose.getAllPoseLandmarks());

                    Log.d("MainActivity", "Camera Keypoints:");
                    for (PoseLandmark landmark : pose.getAllPoseLandmarks()) {
                        Log.d("MainActivity", String.format("Camera Landmark %d: x=%.2f, y=%.2f",
                                landmark.getLandmarkType(), landmark.getPosition().x, landmark.getPosition().y));
                    }

                    if (!poseArrayList.isEmpty() && !videoLandmarksList.isEmpty()) {
                        double similarity = MovementComparison.cosineSimilarity(videoLandmarksList, cameraLandmarksList);
                        double dtwDistance = MovementComparison.dtw(videoLandmarksList, cameraLandmarksList);
                    }
                })
                .addOnFailureListener(e -> Log.e("MainActivity", "Camera ML Kit failed: " + e.getMessage()));
    }

    // Permission Methods
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            return ps != null && ps.length > 0 ? ps : new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Nettoyer les ressources
        if (poseDetector != null) {
            poseDetector.close();
        }

        // Nettoyer les bitmaps
        cleanupBitmaps(bitmapArrayList);
        cleanupBitmaps(bitmap4DisplayArrayList);
        cleanupBitmaps(cameraBitmapList);
        cleanupBitmaps(cameraDisplayBitmapList);

        if (bitmap4Save != null && !bitmap4Save.isRecycled()) {
            bitmap4Save.recycle();
            bitmap4Save = null;
        }
    }

    private void cleanupBitmaps(List<Bitmap> bitmaps) {
        if (bitmaps != null) {
            for (Bitmap bitmap : bitmaps) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            bitmaps.clear();
        }
    }
}