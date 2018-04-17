package net.hockeyapp.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static int determineOrientation(android.content.Context r1, android.net.Uri r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.utils.ImageUtils.determineOrientation(android.content.Context, android.net.Uri):int
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
        r0 = 0;
        r1 = r5.getContentResolver();	 Catch:{ IOException -> 0x001f }
        r1 = r1.openInputStream(r6);	 Catch:{ IOException -> 0x001f }
        r0 = r1;	 Catch:{ IOException -> 0x001f }
        r1 = determineOrientation(r0);	 Catch:{ IOException -> 0x001f }
        if (r0 == 0) goto L_0x001b;
    L_0x0010:
        r0.close();	 Catch:{ IOException -> 0x0014 }
        goto L_0x001b;
    L_0x0014:
        r2 = move-exception;
        r3 = "Unable to close input stream.";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);
        goto L_0x001c;
        return r1;
    L_0x001d:
        r1 = move-exception;
        goto L_0x0035;
    L_0x001f:
        r1 = move-exception;
        r2 = "Unable to determine necessary screen orientation.";	 Catch:{ all -> 0x001d }
        net.hockeyapp.android.utils.HockeyLog.error(r2, r1);	 Catch:{ all -> 0x001d }
        r2 = 1;
        if (r0 == 0) goto L_0x0033;
        r0.close();	 Catch:{ IOException -> 0x002c }
        goto L_0x0033;
    L_0x002c:
        r3 = move-exception;
        r4 = "Unable to close input stream.";
        net.hockeyapp.android.utils.HockeyLog.error(r4, r3);
        goto L_0x0034;
        return r2;
        if (r0 == 0) goto L_0x0043;
        r0.close();	 Catch:{ IOException -> 0x003c }
        goto L_0x0043;
    L_0x003c:
        r2 = move-exception;
        r3 = "Unable to close input stream.";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.ImageUtils.determineOrientation(android.content.Context, android.net.Uri):int");
    }

    public static int determineOrientation(File file) throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            int determineOrientation = determineOrientation(input);
            return determineOrientation;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public static int determineOrientation(InputStream input) {
        Options options = new Options();
        int i = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        if (options.outWidth != -1) {
            if (options.outHeight != -1) {
                if (((float) options.outWidth) / ((float) options.outHeight) > 1.0f) {
                    i = 0;
                }
                return i;
            }
        }
        return 1;
    }

    public static Bitmap decodeSampledBitmap(File file, int reqWidth, int reqHeight) throws IOException {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap decodeSampledBitmap(Context context, Uri imageUri, int reqWidth, int reqHeight) throws IOException {
        InputStream inputBounds = null;
        InputStream inputBitmap = null;
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            inputBounds = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(inputBounds, null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            inputBitmap = context.getContentResolver().openInputStream(imageUri);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputBitmap, null, options);
            return decodeStream;
        } finally {
            if (inputBounds != null) {
                inputBounds.close();
            }
            if (inputBitmap != null) {
                inputBitmap.close();
            }
        }
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
