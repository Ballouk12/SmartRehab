package ma.ensa;

import android.util.Log;

import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.List;

public class MovementComparison {


    private static double[][] landmarksToVectors(ArrayList<List<PoseLandmark>> landmarksList) {
        double[][] vectors = new double[landmarksList.size()][33 * 2]; // 33 landmarks * 2 coordinates

        for (int i = 0; i < landmarksList.size(); i++) {
            List<PoseLandmark> landmarks = landmarksList.get(i);
            int idx = 0;

            for (PoseLandmark landmark : landmarks) {
                if (idx < vectors[i].length - 1) {
                    vectors[i][idx++] = landmark.getPosition().x;
                    vectors[i][idx++] = landmark.getPosition().y;
                }
            }
        }
        return vectors;
    }

    public static double cosineSimilarity(ArrayList<List<PoseLandmark>> landmarks1,
                                          ArrayList<List<PoseLandmark>> landmarks2) {
        if (landmarks1.isEmpty() || landmarks2.isEmpty()) {
            return 0.0;
        }

        double[][] vectors1 = landmarksToVectors(landmarks1);
        double[][] vectors2 = landmarksToVectors(landmarks2);

        double[] flatVec1 = flattenVector(vectors1);
        double[] flatVec2 = flattenVector(vectors2);

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < flatVec1.length; i++) {
            dotProduct += flatVec1[i] * flatVec2[i];
            norm1 += flatVec1[i] * flatVec1[i];
            norm2 += flatVec2[i] * flatVec2[i];
        }

        double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        Log.d("Movement", String.format("Cosine Similarity: %.4f", similarity));
        return similarity;
    }

    public static double dtw(ArrayList<List<PoseLandmark>> landmarks1,
                             ArrayList<List<PoseLandmark>> landmarks2) {
        if (landmarks1.isEmpty() || landmarks2.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }

        double[][] vectors1 = landmarksToVectors(landmarks1);
        double[][] vectors2 = landmarksToVectors(landmarks2);

        int n = vectors1.length;
        int m = vectors2.length;
        double[][] dtw = new double[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dtw[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        dtw[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                double cost = euclideanDistance(vectors1[i-1], vectors2[j-1]);
                dtw[i][j] = cost + Math.min(dtw[i-1][j],
                        Math.min(dtw[i][j-1],
                                dtw[i-1][j-1]));
            }
        }

        double distance = dtw[n][m];
        Log.d("Movement", String.format("DTW Distance: %.4f", distance));
        return distance;
    }

    private static double[] flattenVector(double[][] vector) {
        double[] flat = new double[vector.length * vector[0].length];
        int idx = 0;
        for (double[] row : vector) {
            for (double val : row) {
                flat[idx++] = val;
            }
        }
        return flat;
    }

    private static double euclideanDistance(double[] vec1, double[] vec2) {
        double sum = 0;
        for (int i = 0; i < vec1.length; i++) {
            sum += Math.pow(vec1[i] - vec2[i], 2);
        }
        return Math.sqrt(sum);
    }
}