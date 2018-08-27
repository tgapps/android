package com.google.android.exoplayer2.video;

import android.graphics.SurfaceTexture;

public interface VideoListener {
    void onRenderedFirstFrame();

    boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture);

    void onSurfaceSizeChanged(int i, int i2);

    void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);

    void onVideoSizeChanged(int i, int i2, int i3, float f);
}
