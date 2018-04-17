package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Render {
    public static RectF RenderPath(Path path, RenderState state) {
        state.baseWeight = path.getBaseWeight();
        state.spacing = path.getBrush().getSpacing();
        state.alpha = path.getBrush().getAlpha();
        state.angle = path.getBrush().getAngle();
        state.scale = path.getBrush().getScale();
        int length = path.getLength();
        if (length == 0) {
            return null;
        }
        int i = 0;
        if (length == 1) {
            PaintStamp(path.getPoints()[0], state);
        } else {
            Point[] points = path.getPoints();
            state.prepare();
            while (i < points.length - 1) {
                PaintSegment(points[i], points[i + 1], state);
                i++;
            }
        }
        path.remainder = state.remainder;
        return Draw(state);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void PaintSegment(org.telegram.ui.Components.Paint.Point r28, org.telegram.ui.Components.Paint.Point r29, org.telegram.ui.Components.Paint.RenderState r30) {
        /*
        r0 = r28;
        r1 = r29;
        r12 = r30;
        r2 = r28.getDistanceTo(r29);
        r13 = (double) r2;
        r15 = r1.substract(r0);
        r9 = new org.telegram.ui.Components.Paint.Point;
        r3 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r5 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r7 = 0;
        r2 = r9;
        r2.<init>(r3, r5, r7);
        r3 = r12.angle;
        r3 = java.lang.Math.abs(r3);
        r4 = 0;
        r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r3 <= 0) goto L_0x002a;
    L_0x0026:
        r3 = r12.angle;
    L_0x0028:
        r5 = r3;
        goto L_0x0034;
    L_0x002a:
        r3 = r15.y;
        r5 = r15.x;
        r3 = java.lang.Math.atan2(r3, r5);
        r3 = (float) r3;
        goto L_0x0028;
    L_0x0034:
        r3 = r12.baseWeight;
        r4 = r12.scale;
        r16 = r3 * r4;
        r3 = r12.spacing;
        r3 = r3 * r16;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = java.lang.Math.max(r4, r3);
        r10 = (double) r3;
        r6 = 0;
        r3 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x0052;
    L_0x004b:
        r6 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r6 = r6 / r13;
        r2 = r15.multiplyByScalar(r6);
    L_0x0052:
        r9 = r2;
        r2 = r12.alpha;
        r3 = 1066611507; // 0x3f933333 float:1.15 double:5.26976103E-315;
        r2 = r2 * r3;
        r17 = java.lang.Math.min(r4, r2);
        r2 = r0.edge;
        r8 = r1.edge;
        r3 = r12.remainder;
        r3 = r13 - r3;
        r3 = r3 / r10;
        r3 = java.lang.Math.ceil(r3);
        r7 = (int) r3;
        r4 = r30.getCount();
        r12.appendValuesCount(r7);
        r12.setPosition(r4);
        r18 = r2;
        r2 = r12.remainder;
        r2 = r9.multiplyByScalar(r2);
        r2 = r0.add(r2);
        r3 = 1;
        r19 = r2;
        r20 = r3;
        r2 = r12.remainder;
        r6 = r19;
    L_0x008a:
        r21 = r2;
        r2 = (r21 > r13 ? 1 : (r21 == r13 ? 0 : -1));
        if (r2 > 0) goto L_0x00cb;
    L_0x0090:
        if (r18 == 0) goto L_0x0095;
    L_0x0092:
        r2 = r17;
        goto L_0x0097;
    L_0x0095:
        r2 = r12.alpha;
    L_0x0097:
        r3 = r6;
        r6 = r2;
        r19 = r3.toPointF();
        r23 = -1;
        r2 = r12;
        r24 = r3;
        r3 = r19;
        r19 = r4;
        r4 = r16;
        r25 = r7;
        r7 = r23;
        r20 = r2.addPoint(r3, r4, r5, r6, r7);
        if (r20 != 0) goto L_0x00b6;
    L_0x00b3:
        r3 = r24;
        goto L_0x00d0;
    L_0x00b6:
        r2 = r9.multiplyByScalar(r10);
        r3 = r24;
        r2 = r3.add(r2);
        r18 = 0;
        r3 = r21 + r10;
        r6 = r2;
        r2 = r3;
        r4 = r19;
        r7 = r25;
        goto L_0x008a;
    L_0x00cb:
        r19 = r4;
        r3 = r6;
        r25 = r7;
    L_0x00d0:
        if (r20 == 0) goto L_0x00ed;
    L_0x00d2:
        if (r8 == 0) goto L_0x00ed;
    L_0x00d4:
        r2 = 1;
        r12.appendValuesCount(r2);
        r7 = r29.toPointF();
        r2 = -1;
        r6 = r12;
        r4 = r8;
        r8 = r16;
        r23 = r9;
        r9 = r5;
        r26 = r10;
        r10 = r17;
        r11 = r2;
        r6.addPoint(r7, r8, r9, r10, r11);
        goto L_0x00f2;
    L_0x00ed:
        r4 = r8;
        r23 = r9;
        r26 = r10;
    L_0x00f2:
        r6 = r21 - r13;
        r12.remainder = r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Paint.Render.PaintSegment(org.telegram.ui.Components.Paint.Point, org.telegram.ui.Components.Paint.Point, org.telegram.ui.Components.Paint.RenderState):void");
    }

    private static void PaintStamp(Point point, RenderState state) {
        float brushWeight = state.baseWeight * state.scale;
        PointF start = point.toPointF();
        float angle = Math.abs(state.angle) > 0.0f ? state.angle : 0.0f;
        float alpha = state.alpha;
        state.prepare();
        state.appendValuesCount(1);
        state.addPoint(start, brushWeight, angle, alpha, 0);
    }

    private static RectF Draw(RenderState state) {
        RectF dataBounds = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        int count = state.getCount();
        if (count == 0) {
            return dataBounds;
        }
        ByteBuffer bb;
        int capacity = 20 * ((count * 4) + ((count - 1) * 2));
        ByteBuffer bb2 = ByteBuffer.allocateDirect(capacity);
        bb2.order(ByteOrder.nativeOrder());
        FloatBuffer vertexData = bb2.asFloatBuffer();
        vertexData.position(0);
        state.setPosition(0);
        int n = 0;
        int i = 0;
        while (i < count) {
            float x = state.read();
            float y = state.read();
            float size = state.read();
            float angle = state.read();
            float alpha = state.read();
            RectF rect = new RectF(x - size, y - size, x + size, y + size);
            float[] points = new float[]{rect.left, rect.top, rect.right, rect.top, rect.left, rect.bottom, rect.right, rect.bottom};
            float centerX = rect.centerX();
            float centerY = rect.centerY();
            Matrix t = new Matrix();
            int capacity2 = capacity;
            bb = bb2;
            t.setRotate((float) Math.toDegrees((double) angle), centerX, centerY);
            t.mapPoints(points);
            t.mapRect(rect);
            Utils.RectFIntegral(rect);
            dataBounds.union(rect);
            if (n != 0) {
                vertexData.put(points[0]);
                vertexData.put(points[1]);
                vertexData.put(0.0f);
                vertexData.put(0.0f);
                vertexData.put(alpha);
                n++;
            }
            vertexData.put(points[0]);
            vertexData.put(points[1]);
            vertexData.put(0.0f);
            vertexData.put(0.0f);
            vertexData.put(alpha);
            n++;
            vertexData.put(points[2]);
            vertexData.put(points[3]);
            vertexData.put(1.0f);
            vertexData.put(0.0f);
            vertexData.put(alpha);
            n++;
            vertexData.put(points[4]);
            vertexData.put(points[5]);
            vertexData.put(0.0f);
            vertexData.put(1.0f);
            vertexData.put(alpha);
            n++;
            vertexData.put(points[6]);
            vertexData.put(points[7]);
            vertexData.put(1.0f);
            vertexData.put(1.0f);
            vertexData.put(alpha);
            n++;
            if (i != count - 1) {
                vertexData.put(points[6]);
                vertexData.put(points[7]);
                vertexData.put(1.0f);
                vertexData.put(1.0f);
                vertexData.put(alpha);
                n++;
            }
            i++;
            capacity = capacity2;
            bb2 = bb;
        }
        bb = bb2;
        vertexData.position(0);
        int i2 = 1;
        capacity = 4;
        int i3 = 5;
        int i4 = 20;
        int n2 = n;
        GLES20.glVertexAttribPointer(0, 2, 5126, false, i4, vertexData.slice());
        GLES20.glEnableVertexAttribArray(0);
        vertexData.position(2);
        GLES20.glVertexAttribPointer(1, 2, 5126, true, i4, vertexData.slice());
        GLES20.glEnableVertexAttribArray(i2);
        vertexData.position(capacity);
        GLES20.glVertexAttribPointer(2, 1, 5126, true, i4, vertexData.slice());
        GLES20.glEnableVertexAttribArray(2);
        GLES20.glDrawArrays(i3, 0, n2);
        return dataBounds;
    }
}
