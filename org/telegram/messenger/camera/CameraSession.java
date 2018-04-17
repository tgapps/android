package org.telegram.messenger.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

public class CameraSession {
    public static final int ORIENTATION_HYSTERESIS = 5;
    private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };
    protected CameraInfo cameraInfo;
    private String currentFlashMode = "off";
    private int currentOrientation;
    private int diffOrientation;
    private boolean initied;
    private boolean isVideo;
    private int jpegOrientation;
    private int lastDisplayOrientation = -1;
    private int lastOrientation = -1;
    private boolean meteringAreaSupported;
    private OrientationEventListener orientationEventListener;
    private final int pictureFormat;
    private final Size pictureSize;
    private final Size previewSize;
    private boolean sameTakePictureOrientation;

    protected void configurePhotoCamera() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraSession.configurePhotoCamera():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r11.cameraInfo;	 Catch:{ Throwable -> 0x00f1 }
        r0 = r0.camera;	 Catch:{ Throwable -> 0x00f1 }
        if (r0 == 0) goto L_0x00f0;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0006:
        r1 = new android.hardware.Camera$CameraInfo;	 Catch:{ Throwable -> 0x00f1 }
        r1.<init>();	 Catch:{ Throwable -> 0x00f1 }
        r2 = 0;
        r3 = r0.getParameters();	 Catch:{ Exception -> 0x0012 }
        r2 = r3;
        goto L_0x0016;
    L_0x0012:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ Throwable -> 0x00f1 }
    L_0x0016:
        r3 = r11.cameraInfo;	 Catch:{ Throwable -> 0x00f1 }
        r3 = r3.getCameraId();	 Catch:{ Throwable -> 0x00f1 }
        android.hardware.Camera.getCameraInfo(r3, r1);	 Catch:{ Throwable -> 0x00f1 }
        r3 = 1;	 Catch:{ Throwable -> 0x00f1 }
        r4 = r11.getDisplayOrientation(r1, r3);	 Catch:{ Throwable -> 0x00f1 }
        r5 = "samsung";	 Catch:{ Throwable -> 0x00f1 }
        r6 = android.os.Build.MANUFACTURER;	 Catch:{ Throwable -> 0x00f1 }
        r5 = r5.equals(r6);	 Catch:{ Throwable -> 0x00f1 }
        r6 = 0;	 Catch:{ Throwable -> 0x00f1 }
        if (r5 == 0) goto L_0x003b;	 Catch:{ Throwable -> 0x00f1 }
    L_0x002f:
        r5 = "sf2wifixx";	 Catch:{ Throwable -> 0x00f1 }
        r7 = android.os.Build.PRODUCT;	 Catch:{ Throwable -> 0x00f1 }
        r5 = r5.equals(r7);	 Catch:{ Throwable -> 0x00f1 }
        if (r5 == 0) goto L_0x003b;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0039:
        r5 = 0;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x006e;	 Catch:{ Throwable -> 0x00f1 }
    L_0x003b:
        r5 = 0;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r4;	 Catch:{ Throwable -> 0x00f1 }
        switch(r7) {
            case 0: goto L_0x004a;
            case 1: goto L_0x0047;
            case 2: goto L_0x0044;
            case 3: goto L_0x0041;
            default: goto L_0x0040;
        };	 Catch:{ Throwable -> 0x00f1 }
    L_0x0040:
        goto L_0x004c;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0041:
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x004c;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0044:
        r5 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x004c;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0047:
        r5 = 90;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x004c;	 Catch:{ Throwable -> 0x00f1 }
    L_0x004a:
        r5 = 0;	 Catch:{ Throwable -> 0x00f1 }
    L_0x004c:
        r8 = r1.orientation;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 % 90;	 Catch:{ Throwable -> 0x00f1 }
        if (r8 == 0) goto L_0x0054;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0052:
        r1.orientation = r6;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0054:
        r8 = r1.facing;	 Catch:{ Throwable -> 0x00f1 }
        if (r8 != r3) goto L_0x0064;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0058:
        r8 = r1.orientation;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 + r5;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r8;	 Catch:{ Throwable -> 0x00f1 }
        r8 = 360 - r7;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r8;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x006c;	 Catch:{ Throwable -> 0x00f1 }
    L_0x0064:
        r8 = r1.orientation;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 - r5;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 + 360;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r8;	 Catch:{ Throwable -> 0x00f1 }
        r5 = r7;	 Catch:{ Throwable -> 0x00f1 }
    L_0x006e:
        r11.currentOrientation = r5;	 Catch:{ Throwable -> 0x00f1 }
        r0.setDisplayOrientation(r5);	 Catch:{ Throwable -> 0x00f1 }
        if (r2 == 0) goto L_0x00f0;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r11.previewSize;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r7.getWidth();	 Catch:{ Throwable -> 0x00f1 }
        r8 = r11.previewSize;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x00f1 }
        r2.setPreviewSize(r7, r8);	 Catch:{ Throwable -> 0x00f1 }
        r7 = r11.pictureSize;	 Catch:{ Throwable -> 0x00f1 }
        r7 = r7.getWidth();	 Catch:{ Throwable -> 0x00f1 }
        r8 = r11.pictureSize;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x00f1 }
        r2.setPictureSize(r7, r8);	 Catch:{ Throwable -> 0x00f1 }
        r7 = r11.pictureFormat;	 Catch:{ Throwable -> 0x00f1 }
        r2.setPictureFormat(r7);	 Catch:{ Throwable -> 0x00f1 }
        r7 = "continuous-picture";	 Catch:{ Throwable -> 0x00f1 }
        r8 = r2.getSupportedFocusModes();	 Catch:{ Throwable -> 0x00f1 }
        r8 = r8.contains(r7);	 Catch:{ Throwable -> 0x00f1 }
        if (r8 == 0) goto L_0x00a7;	 Catch:{ Throwable -> 0x00f1 }
        r2.setFocusMode(r7);	 Catch:{ Throwable -> 0x00f1 }
        r8 = 0;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x00f1 }
        r10 = -1;	 Catch:{ Throwable -> 0x00f1 }
        if (r9 == r10) goto L_0x00c4;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r1.facing;	 Catch:{ Throwable -> 0x00f1 }
        if (r9 != r3) goto L_0x00bc;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r1.orientation;	 Catch:{ Throwable -> 0x00f1 }
        r10 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r9 - r10;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r9 + 360;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r9 % 360;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r9;	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x00c4;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r1.orientation;	 Catch:{ Throwable -> 0x00f1 }
        r10 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r9 + r10;	 Catch:{ Throwable -> 0x00f1 }
        r9 = r9 % 360;	 Catch:{ Throwable -> 0x00f1 }
        r8 = r9;
        r2.setRotation(r8);	 Catch:{ Exception -> 0x00dd }
        r9 = r1.facing;	 Catch:{ Exception -> 0x00dd }
        if (r9 != r3) goto L_0x00d6;	 Catch:{ Exception -> 0x00dd }
        r9 = 360 - r4;	 Catch:{ Exception -> 0x00dd }
        r9 = r9 % 360;	 Catch:{ Exception -> 0x00dd }
        if (r9 != r8) goto L_0x00d3;	 Catch:{ Exception -> 0x00dd }
        r6 = r3;	 Catch:{ Exception -> 0x00dd }
        r11.sameTakePictureOrientation = r6;	 Catch:{ Exception -> 0x00dd }
        goto L_0x00dc;	 Catch:{ Exception -> 0x00dd }
        if (r4 != r8) goto L_0x00da;	 Catch:{ Exception -> 0x00dd }
        r6 = r3;	 Catch:{ Exception -> 0x00dd }
        r11.sameTakePictureOrientation = r6;	 Catch:{ Exception -> 0x00dd }
        goto L_0x00de;
    L_0x00dd:
        r6 = move-exception;
        r6 = r11.currentFlashMode;	 Catch:{ Throwable -> 0x00f1 }
        r2.setFlashMode(r6);	 Catch:{ Throwable -> 0x00f1 }
        r0.setParameters(r2);	 Catch:{ Exception -> 0x00e7 }
        goto L_0x00e8;
    L_0x00e7:
        r6 = move-exception;
        r6 = r2.getMaxNumMeteringAreas();	 Catch:{ Throwable -> 0x00f1 }
        if (r6 <= 0) goto L_0x00f0;	 Catch:{ Throwable -> 0x00f1 }
        r11.meteringAreaSupported = r3;	 Catch:{ Throwable -> 0x00f1 }
    L_0x00f0:
        goto L_0x00f5;
    L_0x00f1:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraSession.configurePhotoCamera():void");
    }

    protected void configureRoundCamera() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraSession.configureRoundCamera():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = 1;
        r11.isVideo = r0;	 Catch:{ Throwable -> 0x0153 }
        r1 = r11.cameraInfo;	 Catch:{ Throwable -> 0x0153 }
        r1 = r1.camera;	 Catch:{ Throwable -> 0x0153 }
        if (r1 == 0) goto L_0x0152;	 Catch:{ Throwable -> 0x0153 }
    L_0x0009:
        r2 = new android.hardware.Camera$CameraInfo;	 Catch:{ Throwable -> 0x0153 }
        r2.<init>();	 Catch:{ Throwable -> 0x0153 }
        r3 = 0;
        r4 = r1.getParameters();	 Catch:{ Exception -> 0x0015 }
        r3 = r4;
        goto L_0x0019;
    L_0x0015:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0153 }
    L_0x0019:
        r4 = r11.cameraInfo;	 Catch:{ Throwable -> 0x0153 }
        r4 = r4.getCameraId();	 Catch:{ Throwable -> 0x0153 }
        android.hardware.Camera.getCameraInfo(r4, r2);	 Catch:{ Throwable -> 0x0153 }
        r4 = r11.getDisplayOrientation(r2, r0);	 Catch:{ Throwable -> 0x0153 }
        r5 = "samsung";	 Catch:{ Throwable -> 0x0153 }
        r6 = android.os.Build.MANUFACTURER;	 Catch:{ Throwable -> 0x0153 }
        r5 = r5.equals(r6);	 Catch:{ Throwable -> 0x0153 }
        r6 = 0;	 Catch:{ Throwable -> 0x0153 }
        if (r5 == 0) goto L_0x003d;	 Catch:{ Throwable -> 0x0153 }
    L_0x0031:
        r5 = "sf2wifixx";	 Catch:{ Throwable -> 0x0153 }
        r7 = android.os.Build.PRODUCT;	 Catch:{ Throwable -> 0x0153 }
        r5 = r5.equals(r7);	 Catch:{ Throwable -> 0x0153 }
        if (r5 == 0) goto L_0x003d;	 Catch:{ Throwable -> 0x0153 }
    L_0x003b:
        r5 = 0;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x0070;	 Catch:{ Throwable -> 0x0153 }
    L_0x003d:
        r5 = 0;	 Catch:{ Throwable -> 0x0153 }
        r7 = r4;	 Catch:{ Throwable -> 0x0153 }
        switch(r7) {
            case 0: goto L_0x004c;
            case 1: goto L_0x0049;
            case 2: goto L_0x0046;
            case 3: goto L_0x0043;
            default: goto L_0x0042;
        };	 Catch:{ Throwable -> 0x0153 }
    L_0x0042:
        goto L_0x004e;	 Catch:{ Throwable -> 0x0153 }
    L_0x0043:
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x004e;	 Catch:{ Throwable -> 0x0153 }
    L_0x0046:
        r5 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x004e;	 Catch:{ Throwable -> 0x0153 }
    L_0x0049:
        r5 = 90;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x004e;	 Catch:{ Throwable -> 0x0153 }
    L_0x004c:
        r5 = 0;	 Catch:{ Throwable -> 0x0153 }
    L_0x004e:
        r8 = r2.orientation;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 % 90;	 Catch:{ Throwable -> 0x0153 }
        if (r8 == 0) goto L_0x0056;	 Catch:{ Throwable -> 0x0153 }
    L_0x0054:
        r2.orientation = r6;	 Catch:{ Throwable -> 0x0153 }
    L_0x0056:
        r8 = r2.facing;	 Catch:{ Throwable -> 0x0153 }
        if (r8 != r0) goto L_0x0066;	 Catch:{ Throwable -> 0x0153 }
    L_0x005a:
        r8 = r2.orientation;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 + r5;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x0153 }
        r7 = r8;	 Catch:{ Throwable -> 0x0153 }
        r8 = 360 - r7;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x0153 }
        r7 = r8;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x006e;	 Catch:{ Throwable -> 0x0153 }
    L_0x0066:
        r8 = r2.orientation;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 - r5;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 + 360;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8 % 360;	 Catch:{ Throwable -> 0x0153 }
        r7 = r8;	 Catch:{ Throwable -> 0x0153 }
        r5 = r7;	 Catch:{ Throwable -> 0x0153 }
    L_0x0070:
        r11.currentOrientation = r5;	 Catch:{ Throwable -> 0x0153 }
        r1.setDisplayOrientation(r5);	 Catch:{ Throwable -> 0x0153 }
        r7 = r11.currentOrientation;	 Catch:{ Throwable -> 0x0153 }
        r7 = r7 - r4;	 Catch:{ Throwable -> 0x0153 }
        r11.diffOrientation = r7;	 Catch:{ Throwable -> 0x0153 }
        if (r3 == 0) goto L_0x0152;	 Catch:{ Throwable -> 0x0153 }
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0153 }
        if (r7 == 0) goto L_0x00a8;	 Catch:{ Throwable -> 0x0153 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0153 }
        r7.<init>();	 Catch:{ Throwable -> 0x0153 }
        r8 = "set preview size = ";	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.previewSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getWidth();	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = " ";	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.previewSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x0153 }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Throwable -> 0x0153 }
        r7 = r11.previewSize;	 Catch:{ Throwable -> 0x0153 }
        r7 = r7.getWidth();	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.previewSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x0153 }
        r3.setPreviewSize(r7, r8);	 Catch:{ Throwable -> 0x0153 }
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0153 }
        if (r7 == 0) goto L_0x00e3;	 Catch:{ Throwable -> 0x0153 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0153 }
        r7.<init>();	 Catch:{ Throwable -> 0x0153 }
        r8 = "set picture size = ";	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.pictureSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getWidth();	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = " ";	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.pictureSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x0153 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0153 }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x0153 }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Throwable -> 0x0153 }
        r7 = r11.pictureSize;	 Catch:{ Throwable -> 0x0153 }
        r7 = r7.getWidth();	 Catch:{ Throwable -> 0x0153 }
        r8 = r11.pictureSize;	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.getHeight();	 Catch:{ Throwable -> 0x0153 }
        r3.setPictureSize(r7, r8);	 Catch:{ Throwable -> 0x0153 }
        r7 = r11.pictureFormat;	 Catch:{ Throwable -> 0x0153 }
        r3.setPictureFormat(r7);	 Catch:{ Throwable -> 0x0153 }
        r3.setRecordingHint(r0);	 Catch:{ Throwable -> 0x0153 }
        r7 = "auto";	 Catch:{ Throwable -> 0x0153 }
        r8 = r3.getSupportedFocusModes();	 Catch:{ Throwable -> 0x0153 }
        r8 = r8.contains(r7);	 Catch:{ Throwable -> 0x0153 }
        if (r8 == 0) goto L_0x0109;	 Catch:{ Throwable -> 0x0153 }
        r3.setFocusMode(r7);	 Catch:{ Throwable -> 0x0153 }
        r8 = 0;	 Catch:{ Throwable -> 0x0153 }
        r9 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x0153 }
        r10 = -1;	 Catch:{ Throwable -> 0x0153 }
        if (r9 == r10) goto L_0x0126;	 Catch:{ Throwable -> 0x0153 }
        r9 = r2.facing;	 Catch:{ Throwable -> 0x0153 }
        if (r9 != r0) goto L_0x011e;	 Catch:{ Throwable -> 0x0153 }
        r9 = r2.orientation;	 Catch:{ Throwable -> 0x0153 }
        r10 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x0153 }
        r9 = r9 - r10;	 Catch:{ Throwable -> 0x0153 }
        r9 = r9 + 360;	 Catch:{ Throwable -> 0x0153 }
        r9 = r9 % 360;	 Catch:{ Throwable -> 0x0153 }
        r8 = r9;	 Catch:{ Throwable -> 0x0153 }
        goto L_0x0126;	 Catch:{ Throwable -> 0x0153 }
        r9 = r2.orientation;	 Catch:{ Throwable -> 0x0153 }
        r10 = r11.jpegOrientation;	 Catch:{ Throwable -> 0x0153 }
        r9 = r9 + r10;	 Catch:{ Throwable -> 0x0153 }
        r9 = r9 % 360;	 Catch:{ Throwable -> 0x0153 }
        r8 = r9;
        r3.setRotation(r8);	 Catch:{ Exception -> 0x013f }
        r9 = r2.facing;	 Catch:{ Exception -> 0x013f }
        if (r9 != r0) goto L_0x0138;	 Catch:{ Exception -> 0x013f }
        r9 = 360 - r4;	 Catch:{ Exception -> 0x013f }
        r9 = r9 % 360;	 Catch:{ Exception -> 0x013f }
        if (r9 != r8) goto L_0x0135;	 Catch:{ Exception -> 0x013f }
        r6 = r0;	 Catch:{ Exception -> 0x013f }
        r11.sameTakePictureOrientation = r6;	 Catch:{ Exception -> 0x013f }
        goto L_0x013e;	 Catch:{ Exception -> 0x013f }
        if (r4 != r8) goto L_0x013c;	 Catch:{ Exception -> 0x013f }
        r6 = r0;	 Catch:{ Exception -> 0x013f }
        r11.sameTakePictureOrientation = r6;	 Catch:{ Exception -> 0x013f }
        goto L_0x0140;
    L_0x013f:
        r6 = move-exception;
        r6 = "off";	 Catch:{ Throwable -> 0x0153 }
        r3.setFlashMode(r6);	 Catch:{ Throwable -> 0x0153 }
        r1.setParameters(r3);	 Catch:{ Exception -> 0x0149 }
        goto L_0x014a;
    L_0x0149:
        r6 = move-exception;
        r6 = r3.getMaxNumMeteringAreas();	 Catch:{ Throwable -> 0x0153 }
        if (r6 <= 0) goto L_0x0152;	 Catch:{ Throwable -> 0x0153 }
        r11.meteringAreaSupported = r0;	 Catch:{ Throwable -> 0x0153 }
    L_0x0152:
        goto L_0x0157;
    L_0x0153:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraSession.configureRoundCamera():void");
    }

    public CameraSession(CameraInfo info, Size preview, Size picture, int format) {
        this.previewSize = preview;
        this.pictureSize = picture;
        this.pictureFormat = format;
        this.cameraInfo = info;
        this.currentFlashMode = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).getString(this.cameraInfo.frontCamera != 0 ? "flashMode_front" : "flashMode", "off");
        this.orientationEventListener = new OrientationEventListener(ApplicationLoader.applicationContext) {
            public void onOrientationChanged(int orientation) {
                if (CameraSession.this.orientationEventListener != null && CameraSession.this.initied) {
                    if (orientation != -1) {
                        CameraSession.this.jpegOrientation = CameraSession.this.roundOrientation(orientation, CameraSession.this.jpegOrientation);
                        int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                        if (!(CameraSession.this.lastOrientation == CameraSession.this.jpegOrientation && rotation == CameraSession.this.lastDisplayOrientation)) {
                            if (!CameraSession.this.isVideo) {
                                CameraSession.this.configurePhotoCamera();
                            }
                            CameraSession.this.lastDisplayOrientation = rotation;
                            CameraSession.this.lastOrientation = CameraSession.this.jpegOrientation;
                        }
                    }
                }
            }
        };
        if (this.orientationEventListener.canDetectOrientation()) {
            this.orientationEventListener.enable();
            return;
        }
        this.orientationEventListener.disable();
        this.orientationEventListener = null;
    }

    private int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation;
        if (orientationHistory == -1) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            changeOrientation = Math.min(dist, 360 - dist) >= 50;
        }
        if (changeOrientation) {
            return (((orientation + 45) / 90) * 90) % 360;
        }
        return orientationHistory;
    }

    public void checkFlashMode(String mode) {
        if (!CameraController.getInstance().availableFlashModes.contains(this.currentFlashMode)) {
            this.currentFlashMode = mode;
            configurePhotoCamera();
            ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit().putString(this.cameraInfo.frontCamera != 0 ? "flashMode_front" : "flashMode", mode).commit();
        }
    }

    public void setCurrentFlashMode(String mode) {
        this.currentFlashMode = mode;
        configurePhotoCamera();
        ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit().putString(this.cameraInfo.frontCamera != 0 ? "flashMode_front" : "flashMode", mode).commit();
    }

    public String getCurrentFlashMode() {
        return this.currentFlashMode;
    }

    public String getNextFlashMode() {
        ArrayList<String> modes = CameraController.getInstance().availableFlashModes;
        int a = 0;
        while (a < modes.size()) {
            if (!((String) modes.get(a)).equals(this.currentFlashMode)) {
                a++;
            } else if (a < modes.size() - 1) {
                return (String) modes.get(a + 1);
            } else {
                return (String) modes.get(0);
            }
        }
        return this.currentFlashMode;
    }

    public void setInitied() {
        this.initied = true;
    }

    public boolean isInitied() {
        return this.initied;
    }

    public int getCurrentOrientation() {
        return this.currentOrientation;
    }

    public int getWorldAngle() {
        return this.diffOrientation;
    }

    public boolean isSameTakePictureOrientation() {
        return this.sameTakePictureOrientation;
    }

    protected void focusToRect(Rect focusRect, Rect meteringRect) {
        try {
            Camera camera = this.cameraInfo.camera;
            if (camera != null) {
                camera.cancelAutoFocus();
                Parameters parameters = null;
                try {
                    parameters = camera.getParameters();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (parameters != null) {
                    parameters.setFocusMode("auto");
                    ArrayList<Area> meteringAreas = new ArrayList();
                    meteringAreas.add(new Area(focusRect, 1000));
                    parameters.setFocusAreas(meteringAreas);
                    if (this.meteringAreaSupported) {
                        meteringAreas = new ArrayList();
                        meteringAreas.add(new Area(meteringRect, 1000));
                        parameters.setMeteringAreas(meteringAreas);
                    }
                    try {
                        camera.setParameters(parameters);
                        camera.autoFocus(this.autoFocusCallback);
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                }
            }
        } catch (Throwable e3) {
            FileLog.e(e3);
        }
    }

    protected void configureRecorder(int quality, MediaRecorder recorder) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(this.cameraInfo.cameraId, info);
        int displayOrientation = getDisplayOrientation(info, false);
        int outputOrientation = 0;
        if (this.jpegOrientation != -1) {
            if (info.facing == 1) {
                outputOrientation = ((info.orientation - this.jpegOrientation) + 360) % 360;
            } else {
                outputOrientation = (info.orientation + this.jpegOrientation) % 360;
            }
        }
        recorder.setOrientationHint(outputOrientation);
        int highProfile = getHigh();
        boolean canGoHigh = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, highProfile);
        boolean canGoLow = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, 0);
        if (canGoHigh && (quality == 1 || !canGoLow)) {
            recorder.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, highProfile));
        } else if (canGoLow) {
            recorder.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, 0));
        } else {
            throw new IllegalStateException("cannot find valid CamcorderProfile");
        }
        this.isVideo = true;
    }

    protected void stopVideoRecording() {
        this.isVideo = false;
        configurePhotoCamera();
    }

    private int getHigh() {
        if ("LGE".equals(Build.MANUFACTURER) && "g3_tmo_us".equals(Build.PRODUCT)) {
            return 4;
        }
        return 1;
    }

    private int getDisplayOrientation(CameraInfo info, boolean isStillCapture) {
        int displayOrientation;
        int degrees = 0;
        switch (((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
                break;
            default:
                break;
        }
        if (info.facing == 1) {
            displayOrientation = (360 - ((info.orientation + degrees) % 360)) % 360;
            if (!isStillCapture && displayOrientation == 90) {
                displayOrientation = 270;
            }
            if (!isStillCapture && "Huawei".equals(Build.MANUFACTURER) && "angler".equals(Build.PRODUCT) && displayOrientation == 270) {
                displayOrientation = 90;
            }
        } else {
            displayOrientation = ((info.orientation - degrees) + 360) % 360;
        }
        return displayOrientation;
    }

    public int getDisplayOrientation() {
        try {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(this.cameraInfo.getCameraId(), info);
            return getDisplayOrientation(info, true);
        } catch (Throwable e) {
            FileLog.e(e);
            return 0;
        }
    }

    public void destroy() {
        this.initied = false;
        if (this.orientationEventListener != null) {
            this.orientationEventListener.disable();
            this.orientationEventListener = null;
        }
    }
}
