package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.boxes.apple.TrackLoadSettingsAtom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class zzvk<T extends zzuz> {
    private static final Logger logger = Logger.getLogger(zzut.class.getName());
    private static String zzbyk = "com.google.protobuf.BlazeGeneratedExtensionRegistryLiteLoader";

    zzvk() {
    }

    protected abstract T zzwa();

    static <T extends zzuz> T zzd(Class<T> cls) {
        String str;
        ClassLoader classLoader = zzvk.class.getClassLoader();
        if (cls.equals(zzuz.class)) {
            str = zzbyk;
        } else if (cls.getPackage().equals(zzvk.class.getPackage())) {
            str = String.format("%s.BlazeGenerated%sLoader", new Object[]{cls.getPackage().getName(), cls.getSimpleName()});
        } else {
            throw new IllegalArgumentException(cls.getName());
        }
        try {
            return (zzuz) cls.cast(((zzvk) Class.forName(str, true, classLoader).getConstructor(new Class[0]).newInstance(new Object[0])).zzwa());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        } catch (Throwable e2) {
            throw new IllegalStateException(e2);
        } catch (Throwable e22) {
            throw new IllegalStateException(e22);
        } catch (Throwable e222) {
            throw new IllegalStateException(e222);
        } catch (ClassNotFoundException e3) {
            Iterator it = ServiceLoader.load(zzvk.class, classLoader).iterator();
            ArrayList arrayList = new ArrayList();
            while (it.hasNext()) {
                try {
                    arrayList.add((zzuz) cls.cast(((zzvk) it.next()).zzwa()));
                } catch (Throwable e4) {
                    Logger logger = logger;
                    Level level = Level.SEVERE;
                    String str2 = "com.google.protobuf.GeneratedExtensionRegistryLoader";
                    String str3 = TrackLoadSettingsAtom.TYPE;
                    String str4 = "Unable to load ";
                    String valueOf = String.valueOf(cls.getSimpleName());
                    if (valueOf.length() != 0) {
                        valueOf = str4.concat(valueOf);
                    } else {
                        valueOf = new String(str4);
                    }
                    logger.logp(level, str2, str3, valueOf, e4);
                }
            }
            if (arrayList.size() == 1) {
                return (zzuz) arrayList.get(0);
            }
            if (arrayList.size() == 0) {
                return null;
            }
            try {
                return (zzuz) cls.getMethod("combine", new Class[]{Collection.class}).invoke(null, new Object[]{arrayList});
            } catch (Throwable e2222) {
                throw new IllegalStateException(e2222);
            } catch (Throwable e22222) {
                throw new IllegalStateException(e22222);
            } catch (Throwable e222222) {
                throw new IllegalStateException(e222222);
            }
        }
    }
}
