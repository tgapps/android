package org.telegram.ui.Components.Paint;

import android.graphics.Bitmap;
import android.graphics.PointF;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Size;

public class PhotoFace {
    private float angle;
    private Point chinPoint;
    private Point eyesCenterPoint;
    private float eyesDistance;
    private Point foreheadPoint;
    private Point mouthPoint;
    private float width;

    public PhotoFace(Face face, Bitmap sourceBitmap, Size targetSize, boolean sideward) {
        PhotoFace photoFace = this;
        Bitmap bitmap = sourceBitmap;
        Size size = targetSize;
        boolean z = sideward;
        Point leftEyePoint = null;
        Point rightEyePoint = null;
        Point leftMouthPoint = null;
        Point rightMouthPoint = null;
        for (Landmark landmark : face.getLandmarks()) {
            PointF point = landmark.getPosition();
            switch (landmark.getType()) {
                case 4:
                    leftEyePoint = transposePoint(point, bitmap, size, z);
                    break;
                case 5:
                    leftMouthPoint = transposePoint(point, bitmap, size, z);
                    break;
                case 10:
                    rightEyePoint = transposePoint(point, bitmap, size, z);
                    break;
                case 11:
                    rightMouthPoint = transposePoint(point, bitmap, size, z);
                    break;
                default:
                    break;
            }
        }
        if (!(leftEyePoint == null || rightEyePoint == null)) {
            photoFace.eyesCenterPoint = new Point((leftEyePoint.x * 0.5f) + (rightEyePoint.x * 0.5f), (leftEyePoint.y * 0.5f) + (rightEyePoint.y * 0.5f));
            photoFace.eyesDistance = (float) Math.hypot((double) (rightEyePoint.x - leftEyePoint.x), (double) (rightEyePoint.y - leftEyePoint.y));
            photoFace.angle = (float) Math.toDegrees(3.141592653589793d + Math.atan2((double) (rightEyePoint.y - leftEyePoint.y), (double) (rightEyePoint.x - leftEyePoint.x)));
            photoFace.width = photoFace.eyesDistance * 2.35f;
            float foreheadHeight = 0.8f * photoFace.eyesDistance;
            float upAngle = (float) Math.toRadians((double) (photoFace.angle - 90.0f));
            photoFace.foreheadPoint = new Point(photoFace.eyesCenterPoint.x + (((float) Math.cos((double) upAngle)) * foreheadHeight), photoFace.eyesCenterPoint.y + (((float) Math.sin((double) upAngle)) * foreheadHeight));
        }
        if (leftMouthPoint != null && rightMouthPoint != null) {
            photoFace.mouthPoint = new Point((leftMouthPoint.x * 0.5f) + (rightMouthPoint.x * 0.5f), (leftMouthPoint.y * 0.5f) + (0.5f * rightMouthPoint.y));
            foreheadHeight = 0.7f * photoFace.eyesDistance;
            upAngle = (float) Math.toRadians((double) (photoFace.angle + 90.0f));
            photoFace.chinPoint = new Point(photoFace.mouthPoint.x + (((float) Math.cos((double) upAngle)) * foreheadHeight), photoFace.mouthPoint.y + (((float) Math.sin((double) upAngle)) * foreheadHeight));
        }
    }

    public boolean isSufficient() {
        return this.eyesCenterPoint != null;
    }

    private Point transposePoint(PointF point, Bitmap sourceBitmap, Size targetSize, boolean sideward) {
        return new Point((targetSize.width * point.x) / ((float) (sideward ? sourceBitmap.getHeight() : sourceBitmap.getWidth())), (targetSize.height * point.y) / ((float) (sideward ? sourceBitmap.getWidth() : sourceBitmap.getHeight())));
    }

    public Point getPointForAnchor(int anchor) {
        switch (anchor) {
            case 0:
                return this.foreheadPoint;
            case 1:
                return this.eyesCenterPoint;
            case 2:
                return this.mouthPoint;
            case 3:
                return this.chinPoint;
            default:
                return null;
        }
    }

    public float getWidthForAnchor(int anchor) {
        if (anchor == 1) {
            return this.eyesDistance;
        }
        return this.width;
    }

    public float getAngle() {
        return this.angle;
    }
}
