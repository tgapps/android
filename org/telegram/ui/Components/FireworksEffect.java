package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class FireworksEffect {
    final float angleDiff = 1.0471976f;
    private ArrayList<Particle> freeParticles = new ArrayList();
    private long lastAnimationTime;
    private Paint particlePaint = new Paint(1);
    private ArrayList<Particle> particles = new ArrayList();

    private class Particle {
        float alpha;
        int color;
        float currentTime;
        float lifeTime;
        float scale;
        int type;
        float velocity;
        float vx;
        float vy;
        float x;
        float y;

        private Particle() {
        }

        public void draw(Canvas canvas) {
            if (this.type == 0) {
                FireworksEffect.this.particlePaint.setColor(this.color);
                FireworksEffect.this.particlePaint.setStrokeWidth(((float) AndroidUtilities.dp(1.5f)) * this.scale);
                FireworksEffect.this.particlePaint.setAlpha((int) (255.0f * this.alpha));
                canvas.drawPoint(this.x, this.y, FireworksEffect.this.particlePaint);
            }
        }
    }

    public void onDraw(android.view.View r1, android.graphics.Canvas r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.FireworksEffect.onDraw(android.view.View, android.graphics.Canvas):void
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
        r0 = r18;
        r1 = r20;
        if (r19 == 0) goto L_0x0124;
    L_0x0006:
        if (r1 != 0) goto L_0x000a;
    L_0x0008:
        goto L_0x0124;
    L_0x000a:
        r2 = r0.particles;
        r2 = r2.size();
        r3 = 0;
        r4 = r3;
    L_0x0012:
        if (r4 >= r2) goto L_0x0022;
    L_0x0014:
        r5 = r0.particles;
        r5 = r5.get(r4);
        r5 = (org.telegram.ui.Components.FireworksEffect.Particle) r5;
        r5.draw(r1);
        r4 = r4 + 1;
        goto L_0x0012;
    L_0x0022:
        r4 = org.telegram.messenger.Utilities.random;
        r4 = r4.nextBoolean();
        if (r4 == 0) goto L_0x010d;
    L_0x002a:
        r4 = r0.particles;
        r4 = r4.size();
        r5 = 8;
        r4 = r4 + r5;
        r6 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r4 >= r6) goto L_0x010d;
    L_0x0037:
        r4 = android.os.Build.VERSION.SDK_INT;
        r6 = 21;
        if (r4 < r6) goto L_0x0040;
    L_0x003d:
        r4 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        goto L_0x0041;
    L_0x0040:
        r4 = r3;
    L_0x0041:
        r6 = org.telegram.messenger.Utilities.random;
        r6 = r6.nextFloat();
        r7 = r19.getMeasuredWidth();
        r7 = (float) r7;
        r6 = r6 * r7;
        r7 = (float) r4;
        r8 = org.telegram.messenger.Utilities.random;
        r8 = r8.nextFloat();
        r9 = r19.getMeasuredHeight();
        r10 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r11;
        r9 = r9 - r4;
        r9 = (float) r9;
        r8 = r8 * r9;
        r7 = r7 + r8;
        r8 = org.telegram.messenger.Utilities.random;
        r9 = 4;
        r8 = r8.nextInt(r9);
        switch(r8) {
            case 0: goto L_0x007c;
            case 1: goto L_0x0078;
            case 2: goto L_0x0074;
            case 3: goto L_0x0070;
            default: goto L_0x006d;
        };
    L_0x006d:
        r8 = -5752; // 0xffffffffffffe988 float:NaN double:NaN;
        goto L_0x0080;
    L_0x0070:
        r8 = -15088582; // 0xffffffffff19c43a float:-2.0439075E38 double:NaN;
        goto L_0x0080;
    L_0x0074:
        r8 = -207021; // 0xfffffffffffcd753 float:NaN double:NaN;
        goto L_0x0080;
    L_0x0078:
        r8 = -843755; // 0xfffffffffff32015 float:NaN double:NaN;
        goto L_0x0080;
    L_0x007c:
        r8 = -13357350; // 0xffffffffff342eda float:-2.395043E38 double:NaN;
        r9 = r3;
        if (r9 >= r5) goto L_0x010d;
        r11 = org.telegram.messenger.Utilities.random;
        r12 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r11 = r11.nextInt(r12);
        r11 = r11 + -225;
        r12 = (double) r11;
        r14 = 4580687790476533049; // 0x3f91df46a2529d39 float:-2.854354E-18 double:0.017453292519943295;
        r12 = r12 * r14;
        r12 = java.lang.Math.cos(r12);
        r12 = (float) r12;
        r16 = r6;
        r5 = (double) r11;
        r5 = r5 * r14;
        r5 = java.lang.Math.sin(r5);
        r5 = (float) r5;
        r6 = r0.freeParticles;
        r6 = r6.isEmpty();
        if (r6 != 0) goto L_0x00b9;
        r6 = r0.freeParticles;
        r6 = r6.get(r3);
        r6 = (org.telegram.ui.Components.FireworksEffect.Particle) r6;
        r13 = r0.freeParticles;
        r13.remove(r3);
        goto L_0x00bf;
        r6 = new org.telegram.ui.Components.FireworksEffect$Particle;
        r13 = 0;
        r6.<init>();
        r13 = r16;
        r6.x = r13;
        r6.y = r7;
        r14 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r10 = r12 * r14;
        r6.vx = r10;
        r6.vy = r5;
        r6.color = r8;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6.alpha = r10;
        r3 = 0;
        r6.currentTime = r3;
        r3 = org.telegram.messenger.Utilities.random;
        r3 = r3.nextFloat();
        r3 = r3 * r14;
        r3 = java.lang.Math.max(r10, r3);
        r6.scale = r3;
        r3 = 0;
        r6.type = r3;
        r10 = org.telegram.messenger.Utilities.random;
        r14 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r10 = r10.nextInt(r14);
        r14 = r14 + r10;
        r10 = (float) r14;
        r6.lifeTime = r10;
        r10 = org.telegram.messenger.Utilities.random;
        r10 = r10.nextFloat();
        r14 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r10 = r10 * r14;
        r14 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r10 = r10 + r14;
        r6.velocity = r10;
        r10 = r0.particles;
        r10.add(r6);
        r9 = r9 + 1;
        r6 = r13;
        r10 = r14;
        r5 = 8;
        goto L_0x0082;
    L_0x010d:
        r3 = java.lang.System.currentTimeMillis();
        r5 = 17;
        r7 = r0.lastAnimationTime;
        r9 = r3 - r7;
        r5 = java.lang.Math.min(r5, r9);
        r0.updateParticles(r5);
        r0.lastAnimationTime = r3;
        r19.invalidate();
        return;
    L_0x0124:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.FireworksEffect.onDraw(android.view.View, android.graphics.Canvas):void");
    }

    public FireworksEffect() {
        this.particlePaint.setStrokeWidth((float) AndroidUtilities.dp(1.5f));
        this.particlePaint.setColor(Theme.getColor(Theme.key_actionBarDefaultTitle) & -1644826);
        this.particlePaint.setStrokeCap(Cap.ROUND);
        this.particlePaint.setStyle(Style.STROKE);
        for (int a = 0; a < 20; a++) {
            this.freeParticles.add(new Particle());
        }
    }

    private void updateParticles(long dt) {
        int count = this.particles.size();
        int a = 0;
        while (a < count) {
            Particle particle = (Particle) this.particles.get(a);
            if (particle.currentTime >= particle.lifeTime) {
                if (this.freeParticles.size() < 40) {
                    this.freeParticles.add(particle);
                }
                this.particles.remove(a);
                a--;
                count--;
            } else {
                particle.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(particle.currentTime / particle.lifeTime);
                particle.x += ((particle.vx * particle.velocity) * ((float) dt)) / 500.0f;
                particle.y += ((particle.vy * particle.velocity) * ((float) dt)) / 500.0f;
                particle.vy += ((float) dt) / 100.0f;
                particle.currentTime += (float) dt;
            }
            a++;
        }
    }
}
