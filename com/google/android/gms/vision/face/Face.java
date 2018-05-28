package com.google.android.gms.vision.face;

import android.graphics.PointF;
import java.util.Arrays;
import java.util.List;

public class Face {
    private float height;
    private int id;
    private float width;
    private PointF zzbo;
    private float zzbp;
    private float zzbq;
    private List<Landmark> zzbr;
    private float zzbs;
    private float zzbt;
    private float zzbu;

    public Face(int i, PointF pointF, float f, float f2, float f3, float f4, Landmark[] landmarkArr, float f5, float f6, float f7) {
        this.id = i;
        this.zzbo = pointF;
        this.width = f;
        this.height = f2;
        this.zzbp = f3;
        this.zzbq = f4;
        this.zzbr = Arrays.asList(landmarkArr);
        if (f5 < 0.0f || f5 > 1.0f) {
            this.zzbs = -1.0f;
        } else {
            this.zzbs = f5;
        }
        if (f6 < 0.0f || f6 > 1.0f) {
            this.zzbt = -1.0f;
        } else {
            this.zzbt = f6;
        }
        if (f7 < 0.0f || f7 > 1.0f) {
            this.zzbu = -1.0f;
        } else {
            this.zzbu = f7;
        }
    }

    public int getId() {
        return this.id;
    }

    public List<Landmark> getLandmarks() {
        return this.zzbr;
    }
}
