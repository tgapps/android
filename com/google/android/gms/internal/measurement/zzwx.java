package com.google.android.gms.internal.measurement;

import com.google.android.exoplayer2.C;
import com.google.android.gms.internal.measurement.zzvm.zze;
import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;

final class zzwx<T> implements zzxj<T> {
    private static final int[] zzcax = new int[0];
    private static final Unsafe zzcay = zzyh.zzyk();
    private final int[] zzcaz;
    private final Object[] zzcba;
    private final int zzcbb;
    private final int zzcbc;
    private final zzwt zzcbd;
    private final boolean zzcbe;
    private final boolean zzcbf;
    private final boolean zzcbg;
    private final boolean zzcbh;
    private final int[] zzcbi;
    private final int zzcbj;
    private final int zzcbk;
    private final zzxa zzcbl;
    private final zzwd zzcbm;
    private final zzyb<?, ?> zzcbn;
    private final zzva<?> zzcbo;
    private final zzwo zzcbp;

    private zzwx(int[] iArr, Object[] objArr, int i, int i2, zzwt com_google_android_gms_internal_measurement_zzwt, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzxa com_google_android_gms_internal_measurement_zzxa, zzwd com_google_android_gms_internal_measurement_zzwd, zzyb<?, ?> com_google_android_gms_internal_measurement_zzyb___, zzva<?> com_google_android_gms_internal_measurement_zzva_, zzwo com_google_android_gms_internal_measurement_zzwo) {
        this.zzcaz = iArr;
        this.zzcba = objArr;
        this.zzcbb = i;
        this.zzcbc = i2;
        this.zzcbf = com_google_android_gms_internal_measurement_zzwt instanceof zzvm;
        this.zzcbg = z;
        boolean z3 = com_google_android_gms_internal_measurement_zzva_ != null && com_google_android_gms_internal_measurement_zzva_.zze(com_google_android_gms_internal_measurement_zzwt);
        this.zzcbe = z3;
        this.zzcbh = false;
        this.zzcbi = iArr2;
        this.zzcbj = i3;
        this.zzcbk = i4;
        this.zzcbl = com_google_android_gms_internal_measurement_zzxa;
        this.zzcbm = com_google_android_gms_internal_measurement_zzwd;
        this.zzcbn = com_google_android_gms_internal_measurement_zzyb___;
        this.zzcbo = com_google_android_gms_internal_measurement_zzva_;
        this.zzcbd = com_google_android_gms_internal_measurement_zzwt;
        this.zzcbp = com_google_android_gms_internal_measurement_zzwo;
    }

