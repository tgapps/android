package android.support.v4.widget;

import android.os.Build.VERSION;
import android.widget.EdgeEffect;

public final class EdgeEffectCompat {
    private EdgeEffect mEdgeEffect;

    public static void onPull(EdgeEffect edgeEffect, float deltaDistance, float displacement) {
        if (VERSION.SDK_INT >= 21) {
            edgeEffect.onPull(deltaDistance, displacement);
        } else {
            edgeEffect.onPull(deltaDistance);
        }
    }
}
