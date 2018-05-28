package com.google.android.gms.vision.face;

import android.graphics.PointF;

public final class Landmark {
    private final int type;
    private final PointF zzbo;

    public Landmark(PointF pointF, int i) {
        this.zzbo = pointF;
        this.type = i;
    }

    public final PointF getPosition() {
        return this.zzbo;
    }

    public final int getType() {
        return this.type;
    }
}
