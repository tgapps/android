package com.google.devtools.build.android.desugar.runtime;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class ThrowableExtension {
    static final int API_LEVEL;
    static final AbstractDesugaringStrategy STRATEGY;

    static abstract class AbstractDesugaringStrategy {
        protected static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

        public abstract void addSuppressed(Throwable th, Throwable th2);

        public abstract void printStackTrace(Throwable th);

        public abstract void printStackTrace(Throwable th, PrintStream printStream);

        public abstract void printStackTrace(Throwable th, PrintWriter printWriter);

        AbstractDesugaringStrategy() {
        }
    }

    static final class NullDesugaringStrategy extends AbstractDesugaringStrategy {
        NullDesugaringStrategy() {
        }

        public void addSuppressed(Throwable receiver, Throwable suppressed) {
        }

        public void printStackTrace(Throwable receiver) {
            receiver.printStackTrace();
        }

        public void printStackTrace(Throwable receiver, PrintStream stream) {
            receiver.printStackTrace(stream);
        }

        public void printStackTrace(Throwable receiver, PrintWriter writer) {
            receiver.printStackTrace(writer);
        }
    }

    static final class ReuseDesugaringStrategy extends AbstractDesugaringStrategy {
        ReuseDesugaringStrategy() {
        }

        public void addSuppressed(Throwable receiver, Throwable suppressed) {
            receiver.addSuppressed(suppressed);
        }

        public void printStackTrace(Throwable receiver) {
            receiver.printStackTrace();
        }

        public void printStackTrace(Throwable receiver, PrintStream stream) {
            receiver.printStackTrace(stream);
        }

        public void printStackTrace(Throwable receiver, PrintWriter writer) {
            receiver.printStackTrace(writer);
        }
    }

    static {
        Integer num = null;
        AbstractDesugaringStrategy strategy;
        try {
            num = readApiLevelFromBuildVersion();
            if (num == null || num.intValue() < 19) {
                if (useMimicStrategy()) {
                    strategy = new NullDesugaringStrategy();
                } else {
                    strategy = new NullDesugaringStrategy();
                }
                STRATEGY = strategy;
                API_LEVEL = num != null ? 1 : num.intValue();
            }
            strategy = new ReuseDesugaringStrategy();
            STRATEGY = strategy;
            if (num != null) {
            }
            API_LEVEL = num != null ? 1 : num.intValue();
        } catch (Throwable e) {
            System.err.println("An error has occured when initializing the try-with-resources desuguring strategy. The default strategy " + NullDesugaringStrategy.class.getName() + "will be used. The error is: ");
            e.printStackTrace(System.err);
            strategy = new NullDesugaringStrategy();
        }
    }

    public static void addSuppressed(Throwable receiver, Throwable suppressed) {
        STRATEGY.addSuppressed(receiver, suppressed);
    }

    public static void printStackTrace(Throwable receiver) {
        STRATEGY.printStackTrace(receiver);
    }

    public static void printStackTrace(Throwable receiver, PrintWriter writer) {
        STRATEGY.printStackTrace(receiver, writer);
    }

    public static void printStackTrace(Throwable receiver, PrintStream stream) {
        STRATEGY.printStackTrace(receiver, stream);
    }

    private static boolean useMimicStrategy() {
        return !Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic");
    }

    private static Integer readApiLevelFromBuildVersion() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }
}
