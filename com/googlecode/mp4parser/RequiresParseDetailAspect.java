package com.googlecode.mp4parser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.NoAspectBoundException;

public class RequiresParseDetailAspect {
    private static /* synthetic */ Throwable ajc$initFailureCause;
    public static final /* synthetic */ RequiresParseDetailAspect ajc$perSingletonInstance = null;

    static {
        try {
            ajc$perSingletonInstance = new RequiresParseDetailAspect();
        } catch (Throwable th) {
            ajc$initFailureCause = th;
        }
    }

    public static RequiresParseDetailAspect aspectOf() {
        if (ajc$perSingletonInstance != null) {
            return ajc$perSingletonInstance;
        }
        throw new NoAspectBoundException("com.googlecode.mp4parser.RequiresParseDetailAspect", ajc$initFailureCause);
    }

    public void before(JoinPoint joinPoint) {
        if (!(joinPoint.getTarget() instanceof AbstractBox)) {
            StringBuilder stringBuilder = new StringBuilder("Only methods in subclasses of ");
            stringBuilder.append(AbstractBox.class.getName());
            stringBuilder.append(" can  be annotated with ParseDetail");
            throw new RuntimeException(stringBuilder.toString());
        } else if (!((AbstractBox) joinPoint.getTarget()).isParsed()) {
            ((AbstractBox) joinPoint.getTarget()).parseDetails();
        }
    }
}