    static <T> zzwx<T> zza(Class<T> cls, zzwr com_google_android_gms_internal_measurement_zzwr, zzxa com_google_android_gms_internal_measurement_zzxa, zzwd com_google_android_gms_internal_measurement_zzwd, zzyb<?, ?> com_google_android_gms_internal_measurement_zzyb___, zzva<?> com_google_android_gms_internal_measurement_zzva_, zzwo com_google_android_gms_internal_measurement_zzwo) {
        if (com_google_android_gms_internal_measurement_zzwr instanceof zzxh) {
            int i;
            int i2;
            int i3;
            char charAt;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int[] iArr;
            int i10;
            int i11;
            char charAt2;
            char charAt3;
            zzxh com_google_android_gms_internal_measurement_zzxh = (zzxh) com_google_android_gms_internal_measurement_zzwr;
            boolean z = com_google_android_gms_internal_measurement_zzxh.zzxg() == zze.zzbzc;
            String zzxp = com_google_android_gms_internal_measurement_zzxh.zzxp();
            int length = zzxp.length();
            int i12 = 1;
            char charAt4 = zzxp.charAt(0);
            if (charAt4 >= '?') {
                i = charAt4 & 8191;
                i2 = 13;
                while (true) {
                    i3 = i12 + 1;
                    charAt = zzxp.charAt(i12);
                    if (charAt < '?') {
                        break;
                    }
                    i |= (charAt & 8191) << i2;
                    i2 += 13;
                    i12 = i3;
                }
                i4 = (charAt << i2) | i;
            } else {
                char c = charAt4;
                i3 = 1;
            }
            i12 = i3 + 1;
            i2 = zzxp.charAt(i3);
            if (i2 >= 55296) {
                i = i2 & 8191;
                i2 = 13;
                while (true) {
                    i3 = i12 + 1;
                    charAt = zzxp.charAt(i12);
                    if (charAt < '?') {
                        break;
                    }
                    i |= (charAt & 8191) << i2;
                    i2 += 13;
                    i12 = i3;
                }
                i2 = (charAt << i2) | i;
                i5 = i3;
            } else {
                i5 = i12;
            }
            if (i2 == 0) {
                i3 = 0;
                i6 = 0;
                i7 = 0;
                i8 = 0;
                i9 = 0;
                iArr = zzcax;
                i10 = 0;
                i11 = 0;
            } else {
                char charAt5;
                char charAt6;
                char charAt7;
                char charAt8;
                i12 = i5 + 1;
                i2 = zzxp.charAt(i5);
                if (i2 >= 55296) {
                    i = i2 & 8191;
                    i2 = 13;
                    while (true) {
                        i3 = i12 + 1;
                        charAt = zzxp.charAt(i12);
                        if (charAt < '?') {
                            break;
                        }
                        i |= (charAt & 8191) << i2;
                        i2 += 13;
                        i12 = i3;
                    }
                    i2 = (charAt << i2) | i;
                } else {
                    i3 = i12;
                }
                i6 = i3 + 1;
                i = zzxp.charAt(i3);
                if (i >= 55296) {
                    i12 = i & 8191;
                    i = 13;
                    i3 = i6;
                    while (true) {
                        i6 = i3 + 1;
                        charAt5 = zzxp.charAt(i3);
                        if (charAt5 < '?') {
                            break;
                        }
                        i12 |= (charAt5 & 8191) << i;
                        i += 13;
                        i3 = i6;
                    }
                    i = (charAt5 << i) | i12;
                }
                i7 = i6 + 1;
                charAt = zzxp.charAt(i6);
                if (charAt >= '?') {
                    i3 = charAt & 8191;
                    i12 = 13;
                    i6 = i7;
                    while (true) {
                        i7 = i6 + 1;
                        charAt6 = zzxp.charAt(i6);
                        if (charAt6 < '?') {
                            break;
                        }
                        i3 |= (charAt6 & 8191) << i12;
                        i12 += 13;
                        i6 = i7;
                    }
                    charAt = (charAt6 << i12) | i3;
                }
                int i13 = i7 + 1;
                charAt5 = zzxp.charAt(i7);
                if (charAt5 >= '?') {
                    i6 = charAt5 & 8191;
                    i3 = 13;
                    i7 = i13;
                    while (true) {
                        i13 = i7 + 1;
                        charAt2 = zzxp.charAt(i7);
                        if (charAt2 < '?') {
                            break;
                        }
                        i6 |= (charAt2 & 8191) << i3;
                        i3 += 13;
                        i7 = i13;
                    }
                    i6 = (charAt2 << i3) | i6;
                } else {
                    charAt6 = charAt5;
                }
                i9 = i13 + 1;
                charAt5 = zzxp.charAt(i13);
                if (charAt5 >= '?') {
                    i7 = charAt5 & 8191;
                    i3 = 13;
                    i13 = i9;
                    while (true) {
                        i9 = i13 + 1;
                        charAt7 = zzxp.charAt(i13);
                        if (charAt7 < '?') {
                            break;
                        }
                        i7 |= (charAt7 & 8191) << i3;
                        i3 += 13;
                        i13 = i9;
                    }
                    i7 = (charAt7 << i3) | i7;
                } else {
                    charAt2 = charAt5;
                }
                i10 = i9 + 1;
                charAt5 = zzxp.charAt(i9);
                if (charAt5 >= '?') {
                    i13 = charAt5 & 8191;
                    i3 = 13;
                    i9 = i10;
                    while (true) {
                        i10 = i9 + 1;
                        charAt8 = zzxp.charAt(i9);
                        if (charAt8 < '?') {
                            break;
                        }
                        i13 |= (charAt8 & 8191) << i3;
                        i3 += 13;
                        i9 = i10;
                    }
                    i8 = (charAt8 << i3) | i13;
                } else {
                    char c2 = charAt5;
                }
                i9 = i10 + 1;
                i3 = zzxp.charAt(i10);
                if (i3 >= 55296) {
                    i13 = i3 & 8191;
                    i3 = 13;
                    while (true) {
                        i10 = i9 + 1;
                        charAt8 = zzxp.charAt(i9);
                        if (charAt8 < '?') {
                            break;
                        }
                        i13 |= (charAt8 & 8191) << i3;
                        i3 += 13;
                        i9 = i10;
                    }
                    i3 = (charAt8 << i3) | i13;
                } else {
                    i10 = i9;
                }
                i5 = i10 + 1;
                charAt7 = zzxp.charAt(i10);
                if (charAt7 >= '?') {
                    i9 = charAt7 & 8191;
                    i13 = 13;
                    i10 = i5;
                    while (true) {
                        i5 = i10 + 1;
                        charAt3 = zzxp.charAt(i10);
                        if (charAt3 < '?') {
                            break;
                        }
                        i9 |= (charAt3 & 8191) << i13;
                        i13 += 13;
                        i10 = i5;
                    }
                    i9 = (charAt3 << i13) | i9;
                } else {
                    charAt8 = charAt7;
                }
                iArr = new int[(i3 + (i9 + i8))];
                i10 = i + (i2 << 1);
                charAt5 = charAt;
                i11 = i2;
            }
            Unsafe unsafe = zzcay;
            Object[] zzxq = com_google_android_gms_internal_measurement_zzxh.zzxq();
            int i14 = 0;
            Class cls2 = com_google_android_gms_internal_measurement_zzxh.zzxi().getClass();
            int[] iArr2 = new int[(i7 * 3)];
            Object[] objArr = new Object[(i7 << 1)];
            int i15 = i9 + i8;
            int i16 = 0;
            int i17 = i9;
            int i18 = i10;
            int i19;
            for (i5 = 
/*
Method generation error in method: com.google.android.gms.internal.measurement.zzwx.zza(java.lang.Class, com.google.android.gms.internal.measurement.zzwr, com.google.android.gms.internal.measurement.zzxa, com.google.android.gms.internal.measurement.zzwd, com.google.android.gms.internal.measurement.zzyb, com.google.android.gms.internal.measurement.zzva, com.google.android.gms.internal.measurement.zzwo):com.google.android.gms.internal.measurement.zzwx<T>
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r14_2 'i5' int) = (r14_1 'i5' int), (r14_63 'i5' int) binds: {(r14_63 'i5' int)=B:87:0x021b, (r14_1 'i5' int)=B:21:0x0071} in method: com.google.android.gms.internal.measurement.zzwx.zza(java.lang.Class, com.google.android.gms.internal.measurement.zzwr, com.google.android.gms.internal.measurement.zzxa, com.google.android.gms.internal.measurement.zzwd, com.google.android.gms.internal.measurement.zzyb, com.google.android.gms.internal.measurement.zzva, com.google.android.gms.internal.measurement.zzwo):com.google.android.gms.internal.measurement.zzwx<T>
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:328)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:265)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:228)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:83)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:530)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:514)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 22 more

*/

            private static Field zza(Class<?> cls, String str) {
                Field declaredField;
                try {
                    declaredField = cls.getDeclaredField(str);
                } catch (NoSuchFieldException e) {
                    Field[] declaredFields = cls.getDeclaredFields();
                    int length = declaredFields.length;
                    int i = 0;
                    while (i < length) {
                        declaredField = declaredFields[i];
                        if (!str.equals(declaredField.getName())) {
                            i++;
                        }
                    }
                    String name = cls.getName();
                    String arrays = Arrays.toString(declaredFields);
                    throw new RuntimeException(new StringBuilder(((String.valueOf(str).length() + 40) + String.valueOf(name).length()) + String.valueOf(arrays).length()).append("Field ").append(str).append(" for ").append(name).append(" not found. Known fields are ").append(arrays).toString());
                }
                return declaredField;
            }

            public final T newInstance() {
                return this.zzcbl.newInstance(this.zzcbd);
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final boolean equals(T r12, T r13) {
                /*
                r11 = this;
                r1 = 1;
                r10 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
                r0 = 0;
                r2 = r11.zzcaz;
                r4 = r2.length;
                r3 = r0;
            L_0x0009:
                if (r3 >= r4) goto L_0x01cf;
            L_0x000b:
                r2 = r11.zzbq(r3);
                r5 = r2 & r10;
                r6 = (long) r5;
                r5 = 267386880; // 0xff00000 float:2.3665827E-29 double:1.321066716E-315;
                r2 = r2 & r5;
                r2 = r2 >>> 20;
                switch(r2) {
                    case 0: goto L_0x001e;
                    case 1: goto L_0x0032;
                    case 2: goto L_0x0044;
                    case 3: goto L_0x0058;
                    case 4: goto L_0x006c;
                    case 5: goto L_0x007e;
                    case 6: goto L_0x0092;
                    case 7: goto L_0x00a5;
                    case 8: goto L_0x00b8;
                    case 9: goto L_0x00cf;
                    case 10: goto L_0x00e6;
                    case 11: goto L_0x00fd;
                    case 12: goto L_0x0110;
                    case 13: goto L_0x0123;
                    case 14: goto L_0x0136;
                    case 15: goto L_0x014b;
                    case 16: goto L_0x015e;
                    case 17: goto L_0x0173;
                    case 18: goto L_0x018a;
                    case 19: goto L_0x018a;
                    case 20: goto L_0x018a;
                    case 21: goto L_0x018a;
                    case 22: goto L_0x018a;
                    case 23: goto L_0x018a;
                    case 24: goto L_0x018a;
                    case 25: goto L_0x018a;
                    case 26: goto L_0x018a;
                    case 27: goto L_0x018a;
                    case 28: goto L_0x018a;
                    case 29: goto L_0x018a;
                    case 30: goto L_0x018a;
                    case 31: goto L_0x018a;
                    case 32: goto L_0x018a;
                    case 33: goto L_0x018a;
                    case 34: goto L_0x018a;
                    case 35: goto L_0x018a;
                    case 36: goto L_0x018a;
                    case 37: goto L_0x018a;
                    case 38: goto L_0x018a;
                    case 39: goto L_0x018a;
                    case 40: goto L_0x018a;
                    case 41: goto L_0x018a;
                    case 42: goto L_0x018a;
                    case 43: goto L_0x018a;
                    case 44: goto L_0x018a;
                    case 45: goto L_0x018a;
                    case 46: goto L_0x018a;
                    case 47: goto L_0x018a;
                    case 48: goto L_0x018a;
                    case 49: goto L_0x018a;
                    case 50: goto L_0x0198;
                    case 51: goto L_0x01a6;
                    case 52: goto L_0x01a6;
                    case 53: goto L_0x01a6;
                    case 54: goto L_0x01a6;
                    case 55: goto L_0x01a6;
                    case 56: goto L_0x01a6;
                    case 57: goto L_0x01a6;
                    case 58: goto L_0x01a6;
                    case 59: goto L_0x01a6;
                    case 60: goto L_0x01a6;
                    case 61: goto L_0x01a6;
                    case 62: goto L_0x01a6;
                    case 63: goto L_0x01a6;
                    case 64: goto L_0x01a6;
                    case 65: goto L_0x01a6;
                    case 66: goto L_0x01a6;
                    case 67: goto L_0x01a6;
                    case 68: goto L_0x01a6;
                    default: goto L_0x001a;
                };
            L_0x001a:
                r2 = r1;
            L_0x001b:
                if (r2 != 0) goto L_0x01ca;
            L_0x001d:
                return r0;
            L_0x001e:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0030;
            L_0x0024:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x0030:
                r2 = r0;
                goto L_0x001b;
            L_0x0032:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0042;
            L_0x0038:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x0042:
                r2 = r0;
                goto L_0x001b;
            L_0x0044:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0056;
            L_0x004a:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x0056:
                r2 = r0;
                goto L_0x001b;
            L_0x0058:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x006a;
            L_0x005e:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x006a:
                r2 = r0;
                goto L_0x001b;
            L_0x006c:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x007c;
            L_0x0072:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x007c:
                r2 = r0;
                goto L_0x001b;
            L_0x007e:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0090;
            L_0x0084:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x0090:
                r2 = r0;
                goto L_0x001b;
            L_0x0092:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x00a2;
            L_0x0098:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x00a2:
                r2 = r0;
                goto L_0x001b;
            L_0x00a5:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x00b5;
            L_0x00ab:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzm(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzm(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x00b5:
                r2 = r0;
                goto L_0x001b;
            L_0x00b8:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x00cc;
            L_0x00be:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                if (r2 != 0) goto L_0x001a;
            L_0x00cc:
                r2 = r0;
                goto L_0x001b;
            L_0x00cf:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x00e3;
            L_0x00d5:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                if (r2 != 0) goto L_0x001a;
            L_0x00e3:
                r2 = r0;
                goto L_0x001b;
            L_0x00e6:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x00fa;
            L_0x00ec:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                if (r2 != 0) goto L_0x001a;
            L_0x00fa:
                r2 = r0;
                goto L_0x001b;
            L_0x00fd:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x010d;
            L_0x0103:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x010d:
                r2 = r0;
                goto L_0x001b;
            L_0x0110:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0120;
            L_0x0116:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x0120:
                r2 = r0;
                goto L_0x001b;
            L_0x0123:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0133;
            L_0x0129:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x0133:
                r2 = r0;
                goto L_0x001b;
            L_0x0136:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0148;
            L_0x013c:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x0148:
                r2 = r0;
                goto L_0x001b;
            L_0x014b:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x015b;
            L_0x0151:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r6);
                if (r2 == r5) goto L_0x001a;
            L_0x015b:
                r2 = r0;
                goto L_0x001b;
            L_0x015e:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0170;
            L_0x0164:
                r8 = com.google.android.gms.internal.measurement.zzyh.zzl(r12, r6);
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r13, r6);
                r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
                if (r2 == 0) goto L_0x001a;
            L_0x0170:
                r2 = r0;
                goto L_0x001b;
            L_0x0173:
                r2 = r11.zzc(r12, r13, r3);
                if (r2 == 0) goto L_0x0187;
            L_0x0179:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                if (r2 != 0) goto L_0x001a;
            L_0x0187:
                r2 = r0;
                goto L_0x001b;
            L_0x018a:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                goto L_0x001b;
            L_0x0198:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                goto L_0x001b;
            L_0x01a6:
                r2 = r11.zzbr(r3);
                r5 = r2 & r10;
                r8 = (long) r5;
                r5 = com.google.android.gms.internal.measurement.zzyh.zzk(r12, r8);
                r2 = r2 & r10;
                r8 = (long) r2;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r13, r8);
                if (r5 != r2) goto L_0x01c7;
            L_0x01b9:
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r12, r6);
                r5 = com.google.android.gms.internal.measurement.zzyh.zzp(r13, r6);
                r2 = com.google.android.gms.internal.measurement.zzxl.zze(r2, r5);
                if (r2 != 0) goto L_0x001a;
            L_0x01c7:
                r2 = r0;
                goto L_0x001b;
            L_0x01ca:
                r2 = r3 + 3;
                r3 = r2;
                goto L_0x0009;
            L_0x01cf:
                r2 = r11.zzcbn;
                r2 = r2.zzah(r12);
                r3 = r11.zzcbn;
                r3 = r3.zzah(r13);
                r2 = r2.equals(r3);
                if (r2 == 0) goto L_0x001d;
            L_0x01e1:
                r0 = r11.zzcbe;
                if (r0 == 0) goto L_0x01f7;
            L_0x01e5:
                r0 = r11.zzcbo;
                r0 = r0.zzs(r12);
                r1 = r11.zzcbo;
                r1 = r1.zzs(r13);
                r0 = r0.equals(r1);
                goto L_0x001d;
            L_0x01f7:
                r0 = r1;
                goto L_0x001d;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzwx.equals(java.lang.Object, java.lang.Object):boolean");
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final int hashCode(T r10) {
                /*
                r9 = this;
                r1 = 37;
                r0 = 0;
                r2 = r9.zzcaz;
                r4 = r2.length;
                r3 = r0;
                r2 = r0;
            L_0x0008:
                if (r3 >= r4) goto L_0x0254;
            L_0x000a:
                r0 = r9.zzbq(r3);
                r5 = r9.zzcaz;
                r5 = r5[r3];
                r6 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
                r6 = r6 & r0;
                r6 = (long) r6;
                r8 = 267386880; // 0xff00000 float:2.3665827E-29 double:1.321066716E-315;
                r0 = r0 & r8;
                r0 = r0 >>> 20;
                switch(r0) {
                    case 0: goto L_0x0024;
                    case 1: goto L_0x0034;
                    case 2: goto L_0x0040;
                    case 3: goto L_0x004c;
                    case 4: goto L_0x0058;
                    case 5: goto L_0x0060;
                    case 6: goto L_0x006c;
                    case 7: goto L_0x0074;
                    case 8: goto L_0x0080;
                    case 9: goto L_0x008e;
                    case 10: goto L_0x009c;
                    case 11: goto L_0x00a9;
                    case 12: goto L_0x00b2;
                    case 13: goto L_0x00bb;
                    case 14: goto L_0x00c4;
                    case 15: goto L_0x00d1;
                    case 16: goto L_0x00da;
                    case 17: goto L_0x00e7;
                    case 18: goto L_0x00f6;
                    case 19: goto L_0x00f6;
                    case 20: goto L_0x00f6;
                    case 21: goto L_0x00f6;
                    case 22: goto L_0x00f6;
                    case 23: goto L_0x00f6;
                    case 24: goto L_0x00f6;
                    case 25: goto L_0x00f6;
                    case 26: goto L_0x00f6;
                    case 27: goto L_0x00f6;
                    case 28: goto L_0x00f6;
                    case 29: goto L_0x00f6;
                    case 30: goto L_0x00f6;
                    case 31: goto L_0x00f6;
                    case 32: goto L_0x00f6;
                    case 33: goto L_0x00f6;
                    case 34: goto L_0x00f6;
                    case 35: goto L_0x00f6;
                    case 36: goto L_0x00f6;
                    case 37: goto L_0x00f6;
                    case 38: goto L_0x00f6;
                    case 39: goto L_0x00f6;
                    case 40: goto L_0x00f6;
                    case 41: goto L_0x00f6;
                    case 42: goto L_0x00f6;
                    case 43: goto L_0x00f6;
                    case 44: goto L_0x00f6;
                    case 45: goto L_0x00f6;
                    case 46: goto L_0x00f6;
                    case 47: goto L_0x00f6;
                    case 48: goto L_0x00f6;
                    case 49: goto L_0x00f6;
                    case 50: goto L_0x0103;
                    case 51: goto L_0x0110;
                    case 52: goto L_0x0127;
                    case 53: goto L_0x013a;
                    case 54: goto L_0x014d;
                    case 55: goto L_0x0160;
                    case 56: goto L_0x016f;
                    case 57: goto L_0x0182;
                    case 58: goto L_0x0191;
                    case 59: goto L_0x01a4;
                    case 60: goto L_0x01b9;
                    case 61: goto L_0x01cc;
                    case 62: goto L_0x01df;
                    case 63: goto L_0x01ee;
                    case 64: goto L_0x01fd;
                    case 65: goto L_0x020c;
                    case 66: goto L_0x021f;
                    case 67: goto L_0x022e;
                    case 68: goto L_0x0241;
                    default: goto L_0x001f;
                };
            L_0x001f:
                r0 = r2;
            L_0x0020:
                r3 = r3 + 3;
                r2 = r0;
                goto L_0x0008;
            L_0x0024:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzo(r10, r6);
                r6 = java.lang.Double.doubleToLongBits(r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0034:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzn(r10, r6);
                r2 = java.lang.Float.floatToIntBits(r2);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0040:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x004c:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0058:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0060:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x006c:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0074:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzm(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzw(r2);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0080:
                r2 = r2 * 53;
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r0 = (java.lang.String) r0;
                r0 = r0.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x008e:
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                if (r0 == 0) goto L_0x0276;
            L_0x0094:
                r0 = r0.hashCode();
            L_0x0098:
                r2 = r2 * 53;
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x009c:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00a9:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00b2:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00bb:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00c4:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00d1:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzk(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00da:
                r0 = r2 * 53;
                r6 = com.google.android.gms.internal.measurement.zzyh.zzl(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00e7:
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                if (r0 == 0) goto L_0x0273;
            L_0x00ed:
                r0 = r0.hashCode();
            L_0x00f1:
                r2 = r2 * 53;
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x00f6:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0103:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0110:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0116:
                r0 = r2 * 53;
                r6 = zzf(r10, r6);
                r6 = java.lang.Double.doubleToLongBits(r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0127:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x012d:
                r0 = r2 * 53;
                r2 = zzg(r10, r6);
                r2 = java.lang.Float.floatToIntBits(r2);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x013a:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0140:
                r0 = r2 * 53;
                r6 = zzi(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x014d:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0153:
                r0 = r2 * 53;
                r6 = zzi(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0160:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0166:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x016f:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0175:
                r0 = r2 * 53;
                r6 = zzi(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0182:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0188:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0191:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0197:
                r0 = r2 * 53;
                r2 = zzj(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzw(r2);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01a4:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x01aa:
                r2 = r2 * 53;
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r0 = (java.lang.String) r0;
                r0 = r0.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01b9:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x01bf:
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2 * 53;
                r0 = r0.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01cc:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x01d2:
                r0 = r2 * 53;
                r2 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01df:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x01e5:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01ee:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x01f4:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x01fd:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0203:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x020c:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0212:
                r0 = r2 * 53;
                r6 = zzi(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x021f:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0225:
                r0 = r2 * 53;
                r2 = zzh(r10, r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x022e:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0234:
                r0 = r2 * 53;
                r6 = zzi(r10, r6);
                r2 = com.google.android.gms.internal.measurement.zzvo.zzbf(r6);
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0241:
                r0 = r9.zza(r10, r5, r3);
                if (r0 == 0) goto L_0x001f;
            L_0x0247:
                r0 = com.google.android.gms.internal.measurement.zzyh.zzp(r10, r6);
                r2 = r2 * 53;
                r0 = r0.hashCode();
                r0 = r0 + r2;
                goto L_0x0020;
            L_0x0254:
                r0 = r2 * 53;
                r1 = r9.zzcbn;
                r1 = r1.zzah(r10);
                r1 = r1.hashCode();
                r0 = r0 + r1;
                r1 = r9.zzcbe;
                if (r1 == 0) goto L_0x0272;
            L_0x0265:
                r0 = r0 * 53;
                r1 = r9.zzcbo;
                r1 = r1.zzs(r10);
                r1 = r1.hashCode();
                r0 = r0 + r1;
            L_0x0272:
                return r0;
            L_0x0273:
                r0 = r1;
                goto L_0x00f1;
            L_0x0276:
                r0 = r1;
                goto L_0x0098;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzwx.hashCode(java.lang.Object):int");
            }

            public final void zzd(T t, T t2) {
                if (t2 == null) {
                    throw new NullPointerException();
                }
                for (int i = 0; i < this.zzcaz.length; i += 3) {
                    int zzbq = zzbq(i);
                    long j = (long) (1048575 & zzbq);
                    int i2 = this.zzcaz[i];
                    switch ((zzbq & 267386880) >>> 20) {
                        case 0:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzo(t2, j));
                            zzc(t, i);
                            break;
                        case 1:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzn(t2, j));
                            zzc(t, i);
                            break;
                        case 2:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzl(t2, j));
                            zzc(t, i);
                            break;
                        case 3:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzl(t2, j));
                            zzc(t, i);
                            break;
                        case 4:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 5:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzl(t2, j));
                            zzc(t, i);
                            break;
                        case 6:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 7:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzm(t2, j));
                            zzc(t, i);
                            break;
                        case 8:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzp(t2, j));
                            zzc(t, i);
                            break;
                        case 9:
                            zza((Object) t, (Object) t2, i);
                            break;
                        case 10:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzp(t2, j));
                            zzc(t, i);
                            break;
                        case 11:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 12:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 13:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 14:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzl(t2, j));
                            zzc(t, i);
                            break;
                        case 15:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zzb((Object) t, j, zzyh.zzk(t2, j));
                            zzc(t, i);
                            break;
                        case 16:
                            if (!zzb((Object) t2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzl(t2, j));
                            zzc(t, i);
                            break;
                        case 17:
                            zza((Object) t, (Object) t2, i);
                            break;
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                        case 24:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                        case 27:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                        case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                        case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                            this.zzcbm.zza(t, t2, j);
                            break;
                        case 50:
                            zzxl.zza(this.zzcbp, (Object) t, (Object) t2, j);
                            break;
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                            if (!zza((Object) t2, i2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzp(t2, j));
                            zzb((Object) t, i2, i);
                            break;
                        case 60:
                            zzb((Object) t, (Object) t2, i);
                            break;
                        case 61:
                        case 62:
                        case 63:
                        case 64:
                        case VoIPService.CALL_MIN_LAYER /*65*/:
                        case 66:
                        case 67:
                            if (!zza((Object) t2, i2, i)) {
                                break;
                            }
                            zzyh.zza((Object) t, j, zzyh.zzp(t2, j));
                            zzb((Object) t, i2, i);
                            break;
                        case 68:
                            zzb((Object) t, (Object) t2, i);
                            break;
                        default:
                            break;
                    }
                }
                if (!this.zzcbg) {
                    zzxl.zza(this.zzcbn, (Object) t, (Object) t2);
                    if (this.zzcbe) {
                        zzxl.zza(this.zzcbo, (Object) t, (Object) t2);
                    }
                }
            }

            private final void zza(T t, T t2, int i) {
                long zzbq = (long) (zzbq(i) & 1048575);
                if (zzb((Object) t2, i)) {
                    Object zzp = zzyh.zzp(t, zzbq);
                    Object zzp2 = zzyh.zzp(t2, zzbq);
                    if (zzp != null && zzp2 != null) {
                        zzyh.zza((Object) t, zzbq, zzvo.zzb(zzp, zzp2));
                        zzc(t, i);
                    } else if (zzp2 != null) {
                        zzyh.zza((Object) t, zzbq, zzp2);
                        zzc(t, i);
                    }
                }
            }

            private final void zzb(T t, T t2, int i) {
                int zzbq = zzbq(i);
                int i2 = this.zzcaz[i];
                long j = (long) (zzbq & 1048575);
                if (zza((Object) t2, i2, i)) {
                    Object zzp = zzyh.zzp(t, j);
                    Object zzp2 = zzyh.zzp(t2, j);
                    if (zzp != null && zzp2 != null) {
                        zzyh.zza((Object) t, j, zzvo.zzb(zzp, zzp2));
                        zzb((Object) t, i2, i);
                    } else if (zzp2 != null) {
                        zzyh.zza((Object) t, j, zzp2);
                        zzb((Object) t, i2, i);
                    }
                }
            }

            public final int zzae(T t) {
                int i;
                int i2;
                int zzbq;
                int i3;
                int i4;
                int i5;
                Object zzp;
                if (this.zzcbg) {
                    Unsafe unsafe = zzcay;
                    i = 0;
                    for (i2 = 0; i2 < this.zzcaz.length; i2 += 3) {
                        zzbq = zzbq(i2);
                        i3 = (267386880 & zzbq) >>> 20;
                        i4 = this.zzcaz[i2];
                        long j = (long) (zzbq & 1048575);
                        if (i3 < zzvg.DOUBLE_LIST_PACKED.id() || i3 > zzvg.SINT64_LIST_PACKED.id()) {
                            i5 = 0;
                        } else {
                            i5 = this.zzcaz[i2 + 2] & 1048575;
                        }
                        switch (i3) {
                            case 0:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzb(i4, 0.0d);
                                break;
                            case 1:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzb(i4, 0.0f);
                                break;
                            case 2:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzd(i4, zzyh.zzl(t, j));
                                break;
                            case 3:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zze(i4, zzyh.zzl(t, j));
                                break;
                            case 4:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzh(i4, zzyh.zzk(t, j));
                                break;
                            case 5:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzg(i4, 0);
                                break;
                            case 6:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzk(i4, 0);
                                break;
                            case 7:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, true);
                                break;
                            case 8:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                zzp = zzyh.zzp(t, j);
                                if (!(zzp instanceof zzud)) {
                                    i += zzut.zzc(i4, (String) zzp);
                                    break;
                                }
                                i += zzut.zzc(i4, (zzud) zzp);
                                break;
                            case 9:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzxl.zzc(i4, zzyh.zzp(t, j), zzbn(i2));
                                break;
                            case 10:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, (zzud) zzyh.zzp(t, j));
                                break;
                            case 11:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzi(i4, zzyh.zzk(t, j));
                                break;
                            case 12:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzm(i4, zzyh.zzk(t, j));
                                break;
                            case 13:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzl(i4, 0);
                                break;
                            case 14:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzh(i4, 0);
                                break;
                            case 15:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzj(i4, zzyh.zzk(t, j));
                                break;
                            case 16:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzf(i4, zzyh.zzl(t, j));
                                break;
                            case 17:
                                if (!zzb((Object) t, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, (zzwt) zzyh.zzp(t, j), zzbn(i2));
                                break;
                            case 18:
                                i += zzxl.zzw(i4, zze(t, j), false);
                                break;
                            case 19:
                                i += zzxl.zzv(i4, zze(t, j), false);
                                break;
                            case 20:
                                i += zzxl.zzo(i4, zze(t, j), false);
                                break;
                            case 21:
                                i += zzxl.zzp(i4, zze(t, j), false);
                                break;
                            case 22:
                                i += zzxl.zzs(i4, zze(t, j), false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                                i += zzxl.zzw(i4, zze(t, j), false);
                                break;
                            case 24:
                                i += zzxl.zzv(i4, zze(t, j), false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                                i += zzxl.zzx(i4, zze(t, j), false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                                i += zzxl.zzc(i4, zze(t, j));
                                break;
                            case 27:
                                i += zzxl.zzc(i4, zze(t, j), zzbn(i2));
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                                i += zzxl.zzd(i4, zze(t, j));
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                                i += zzxl.zzt(i4, zze(t, j), false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                                i += zzxl.zzr(i4, zze(t, j), false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                                i += zzxl.zzv(i4, zze(t, j), false);
                                break;
                            case 32:
                                i += zzxl.zzw(i4, zze(t, j), false);
                                break;
                            case 33:
                                i += zzxl.zzu(i4, zze(t, j), false);
                                break;
                            case 34:
                                i += zzxl.zzq(i4, zze(t, j), false);
                                break;
                            case 35:
                                zzbq = zzxl.zzaf((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 36:
                                zzbq = zzxl.zzae((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                                zzbq = zzxl.zzx((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                                zzbq = zzxl.zzy((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 39:
                                zzbq = zzxl.zzab((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 40:
                                zzbq = zzxl.zzaf((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 41:
                                zzbq = zzxl.zzae((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 42:
                                zzbq = zzxl.zzag((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 43:
                                zzbq = zzxl.zzac((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 44:
                                zzbq = zzxl.zzaa((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 45:
                                zzbq = zzxl.zzae((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 46:
                                zzbq = zzxl.zzaf((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 47:
                                zzbq = zzxl.zzad((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 48:
                                zzbq = zzxl.zzz((List) unsafe.getObject(t, j));
                                if (zzbq > 0) {
                                    if (this.zzcbh) {
                                        unsafe.putInt(t, (long) i5, zzbq);
                                    }
                                    i += zzbq + (zzut.zzbb(i4) + zzut.zzbd(zzbq));
                                    break;
                                }
                                break;
                            case 49:
                                i += zzxl.zzd(i4, zze(t, j), zzbn(i2));
                                break;
                            case 50:
                                i += this.zzcbp.zzb(i4, zzyh.zzp(t, j), zzbo(i2));
                                break;
                            case 51:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzb(i4, 0.0d);
                                break;
                            case 52:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzb(i4, 0.0f);
                                break;
                            case 53:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzd(i4, zzi(t, j));
                                break;
                            case 54:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zze(i4, zzi(t, j));
                                break;
                            case 55:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzh(i4, zzh(t, j));
                                break;
                            case 56:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzg(i4, 0);
                                break;
                            case 57:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzk(i4, 0);
                                break;
                            case 58:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, true);
                                break;
                            case 59:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                zzp = zzyh.zzp(t, j);
                                if (!(zzp instanceof zzud)) {
                                    i += zzut.zzc(i4, (String) zzp);
                                    break;
                                }
                                i += zzut.zzc(i4, (zzud) zzp);
                                break;
                            case 60:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzxl.zzc(i4, zzyh.zzp(t, j), zzbn(i2));
                                break;
                            case 61:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, (zzud) zzyh.zzp(t, j));
                                break;
                            case 62:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzi(i4, zzh(t, j));
                                break;
                            case 63:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzm(i4, zzh(t, j));
                                break;
                            case 64:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzl(i4, 0);
                                break;
                            case VoIPService.CALL_MIN_LAYER /*65*/:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzh(i4, 0);
                                break;
                            case 66:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzj(i4, zzh(t, j));
                                break;
                            case 67:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzf(i4, zzi(t, j));
                                break;
                            case 68:
                                if (!zza((Object) t, i4, i2)) {
                                    break;
                                }
                                i += zzut.zzc(i4, (zzwt) zzyh.zzp(t, j), zzbn(i2));
                                break;
                            default:
                                break;
                        }
                    }
                    return zza(this.zzcbn, (Object) t) + i;
                }
                int i6 = 0;
                Unsafe unsafe2 = zzcay;
                i5 = -1;
                i = 0;
                for (i2 = 0; i2 < this.zzcaz.length; i2 += 3) {
                    int zzbq2 = zzbq(i2);
                    int i7 = this.zzcaz[i2];
                    int i8 = (267386880 & zzbq2) >>> 20;
                    zzbq = 0;
                    if (i8 <= 17) {
                        i4 = this.zzcaz[i2 + 2];
                        zzbq = 1048575 & i4;
                        i3 = 1 << (i4 >>> 20);
                        if (zzbq != i5) {
                            i = unsafe2.getInt(t, (long) zzbq);
                            i5 = zzbq;
                        }
                        zzbq = i3;
                    } else if (!this.zzcbh || i8 < zzvg.DOUBLE_LIST_PACKED.id() || i8 > zzvg.SINT64_LIST_PACKED.id()) {
                        i4 = 0;
                    } else {
                        i4 = this.zzcaz[i2 + 2] & 1048575;
                    }
                    long j2 = (long) (1048575 & zzbq2);
                    switch (i8) {
                        case 0:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzb(i7, 0.0d);
                            break;
                        case 1:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzb(i7, 0.0f);
                            break;
                        case 2:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzd(i7, unsafe2.getLong(t, j2));
                            break;
                        case 3:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zze(i7, unsafe2.getLong(t, j2));
                            break;
                        case 4:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzh(i7, unsafe2.getInt(t, j2));
                            break;
                        case 5:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzg(i7, 0);
                            break;
                        case 6:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzk(i7, 0);
                            break;
                        case 7:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzc(i7, true);
                            break;
                        case 8:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            zzp = unsafe2.getObject(t, j2);
                            if (!(zzp instanceof zzud)) {
                                i6 += zzut.zzc(i7, (String) zzp);
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzud) zzp);
                            break;
                        case 9:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzxl.zzc(i7, unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        case 10:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzud) unsafe2.getObject(t, j2));
                            break;
                        case 11:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzi(i7, unsafe2.getInt(t, j2));
                            break;
                        case 12:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzm(i7, unsafe2.getInt(t, j2));
                            break;
                        case 13:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzl(i7, 0);
                            break;
                        case 14:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzh(i7, 0);
                            break;
                        case 15:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzj(i7, unsafe2.getInt(t, j2));
                            break;
                        case 16:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzf(i7, unsafe2.getLong(t, j2));
                            break;
                        case 17:
                            if ((zzbq & i) == 0) {
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzwt) unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        case 18:
                            i6 += zzxl.zzw(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 19:
                            i6 += zzxl.zzv(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 20:
                            i6 += zzxl.zzo(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 21:
                            i6 += zzxl.zzp(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 22:
                            i6 += zzxl.zzs(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                            i6 += zzxl.zzw(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 24:
                            i6 += zzxl.zzv(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                            i6 += zzxl.zzx(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                            i6 += zzxl.zzc(i7, (List) unsafe2.getObject(t, j2));
                            break;
                        case 27:
                            i6 += zzxl.zzc(i7, (List) unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                            i6 += zzxl.zzd(i7, (List) unsafe2.getObject(t, j2));
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                            i6 += zzxl.zzt(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                            i6 += zzxl.zzr(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                            i6 += zzxl.zzv(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 32:
                            i6 += zzxl.zzw(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 33:
                            i6 += zzxl.zzu(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 34:
                            i6 += zzxl.zzq(i7, (List) unsafe2.getObject(t, j2), false);
                            break;
                        case 35:
                            zzbq = zzxl.zzaf((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 36:
                            zzbq = zzxl.zzae((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                            zzbq = zzxl.zzx((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                            zzbq = zzxl.zzy((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 39:
                            zzbq = zzxl.zzab((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 40:
                            zzbq = zzxl.zzaf((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 41:
                            zzbq = zzxl.zzae((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 42:
                            zzbq = zzxl.zzag((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 43:
                            zzbq = zzxl.zzac((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 44:
                            zzbq = zzxl.zzaa((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 45:
                            zzbq = zzxl.zzae((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 46:
                            zzbq = zzxl.zzaf((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 47:
                            zzbq = zzxl.zzad((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 48:
                            zzbq = zzxl.zzz((List) unsafe2.getObject(t, j2));
                            if (zzbq > 0) {
                                if (this.zzcbh) {
                                    unsafe2.putInt(t, (long) i4, zzbq);
                                }
                                i6 += zzbq + (zzut.zzbb(i7) + zzut.zzbd(zzbq));
                                break;
                            }
                            break;
                        case 49:
                            i6 += zzxl.zzd(i7, (List) unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        case 50:
                            i6 += this.zzcbp.zzb(i7, unsafe2.getObject(t, j2), zzbo(i2));
                            break;
                        case 51:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzb(i7, 0.0d);
                            break;
                        case 52:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzb(i7, 0.0f);
                            break;
                        case 53:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzd(i7, zzi(t, j2));
                            break;
                        case 54:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zze(i7, zzi(t, j2));
                            break;
                        case 55:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzh(i7, zzh(t, j2));
                            break;
                        case 56:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzg(i7, 0);
                            break;
                        case 57:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzk(i7, 0);
                            break;
                        case 58:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzc(i7, true);
                            break;
                        case 59:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            zzp = unsafe2.getObject(t, j2);
                            if (!(zzp instanceof zzud)) {
                                i6 += zzut.zzc(i7, (String) zzp);
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzud) zzp);
                            break;
                        case 60:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzxl.zzc(i7, unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        case 61:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzud) unsafe2.getObject(t, j2));
                            break;
                        case 62:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzi(i7, zzh(t, j2));
                            break;
                        case 63:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzm(i7, zzh(t, j2));
                            break;
                        case 64:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzl(i7, 0);
                            break;
                        case VoIPService.CALL_MIN_LAYER /*65*/:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzh(i7, 0);
                            break;
                        case 66:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzj(i7, zzh(t, j2));
                            break;
                        case 67:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzf(i7, zzi(t, j2));
                            break;
                        case 68:
                            if (!zza((Object) t, i7, i2)) {
                                break;
                            }
                            i6 += zzut.zzc(i7, (zzwt) unsafe2.getObject(t, j2), zzbn(i2));
                            break;
                        default:
                            break;
                    }
                }
                zzbq = zza(this.zzcbn, (Object) t) + i6;
                if (this.zzcbe) {
                    return zzbq + this.zzcbo.zzs(t).zzvu();
                }
                return zzbq;
            }

            private static <UT, UB> int zza(zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB, T t) {
                return com_google_android_gms_internal_measurement_zzyb_UT__UB.zzae(com_google_android_gms_internal_measurement_zzyb_UT__UB.zzah(t));
            }

            private static <E> List<E> zze(Object obj, long j) {
                return (List) zzyh.zzp(obj, j);
            }

            public final void zza(T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
                Iterator it;
                Entry entry;
                zzvd zzs;
                int length;
                int zzbq;
                int i;
                Entry entry2;
                if (com_google_android_gms_internal_measurement_zzyw.zzvj() == zze.zzbzf) {
                    zza(this.zzcbn, (Object) t, com_google_android_gms_internal_measurement_zzyw);
                    it = null;
                    entry = null;
                    if (this.zzcbe) {
                        zzs = this.zzcbo.zzs(t);
                        if (!zzs.isEmpty()) {
                            it = zzs.descendingIterator();
                            entry = (Entry) it.next();
                        }
                    }
                    length = this.zzcaz.length - 3;
                    while (length >= 0) {
                        zzbq = zzbq(length);
                        i = this.zzcaz[length];
                        entry2 = entry;
                        while (entry2 != null && this.zzcbo.zzb(entry2) > i) {
                            this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry2);
                            entry2 = it.hasNext() ? (Entry) it.next() : null;
                        }
                        switch ((267386880 & zzbq) >>> 20) {
                            case 0:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzyh.zzo(t, (long) (1048575 & zzbq)));
                                break;
                            case 1:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzyh.zzn(t, (long) (1048575 & zzbq)));
                                break;
                            case 2:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzi(i, zzyh.zzl(t, (long) (1048575 & zzbq)));
                                break;
                            case 3:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzyh.zzl(t, (long) (1048575 & zzbq)));
                                break;
                            case 4:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzd(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 5:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzc(i, zzyh.zzl(t, (long) (1048575 & zzbq)));
                                break;
                            case 6:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzg(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 7:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzyh.zzm(t, (long) (1048575 & zzbq)));
                                break;
                            case 8:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                zza(i, zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 9:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzyh.zzp(t, (long) (1048575 & zzbq)), zzbn(length));
                                break;
                            case 10:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, (zzud) zzyh.zzp(t, (long) (1048575 & zzbq)));
                                break;
                            case 11:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zze(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 12:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzo(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 13:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzn(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 14:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzj(i, zzyh.zzl(t, (long) (1048575 & zzbq)));
                                break;
                            case 15:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzf(i, zzyh.zzk(t, (long) (1048575 & zzbq)));
                                break;
                            case 16:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzyh.zzl(t, (long) (1048575 & zzbq)));
                                break;
                            case 17:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzyh.zzp(t, (long) (1048575 & zzbq)), zzbn(length));
                                break;
                            case 18:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 19:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 20:
                                zzxl.zzc(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 21:
                                zzxl.zzd(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 22:
                                zzxl.zzh(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                                zzxl.zzf(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 24:
                                zzxl.zzk(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                                zzxl.zzn(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 27:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, zzbn(length));
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                                zzxl.zzi(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                                zzxl.zzm(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                                zzxl.zzl(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 32:
                                zzxl.zzg(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 33:
                                zzxl.zzj(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 34:
                                zzxl.zze(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 35:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 36:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                                zzxl.zzc(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                                zzxl.zzd(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 39:
                                zzxl.zzh(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 40:
                                zzxl.zzf(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 41:
                                zzxl.zzk(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 42:
                                zzxl.zzn(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 43:
                                zzxl.zzi(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 44:
                                zzxl.zzm(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 45:
                                zzxl.zzl(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 46:
                                zzxl.zzg(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 47:
                                zzxl.zzj(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 48:
                                zzxl.zze(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 49:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw, zzbn(length));
                                break;
                            case 50:
                                zza(com_google_android_gms_internal_measurement_zzyw, i, zzyh.zzp(t, (long) (1048575 & zzbq)), length);
                                break;
                            case 51:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzf(t, (long) (1048575 & zzbq)));
                                break;
                            case 52:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzg(t, (long) (1048575 & zzbq)));
                                break;
                            case 53:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzi(i, zzi(t, (long) (1048575 & zzbq)));
                                break;
                            case 54:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzi(t, (long) (1048575 & zzbq)));
                                break;
                            case 55:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzd(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case 56:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzc(i, zzi(t, (long) (1048575 & zzbq)));
                                break;
                            case 57:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzg(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case 58:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzj(t, (long) (1048575 & zzbq)));
                                break;
                            case 59:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                zza(i, zzyh.zzp(t, (long) (1048575 & zzbq)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 60:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, zzyh.zzp(t, (long) (1048575 & zzbq)), zzbn(length));
                                break;
                            case 61:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i, (zzud) zzyh.zzp(t, (long) (1048575 & zzbq)));
                                break;
                            case 62:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zze(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case 63:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzo(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case 64:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzn(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case VoIPService.CALL_MIN_LAYER /*65*/:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzj(i, zzi(t, (long) (1048575 & zzbq)));
                                break;
                            case 66:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzf(i, zzh(t, (long) (1048575 & zzbq)));
                                break;
                            case 67:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzi(t, (long) (1048575 & zzbq)));
                                break;
                            case 68:
                                if (!zza((Object) t, i, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i, zzyh.zzp(t, (long) (1048575 & zzbq)), zzbn(length));
                                break;
                            default:
                                break;
                        }
                        length -= 3;
                        entry = entry2;
                    }
                    while (entry != null) {
                        this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry);
                        entry = it.hasNext() ? (Entry) it.next() : null;
                    }
                } else if (this.zzcbg) {
                    it = null;
                    entry = null;
                    if (this.zzcbe) {
                        zzs = this.zzcbo.zzs(t);
                        if (!zzs.isEmpty()) {
                            it = zzs.iterator();
                            entry = (Entry) it.next();
                        }
                    }
                    zzbq = this.zzcaz.length;
                    length = 0;
                    while (length < zzbq) {
                        i = zzbq(length);
                        int i2 = this.zzcaz[length];
                        entry2 = entry;
                        while (entry2 != null && this.zzcbo.zzb(entry2) <= i2) {
                            this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry2);
                            entry2 = it.hasNext() ? (Entry) it.next() : null;
                        }
                        switch ((267386880 & i) >>> 20) {
                            case 0:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzyh.zzo(t, (long) (1048575 & i)));
                                break;
                            case 1:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzyh.zzn(t, (long) (1048575 & i)));
                                break;
                            case 2:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzi(i2, zzyh.zzl(t, (long) (1048575 & i)));
                                break;
                            case 3:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzyh.zzl(t, (long) (1048575 & i)));
                                break;
                            case 4:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzd(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 5:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzc(i2, zzyh.zzl(t, (long) (1048575 & i)));
                                break;
                            case 6:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzg(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 7:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzyh.zzm(t, (long) (1048575 & i)));
                                break;
                            case 8:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                zza(i2, zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 9:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzyh.zzp(t, (long) (1048575 & i)), zzbn(length));
                                break;
                            case 10:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, (zzud) zzyh.zzp(t, (long) (1048575 & i)));
                                break;
                            case 11:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zze(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 12:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzo(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 13:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzn(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 14:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzj(i2, zzyh.zzl(t, (long) (1048575 & i)));
                                break;
                            case 15:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzf(i2, zzyh.zzk(t, (long) (1048575 & i)));
                                break;
                            case 16:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzyh.zzl(t, (long) (1048575 & i)));
                                break;
                            case 17:
                                if (!zzb((Object) t, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzyh.zzp(t, (long) (1048575 & i)), zzbn(length));
                                break;
                            case 18:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 19:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 20:
                                zzxl.zzc(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 21:
                                zzxl.zzd(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 22:
                                zzxl.zzh(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                                zzxl.zzf(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 24:
                                zzxl.zzk(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                                zzxl.zzn(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 27:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, zzbn(length));
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                                zzxl.zzi(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                                zzxl.zzm(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                                zzxl.zzl(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 32:
                                zzxl.zzg(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 33:
                                zzxl.zzj(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 34:
                                zzxl.zze(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, false);
                                break;
                            case 35:
                                zzxl.zza(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 36:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                                zzxl.zzc(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                                zzxl.zzd(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 39:
                                zzxl.zzh(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 40:
                                zzxl.zzf(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 41:
                                zzxl.zzk(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 42:
                                zzxl.zzn(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 43:
                                zzxl.zzi(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 44:
                                zzxl.zzm(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 45:
                                zzxl.zzl(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 46:
                                zzxl.zzg(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 47:
                                zzxl.zzj(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 48:
                                zzxl.zze(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, true);
                                break;
                            case 49:
                                zzxl.zzb(this.zzcaz[length], (List) zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw, zzbn(length));
                                break;
                            case 50:
                                zza(com_google_android_gms_internal_measurement_zzyw, i2, zzyh.zzp(t, (long) (1048575 & i)), length);
                                break;
                            case 51:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzf(t, (long) (1048575 & i)));
                                break;
                            case 52:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzg(t, (long) (1048575 & i)));
                                break;
                            case 53:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzi(i2, zzi(t, (long) (1048575 & i)));
                                break;
                            case 54:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzi(t, (long) (1048575 & i)));
                                break;
                            case 55:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzd(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case 56:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzc(i2, zzi(t, (long) (1048575 & i)));
                                break;
                            case 57:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzg(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case 58:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzj(t, (long) (1048575 & i)));
                                break;
                            case 59:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                zza(i2, zzyh.zzp(t, (long) (1048575 & i)), com_google_android_gms_internal_measurement_zzyw);
                                break;
                            case 60:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, zzyh.zzp(t, (long) (1048575 & i)), zzbn(length));
                                break;
                            case 61:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zza(i2, (zzud) zzyh.zzp(t, (long) (1048575 & i)));
                                break;
                            case 62:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zze(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case 63:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzo(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case 64:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzn(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case VoIPService.CALL_MIN_LAYER /*65*/:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzj(i2, zzi(t, (long) (1048575 & i)));
                                break;
                            case 66:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzf(i2, zzh(t, (long) (1048575 & i)));
                                break;
                            case 67:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzi(t, (long) (1048575 & i)));
                                break;
                            case 68:
                                if (!zza((Object) t, i2, length)) {
                                    break;
                                }
                                com_google_android_gms_internal_measurement_zzyw.zzb(i2, zzyh.zzp(t, (long) (1048575 & i)), zzbn(length));
                                break;
                            default:
                                break;
                        }
                        length += 3;
                        entry = entry2;
                    }
                    while (entry != null) {
                        this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry);
                        entry = it.hasNext() ? (Entry) it.next() : null;
                    }
                    zza(this.zzcbn, (Object) t, com_google_android_gms_internal_measurement_zzyw);
                } else {
                    zzb((Object) t, com_google_android_gms_internal_measurement_zzyw);
                }
            }

            private final void zzb(T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
                Iterator it = null;
                Entry entry = null;
                if (this.zzcbe) {
                    zzvd zzs = this.zzcbo.zzs(t);
                    if (!zzs.isEmpty()) {
                        it = zzs.iterator();
                        entry = (Entry) it.next();
                    }
                }
                int i = -1;
                int i2 = 0;
                int length = this.zzcaz.length;
                Unsafe unsafe = zzcay;
                int i3 = 0;
                Entry entry2 = entry;
                while (i3 < length) {
                    int i4;
                    int i5;
                    int zzbq = zzbq(i3);
                    int i6 = this.zzcaz[i3];
                    int i7 = (267386880 & zzbq) >>> 20;
                    if (this.zzcbg || i7 > 17) {
                        i4 = 0;
                        i5 = i2;
                    } else {
                        int i8;
                        i4 = this.zzcaz[i3 + 2];
                        int i9 = i4 & 1048575;
                        if (i9 != i) {
                            i8 = unsafe.getInt(t, (long) i9);
                        } else {
                            i8 = i2;
                            i9 = i;
                        }
                        i4 = 1 << (i4 >>> 20);
                        i5 = i8;
                        i = i9;
                    }
                    while (entry2 != null && this.zzcbo.zzb(entry2) <= i6) {
                        this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry2);
                        entry2 = it.hasNext() ? (Entry) it.next() : null;
                    }
                    long j = (long) (1048575 & zzbq);
                    switch (i7) {
                        case 0:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, zzyh.zzo(t, j));
                            break;
                        case 1:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, zzyh.zzn(t, j));
                            break;
                        case 2:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzi(i6, unsafe.getLong(t, j));
                            break;
                        case 3:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, unsafe.getLong(t, j));
                            break;
                        case 4:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzd(i6, unsafe.getInt(t, j));
                            break;
                        case 5:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzc(i6, unsafe.getLong(t, j));
                            break;
                        case 6:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzg(i6, unsafe.getInt(t, j));
                            break;
                        case 7:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, zzyh.zzm(t, j));
                            break;
                        case 8:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            zza(i6, unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw);
                            break;
                        case 9:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, unsafe.getObject(t, j), zzbn(i3));
                            break;
                        case 10:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, (zzud) unsafe.getObject(t, j));
                            break;
                        case 11:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zze(i6, unsafe.getInt(t, j));
                            break;
                        case 12:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzo(i6, unsafe.getInt(t, j));
                            break;
                        case 13:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzn(i6, unsafe.getInt(t, j));
                            break;
                        case 14:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzj(i6, unsafe.getLong(t, j));
                            break;
                        case 15:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzf(i6, unsafe.getInt(t, j));
                            break;
                        case 16:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, unsafe.getLong(t, j));
                            break;
                        case 17:
                            if ((i5 & i4) == 0) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, unsafe.getObject(t, j), zzbn(i3));
                            break;
                        case 18:
                            zzxl.zza(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 19:
                            zzxl.zzb(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 20:
                            zzxl.zzc(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 21:
                            zzxl.zzd(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 22:
                            zzxl.zzh(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                            zzxl.zzf(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 24:
                            zzxl.zzk(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                            zzxl.zzn(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                            zzxl.zza(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw);
                            break;
                        case 27:
                            zzxl.zza(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, zzbn(i3));
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                            zzxl.zzb(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                            zzxl.zzi(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                            zzxl.zzm(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                            zzxl.zzl(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 32:
                            zzxl.zzg(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 33:
                            zzxl.zzj(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 34:
                            zzxl.zze(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, false);
                            break;
                        case 35:
                            zzxl.zza(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 36:
                            zzxl.zzb(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                            zzxl.zzc(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                            zzxl.zzd(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 39:
                            zzxl.zzh(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 40:
                            zzxl.zzf(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 41:
                            zzxl.zzk(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 42:
                            zzxl.zzn(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 43:
                            zzxl.zzi(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 44:
                            zzxl.zzm(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 45:
                            zzxl.zzl(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 46:
                            zzxl.zzg(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 47:
                            zzxl.zzj(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 48:
                            zzxl.zze(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, true);
                            break;
                        case 49:
                            zzxl.zzb(this.zzcaz[i3], (List) unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw, zzbn(i3));
                            break;
                        case 50:
                            zza(com_google_android_gms_internal_measurement_zzyw, i6, unsafe.getObject(t, j), i3);
                            break;
                        case 51:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, zzf(t, j));
                            break;
                        case 52:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, zzg(t, j));
                            break;
                        case 53:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzi(i6, zzi(t, j));
                            break;
                        case 54:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, zzi(t, j));
                            break;
                        case 55:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzd(i6, zzh(t, j));
                            break;
                        case 56:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzc(i6, zzi(t, j));
                            break;
                        case 57:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzg(i6, zzh(t, j));
                            break;
                        case 58:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, zzj(t, j));
                            break;
                        case 59:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            zza(i6, unsafe.getObject(t, j), com_google_android_gms_internal_measurement_zzyw);
                            break;
                        case 60:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, unsafe.getObject(t, j), zzbn(i3));
                            break;
                        case 61:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zza(i6, (zzud) unsafe.getObject(t, j));
                            break;
                        case 62:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zze(i6, zzh(t, j));
                            break;
                        case 63:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzo(i6, zzh(t, j));
                            break;
                        case 64:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzn(i6, zzh(t, j));
                            break;
                        case VoIPService.CALL_MIN_LAYER /*65*/:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzj(i6, zzi(t, j));
                            break;
                        case 66:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzf(i6, zzh(t, j));
                            break;
                        case 67:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, zzi(t, j));
                            break;
                        case 68:
                            if (!zza((Object) t, i6, i3)) {
                                break;
                            }
                            com_google_android_gms_internal_measurement_zzyw.zzb(i6, unsafe.getObject(t, j), zzbn(i3));
                            break;
                        default:
                            break;
                    }
                    i3 += 3;
                    i2 = i5;
                }
                for (entry = entry2; entry != null; entry = it.hasNext() ? (Entry) it.next() : null) {
                    this.zzcbo.zza(com_google_android_gms_internal_measurement_zzyw, entry);
                }
                zza(this.zzcbn, (Object) t, com_google_android_gms_internal_measurement_zzyw);
            }

            private final <K, V> void zza(zzyw com_google_android_gms_internal_measurement_zzyw, int i, Object obj, int i2) throws IOException {
                if (obj != null) {
                    com_google_android_gms_internal_measurement_zzyw.zza(i, this.zzcbp.zzad(zzbo(i2)), this.zzcbp.zzz(obj));
                }
            }

            private static <UT, UB> void zza(zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB, T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
                com_google_android_gms_internal_measurement_zzyb_UT__UB.zza(com_google_android_gms_internal_measurement_zzyb_UT__UB.zzah(t), com_google_android_gms_internal_measurement_zzyw);
            }

            public final void zza(T t, zzxi com_google_android_gms_internal_measurement_zzxi, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
                Throwable th;
                if (com_google_android_gms_internal_measurement_zzuz == null) {
                    throw new NullPointerException();
                }
                zzyb com_google_android_gms_internal_measurement_zzyb = this.zzcbn;
                zzva com_google_android_gms_internal_measurement_zzva = this.zzcbo;
                Object obj = null;
                zzvd com_google_android_gms_internal_measurement_zzvd = null;
                while (true) {
                    int i;
                    int i2;
                    int length;
                    Object zzac;
                    int i3;
                    int zzve = com_google_android_gms_internal_measurement_zzxi.zzve();
                    if (zzve < this.zzcbb || zzve > this.zzcbc) {
                        i = -1;
                    } else {
                        i2 = 0;
                        length = (this.zzcaz.length / 3) - 1;
                        while (i2 <= length) {
                            int i4 = (length + i2) >>> 1;
                            i = i4 * 3;
                            int i5 = this.zzcaz[i];
                            if (zzve != i5) {
                                if (zzve < i5) {
                                    length = i4 - 1;
                                } else {
                                    i2 = i4 + 1;
                                }
                            }
                        }
                        i = -1;
                    }
                    Object zzp;
                    if (i >= 0) {
                        length = zzbq(i);
                        zzvr zzbp;
                        List zza;
                        switch ((267386880 & length) >>> 20) {
                            case 0:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.readDouble());
                                zzc(t, i);
                                continue;
                            case 1:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.readFloat());
                                zzc(t, i);
                                continue;
                            case 2:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzui());
                                zzc(t, i);
                                continue;
                            case 3:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuh());
                                zzc(t, i);
                                continue;
                            case 4:
                                zzyh.zzb((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuj());
                                zzc(t, i);
                                continue;
                            case 5:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuk());
                                zzc(t, i);
                                continue;
                            case 6:
                                zzyh.zzb((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzul());
                                zzc(t, i);
                                continue;
                            case 7:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzum());
                                zzc(t, i);
                                continue;
                            case 8:
                                zza((Object) t, length, com_google_android_gms_internal_measurement_zzxi);
                                zzc(t, i);
                                continue;
                            case 9:
                                if (!zzb((Object) t, i)) {
                                    zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zza(zzbn(i), com_google_android_gms_internal_measurement_zzuz));
                                    zzc(t, i);
                                    break;
                                }
                                zzyh.zza((Object) t, (long) (length & 1048575), zzvo.zzb(zzyh.zzp(t, (long) (1048575 & length)), com_google_android_gms_internal_measurement_zzxi.zza(zzbn(i), com_google_android_gms_internal_measurement_zzuz)));
                                continue;
                            case 10:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuo());
                                zzc(t, i);
                                continue;
                            case 11:
                                zzyh.zzb((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzup());
                                zzc(t, i);
                                continue;
                            case 12:
                                i2 = com_google_android_gms_internal_measurement_zzxi.zzuq();
                                zzbp = zzbp(i);
                                if (zzbp != null && !zzbp.zzb(i2)) {
                                    obj = zzxl.zza(zzve, i2, obj, com_google_android_gms_internal_measurement_zzyb);
                                    break;
                                }
                                zzyh.zzb((Object) t, (long) (length & 1048575), i2);
                                zzc(t, i);
                                continue;
                                break;
                            case 13:
                                zzyh.zzb((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzur());
                                zzc(t, i);
                                continue;
                            case 14:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzus());
                                zzc(t, i);
                                continue;
                            case 15:
                                zzyh.zzb((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzut());
                                zzc(t, i);
                                continue;
                            case 16:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuu());
                                zzc(t, i);
                                continue;
                            case 17:
                                if (!zzb((Object) t, i)) {
                                    zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzb(zzbn(i), com_google_android_gms_internal_measurement_zzuz));
                                    zzc(t, i);
                                    break;
                                }
                                zzyh.zza((Object) t, (long) (length & 1048575), zzvo.zzb(zzyh.zzp(t, (long) (1048575 & length)), com_google_android_gms_internal_measurement_zzxi.zzb(zzbn(i), com_google_android_gms_internal_measurement_zzuz)));
                                continue;
                            case 18:
                                com_google_android_gms_internal_measurement_zzxi.zzh(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 19:
                                com_google_android_gms_internal_measurement_zzxi.zzi(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 20:
                                com_google_android_gms_internal_measurement_zzxi.zzk(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 21:
                                com_google_android_gms_internal_measurement_zzxi.zzj(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 22:
                                com_google_android_gms_internal_measurement_zzxi.zzl(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_IRAP_VCL23 /*23*/:
                                com_google_android_gms_internal_measurement_zzxi.zzm(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 24:
                                com_google_android_gms_internal_measurement_zzxi.zzn(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL25 /*25*/:
                                com_google_android_gms_internal_measurement_zzxi.zzo(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                                if (!zzbs(length)) {
                                    com_google_android_gms_internal_measurement_zzxi.readStringList(this.zzcbm.zza(t, (long) (length & 1048575)));
                                    break;
                                } else {
                                    com_google_android_gms_internal_measurement_zzxi.zzp(this.zzcbm.zza(t, (long) (length & 1048575)));
                                    continue;
                                }
                            case 27:
                                com_google_android_gms_internal_measurement_zzxi.zza(this.zzcbm.zza(t, (long) (length & 1048575)), zzbn(i), com_google_android_gms_internal_measurement_zzuz);
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL28 /*28*/:
                                com_google_android_gms_internal_measurement_zzxi.zzq(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /*29*/:
                                com_google_android_gms_internal_measurement_zzxi.zzr(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL30 /*30*/:
                                zza = this.zzcbm.zza(t, (long) (length & 1048575));
                                com_google_android_gms_internal_measurement_zzxi.zzs(zza);
                                obj = zzxl.zza(zzve, zza, zzbp(i), obj, com_google_android_gms_internal_measurement_zzyb);
                                continue;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL31 /*31*/:
                                com_google_android_gms_internal_measurement_zzxi.zzt(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 32:
                                com_google_android_gms_internal_measurement_zzxi.zzu(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 33:
                                com_google_android_gms_internal_measurement_zzxi.zzv(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 34:
                                com_google_android_gms_internal_measurement_zzxi.zzw(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 35:
                                com_google_android_gms_internal_measurement_zzxi.zzh(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 36:
                                com_google_android_gms_internal_measurement_zzxi.zzi(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                                com_google_android_gms_internal_measurement_zzxi.zzk(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case NalUnitTypes.NAL_TYPE_FD_NUT /*38*/:
                                com_google_android_gms_internal_measurement_zzxi.zzj(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 39:
                                com_google_android_gms_internal_measurement_zzxi.zzl(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 40:
                                com_google_android_gms_internal_measurement_zzxi.zzm(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 41:
                                com_google_android_gms_internal_measurement_zzxi.zzn(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 42:
                                com_google_android_gms_internal_measurement_zzxi.zzo(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 43:
                                com_google_android_gms_internal_measurement_zzxi.zzr(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 44:
                                zza = this.zzcbm.zza(t, (long) (length & 1048575));
                                com_google_android_gms_internal_measurement_zzxi.zzs(zza);
                                obj = zzxl.zza(zzve, zza, zzbp(i), obj, com_google_android_gms_internal_measurement_zzyb);
                                continue;
                            case 45:
                                com_google_android_gms_internal_measurement_zzxi.zzt(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 46:
                                com_google_android_gms_internal_measurement_zzxi.zzu(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 47:
                                com_google_android_gms_internal_measurement_zzxi.zzv(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 48:
                                com_google_android_gms_internal_measurement_zzxi.zzw(this.zzcbm.zza(t, (long) (length & 1048575)));
                                continue;
                            case 49:
                                long j = (long) (length & 1048575);
                                com_google_android_gms_internal_measurement_zzxi.zzb(this.zzcbm.zza(t, j), zzbn(i), com_google_android_gms_internal_measurement_zzuz);
                                continue;
                            case 50:
                                Object zzbo = zzbo(i);
                                long zzbq = (long) (zzbq(i) & 1048575);
                                zzp = zzyh.zzp(t, zzbq);
                                if (zzp == null) {
                                    zzac = this.zzcbp.zzac(zzbo);
                                    zzyh.zza((Object) t, zzbq, zzac);
                                } else if (this.zzcbp.zzaa(zzp)) {
                                    zzac = this.zzcbp.zzac(zzbo);
                                    this.zzcbp.zzc(zzac, zzp);
                                    zzyh.zza((Object) t, zzbq, zzac);
                                } else {
                                    zzac = zzp;
                                }
                                com_google_android_gms_internal_measurement_zzxi.zza(this.zzcbp.zzy(zzac), this.zzcbp.zzad(zzbo), com_google_android_gms_internal_measurement_zzuz);
                                continue;
                            case 51:
                                zzyh.zza((Object) t, (long) (length & 1048575), Double.valueOf(com_google_android_gms_internal_measurement_zzxi.readDouble()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 52:
                                zzyh.zza((Object) t, (long) (length & 1048575), Float.valueOf(com_google_android_gms_internal_measurement_zzxi.readFloat()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 53:
                                zzyh.zza((Object) t, (long) (length & 1048575), Long.valueOf(com_google_android_gms_internal_measurement_zzxi.zzui()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 54:
                                zzyh.zza((Object) t, (long) (length & 1048575), Long.valueOf(com_google_android_gms_internal_measurement_zzxi.zzuh()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 55:
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(com_google_android_gms_internal_measurement_zzxi.zzuj()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 56:
                                zzyh.zza((Object) t, (long) (length & 1048575), Long.valueOf(com_google_android_gms_internal_measurement_zzxi.zzuk()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 57:
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(com_google_android_gms_internal_measurement_zzxi.zzul()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 58:
                                zzyh.zza((Object) t, (long) (length & 1048575), Boolean.valueOf(com_google_android_gms_internal_measurement_zzxi.zzum()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 59:
                                zza((Object) t, length, com_google_android_gms_internal_measurement_zzxi);
                                zzb((Object) t, zzve, i);
                                continue;
                            case 60:
                                if (zza((Object) t, zzve, i)) {
                                    zzyh.zza((Object) t, (long) (length & 1048575), zzvo.zzb(zzyh.zzp(t, (long) (1048575 & length)), com_google_android_gms_internal_measurement_zzxi.zza(zzbn(i), com_google_android_gms_internal_measurement_zzuz)));
                                } else {
                                    zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zza(zzbn(i), com_google_android_gms_internal_measurement_zzuz));
                                    zzc(t, i);
                                }
                                zzb((Object) t, zzve, i);
                                continue;
                            case 61:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuo());
                                zzb((Object) t, zzve, i);
                                continue;
                            case 62:
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(com_google_android_gms_internal_measurement_zzxi.zzup()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 63:
                                i2 = com_google_android_gms_internal_measurement_zzxi.zzuq();
                                zzbp = zzbp(i);
                                if (zzbp != null && !zzbp.zzb(i2)) {
                                    obj = zzxl.zza(zzve, i2, obj, com_google_android_gms_internal_measurement_zzyb);
                                    break;
                                }
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(i2));
                                zzb((Object) t, zzve, i);
                                continue;
                                break;
                            case 64:
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(com_google_android_gms_internal_measurement_zzxi.zzur()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case VoIPService.CALL_MIN_LAYER /*65*/:
                                zzyh.zza((Object) t, (long) (length & 1048575), Long.valueOf(com_google_android_gms_internal_measurement_zzxi.zzus()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 66:
                                zzyh.zza((Object) t, (long) (length & 1048575), Integer.valueOf(com_google_android_gms_internal_measurement_zzxi.zzut()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 67:
                                zzyh.zza((Object) t, (long) (length & 1048575), Long.valueOf(com_google_android_gms_internal_measurement_zzxi.zzuu()));
                                zzb((Object) t, zzve, i);
                                continue;
                            case 68:
                                zzyh.zza((Object) t, (long) (length & 1048575), com_google_android_gms_internal_measurement_zzxi.zzb(zzbn(i), com_google_android_gms_internal_measurement_zzuz));
                                zzb((Object) t, zzve, i);
                                continue;
                            default:
                                if (obj == null) {
                                    try {
                                        zzac = com_google_android_gms_internal_measurement_zzyb.zzye();
                                    } catch (zzvu e) {
                                        break;
                                    }
                                }
                                zzac = obj;
                                try {
                                    if (com_google_android_gms_internal_measurement_zzyb.zza(zzac, com_google_android_gms_internal_measurement_zzxi)) {
                                        obj = zzac;
                                        continue;
                                    } else {
                                        for (i3 = this.zzcbj; i3 < this.zzcbk; i3++) {
                                            zzac = zza((Object) t, this.zzcbi[i3], zzac, com_google_android_gms_internal_measurement_zzyb);
                                        }
                                        if (zzac != null) {
                                            com_google_android_gms_internal_measurement_zzyb.zzg(t, zzac);
                                            return;
                                        }
                                        return;
                                    }
                                } catch (zzvu e2) {
                                    obj = zzac;
                                    break;
                                }
                        }
                        try {
                            com_google_android_gms_internal_measurement_zzyb.zza(com_google_android_gms_internal_measurement_zzxi);
                            if (obj == null) {
                                zzac = com_google_android_gms_internal_measurement_zzyb.zzai(t);
                            } else {
                                zzac = obj;
                            }
                            if (com_google_android_gms_internal_measurement_zzyb.zza(zzac, com_google_android_gms_internal_measurement_zzxi)) {
                                obj = zzac;
                            } else {
                                for (i3 = this.zzcbj; i3 < this.zzcbk; i3++) {
                                    zzac = zza((Object) t, this.zzcbi[i3], zzac, com_google_android_gms_internal_measurement_zzyb);
                                }
                                if (zzac != null) {
                                    com_google_android_gms_internal_measurement_zzyb.zzg(t, zzac);
                                    return;
                                }
                                return;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            zzac = obj;
                        }
                    } else if (zzve == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                        for (i3 = this.zzcbj; i3 < this.zzcbk; i3++) {
                            obj = zza((Object) t, this.zzcbi[i3], obj, com_google_android_gms_internal_measurement_zzyb);
                        }
                        if (obj != null) {
                            com_google_android_gms_internal_measurement_zzyb.zzg(t, obj);
                            return;
                        }
                        return;
                    } else {
                        if (this.zzcbe) {
                            zzp = com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzuz, this.zzcbd, zzve);
                        } else {
                            zzp = null;
                        }
                        if (zzp != null) {
                            if (com_google_android_gms_internal_measurement_zzvd == null) {
                                com_google_android_gms_internal_measurement_zzvd = com_google_android_gms_internal_measurement_zzva.zzt(t);
                            }
                            obj = com_google_android_gms_internal_measurement_zzva.zza(com_google_android_gms_internal_measurement_zzxi, zzp, com_google_android_gms_internal_measurement_zzuz, com_google_android_gms_internal_measurement_zzvd, obj, com_google_android_gms_internal_measurement_zzyb);
                        } else {
                            com_google_android_gms_internal_measurement_zzyb.zza(com_google_android_gms_internal_measurement_zzxi);
                            if (obj == null) {
                                zzac = com_google_android_gms_internal_measurement_zzyb.zzai(t);
                            } else {
                                zzac = obj;
                            }
                            try {
                                if (com_google_android_gms_internal_measurement_zzyb.zza(zzac, com_google_android_gms_internal_measurement_zzxi)) {
                                    obj = zzac;
                                } else {
                                    for (i3 = this.zzcbj; i3 < this.zzcbk; i3++) {
                                        zzac = zza((Object) t, this.zzcbi[i3], zzac, com_google_android_gms_internal_measurement_zzyb);
                                    }
                                    if (zzac != null) {
                                        com_google_android_gms_internal_measurement_zzyb.zzg(t, zzac);
                                        return;
                                    }
                                    return;
                                }
                            } catch (Throwable th22) {
                                th = th22;
                            }
                        }
                    }
                }
                for (i3 = this.zzcbj; i3 < this.zzcbk; i3++) {
                    zzac = zza((Object) t, this.zzcbi[i3], zzac, com_google_android_gms_internal_measurement_zzyb);
                }
                if (zzac != null) {
                    com_google_android_gms_internal_measurement_zzyb.zzg(t, zzac);
                }
                throw th;
            }

            private final zzxj zzbn(int i) {
                int i2 = (i / 3) << 1;
                zzxj com_google_android_gms_internal_measurement_zzxj = (zzxj) this.zzcba[i2];
                if (com_google_android_gms_internal_measurement_zzxj != null) {
                    return com_google_android_gms_internal_measurement_zzxj;
                }
                com_google_android_gms_internal_measurement_zzxj = zzxf.zzxn().zzi((Class) this.zzcba[i2 + 1]);
                this.zzcba[i2] = com_google_android_gms_internal_measurement_zzxj;
                return com_google_android_gms_internal_measurement_zzxj;
            }

            private final Object zzbo(int i) {
                return this.zzcba[(i / 3) << 1];
            }

            private final zzvr zzbp(int i) {
                return (zzvr) this.zzcba[((i / 3) << 1) + 1];
            }

            public final void zzu(T t) {
                int i;
                for (i = this.zzcbj; i < this.zzcbk; i++) {
                    long zzbq = (long) (zzbq(this.zzcbi[i]) & 1048575);
                    Object zzp = zzyh.zzp(t, zzbq);
                    if (zzp != null) {
                        zzyh.zza((Object) t, zzbq, this.zzcbp.zzab(zzp));
                    }
                }
                int length = this.zzcbi.length;
                for (i = this.zzcbk; i < length; i++) {
                    this.zzcbm.zzb(t, (long) this.zzcbi[i]);
                }
                this.zzcbn.zzu(t);
                if (this.zzcbe) {
                    this.zzcbo.zzu(t);
                }
            }

            private final <UT, UB> UB zza(Object obj, int i, UB ub, zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB) {
                int i2 = this.zzcaz[i];
                Object zzp = zzyh.zzp(obj, (long) (zzbq(i) & 1048575));
                if (zzp == null) {
                    return ub;
                }
                zzvr zzbp = zzbp(i);
                if (zzbp == null) {
                    return ub;
                }
                return zza(i, i2, this.zzcbp.zzy(zzp), zzbp, ub, com_google_android_gms_internal_measurement_zzyb_UT__UB);
            }

            private final <K, V, UT, UB> UB zza(int i, int i2, Map<K, V> map, zzvr com_google_android_gms_internal_measurement_zzvr, UB ub, zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB) {
                zzwm zzad = this.zzcbp.zzad(zzbo(i));
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (!com_google_android_gms_internal_measurement_zzvr.zzb(((Integer) entry.getValue()).intValue())) {
                        if (ub == null) {
                            ub = com_google_android_gms_internal_measurement_zzyb_UT__UB.zzye();
                        }
                        zzuk zzam = zzud.zzam(zzwl.zza(zzad, entry.getKey(), entry.getValue()));
                        try {
                            zzwl.zza(zzam.zzuf(), zzad, entry.getKey(), entry.getValue());
                            com_google_android_gms_internal_measurement_zzyb_UT__UB.zza((Object) ub, i2, zzam.zzue());
                            it.remove();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return ub;
            }

            public final boolean zzaf(T t) {
                int i = 0;
                int i2 = -1;
                for (int i3 = 0; i3 < this.zzcbj; i3++) {
                    int i4;
                    int i5;
                    int i6 = this.zzcbi[i3];
                    int i7 = this.zzcaz[i6];
                    int zzbq = zzbq(i6);
                    if (this.zzcbg) {
                        i4 = 0;
                    } else {
                        i5 = this.zzcaz[i6 + 2];
                        int i8 = i5 & 1048575;
                        i5 = 1 << (i5 >>> 20);
                        if (i8 != i2) {
                            i = zzcay.getInt(t, (long) i8);
                            i4 = i5;
                            i2 = i8;
                        } else {
                            i4 = i5;
                        }
                    }
                    if ((C.ENCODING_PCM_MU_LAW & zzbq) != 0) {
                        i5 = 1;
                    } else {
                        boolean z = false;
                    }
                    if (i5 != 0 && !zza((Object) t, i6, i, i4)) {
                        return false;
                    }
                    switch ((267386880 & zzbq) >>> 20) {
                        case 9:
                        case 17:
                            if (zza((Object) t, i6, i, i4) && !zza((Object) t, zzbq, zzbn(i6))) {
                                return false;
                            }
                        case 27:
                        case 49:
                            List list = (List) zzyh.zzp(t, (long) (zzbq & 1048575));
                            if (!list.isEmpty()) {
                                zzxj zzbn = zzbn(i6);
                                for (i4 = 0; i4 < list.size(); i4++) {
                                    if (!zzbn.zzaf(list.get(i4))) {
                                        z = false;
                                        if (z) {
                                            break;
                                        }
                                        return false;
                                    }
                                }
                            }
                            z = true;
                            if (z) {
                                return false;
                            }
                        case 50:
                            Map zzz = this.zzcbp.zzz(zzyh.zzp(t, (long) (zzbq & 1048575)));
                            if (!zzz.isEmpty()) {
                                if (this.zzcbp.zzad(zzbo(i6)).zzcat.zzyp() == zzyv.MESSAGE) {
                                    zzxj com_google_android_gms_internal_measurement_zzxj = null;
                                    for (Object next : zzz.values()) {
                                        if (com_google_android_gms_internal_measurement_zzxj == null) {
                                            com_google_android_gms_internal_measurement_zzxj = zzxf.zzxn().zzi(next.getClass());
                                        }
                                        if (!com_google_android_gms_internal_measurement_zzxj.zzaf(next)) {
                                            z = false;
                                            if (z) {
                                                break;
                                            }
                                            return false;
                                        }
                                    }
                                }
                            }
                            z = true;
                            if (z) {
                                return false;
                            }
                        case 60:
                        case 68:
                            if (zza((Object) t, i7, i6) && !zza((Object) t, zzbq, zzbn(i6))) {
                                return false;
                            }
                        default:
                            break;
                    }
                }
                if (!this.zzcbe || this.zzcbo.zzs(t).isInitialized()) {
                    return true;
                }
                return false;
            }

            private static boolean zza(Object obj, int i, zzxj com_google_android_gms_internal_measurement_zzxj) {
                return com_google_android_gms_internal_measurement_zzxj.zzaf(zzyh.zzp(obj, (long) (1048575 & i)));
            }

            private static void zza(int i, Object obj, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
                if (obj instanceof String) {
                    com_google_android_gms_internal_measurement_zzyw.zzb(i, (String) obj);
                } else {
                    com_google_android_gms_internal_measurement_zzyw.zza(i, (zzud) obj);
                }
            }

            private final void zza(Object obj, int i, zzxi com_google_android_gms_internal_measurement_zzxi) throws IOException {
                if (zzbs(i)) {
                    zzyh.zza(obj, (long) (i & 1048575), com_google_android_gms_internal_measurement_zzxi.zzun());
                } else if (this.zzcbf) {
                    zzyh.zza(obj, (long) (i & 1048575), com_google_android_gms_internal_measurement_zzxi.readString());
                } else {
                    zzyh.zza(obj, (long) (i & 1048575), com_google_android_gms_internal_measurement_zzxi.zzuo());
                }
            }

            private final int zzbq(int i) {
                return this.zzcaz[i + 1];
            }

            private final int zzbr(int i) {
                return this.zzcaz[i + 2];
            }

            private static boolean zzbs(int i) {
                return (C.ENCODING_PCM_A_LAW & i) != 0;
            }

            private static <T> double zzf(T t, long j) {
                return ((Double) zzyh.zzp(t, j)).doubleValue();
            }

            private static <T> float zzg(T t, long j) {
                return ((Float) zzyh.zzp(t, j)).floatValue();
            }

            private static <T> int zzh(T t, long j) {
                return ((Integer) zzyh.zzp(t, j)).intValue();
            }

            private static <T> long zzi(T t, long j) {
                return ((Long) zzyh.zzp(t, j)).longValue();
            }

            private static <T> boolean zzj(T t, long j) {
                return ((Boolean) zzyh.zzp(t, j)).booleanValue();
            }

            private final boolean zzc(T t, T t2, int i) {
                return zzb((Object) t, i) == zzb((Object) t2, i);
            }

            private final boolean zza(T t, int i, int i2, int i3) {
                if (this.zzcbg) {
                    return zzb((Object) t, i);
                }
                return (i2 & i3) != 0;
            }

            private final boolean zzb(T t, int i) {
                int zzbq;
                if (this.zzcbg) {
                    zzbq = zzbq(i);
                    long j = (long) (zzbq & 1048575);
                    switch ((zzbq & 267386880) >>> 20) {
                        case 0:
                            if (zzyh.zzo(t, j) != 0.0d) {
                                return true;
                            }
                            return false;
                        case 1:
                            return zzyh.zzn(t, j) != 0.0f;
                        case 2:
                            return zzyh.zzl(t, j) != 0;
                        case 3:
                            return zzyh.zzl(t, j) != 0;
                        case 4:
                            return zzyh.zzk(t, j) != 0;
                        case 5:
                            return zzyh.zzl(t, j) != 0;
                        case 6:
                            return zzyh.zzk(t, j) != 0;
                        case 7:
                            return zzyh.zzm(t, j);
                        case 8:
                            Object zzp = zzyh.zzp(t, j);
                            if (zzp instanceof String) {
                                return !((String) zzp).isEmpty();
                            } else {
                                if (zzp instanceof zzud) {
                                    return !zzud.zzbtz.equals(zzp);
                                } else {
                                    throw new IllegalArgumentException();
                                }
                            }
                        case 9:
                            return zzyh.zzp(t, j) != null;
                        case 10:
                            return !zzud.zzbtz.equals(zzyh.zzp(t, j));
                        case 11:
                            return zzyh.zzk(t, j) != 0;
                        case 12:
                            return zzyh.zzk(t, j) != 0;
                        case 13:
                            return zzyh.zzk(t, j) != 0;
                        case 14:
                            return zzyh.zzl(t, j) != 0;
                        case 15:
                            return zzyh.zzk(t, j) != 0;
                        case 16:
                            return zzyh.zzl(t, j) != 0;
                        case 17:
                            return zzyh.zzp(t, j) != null;
                        default:
                            throw new IllegalArgumentException();
                    }
                }
                zzbq = zzbr(i);
                return (zzyh.zzk(t, (long) (zzbq & 1048575)) & (1 << (zzbq >>> 20))) != 0;
            }

            private final void zzc(T t, int i) {
                if (!this.zzcbg) {
                    int zzbr = zzbr(i);
                    long j = (long) (zzbr & 1048575);
                    zzyh.zzb((Object) t, j, zzyh.zzk(t, j) | (1 << (zzbr >>> 20)));
                }
            }

            private final boolean zza(T t, int i, int i2) {
                return zzyh.zzk(t, (long) (zzbr(i2) & 1048575)) == i;
            }

            private final void zzb(T t, int i, int i2) {
                zzyh.zzb((Object) t, (long) (zzbr(i2) & 1048575), i);
            }
        }
