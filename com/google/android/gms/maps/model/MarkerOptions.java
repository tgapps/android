package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;

public final class MarkerOptions extends AbstractSafeParcelable {
    public static final Creator<MarkerOptions> CREATOR = new zzh();
    private float alpha = 1.0f;
    private LatLng position;
    private float zzcr;
    private boolean zzcs = true;
    private float zzda = 0.5f;
    private float zzdb = 1.0f;
    private String zzdm;
    private String zzdn;
    private BitmapDescriptor zzdo;
    private boolean zzdp;
    private boolean zzdq = false;
    private float zzdr = 0.0f;
    private float zzds = 0.5f;
    private float zzdt = 0.0f;

    MarkerOptions(LatLng latLng, String str, String str2, IBinder iBinder, float f, float f2, boolean z, boolean z2, boolean z3, float f3, float f4, float f5, float f6, float f7) {
        this.position = latLng;
        this.zzdm = str;
        this.zzdn = str2;
        if (iBinder == null) {
            r0.zzdo = null;
        } else {
            r0.zzdo = new BitmapDescriptor(Stub.asInterface(iBinder));
        }
        r0.zzda = f;
        r0.zzdb = f2;
        r0.zzdp = z;
        r0.zzcs = z2;
        r0.zzdq = z3;
        r0.zzdr = f3;
        r0.zzds = f4;
        r0.zzdt = f5;
        r0.alpha = f6;
        r0.zzcr = f7;
    }

    public final MarkerOptions anchor(float f, float f2) {
        this.zzda = f;
        this.zzdb = f2;
        return this;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final float getAnchorU() {
        return this.zzda;
    }

    public final float getAnchorV() {
        return this.zzdb;
    }

    public final float getInfoWindowAnchorU() {
        return this.zzds;
    }

    public final float getInfoWindowAnchorV() {
        return this.zzdt;
    }

    public final LatLng getPosition() {
        return this.position;
    }

    public final float getRotation() {
        return this.zzdr;
    }

    public final String getSnippet() {
        return this.zzdn;
    }

    public final String getTitle() {
        return this.zzdm;
    }

    public final float getZIndex() {
        return this.zzcr;
    }

    public final MarkerOptions icon(BitmapDescriptor bitmapDescriptor) {
        this.zzdo = bitmapDescriptor;
        return this;
    }

    public final boolean isDraggable() {
        return this.zzdp;
    }

    public final boolean isFlat() {
        return this.zzdq;
    }

    public final boolean isVisible() {
        return this.zzcs;
    }

    public final MarkerOptions position(LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("latlng cannot be null - a position is required.");
        }
        this.position = latLng;
        return this;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getPosition(), i, false);
        SafeParcelWriter.writeString(parcel, 3, getTitle(), false);
        SafeParcelWriter.writeString(parcel, 4, getSnippet(), false);
        SafeParcelWriter.writeIBinder(parcel, 5, this.zzdo == null ? null : this.zzdo.zza().asBinder(), false);
        SafeParcelWriter.writeFloat(parcel, 6, getAnchorU());
        SafeParcelWriter.writeFloat(parcel, 7, getAnchorV());
        SafeParcelWriter.writeBoolean(parcel, 8, isDraggable());
        SafeParcelWriter.writeBoolean(parcel, 9, isVisible());
        SafeParcelWriter.writeBoolean(parcel, 10, isFlat());
        SafeParcelWriter.writeFloat(parcel, 11, getRotation());
        SafeParcelWriter.writeFloat(parcel, 12, getInfoWindowAnchorU());
        SafeParcelWriter.writeFloat(parcel, 13, getInfoWindowAnchorV());
        SafeParcelWriter.writeFloat(parcel, 14, getAlpha());
        SafeParcelWriter.writeFloat(parcel, 15, getZIndex());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
