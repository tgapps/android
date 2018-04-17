package com.googlecode.mp4parser.util;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class Matrix {
    public static final Matrix ROTATE_0 = new Matrix(1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_180 = new Matrix(-1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_270 = new Matrix(0.0d, -1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_90 = new Matrix(0.0d, 1.0d, -1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    double a;
    double b;
    double c;
    double d;
    double tx;
    double ty;
    double u;
    double v;
    double w;

    public Matrix(double a, double b, double c, double d, double u, double v, double w, double tx, double ty) {
        this.u = u;
        this.v = v;
        this.w = w;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null) {
            if (getClass() == o.getClass()) {
                Matrix matrix = (Matrix) o;
                if (Double.compare(matrix.a, this.a) == 0 && Double.compare(matrix.b, this.b) == 0 && Double.compare(matrix.c, this.c) == 0 && Double.compare(matrix.d, this.d) == 0 && Double.compare(matrix.tx, this.tx) == 0 && Double.compare(matrix.ty, this.ty) == 0 && Double.compare(matrix.u, this.u) == 0 && Double.compare(matrix.v, this.v) == 0 && Double.compare(matrix.w, this.w) == 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.u);
        int result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.v);
        int result2 = (31 * result) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.w);
        result = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.a);
        result2 = (31 * result) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.b);
        result = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.c);
        result2 = (31 * result) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.d);
        result = (31 * result2) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.tx);
        result2 = (31 * result) + ((int) (temp ^ (temp >>> 32)));
        temp = Double.doubleToLongBits(this.ty);
        return (31 * result2) + ((int) (temp ^ (temp >>> 32)));
    }

    public String toString() {
        if (equals(ROTATE_0)) {
            return "Rotate 0°";
        }
        if (equals(ROTATE_90)) {
            return "Rotate 90°";
        }
        if (equals(ROTATE_180)) {
            return "Rotate 180°";
        }
        if (equals(ROTATE_270)) {
            return "Rotate 270°";
        }
        StringBuilder stringBuilder = new StringBuilder("Matrix{u=");
        stringBuilder.append(this.u);
        stringBuilder.append(", v=");
        stringBuilder.append(this.v);
        stringBuilder.append(", w=");
        stringBuilder.append(this.w);
        stringBuilder.append(", a=");
        stringBuilder.append(this.a);
        stringBuilder.append(", b=");
        stringBuilder.append(this.b);
        stringBuilder.append(", c=");
        stringBuilder.append(this.c);
        stringBuilder.append(", d=");
        stringBuilder.append(this.d);
        stringBuilder.append(", tx=");
        stringBuilder.append(this.tx);
        stringBuilder.append(", ty=");
        stringBuilder.append(this.ty);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static Matrix fromFileOrder(double a, double b, double u, double c, double d, double v, double tx, double ty, double w) {
        return new Matrix(a, b, c, d, u, v, w, tx, ty);
    }

    public static Matrix fromByteBuffer(ByteBuffer byteBuffer) {
        return fromFileOrder(IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer));
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.a);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.b);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.u);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.c);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.d);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.v);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.tx);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.ty);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.w);
    }
}
