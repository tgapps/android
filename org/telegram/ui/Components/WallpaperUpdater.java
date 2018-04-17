package org.telegram.ui.Components;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import java.io.File;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.AlertDialog.Builder;

public class WallpaperUpdater {
    private String currentPicturePath;
    private File currentWallpaperPath;
    private WallpaperUpdaterDelegate delegate;
    private Activity parentActivity;
    private File picturePath = null;

    public interface WallpaperUpdaterDelegate {
        void didSelectWallpaper(File file, Bitmap bitmap);

        void needOpenColorPicker();
    }

    public void onActivityResult(int r1, int r2, android.content.Intent r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.WallpaperUpdater.onActivityResult(int, int, android.content.Intent):void
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
        r0 = -1;
        if (r10 != r0) goto L_0x0096;
    L_0x0003:
        r0 = 10;
        r1 = 87;
        r2 = 1;
        r3 = 0;
        if (r9 != r0) goto L_0x005d;
    L_0x000b:
        r0 = r8.currentPicturePath;
        org.telegram.messenger.AndroidUtilities.addMediaToGallery(r0);
        r0 = r3;
        r4 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();	 Catch:{ Exception -> 0x0043 }
        r5 = r8.currentPicturePath;	 Catch:{ Exception -> 0x0043 }
        r6 = r4.x;	 Catch:{ Exception -> 0x0043 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x0043 }
        r7 = r4.y;	 Catch:{ Exception -> 0x0043 }
        r7 = (float) r7;	 Catch:{ Exception -> 0x0043 }
        r2 = org.telegram.messenger.ImageLoader.loadBitmap(r5, r3, r6, r7, r2);	 Catch:{ Exception -> 0x0043 }
        r5 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0043 }
        r6 = r8.currentWallpaperPath;	 Catch:{ Exception -> 0x0043 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x0043 }
        r0 = r5;	 Catch:{ Exception -> 0x0043 }
        r5 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0043 }
        r2.compress(r5, r1, r0);	 Catch:{ Exception -> 0x0043 }
        r1 = r8.delegate;	 Catch:{ Exception -> 0x0043 }
        r5 = r8.currentWallpaperPath;	 Catch:{ Exception -> 0x0043 }
        r1.didSelectWallpaper(r5, r2);	 Catch:{ Exception -> 0x0043 }
        if (r0 == 0) goto L_0x0040;
    L_0x0037:
        r0.close();	 Catch:{ Exception -> 0x003b }
        goto L_0x0040;
    L_0x003b:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x004d;
    L_0x0040:
        goto L_0x004d;
    L_0x0041:
        r1 = move-exception;
        goto L_0x0050;
    L_0x0043:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0041 }
        if (r0 == 0) goto L_0x0040;
        r0.close();	 Catch:{ Exception -> 0x003b }
        goto L_0x0040;
    L_0x004d:
        r8.currentPicturePath = r3;
        goto L_0x0096;
        if (r0 == 0) goto L_0x005c;
        r0.close();	 Catch:{ Exception -> 0x0057 }
        goto L_0x005c;
    L_0x0057:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
    L_0x005d:
        r0 = 11;
        if (r9 != r0) goto L_0x0096;
        if (r11 == 0) goto L_0x0095;
        r0 = r11.getData();
        if (r0 != 0) goto L_0x006a;
        goto L_0x0095;
        r0 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();	 Catch:{ Exception -> 0x0090 }
        r4 = r11.getData();	 Catch:{ Exception -> 0x0090 }
        r5 = r0.x;	 Catch:{ Exception -> 0x0090 }
        r5 = (float) r5;	 Catch:{ Exception -> 0x0090 }
        r6 = r0.y;	 Catch:{ Exception -> 0x0090 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x0090 }
        r2 = org.telegram.messenger.ImageLoader.loadBitmap(r3, r4, r5, r6, r2);	 Catch:{ Exception -> 0x0090 }
        r3 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0090 }
        r4 = r8.currentWallpaperPath;	 Catch:{ Exception -> 0x0090 }
        r3.<init>(r4);	 Catch:{ Exception -> 0x0090 }
        r4 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0090 }
        r2.compress(r4, r1, r3);	 Catch:{ Exception -> 0x0090 }
        r1 = r8.delegate;	 Catch:{ Exception -> 0x0090 }
        r4 = r8.currentWallpaperPath;	 Catch:{ Exception -> 0x0090 }
        r1.didSelectWallpaper(r4, r2);	 Catch:{ Exception -> 0x0090 }
        goto L_0x0096;
    L_0x0090:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        goto L_0x0096;
        return;
    L_0x0096:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.WallpaperUpdater.onActivityResult(int, int, android.content.Intent):void");
    }

    public WallpaperUpdater(Activity activity, WallpaperUpdaterDelegate wallpaperUpdaterDelegate) {
        this.parentActivity = activity;
        this.delegate = wallpaperUpdaterDelegate;
        File directory = FileLoader.getDirectory(4);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utilities.random.nextInt());
        stringBuilder.append(".jpg");
        this.currentWallpaperPath = new File(directory, stringBuilder.toString());
    }

    public void showAlert(final boolean fromTheme) {
        CharSequence[] items;
        Builder builder = new Builder(this.parentActivity);
        if (fromTheme) {
            items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley), LocaleController.getString("SelectColor", R.string.SelectColor), LocaleController.getString("Default", R.string.Default), LocaleController.getString("Cancel", R.string.Cancel)};
        } else {
            items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley), LocaleController.getString("Cancel", R.string.Cancel)};
        }
        builder.setItems(items, new OnClickListener() {
            public void onClick(android.content.DialogInterface r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.WallpaperUpdater.1.onClick(android.content.DialogInterface, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = 2;
                if (r9 != 0) goto L_0x0052;
            L_0x0004:
                r2 = new android.content.Intent;	 Catch:{ Exception -> 0x004d }
                r3 = "android.media.action.IMAGE_CAPTURE";	 Catch:{ Exception -> 0x004d }
                r2.<init>(r3);	 Catch:{ Exception -> 0x004d }
                r3 = org.telegram.messenger.AndroidUtilities.generatePicturePath();	 Catch:{ Exception -> 0x004d }
                if (r3 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x004d }
            L_0x0011:
                r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x004d }
                r5 = 24;	 Catch:{ Exception -> 0x004d }
                if (r4 < r5) goto L_0x002f;	 Catch:{ Exception -> 0x004d }
            L_0x0017:
                r4 = "output";	 Catch:{ Exception -> 0x004d }
                r5 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x004d }
                r5 = r5.parentActivity;	 Catch:{ Exception -> 0x004d }
                r6 = "org.telegram.messenger.beta.provider";	 Catch:{ Exception -> 0x004d }
                r5 = android.support.v4.content.FileProvider.getUriForFile(r5, r6, r3);	 Catch:{ Exception -> 0x004d }
                r2.putExtra(r4, r5);	 Catch:{ Exception -> 0x004d }
                r2.addFlags(r1);	 Catch:{ Exception -> 0x004d }
                r2.addFlags(r0);	 Catch:{ Exception -> 0x004d }
                goto L_0x0038;	 Catch:{ Exception -> 0x004d }
            L_0x002f:
                r0 = "output";	 Catch:{ Exception -> 0x004d }
                r1 = android.net.Uri.fromFile(r3);	 Catch:{ Exception -> 0x004d }
                r2.putExtra(r0, r1);	 Catch:{ Exception -> 0x004d }
            L_0x0038:
                r0 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x004d }
                r1 = r3.getAbsolutePath();	 Catch:{ Exception -> 0x004d }
                r0.currentPicturePath = r1;	 Catch:{ Exception -> 0x004d }
            L_0x0041:
                r0 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x004d }
                r0 = r0.parentActivity;	 Catch:{ Exception -> 0x004d }
                r1 = 10;	 Catch:{ Exception -> 0x004d }
                r0.startActivityForResult(r2, r1);	 Catch:{ Exception -> 0x004d }
                goto L_0x0051;
            L_0x004d:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);	 Catch:{ Exception -> 0x006c }
            L_0x0051:
                goto L_0x008b;	 Catch:{ Exception -> 0x006c }
            L_0x0052:
                if (r9 != r0) goto L_0x006e;	 Catch:{ Exception -> 0x006c }
            L_0x0054:
                r0 = new android.content.Intent;	 Catch:{ Exception -> 0x006c }
                r1 = "android.intent.action.PICK";	 Catch:{ Exception -> 0x006c }
                r0.<init>(r1);	 Catch:{ Exception -> 0x006c }
                r1 = "image/*";	 Catch:{ Exception -> 0x006c }
                r0.setType(r1);	 Catch:{ Exception -> 0x006c }
                r1 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x006c }
                r1 = r1.parentActivity;	 Catch:{ Exception -> 0x006c }
                r2 = 11;	 Catch:{ Exception -> 0x006c }
                r1.startActivityForResult(r0, r2);	 Catch:{ Exception -> 0x006c }
                goto L_0x008b;	 Catch:{ Exception -> 0x006c }
            L_0x006c:
                r0 = move-exception;	 Catch:{ Exception -> 0x006c }
                goto L_0x008c;	 Catch:{ Exception -> 0x006c }
            L_0x006e:
                r0 = r11;	 Catch:{ Exception -> 0x006c }
                if (r0 == 0) goto L_0x008b;	 Catch:{ Exception -> 0x006c }
                if (r9 != r1) goto L_0x007e;	 Catch:{ Exception -> 0x006c }
                r0 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x006c }
                r0 = r0.delegate;	 Catch:{ Exception -> 0x006c }
                r0.needOpenColorPicker();	 Catch:{ Exception -> 0x006c }
                goto L_0x008b;	 Catch:{ Exception -> 0x006c }
                r0 = 3;	 Catch:{ Exception -> 0x006c }
                if (r9 != r0) goto L_0x008b;	 Catch:{ Exception -> 0x006c }
                r0 = org.telegram.ui.Components.WallpaperUpdater.this;	 Catch:{ Exception -> 0x006c }
                r0 = r0.delegate;	 Catch:{ Exception -> 0x006c }
                r1 = 0;	 Catch:{ Exception -> 0x006c }
                r0.didSelectWallpaper(r1, r1);	 Catch:{ Exception -> 0x006c }
            L_0x008b:
                goto L_0x0090;
                org.telegram.messenger.FileLog.e(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.WallpaperUpdater.1.onClick(android.content.DialogInterface, int):void");
            }
        });
        builder.show();
    }

    public void cleanup() {
        this.currentWallpaperPath.delete();
    }

    public File getCurrentWallpaperPath() {
        return this.currentWallpaperPath;
    }

    public String getCurrentPicturePath() {
        return this.currentPicturePath;
    }

    public void setCurrentPicturePath(String value) {
        this.currentPicturePath = value;
    }
}
