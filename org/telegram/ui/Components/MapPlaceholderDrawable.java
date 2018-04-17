package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;

public class MapPlaceholderDrawable extends Drawable {
    private Paint linePaint;
    private Paint paint = new Paint();

    public void draw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.MapPlaceholderDrawable.draw(android.graphics.Canvas):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r14.getBounds();
        r1 = r14.paint;
        r15.drawRect(r0, r1);
        r0 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r1 = r14.getBounds();
        r1 = r1.width();
        r1 = r1 / r0;
        r2 = r14.getBounds();
        r2 = r2.height();
        r2 = r2 / r0;
        r3 = r14.getBounds();
        r3 = r3.left;
        r4 = r14.getBounds();
        r4 = r4.top;
        r5 = 0;
        r6 = r5;
        if (r6 >= r1) goto L_0x004f;
    L_0x0031:
        r7 = r6 + 1;
        r7 = r7 * r0;
        r7 = r7 + r3;
        r9 = (float) r7;
        r10 = (float) r4;
        r7 = r6 + 1;
        r7 = r7 * r0;
        r7 = r7 + r3;
        r11 = (float) r7;
        r7 = r14.getBounds();
        r7 = r7.height();
        r7 = r7 + r4;
        r12 = (float) r7;
        r13 = r14.linePaint;
        r8 = r15;
        r8.drawLine(r9, r10, r11, r12, r13);
        r6 = r6 + 1;
        goto L_0x002f;
        if (r5 >= r2) goto L_0x0070;
        r7 = (float) r3;
        r6 = r5 + 1;
        r6 = r6 * r0;
        r6 = r6 + r4;
        r8 = (float) r6;
        r6 = r14.getBounds();
        r6 = r6.width();
        r6 = r6 + r3;
        r9 = (float) r6;
        r6 = r5 + 1;
        r6 = r6 * r0;
        r6 = r6 + r4;
        r10 = (float) r6;
        r11 = r14.linePaint;
        r6 = r15;
        r6.drawLine(r7, r8, r9, r10, r11);
        r5 = r5 + 1;
        goto L_0x0050;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.MapPlaceholderDrawable.draw(android.graphics.Canvas):void");
    }

    public MapPlaceholderDrawable() {
        this.paint.setColor(-2172970);
        this.linePaint = new Paint();
        this.linePaint.setColor(-3752002);
        this.linePaint.setStrokeWidth((float) AndroidUtilities.dp(1.0f));
    }

    public void setAlpha(int alpha) {
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public int getOpacity() {
        return 0;
    }

    public int getIntrinsicWidth() {
        return 0;
    }

    public int getIntrinsicHeight() {
        return 0;
    }
}
