package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ObjectDescriptorFactory {
    protected static Map<Integer, Map<Integer, Class<? extends BaseDescriptor>>> descriptorRegistry = new HashMap();
    protected static Logger log = Logger.getLogger(ObjectDescriptorFactory.class.getName());

    public static com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor createFrom(int r1, java.nio.ByteBuffer r2) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ObjectDescriptorFactory.createFrom(int, java.nio.ByteBuffer):com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = com.coremedia.iso.IsoTypeReader.readUInt8(r9);
        r1 = descriptorRegistry;
        r2 = java.lang.Integer.valueOf(r8);
        r1 = r1.get(r2);
        r1 = (java.util.Map) r1;
        if (r1 != 0) goto L_0x0020;
    L_0x0012:
        r2 = descriptorRegistry;
        r3 = -1;
        r3 = java.lang.Integer.valueOf(r3);
        r2 = r2.get(r3);
        r1 = r2;
        r1 = (java.util.Map) r1;
    L_0x0020:
        r2 = java.lang.Integer.valueOf(r0);
        r2 = r1.get(r2);
        r2 = (java.lang.Class) r2;
        if (r2 == 0) goto L_0x0070;
    L_0x002c:
        r3 = r2.isInterface();
        if (r3 != 0) goto L_0x0070;
    L_0x0032:
        r3 = r2.getModifiers();
        r3 = java.lang.reflect.Modifier.isAbstract(r3);
        if (r3 == 0) goto L_0x003d;
    L_0x003c:
        goto L_0x0070;
    L_0x003d:
        r3 = r2.newInstance();	 Catch:{ Exception -> 0x0044 }
        r3 = (com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor) r3;	 Catch:{ Exception -> 0x0044 }
        goto L_0x00a1;
    L_0x0044:
        r3 = move-exception;
        r4 = log;
        r5 = java.util.logging.Level.SEVERE;
        r6 = new java.lang.StringBuilder;
        r7 = "Couldn't instantiate BaseDescriptor class ";
        r6.<init>(r7);
        r6.append(r2);
        r7 = " for objectTypeIndication ";
        r6.append(r7);
        r6.append(r8);
        r7 = " and tag ";
        r6.append(r7);
        r6.append(r0);
        r6 = r6.toString();
        r4.log(r5, r6, r3);
        r4 = new java.lang.RuntimeException;
        r4.<init>(r3);
        throw r4;
    L_0x0070:
        r3 = log;
        r4 = new java.lang.StringBuilder;
        r5 = "No ObjectDescriptor found for objectTypeIndication ";
        r4.<init>(r5);
        r5 = java.lang.Integer.toHexString(r8);
        r4.append(r5);
        r5 = " and tag ";
        r4.append(r5);
        r5 = java.lang.Integer.toHexString(r0);
        r4.append(r5);
        r5 = " found: ";
        r4.append(r5);
        r4.append(r2);
        r4 = r4.toString();
        r3.warning(r4);
        r3 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.UnknownDescriptor;
        r3.<init>();
        r3.parse(r0, r9);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ObjectDescriptorFactory.createFrom(int, java.nio.ByteBuffer):com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor");
    }

    static {
        Set<Class<? extends BaseDescriptor>> annotated = new HashSet();
        annotated.add(DecoderSpecificInfo.class);
        annotated.add(SLConfigDescriptor.class);
        annotated.add(BaseDescriptor.class);
        annotated.add(ExtensionDescriptor.class);
        annotated.add(ObjectDescriptorBase.class);
        annotated.add(ProfileLevelIndicationDescriptor.class);
        annotated.add(AudioSpecificConfig.class);
        annotated.add(ExtensionProfileLevelDescriptor.class);
        annotated.add(ESDescriptor.class);
        annotated.add(DecoderConfigDescriptor.class);
        for (Class<? extends BaseDescriptor> clazz : annotated) {
            Descriptor descriptor = (Descriptor) clazz.getAnnotation(Descriptor.class);
            int[] tags = descriptor.tags();
            int objectTypeInd = descriptor.objectTypeIndication();
            Map<Integer, Class<? extends BaseDescriptor>> tagMap = (Map) descriptorRegistry.get(Integer.valueOf(objectTypeInd));
            if (tagMap == null) {
                tagMap = new HashMap();
            }
            for (int tag : tags) {
                tagMap.put(Integer.valueOf(tag), clazz);
            }
            descriptorRegistry.put(Integer.valueOf(objectTypeInd), tagMap);
        }
    }
}
